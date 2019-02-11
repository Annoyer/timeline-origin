package org.jcy.timeline.core.provider.git;


import org.jcy.timeline.core.model.ItemSerialization;
import org.jcy.timeline.util.Messages;

import static org.jcy.timeline.util.Assertion.checkArgument;
import static java.lang.Long.parseLong;

public class GitItemSerialization implements ItemSerialization<GitItem> {

    private static final String SEPARATOR = "@;@;@;@";

    @Override
    public String serialize(GitItem item) {
        checkArgument(item != null, Messages.get("ITEM_MUST_NOT_BE_NULL"));

        return new StringBuilder()
                .append(item.getId()).append(SEPARATOR)
                .append(item.getTimeStamp()).append(SEPARATOR)
                .append(item.getAuthor()).append(SEPARATOR)
                .append(item.getContent()).toString();
    }

    @Override
    public GitItem deserialize(String input) {
        checkArgument(input != null, Messages.get("INPUT_MUST_NOT_BE_NULL"));

        String[] split = input.split(SEPARATOR);
        return new GitItem(split[0], parseLong(split[1]), split[2], split[3]);
    }
}