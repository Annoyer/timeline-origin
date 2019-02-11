package org.jcy.timeline.core.provider.git;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.util.Messages;

import static org.jcy.timeline.util.Assertion.checkArgument;

public class GitItem extends Item {

    private final String content;
    private final String author;

    // 封装一条commit记录
    public static GitItem ofCommit(RevCommit commit) {
        return new GitItem(getId(commit), getTimeStamp(commit), getAuthor(commit), getContent(commit));
    }

    public GitItem(String id, long timeStamp, String author, String content) {
        super(id, timeStamp);
        checkArgument(author != null, Messages.get("AUTHOR_MUST_NOT_BE_NULL"));
        checkArgument(content != null, Messages.get("CONTENT_MUST_NOT_BE_NULL"));

        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "GitItem [content=" + content + ", author=" + author + ", timeStamp=" + timeStamp + ", id=" + id + "]";
    }

    static String getContent(RevCommit commit) {
        return commit.getShortMessage();
    }

    static String getAuthor(RevCommit commit) {
        return commit.getAuthorIdent().getName();
    }

    static long getTimeStamp(RevCommit commit) {
        return commit.getCommitterIdent().getWhen().getTime();
    }

    static String getId(RevCommit commit) {
        return ObjectId.toString(commit.getId());
    }
}