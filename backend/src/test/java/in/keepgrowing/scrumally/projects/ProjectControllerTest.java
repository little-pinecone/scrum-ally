package in.keepgrowing.scrumally.projects;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.keepgrowing.scrumally.config.SecurityConfig;
import in.keepgrowing.scrumally.projects.model.Project;
import in.keepgrowing.scrumally.projects.model.ProjectRole;
import in.keepgrowing.scrumally.projects.viewmodel.ProjectDto;
import in.keepgrowing.scrumally.projects.viewmodel.ProjectEntityDtoConverter;
import in.keepgrowing.scrumally.projects.viewmodel.ProjectMemberDto;
import in.keepgrowing.scrumally.projects.viewmodel.ProjectWithMembersDto;
import in.keepgrowing.scrumally.security.CustomUserDetailsService;
import in.keepgrowing.scrumally.security.TokenProperties;
import in.keepgrowing.scrumally.security.websecurityexpression.UserUnauthorisedException;
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
import org.springframework.data.domain.Page;
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
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ProjectController.class)
@Import({TokenProperties.class, BCryptPasswordEncoder.class, CustomUserDetailsService.class, SecurityConfig.class})
@EnableSpringDataWebSupport
public class ProjectControllerTest {

    private final String apiPath = "/api/projects";

    @MockBean
    private ProjectService projectService;

    @MockBean
    private UserService userService;

    @MockBean
    private ProjectEntityDtoConverter entityDtoConverter;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<ProjectWithMembersDto> projectWithMembersJacksonTester;
    private JacksonTester<ProjectDto> projectJacksonTester;

    @Autowired
    private WebApplicationContext applicationContext;

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
    public void savesProject() throws Exception {
        var project = getProject();
        var projectDto = getDto();

        given(entityDtoConverter.toEntity(projectDto))
                .willReturn(project);
        given(projectService.saveProject(project))
                .willReturn(project);
        given(entityDtoConverter.toDto(project))
                .willReturn(projectDto);

        mvc.perform(post(apiPath)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJacksonTester.write(projectDto).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(projectDto.getName())));
    }

    private Project getProject() {
        return new Project("name", "");
    }

    private ProjectDto getDto() {
        return new ProjectDto(3L, "name", "");
    }

    @Test
    @WithMockUser(roles = "USER")
    public void returnsForbiddenStatusWhenUserIsUnauthorised() throws Exception {
        var project = getProject();
        var projectDto = getDto();

        given(entityDtoConverter.toEntity(projectDto))
                .willReturn(project);
        given(projectService.saveProject(project))
                .willThrow(UserUnauthorisedException.class);

        mvc.perform(post(apiPath)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJacksonTester.write(projectDto).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void findsAllForCurrentUser() throws Exception {
        Project project = getProject();
        Page<Project> page = new PageImpl<>(Collections.singletonList(project));
        given(projectService.findAllForCurrentUser(any()))
                .willReturn(page);
        var projectDto = getDto();
        given(entityDtoConverter.toDto(project))
                .willReturn(projectDto);

        mvc.perform(get(apiPath)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is(project.getName())));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void findsOneForCurrentUser() throws Exception {
        var project = getProject();
        var dto = getDtoWithMembers();

        given(projectService.findOneForCurrentUser(1L))
                .willReturn(Optional.of(project));
        given(entityDtoConverter.toDtoWithMembers(project))
                .willReturn(dto);

        mvc.perform(get(apiPath + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dto.getName())));
    }

    private ProjectWithMembersDto getDtoWithMembers() {
        Set<ProjectMemberDto> dtoMembers = Set.of(new ProjectMemberDto(null, null, ProjectRole.GUEST));
        return new ProjectWithMembersDto(3L, "name", "", dtoMembers);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void statusNotFoundWhenGettingNonExistingProject() throws Exception {
        given(projectService.findOneForCurrentUser(1L))
                .willReturn(Optional.empty());

        mvc.perform(get(apiPath + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updatesProject() throws Exception {
        var project = getProject();
        var dto = getDtoWithMembers();

        given(entityDtoConverter.toEntityWithMembers(dto))
                .willReturn(project);
        given(projectService.updateProject(project, 1L))
                .willReturn(Optional.of(project));
        given(entityDtoConverter.toDtoWithMembers(project))
                .willReturn(dto);

        mvc.perform(put(apiPath + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectWithMembersJacksonTester.write(dto).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(project.getName())));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void statusNotFoundWhenUpdatingNonExistingProject() throws Exception {
        var project = getProject();
        var dto = getDtoWithMembers();

        given(projectService.updateProject(project, 1L))
                .willReturn(Optional.empty());

        mvc.perform(put(apiPath + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectWithMembersJacksonTester.write(dto).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deletesProjectById() throws Exception {
        mvc.perform(delete(apiPath + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void statusNotFoundWhenDeletingNonExistingProject() throws Exception {
        willThrow(EmptyResultDataAccessException.class)
                .given(projectService)
                .deleteProjectOwnedByCurrentUser(1L);

        mvc.perform(delete(apiPath + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}