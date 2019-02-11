package org.jcy.timeline.core.provider.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.jcy.timeline.util.Messages;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import static java.lang.String.format;
import static org.eclipse.jgit.api.Git.open;
import static org.jcy.timeline.util.Exceptions.guard;

class GitOperator {

    private final File repositoryLocation;

    @FunctionalInterface
    interface GitOperation<T> {
        T execute(Git git) throws Exception;
    }

    GitOperator(File repositoryLocation) {
        this.repositoryLocation = repositoryLocation;
        openRepository().close();
    }

    <T> T execute(GitOperation<T> gitOperation) {
        Git git = openRepository();
        try {
            return guarded(() -> gitOperation.execute(git));
        } finally {
            git.close();
        }
    }

    static <T> T guarded(Callable<T> callable) {
        return guard(callable).with(IllegalStateException.class);
    }

    private Git openRepository() {
        return guarded(() -> openRepository(repositoryLocation));
    }

    private static Git openRepository(File repositoryDir) throws IOException {
        try {
            return open(repositoryDir);
        } catch (RepositoryNotFoundException rnfe) {
            throw new IllegalArgumentException(Messages.get("DIRECTORY_CONTAINS_NO_GIT_REPOSITORY", repositoryDir), rnfe);
        }
    }
}