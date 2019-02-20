package org.jcy.timeline.web.model;

import java.util.List;

public class FetchResponse {
    private boolean success;
    private String cause;
    private List<GitItemUi> items;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public List<GitItemUi> getItems() {
        return items;
    }

    public void setItems(List<GitItemUi> items) {
        this.items = items;
    }
}
