package org.jcy.timeline.web.controller;

import com.google.gson.Gson;
import org.jcy.timeline.web.ItemFactory;
import org.jcy.timeline.web.model.*;
import org.jcy.timeline.web.service.TimelineService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimelineControllerTest {

    private static final String URI = "https://github.com/Annoyer/jenkins-web-test.git";
    private static final String PROJECT_NAME = "jenkins-web-test";

    private static final String SESSION_ID = "111";

    private static final Gson GSON = new Gson();

    @Mock
    private TimelineService service;
    @InjectMocks
    private TimelineController controller;

    private MockMvc mvc;

    private MockHttpSession session;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        session = new MockHttpSession();
    }

    @Test
    public void index() {
        Assert.assertEquals("index", controller.index());
    }

    @Test
    public void register() throws Exception {
        RegisterResponse response = new RegisterResponse();
        List<GitItemUi> firstItems = ItemFactory.createNewItemUis(1, 10);
        response.setId(session.getId());
        response.setItems(firstItems);
        response.setSuccess(true);

        when(service.register(session.getId(), URI, PROJECT_NAME)).thenReturn(response);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUri(URI);
        registerRequest.setProjectName(PROJECT_NAME);

        mvc.perform(post("/register")
                .session(session)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(GSON.toJson(registerRequest)))
                .andExpect(status().isOk());

//        verify(service).register(session.getId(), URI, PROJECT_NAME);

    }

    @Test
    public void startAutoUpdateWithNullPrincipal() {
        controller.startAutoUpdate(null);

        verify(service, never()).startAutoUpdate(anyObject());
    }

    @Test
    public void startAutoUpdate() {
        Principal principal = () -> "test-user";
        controller.startAutoUpdate(principal);

        verify(service).startAutoUpdate(principal.getName());
    }

    @Test
    public void fetchMore() throws Exception {
        FetchResponse response = new FetchResponse();
        List<GitItemUi> items = ItemFactory.createNewItemUis(100, 10);
        response.setItems(items);
        response.setSuccess(true);

        when(service.fetchMore(SESSION_ID)).thenReturn(response);

        FetchRequest fetchRequest = new FetchRequest();
        fetchRequest.setId(SESSION_ID);

        mvc.perform(post("/more")
                .session(session)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(GSON.toJson(fetchRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        //verify(service).fetchMore(SESSION_ID);
    }

    @Test
    public void fetchNew() throws Exception {
        FetchResponse response = new FetchResponse();
        List<GitItemUi> items = ItemFactory.createNewItemUis(1000, 5);
        response.setItems(items);
        response.setSuccess(true);

        when(service.fetchNew(SESSION_ID)).thenReturn(response);

        FetchRequest fetchRequest = new FetchRequest();
        fetchRequest.setId(SESSION_ID);

        mvc.perform(post("/new")
                .session(session)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(GSON.toJson(fetchRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        //verify(service).fetchNew(SESSION_ID);
    }
}