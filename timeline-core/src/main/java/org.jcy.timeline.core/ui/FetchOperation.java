package org.jcy.timeline.core.ui;

import org.jcy.timeline.core.model.Item;
import org.jcy.timeline.core.model.Timeline;
import org.jcy.timeline.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.jcy.timeline.util.Assertion.checkArgument;

public enum FetchOperation {

    /**
     * 后台线程每5s尝试fetchNewCount，如有新的commit，在Header上显示‘new’按。
     * 点击new按钮后执行此操作。
     * 拉取 缓存中最新一条更新之后的 最新的最多100条更新
     */
    NEW {
        @Override
        public <T extends Item> void fetch(Timeline<T> timeline) {
            log.info("Fetch Operation [NEW] is called.");
            checkArgument(timeline != null, Messages.get("TIMELINE_MUST_NOT_BE_NULL"));

            timeline.fetchNew();
        }
    },

    /**
     * 初始化和点击more按钮时执行的操作。
     * 拉取 缓存中最旧的一条记录开始，更旧的10条更新记录。
     */
    MORE {
        @Override
        public <T extends Item> void fetch(Timeline<T> timeline) {
            log.info("Fetch Operation [MORE] is called.");
            checkArgument(timeline != null, Messages.get("TIMELINE_MUST_NOT_BE_NULL"));

            timeline.fetchItems();
        }
    };

    private static final Logger log = LoggerFactory.getLogger(FetchOperation.class);

    public abstract <T extends Item> void fetch(Timeline<T> timeline);
}