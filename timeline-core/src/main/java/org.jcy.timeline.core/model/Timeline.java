package org.jcy.timeline.core.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.jcy.timeline.util.Assertion.checkArgument;
import static java.lang.Long.compare;
import static java.util.Collections.sort;

public class Timeline<T extends Item> {

    private static final Logger log = LoggerFactory.getLogger(Timeline.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final int FETCH_COUNT_LOWER_BOUND = 1;
    public static final int FETCH_COUNT_UPPER_BOUND = 20;
    public static final int DEFAULT_FETCH_COUNT = 10;

    static final String ERROR_EXCEEDS_LOWER_BOUND
            = "FetchCount of %s exceeds lower bound of " + FETCH_COUNT_LOWER_BOUND + ".";
    static final String ERROR_EXCEEDS_UPPER_BOUND
            = "FetchCount of %s exceeds upper bound of " + FETCH_COUNT_UPPER_BOUND + ".";
    static final String ERROR_TOP_ITEM_IS_UNRELATED = "TopItem <%s> is not contained in item list.";
    static final String ERROR_TOP_ITEM_MUST_NOT_BE_NULL = "Argument 'topItem' must not be null.";
    static final String ERROR_SESSION_PROVIDER_MUST_NOT_BE_NULL = "Argument 'sessionProvider' must not be null.";
    static final String ERROR_ITEM_PROVIDER_MUST_NOT_BE_NULL = "Argument 'itemProvider' must not be null.";

    private final SessionStorage<T> sessionStorage;
    private final ItemProvider<T> itemProvider;
    private final List<T> items;

    private Optional<T> topItem;
    private int fetchCount;

    public Timeline(ItemProvider<T> itemProvider, SessionStorage<T> sessionStorage) {
        checkArgument(itemProvider != null, ERROR_ITEM_PROVIDER_MUST_NOT_BE_NULL);
        checkArgument(sessionStorage != null, ERROR_SESSION_PROVIDER_MUST_NOT_BE_NULL);

        this.itemProvider = itemProvider;
        this.sessionStorage = sessionStorage;
        this.items = new ArrayList<>();
        // 每次只拉10条
        this.fetchCount = DEFAULT_FETCH_COUNT;
        this.topItem = Optional.empty();
        // 首先读取本地文件中缓存的commit
        restore();
    }

    public void setFetchCount(int fetchCount) {
        checkArgument(fetchCount <= FETCH_COUNT_UPPER_BOUND, ERROR_EXCEEDS_UPPER_BOUND, fetchCount);
        checkArgument(fetchCount >= FETCH_COUNT_LOWER_BOUND, ERROR_EXCEEDS_LOWER_BOUND, fetchCount);
        this.fetchCount = fetchCount;
    }

    public int getFetchCount() {
        return fetchCount;
    }

    public Optional<T> getTopItem() {
        return topItem;
    }

    public void setTopItem(T topItem) {
        checkArgument(topItem != null, ERROR_TOP_ITEM_MUST_NOT_BE_NULL);
        checkArgument(items.contains(topItem), ERROR_TOP_ITEM_IS_UNRELATED, topItem.getId());

        this.topItem = Optional.of(topItem);
        sessionStorage.store(createMemento());
    }

    /**
     * 获取oldest开始的fetchCount条记录，存入内存，并写入本地文件。
     */
    public void fetchItems() {
        log.info("Timeline is trying fetching older commits. Start from oldest item {}. Max fetch count is [{}].",
                briefOf(getOldest()), getFetchCount());
        addSorted(itemProvider.fetchItems(getOldest(), getFetchCount()));
        updateTopItem();
        sessionStorage.store(createMemento());
    }

    public int getNewCount() {
        log.info("Timeline is trying fetching count of new commits. Start from latest item {}.",
                briefOf(getLatest()));
        return itemProvider.getNewCount(getLatest());
    }

    /**
     * 获取latest之后的最多100条 ！最新的！ commit记录，存入内存，并写入本地文件。
     */
    public void fetchNew() {
        log.info("Timeline is trying fetching new commits. Start from latest item {}.",
                 briefOf(getLatest()));
        addSorted(itemProvider.fetchNew(getLatest()));
        sessionStorage.store(createMemento());
    }

    public List<T> getItems() {
        return new ArrayList<>(items);
    }

    private T getLatest() {
        if (items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }

    private T getOldest() {
        if (items.isEmpty()) {
            return null;
        }
        return items.get(items.size() - 1);
    }

    private void addSorted(List<T> additionalItems) {
        items.addAll(additionalItems);
        sort(items, (first, second) -> compare(second.getTimeStamp(), first.getTimeStamp()));

        log.info("{} items are added. Now we have {} commits. " +
                "The latest commit is {}.",
                additionalItems.size(),
                items.size(),
                briefOf(getLatest()));
    }

    /**
     * 读取本地文件中缓存的commit记录，并存到内存中。
     */
    private void restore() {
        Memento<T> memento = sessionStorage.read();
        if (!memento.getItems().isEmpty()) {
            addSorted(new ArrayList<>(memento.getItems()));
            setTopItem(memento.getTopItem().get());
            log.info("Commits from local file cache is loaded into memory. oldest item = {}, latest item = {}.",
                    briefOf(getOldest()),
                    briefOf(getLatest()));
        }
    }

    private void updateTopItem() {
        if (!topItem.isPresent()) {
            topItem = items.isEmpty() ? Optional.empty() : Optional.of(items.get(0));
        }
    }

    private Memento<T> createMemento() {
        return new Memento<>(new HashSet<>(items), getTopItem());
    }



    // For log
    private String briefOf(T item) {
        StringBuilder builder = new StringBuilder();
        builder.append("[CommitTime=").append(commitTimeOf(item))
                .append(" ID=").append(idOf(item))
                .append("]");
        return builder.toString();
    }

    private String idOf(T item) {
        return item != null ? item.getId() : "NULL";
    }

    private String commitTimeOf(T item) {
        if (item == null) return "NULL";
        Date date = new Date();
        date.setTime(item.getTimeStamp());
        return SDF.format(date);
    }
}