package com.fombico.contentnegotiation;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final String BASE_URL = "/v1/person";

    @Test
    public void getPerson_returnsJson() throws Exception {
        mvc.perform(request(HttpMethod.GET, BASE_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Joe")))
                .andExpect(jsonPath("$.age", is(20)));
    }

    @Test
    public void getPerson_returnsXml() throws Exception {
        mvc.perform(request(HttpMethod.GET, BASE_URL)
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
                .andExpect(xpath("person/@name").string("Joe"))
                .andExpect(xpath("person/age").number(20.0));
    }

    @Test
    public void addPerson_acceptsJsonRequest() throws Exception {
        mvc.perform(request(HttpMethod.POST, BASE_URL)
                .content("{\"name\":\"Joe\",\"age\":20}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(xpath("person/@name").string("Joe"))
                .andExpect(xpath("person/age").number(20.0));
    }

    @Test
    public void addPerson_acceptsXmRequest() throws Exception {
        mvc.perform(request(HttpMethod.POST, BASE_URL)
                .content("<person name=\"Joe\"><age>20</age></person>")
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Joe")))
                .andExpect(jsonPath("$.age", is(20)));
    }

    @Test
    public void postPerson_returnsBadRequest_whenJsonRequestIsInvalid() throws Exception {
        mvc.perform(request(HttpMethod.POST, BASE_URL)
                .content("{\"age\":20}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(request(HttpMethod.POST, BASE_URL)
                .content("{\"name\":\"Joe\",\"age\":-100}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postPerson_returnsBadRequest_whenXmlRequestIsInvalid() throws Exception {
        mvc.perform(request(HttpMethod.POST, BASE_URL)
                .content("<age>20</age>")
                .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isBadRequest());

        mvc.perform(request(HttpMethod.POST, BASE_URL)
                .content("<person name=\"Joe\"><age>-100</age></person>")
                .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isBadRequest());
    }
}