package org.jcy.timeline.web;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

import static java.lang.String.format;
import static org.jcy.timeline.test.util.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jcy.timeline.web.StorageDirectoryProvider.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class StorageDirectoryProviderITest {

    private static final String DIRECTORY_NAME = "directoryName";

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private StorageDirectoryProvider provider;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private File root;

    @Before
    public void setUp() throws IOException {
        root = temporaryFolder.newFolder();
        provider = new StorageDirectoryProvider(root);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void getDirectoryIfCookieDoesNotExist() {
        ArgumentCaptor<Cookie> captor = forClass(Cookie.class);

        File actual = provider.getDirectory(request, response);

        verify(response).addCookie(captor.capture());
        assertThat(actual)
                .exists()
                .isEqualTo(new File(root, captor.getValue().getValue()))
                .isDirectory();
    }

    @Test
    public void getDirectoryIfCookieExists() {
        new File(root, DIRECTORY_NAME).mkdirs();
        Cookie cookie = StorageDirectoryProvider.createCookie(DIRECTORY_NAME);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        File actual = provider.getDirectory(request, response);

        verify(response, never()).addCookie(any(Cookie.class));
        assertThat(actual)
                .exists()
                .isEqualTo(new File(root, cookie.getValue()))
                .isDirectory();
    }

    @Test
    public void getDirectoryIfCookieExistsButDirectoryDoesNot() {
        Cookie cookie = StorageDirectoryProvider.createCookie(DIRECTORY_NAME);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        File actual = provider.getDirectory(request, response);

        verify(response, never()).addCookie(any(Cookie.class));
        assertThat(actual)
                .exists()
                .isEqualTo(new File(root, cookie.getValue()))
                .isDirectory();
    }

    @Test
    public void constructWithNullAsRoot() {
        Throwable actual = thrownBy(() -> new StorageDirectoryProvider(null));

        assertThat(actual)
                .hasMessage(ROOT_MUST_NOT_BE_NULL)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithNonExistingRoot() {
        root = new File("doesNotExist");

        Throwable actual = thrownBy(() -> new StorageDirectoryProvider(root));

        assertThat(actual)
                .hasMessage(format(ROOT_DOES_NOT_EXIST, root))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructWithRootThatIsNoDirectory() throws IOException {
        root = temporaryFolder.newFile();

        Throwable actual = thrownBy(() -> new StorageDirectoryProvider(root));

        assertThat(actual)
                .hasMessage(format(ROOT_IS_NOT_A_DIRECTORY, root))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void getDirectoryIfRootHasBeenDeleted() {
        root.delete();

        Throwable actual = thrownBy(() -> provider.getDirectory(request, response));

        assertThat(actual)
                .hasRootCauseInstanceOf(NoSuchFileException.class)
                .isInstanceOf(IllegalStateException.class);
    }
}