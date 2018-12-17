package in.keepgrowing.scrumally.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.keepgrowing.scrumally.config.SecurityConfig;
import in.keepgrowing.scrumally.security.TokenProperties;
import in.keepgrowing.scrumally.security.CustomUserDetailsService;
import in.keepgrowing.scrumally.user.model.User;
import in.keepgrowing.scrumally.user.model.UserCredentials;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import({TokenProperties.class, BCryptPasswordEncoder.class, CustomUserDetailsService.class, SecurityConfig.class})
public class UserControllerTest {

    private final String apiPath = "/api/users";
    private JacksonTester<User> userJacksonTester;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;
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
    public void registersNewUser() throws Exception {
        User user = createTestUser();
        given(userService.register(user))
                .willReturn(user);

        mvc.perform(post(apiPath)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJacksonTester.write(user).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userCredentials.username", is(user.getUserCredentials().getUsername())));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void updates() throws Exception {
        User user = createTestUser();
        given(userService.update(user, 1L))
                .willReturn(Optional.of(user));

        mvc.perform(put(apiPath + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJacksonTester.write(user).getJson()))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void updateGivesForbiddenStatusWhenGivenInvalidRole() throws Exception {

        mvc.perform(put(apiPath + "/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateGivesUnauthorisedStatusWhenUserNotAuthenticated() throws Exception {
        mvc.perform(put(apiPath + "/1"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void statusNotFoundWhenUpdatingNonExistingUser() throws Exception {
        given(userService.findByUsername("user"))
                .willReturn(Optional.empty());
        User user = createTestUser();

        mvc.perform(put(apiPath + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJacksonTester.write(user).getJson()))
                .andExpect(status().isNotFound());
    }

    private User createTestUser() {
        UserCredentials credentials = new UserCredentials("user", "start", "");
        return new User(credentials);
    }
}