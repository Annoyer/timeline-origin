package org.jcy.timeline.web.model;

import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.util.NiceTime;

import java.util.Date;

public class GitItemUi {
    private String id;
    private String author;
    private String content;
    private String time;

    public GitItemUi(GitItem item) {
        this.id = item.getId();
        this.author = item.getAuthor();
        this.content = item.getContent();
        this.time = new NiceTime().format(new Date(item.getTimeStamp()));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
