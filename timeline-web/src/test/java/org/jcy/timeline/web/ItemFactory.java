package org.jcy.timeline.web;

import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.web.model.GitItemUi;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemFactory {

    private static final List<GitItem> items = new ArrayList<>();

    private static final DecimalFormat FORMAT = new DecimalFormat("0000000");

    static {
        for (int i = 8; i > 0; i--) {
            items.add(new GitItem(FORMAT.format(i), i, "author" + i, "content" + i));
        }
    }

    public static GitItem[] createNewItems(int currentMaxId, int count) {
        GitItem[] result = new GitItem[count];
        for (int i = 0; i < result.length; i++) {
            int current = currentMaxId + i + 1;
            result[i] = new GitItem(FORMAT.format(current),
                    current,
                    "author" + current,
                    "content" + current);
        }
        return result;
    }

    public static List<GitItemUi> createNewItemUis(int currentMaxId, int count) {
        GitItem[] result = createNewItems(currentMaxId, count);

        return Arrays.stream(result).map(GitItemUi::new).collect(Collectors.toList());
    }

    public static GitItem[] createMoreItems(int currentMinId, int count) {
        return createNewItems(currentMinId - count - 1, count);
    }

}
