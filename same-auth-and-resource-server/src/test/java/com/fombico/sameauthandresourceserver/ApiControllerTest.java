package com.fombico.sameauthandresourceserver;

import com.fombico.sameauthandresourceserver.utils.WithMockOAuth2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ApiController.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MethodSecurityConfig.class)
})
public class ApiControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    public void api_returns401_whenUnauthorized() throws Exception {
        mvc.perform(get("/api"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void info_needsNoAuthority() throws Exception {
        mvc.perform(get("/api/info"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "user")
    public void user_returns200_whenUserAuthorityIsPresent() throws Exception {
        mvc.perform(get("/api/user"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "readonly")
    public void user_returns403_whenUserAuthorityIsAbsent() throws Exception {
        mvc.perform(get("/api/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void admin_returns200_whenAdminAuthorityIsPresent() throws Exception {
        mvc.perform(get("/api/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "readonly")
    public void admin_returns403_whenAdminAuthorityIsAbsent() throws Exception {
        mvc.perform(get("/api/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockOAuth2(scopes = "user")
    public void userScope_returns200_whenUserScopeIsPresent() throws Exception {
        mvc.perform(get("/api/scope/user"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockOAuth2(scopes = "readonly")
    public void userScope_returns403_whenUserScopeIsAbsent() throws Exception {
        mvc.perform(get("/api/scope/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockOAuth2(scopes = "admin")
    public void adminScope_returns200_whenAdminScopeIsPresent() throws Exception {
        mvc.perform(get("/api/scope/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockOAuth2(scopes = "readonly")
    public void adminScope_returns403_whenAdminScopeIsAbsent() throws Exception {
        mvc.perform(get("/api/scope/admin"))
                .andExpect(status().isForbidden());
    }
}