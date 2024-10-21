package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.services.BeerService;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

@SpringBootTest
class BeerRestControllerIT {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @MockBean
    BeerService beerService;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    //This method would fail if we @MockBean the service class as that would mean no(blank mocked) response from service -> controller
    @Test
    @WithMockUser("scott")
    void listBeers() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer")
                        /*.queryParam("beerName", "IPA")
                        .queryParam("beerStyle", "IPA")
                        .queryParam("showInventoryOnHand", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50")*/)
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()", Is.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].beerName", Is.is("Mango Bobs")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].beerStyle", Is.is("PALE_ALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].upc", Is.is("0083783375213")));
    }

    //This method would fail if we @MockBean the service class as that would mean no(blank mocked) response from service -> controller
    @Test
    void listBeersWithHttpBasic() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("scott", "tiger")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()", Is.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].beerName", Is.is("Mango Bobs")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].beerStyle", Is.is("PALE_ALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].upc", Is.is("0083783375213")));
    }

    @Test
    void checkRootUrlOpenToAll() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getBeerById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/6ad88cfb-9fa1-4118-ae05-57cb82bf9ca3"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getBeerByIdWithUser() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/6ad88cfb-9fa1-4118-ae05-57cb82bf9ca3")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "password")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getBeerByIdWithScott() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/6ad88cfb-9fa1-4118-ae05-57cb82bf9ca3")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("scott", "tiger")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getBeerByIdWithJohn() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/6ad88cfb-9fa1-4118-ae05-57cb82bf9ca3")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("John", "Doe")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getBeerByIdWithJane() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/6ad88cfb-9fa1-4118-ae05-57cb82bf9ca3")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("Jane", "Smith")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void listBeersWithoutMockUser() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}