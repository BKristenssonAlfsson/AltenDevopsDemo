package se.alten.demo.book;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import se.alten.demo.initialization.AbstractTestContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class BookControllerTest extends AbstractTestContainer {

    @Autowired
    private WebApplicationContext context;

    @BeforeAll
    void setup() throws Exception {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        this.mockMvc.perform(post("/login/add")
                .content("{ \"username\":\"test\",\"password\":\"test\",\"active\":1,\"role\":[\"USER\"] }")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        MvcResult result = this.mockMvc.perform(post("/login")
                .content("{ \"username\": \"test\", \"password\": \"test\" }")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        this.mockMvc.perform(post("/login/add")
                .content("{ \"username\":\"testa\",\"password\":\"testa\",\"active\":1,\"role\":[\"ADMIN\"] }")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        MvcResult result2 = this.mockMvc.perform(post("/login")
                .content("{ \"username\": \"testa\", \"password\": \"testa\" }")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        this.userToken = result.getResponse().getHeader("Authorization");
        this.adminToken = result2.getResponse().getHeader("Authorization");
    }

    @Test
    @Order(1)
    public void shouldNotBeAbleToAddABook() throws Exception {
        HttpHeaders httpHeaders = getHttpHeadersUser();

        this.mockMvc.perform(post("/book")
                .headers(httpHeaders)
                .content("{ \"author\": \"test\", \"title\": \"test\" }")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(2)
    public void shouldBeAbleToAddABook() throws Exception {
        HttpHeaders httpHeaders = getHttpHeadersAdmin();

        MvcResult result = this.mockMvc.perform(post("/book")
                .headers(httpHeaders)
                .content("{ \"author\": \"test\", \"title\": \"test\" }")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertEquals(200, result.getResponse().getStatus(), "We got 200 OK back");
    }

    @Test
    @Order(3)
    public void shouldHaveOneBook() throws Exception {
        HttpHeaders httpHeaders = getHttpHeadersUser();

        MvcResult result = this.mockMvc.perform(get("/book")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getContentAsString(), "[{\"id\":1,\"title\":\"test\",\"author\":\"test\"}]");

    }

    private HttpHeaders getHttpHeadersUser() {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", this.userToken);
        httpHeaders.add("Content-Type", "application/json");

        return httpHeaders;
    }

    private HttpHeaders getHttpHeadersAdmin() {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", this.adminToken);
        httpHeaders.add("Content-Type", "application/json");

        return httpHeaders;
    }
}