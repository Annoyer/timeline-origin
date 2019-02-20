package org.jcy.timeline.web.model;

import org.jcy.timeline.core.provider.git.GitItem;

import java.util.List;
import java.util.stream.Collectors;

public class UpdateInfo {
    private int newCount;
    private List<GitItemUi> items;

    public UpdateInfo(int newCount, List<GitItem> items) {
        this.newCount = newCount;
        this.items = items.stream()
                .map(GitItemUi::new)
                .collect(Collectors.toList());
    }

    public int getNewCount() {
        return newCount;
    }

    public List<GitItemUi> getItems() {
        return items;
    }
}