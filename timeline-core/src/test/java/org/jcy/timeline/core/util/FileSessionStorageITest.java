package org.jcy.timeline.core.util;

import org.jcy.timeline.core.model.FakeItem;
import org.jcy.timeline.core.model.FakeItemSerialization;
import org.jcy.timeline.core.model.Memento;
import org.jcy.timeline.core.model.MementoAssert;
import org.jcy.timeline.util.Messages;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.jcy.timeline.core.model.FakeItems.ALL_ITEMS;
import static org.jcy.timeline.core.model.FakeItems.FIRST_ITEM;
import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.api.Assertions.assertThat;

public class FileSessionStorageITest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void storage() throws IOException {
        File storageLocation = temporaryFolder.newFile();
        FileSessionStorage<FakeItem> storage = createStorage(storageLocation);
        Memento<FakeItem> expected = createMemento();

        storage.store(expected);
        Memento<FakeItem> actual = storage.read();

        assertThat(storedMemento(storageLocation)).isNotEmpty();
        MementoAssert.assertThat(actual)
                .isEqualTo(expected)
                .isNotSameAs(expected);
    }

    @Test
    public void storageOfEmptyMemento() throws IOException {
        File storageLocation = temporaryFolder.newFile();
        FileSessionStorage<FakeItem> storage = createStorage(storageLocation);
        storeNonEmptyMemento(storage);

        storage.store(Memento.empty());
        Memento<FakeItem> actual = storage.read();

        assertThat(storedMemento(storageLocation)).isEmpty();
        MementoAssert.assertThat(actual)
                .isEqualTo(Memento.empty())
                .isSameAs(Memento.empty());
    }

    @Test
    public void constructIfStorageLocationIsNotAccesible() {
        File nonExistingLocation = new File("doesNotExist");

        Throwable actual = thrownBy(() -> new FileSessionStorage<>(nonExistingLocation, new FakeItemSerialization()));

        assertThat(actual)
                .hasMessageContaining(nonExistingLocation.toString())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructIfStorageLocationIsNoFile() throws IOException {
        File folder = temporaryFolder.newFolder();

        Throwable actual = thrownBy(() -> new FileSessionStorage<>(folder, new FakeItemSerialization()));

        assertThat(actual)
                .hasMessageContaining(folder.toString())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsStorageLocation() {
        Throwable actual = thrownBy(() -> new FileSessionStorage<>(null, new FakeItemSerialization()));

        assertThat(actual)
                .hasMessageContaining(Messages.get("STORAGE_LOCATION_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNullAsItemSerialisation() {
        Throwable actual = thrownBy(() -> new FileSessionStorage<>(temporaryFolder.newFile(), null));

        assertThat(actual)
                .hasMessageContaining(Messages.get("ITEM_SERIALIZATION_MUST_NOT_BE_NULL"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void storeWithIOProblem() throws IOException {
        File storageLocation = temporaryFolder.newFile();
        FileSessionStorage<FakeItem> storage = createStorage(storageLocation);
        Memento<FakeItem> expected = createMemento();
        storageLocation.delete();

        Throwable actual = thrownBy(() -> storage.store(expected));

        assertThat(actual)
                .isInstanceOf(IllegalStateException.class)
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    public void readWithIOProblem() throws IOException {
        File storageLocation = temporaryFolder.newFile();
        FileSessionStorage<FakeItem> storage = createStorage(storageLocation);
        storageLocation.delete();

        Throwable actual = thrownBy(() -> storage.read());

        assertThat(actual)
                .isInstanceOf(IllegalStateException.class)
                .hasCauseInstanceOf(IOException.class);
    }

    private FileSessionStorage<FakeItem> createStorage(File storageLocation) {
        return new FileSessionStorage<>(storageLocation, new FakeItemSerialization());
    }

    private static void storeNonEmptyMemento(FileSessionStorage<FakeItem> storage) {
        storage.store(createMemento());
    }

    private static Memento<FakeItem> createMemento() {
        return new Memento<>(ALL_ITEMS, Optional.of(FIRST_ITEM));
    }

    private byte[] storedMemento(File storageLocation) throws IOException {
        return readAllBytes(storageLocation.toPath());
    }
}