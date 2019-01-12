package org.jcy.timeline.core.util;

import org.jcy.timeline.test.util.ConditionalIgnoreRule;
import org.jcy.timeline.test.util.ConditionalIgnoreRule.ConditionalIgnore;
import org.jcy.timeline.test.util.NotRunningOnWindows;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static org.jcy.timeline.core.util.FileStorageStructure.STORAGE_FILE;
import static org.jcy.timeline.core.util.FileStorageStructure.TIMELINE_DIRECTORY;
import static org.assertj.core.api.Assertions.assertThat;

public class FileStorageStructureITest {

    @Rule
    public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private FileStorageStructure storageStructure;
    private File timelineDirectory;
    private File baseDirectory;
    private File storageFile;

    @Before
    public void setUp() throws IOException {
        baseDirectory = temporaryFolder.newFolder();
        storageStructure = new FileStorageStructure(baseDirectory);
        timelineDirectory = createCanonicalFile(baseDirectory, TIMELINE_DIRECTORY);
        storageFile = createCanonicalFile(timelineDirectory, STORAGE_FILE);
    }

    @Test
    public void initialState() {
        assertThat(timelineDirectory).doesNotExist();
        assertThat(storageFile).doesNotExist();
    }

    @Test
    public void getStorageFile() throws IOException {
        File actual = storageStructure.getStorageFile();

        assertThat(actual)
                .isEqualTo(storageFile)
                .exists();
    }

    @Test
    public void getStorageFileThatAlreadyExists() throws IOException {
        storageStructure.getStorageFile();

        File actual = storageStructure.getStorageFile();

        assertThat(actual)
                .isEqualTo(storageFile)
                .exists();
    }

    @Test
    public void getTimelineDirectory() {
        File actual = storageStructure.getTimelineDirectory();

        assertThat(actual)
                .isEqualTo(timelineDirectory)
                .exists();
    }

    @Test
    public void getTimelineDirectoryThatAlreadyExists() {
        File actual = storageStructure.getTimelineDirectory();

        storageStructure.getTimelineDirectory();

        assertThat(actual)
                .isEqualTo(timelineDirectory)
                .exists();
    }

    @Test
    @ConditionalIgnore(condition = NotRunningOnWindows.class)
    public void constructWithInvalidFileName() throws IOException {
        Throwable actual = thrownBy(() -> new FileStorageStructure(new File("?<>% *:|")));

        assertThat(actual)
                .isInstanceOf(IllegalArgumentException.class)
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    public void constructWithNullAsArgument() throws IOException {
        Throwable actual = thrownBy(() -> new FileStorageStructure(null));

        assertThat(actual)
                .hasMessage(FileStorageStructure.BASE_DIRECTORY_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static File createCanonicalFile(File baseDirectory, String fileName) throws IOException {
        return new File(baseDirectory, fileName).getCanonicalFile();
    }
}