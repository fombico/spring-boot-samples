package com.fombico.sameauthandresourceserver;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@ActiveProfiles("integrationTest")
public class WebSecurityConfigIntegrationTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    UserDetailsService userDetailsService;

    @Autowired
    MockMvc mvc;

    @Before
    public void beforeEachTest() {
        when(userDetailsService.loadUserByUsername("joe"))
                .thenReturn(User.builder()
                        .username("joe")
                        .password(passwordEncoder.encode("pass"))
                        .authorities("user")
                        .build());

    }

    @Test
    public void apis_requireAuthentication() throws Exception {
        mvc.perform(get("/api"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void apis_badToken_returns401() throws Exception {
        mvc.perform(get("/api/info")
                .header("Authorization", "Bearer badToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    @Test
    public void noLoginPage() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void login_noCredentials_returns401() throws Exception {
        mvc.perform(formLogin())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void login_withBadCredentials_returns401() throws Exception {
        mvc.perform(formLogin().user("bad").password("user"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""))
                .andReturn();
    }

    @Test
    public void login_withCredentials_returns200_andToken() throws Exception {
        MvcResult mvcResult = mvc.perform(formLogin().user("joe").password("pass"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String accessToken = new JSONObject(response).getString("accessToken");

        mvc.perform(get("/api/info")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void login_withBody_returns200_andToken() throws Exception {
        String loginRequest = new JSONObject()
                .put("username", "joe")
                .put("password", "pass")
                .toString();

        MvcResult mvcResult = mvc.perform(post("/login")
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String accessToken = new JSONObject(response).getString("accessToken");

        mvc.perform(get("/api/info")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

    }
}
