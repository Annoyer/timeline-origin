package org.jcy.timeline.swing.git;

import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.core.util.FileStorageStructure;
import org.jcy.timeline.swing.ui.SwingTimeline;
import org.jcy.timeline.test.util.GitRepository;
import org.jcy.timeline.test.util.GitRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.api.Assertions.assertThat;

public class GitTimelineFactoryITest {

    private static final String CLONE_NAME = "clone";

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Rule
    public final GitRule gitRule = new GitRule();

    private FileStorageStructure storageStructure;
    private String remoteRepositoryUri;

    @Before
    public void setUp() throws IOException {
        File baseDirectory = temporaryFolder.newFolder();
        storageStructure = new FileStorageStructure(baseDirectory);
        remoteRepositoryUri = createRepository(baseDirectory);
    }

    @Test
    public void create() throws IOException {
        GitTimelineFactory factory = new GitTimelineFactory(storageStructure);

        SwingTimeline<GitItem> actual = factory.create(remoteRepositoryUri, CLONE_NAME);

        assertThat(actual).isNotNull();
        assertThat(cloneLocation()).exists();
        assertThat(storedMemento()).isNotEmpty();
    }

    private File cloneLocation() {
        return new File(storageStructure.getTimelineDirectory(), CLONE_NAME);
    }

    private String storedMemento() throws IOException {
        byte[] bytes = readAllBytes(storageStructure.getStorageFile().toPath());
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private String createRepository(File baseDirectory) throws IOException {
        File remoteRepositoryLocation = new File(baseDirectory, "repository.git");
        GitRepository remote = gitRule.create(remoteRepositoryLocation);
        remote.commitFile("file", "content", "message");
        return remoteRepositoryLocation.toURI().toString();
    }
}