package org.jcy.timeline.web.model;

import java.util.List;

public class RegisterResponse {
    private String id;
    private boolean success;
    private String cause;
    private List<GitItemUi> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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