package in.keepgrowing.scrumally.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TaskController.class)
@EnableSpringDataWebSupport
public class TaskControllerTest {

    private final String apiPath = "/api/tasks";
    @MockBean
    private TaskService taskService;
    @Autowired
    private MockMvc mvc;
    private JacksonTester<Task> taskJacksonTester;

    @Before
    public void setUp() {
        // required to initialize taskJacksonTester
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void savesTask() throws Exception {
        Task task = new Task("test_task", "");
        task.setProjectFromId(1L);
        given(taskService.saveTask(task))
                .willReturn(task);

        mvc.perform(post(apiPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJacksonTester.write(task).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(task.getName())))
                .andExpect(jsonPath("$.projectId").value(is(task.getProject().getId()), Long.class));
    }

    @Test
    public void getsTasks() throws Exception {
        Task task = new Task("test_task", "");
        task.setProjectFromId(1L);
        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task));
        given(taskService.getTasksByProjectId(eq(1L), any()))
                .willReturn(taskPage);

        mvc.perform(get(apiPath + "?project-id=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is(task.getName())))
                .andExpect(jsonPath("$.content[0].projectId").value(is(task.getProject().getId()), Long.class));
    }

    @Test
    public void getsTaskById() throws Exception {
        Task task = new Task("test_task", "");
        task.setProjectFromId(1L);
        given(taskService.getTaskById(1L))
                .willReturn(Optional.of(task));

        mvc.perform(get(apiPath + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(task.getName())))
                .andExpect(jsonPath("$.projectId").value(is(task.getProject().getId()), Long.class));
    }

    @Test
    public void statusNotFoundWhenGettingNonExistingTask() throws Exception {
        given(taskService.getTaskById(1L))
                .willReturn(Optional.empty());

        mvc.perform(get(apiPath + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatesTask() throws Exception {
        Task task = new Task("test_task", "");
        task.setProjectFromId(1L);
        given(taskService.updateTask(task, 1L))
                .willReturn(Optional.of(task));

        mvc.perform(put(apiPath + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJacksonTester.write(task).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(task.getName())))
                .andExpect(jsonPath("$.projectId").value(is(task.getProject().getId()), Long.class));
    }

    @Test
    public void statusNotFoundWhenUpdatingNonExistinTask() throws Exception {
        Task task = new Task("test_task", "");
        given(taskService.updateTask(task, 1L))
                .willReturn(Optional.empty());

        mvc.perform(put(apiPath + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJacksonTester.write(task).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletesTask() throws Exception {
        mvc.perform(delete(apiPath + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void statusNotFoundWhenDeletingNonExistingTask() throws Exception {
        willThrow(EmptyResultDataAccessException.class)
                .given(taskService)
                .deleteTaskById(1L);

        mvc.perform(delete(apiPath + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}