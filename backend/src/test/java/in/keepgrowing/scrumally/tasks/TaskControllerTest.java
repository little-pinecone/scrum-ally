package in.keepgrowing.scrumally.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.keepgrowing.scrumally.config.SecurityConfig;
import in.keepgrowing.scrumally.security.CustomUserDetailsService;
import in.keepgrowing.scrumally.security.TokenProperties;
import in.keepgrowing.scrumally.tasks.viewmodel.TaskDto;
import in.keepgrowing.scrumally.tasks.viewmodel.TaskEntityDtoConverter;
import in.keepgrowing.scrumally.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TaskController.class)
@Import({TokenProperties.class, BCryptPasswordEncoder.class, CustomUserDetailsService.class, SecurityConfig.class})
@EnableSpringDataWebSupport
public class TaskControllerTest {

    private final String apiPath = "/api/tasks";

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;

    @MockBean
    private TaskEntityDtoConverter converter;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext applicationContext;

    private JacksonTester<TaskDto> taskJacksonTester;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void savesTask() throws Exception {
        var task = getTask();
        var dto = getTaskDto();

        given(converter.toEntity(dto))
                .willReturn(task);
        given(taskService.saveTask(task))
                .willReturn(task);
        given(converter.toDto(task))
                .willReturn(dto);

        mvc.perform(post(apiPath)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJacksonTester.write(dto).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.projectId").value(is(dto.getProjectId()), Long.class));
    }

    private Task getTask() {
        var task = new Task("test_task", "", TaskPriority.CRITICAL);
        task.setProjectFromId(1L);
        return task;
    }

    private TaskDto getTaskDto() {
        return new TaskDto(null, "test_task", "", 1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getsTasks() throws Exception {
        var task = getTask();
        var dto = getTaskDto();
        var taskPage = new PageImpl<>(Collections.singletonList(task));

        given(taskService.getTasksByProjectId(eq(1L), any()))
                .willReturn(taskPage);
        given(converter.toDto(task))
                .willReturn(dto);

        mvc.perform(get(apiPath + "?project-id=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is(dto.getName())))
                .andExpect(jsonPath("$.content[0].projectId").value(is(dto.getProjectId()), Long.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getsTaskById() throws Exception {
        var task = getTask();
        var dto = getTaskDto();

        given(taskService.getTaskById(1L))
                .willReturn(Optional.of(task));
        given(converter.toDto(task))
                .willReturn(dto);

        mvc.perform(get(apiPath + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.projectId").value(is(dto.getProjectId()), Long.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void statusNotFoundWhenGettingNonExistingTask() throws Exception {
        given(taskService.getTaskById(1L))
                .willReturn(Optional.empty());

        mvc.perform(get(apiPath + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updatesTask() throws Exception {
        var task = getTask();
        var dto = getTaskDto();

        given(converter.toEntity(dto))
                .willReturn(task);
        given(taskService.updateTask(task, 1L))
                .willReturn(Optional.of(task));
        given(converter.toDto(task))
                .willReturn(dto);

        mvc.perform(put(apiPath + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJacksonTester.write(dto).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.projectId").value(is(dto.getProjectId()), Long.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void statusNotFoundWhenUpdatingNonExistingTask() throws Exception {
        var task = getTask();
        var dto = getTaskDto();

        given(taskService.updateTask(task, 1L))
                .willReturn(Optional.empty());

        mvc.perform(put(apiPath + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJacksonTester.write(dto).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deletesTask() throws Exception {
        mvc.perform(delete(apiPath + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void statusNotFoundWhenDeletingNonExistingTask() throws Exception {
        willThrow(EmptyResultDataAccessException.class)
                .given(taskService)
                .deleteTaskById(1L);

        mvc.perform(delete(apiPath + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}