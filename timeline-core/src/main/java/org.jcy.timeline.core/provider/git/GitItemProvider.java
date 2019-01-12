package org.jcy.timeline.core.provider.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.jcy.timeline.core.model.ItemProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.jcy.timeline.core.provider.git.GitItem.ofCommit;
import static org.jcy.timeline.core.provider.git.GitOperator.guarded;
import static org.jcy.timeline.util.Assertion.checkArgument;
import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.util.stream.Collectors.toList;
import static org.eclipse.jgit.api.Git.cloneRepository;
import static org.jcy.timeline.util.Iterables.asList;

public class GitItemProvider implements ItemProvider<GitItem> {

    static final String URI_MUST_NOT_BE_NULL = "Argument 'uri' must not be null.";
    static final String DESTINATION_MUST_NOT_BE_NULL = "Argument 'destination' must not be null.";
    static final String NAME_MUST_NOT_BE_NULL = "Argument 'name' must not be null.";
    static final String FETCH_COUNT_MUST_NOT_BE_NEGATIVE = "Argument 'fetchCount' must not be negative.";
    static final String LATEST_ITEM_MUST_NOT_BE_NULL = "Argument 'latestItem' must not be null.";
    static final String DESTINATION_MUST_BE_A_DIRECTORY = "Destination <%s> must be a directory.";
    static final String UNKNOWN_GIT_ITEM = "GitItem <%s> is unknown in repository <%s>.";
    static final String URI_IS_NOT_VALID = "URI <%s> is not valid.";

    private final GitOperator operator;

    public GitItemProvider(String uri, String name) {
        this(uri, new File(getProperty("java.io.tmpdir")), name);
    }

    public GitItemProvider(String uri, File destination, String name) {
        checkArgument(uri != null, URI_MUST_NOT_BE_NULL);
        checkArgument(destination != null, DESTINATION_MUST_NOT_BE_NULL);
        checkArgument(name != null, NAME_MUST_NOT_BE_NULL);
        checkArgument(!destination.exists() || destination.isDirectory(), DESTINATION_MUST_BE_A_DIRECTORY, destination);

        operator = new GitOperator(cloneIfNeeded(uri, destination, name));
    }

    @Override
    public List<GitItem> fetchItems(GitItem oldestItem, int fetchCount) {
        checkArgument(fetchCount >= 0, FETCH_COUNT_MUST_NOT_BE_NEGATIVE);

        // 拉取oldestItem开始的fetchCount+1条commits，删除oldestItem本身，封装并返回。
        // 如果oldestItem == null，拿最新的fetchCount条
        return readCommits(oldestItem, fetchCount)
                .stream()
                .filter(commit -> oldestItem == null || !commit.getId().equals(getId(oldestItem)))
                .map(commit -> ofCommit(commit))
                .collect(toList());
    }

    @Override
    public int getNewCount(GitItem item) {
        return fetchNew(item).size();
    }

    @Override
    public List<GitItem> fetchNew(GitItem latestItem) {
        checkArgument(latestItem != null, LATEST_ITEM_MUST_NOT_BE_NULL);

        // 拉取应用启动后的更新记录，最多100条。
        // computeNewCount计算出latestItem在commits中的位置，如果存在，只返回latestItem之后提交的commit记录。

        // 优化：拉取所有不在缓存中的，新的更新记录。
        operator.execute(git -> git.pull().call());
        List<GitItem> commits = new ArrayList<>();
        int callSize = 2;
        List<RevCommit> currentFetch = operator.execute(git -> asList(git.log().setMaxCount(callSize).call()));
        while (!findLimiter(latestItem, currentFetch).isPresent() && currentFetch.size() > 0) {
            for (RevCommit c : currentFetch) {
                commits.add(ofCommit(c));
            }

            currentFetch = operator.execute(git -> asList(git.log().add(getId(commits.get(commits.size()-1))).setMaxCount(callSize+1).call()));
            if (currentFetch.size() > 0) currentFetch.remove(0);
        }

        for (int i = 0; i < computeNewCount(latestItem, currentFetch); i++) {
            commits.add(ofCommit(currentFetch.get(i)));
        }

        return commits;
    }

    private File cloneIfNeeded(String uri, File destination, String name) {
        File result = new File(destination, name);
        if (!result.exists()) {
            result.mkdirs();
            guarded(() -> cloneRemote(uri, result)).close();
        }
        return result;
    }

    private Git cloneRemote(String uri, File repositoryDir) throws GitAPIException, TransportException {
        try {
            return cloneRepository().setURI(uri).setDirectory(repositoryDir).call();
        } catch (InvalidRemoteException ire) {
            throw new IllegalArgumentException(format(URI_IS_NOT_VALID, uri));
        }
    }

    private List<RevCommit> readCommits(GitItem oldestItem, int fetchCount) {
        if (oldestItem != null) {
            return operator.execute(git -> fetchPredecessors(git, oldestItem, fetchCount));
        }
        return operator.execute(git -> asList(git.log().setMaxCount(fetchCount).call()));
    }

    /**
     * 从git获取从 {@param oldestItem} 开始的 fetchCount+1条commit记录，包括{@param oldestItem}。
     *
     * @param git
     * @param oldestItem
     * @param fetchCount
     * @return
     * @throws Exception
     */
    private List<RevCommit> fetchPredecessors(Git git, GitItem oldestItem, int fetchCount) throws Exception {
        try {
            return asList(git.log().add(getId(oldestItem)).setMaxCount(fetchCount + 1).call());
        } catch (MissingObjectException moe) {
            File directory = git.getRepository().getDirectory();
            throw new IllegalArgumentException(format(UNKNOWN_GIT_ITEM, oldestItem, directory), moe);
        }
    }

    private static int computeNewCount(GitItem latestItem, List<RevCommit> commits) {
        Optional<RevCommit> limiter = findLimiter(latestItem, commits);
        if (limiter.isPresent()) {
            return commits.indexOf(limiter.get());
        }
        return commits.size();
    }

    private static Optional<RevCommit> findLimiter(GitItem latestItem, List<RevCommit> commits) {
        return commits
                .stream()
                .filter(commit -> commit.getId().equals(ObjectId.fromString(latestItem.getId())))
                .findFirst();
    }

    private static ObjectId getId(GitItem oldestItem) {
        return ObjectId.fromString(oldestItem.getId());
    }
}