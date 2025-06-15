package org.example.springcakemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springcakemanager.dto.CakeDTO;
import org.example.springcakemanager.model.CakeEntity;
import org.example.springcakemanager.repository.CakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CakeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CakeRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ADMIN_USER = "testadmin";
    private static final String ADMIN_PASS = "testadmin123";
    private static final String USER_USER = "testuser";
    private static final String USER_PASS = "testuser123";

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void addCake_thenRetrieveIt() throws Exception {
        CakeDTO dto = new CakeDTO(null, "Choco", "Rich", "https://img.com/c.png");

        String json = objectMapper.writeValueAsString(dto);

        String response = mockMvc.perform(post("/api/v1/cakes")
                        .with(httpBasic(ADMIN_USER, ADMIN_PASS))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CakeDTO created = objectMapper.readValue(response, CakeDTO.class);

        mockMvc.perform(get("/api/v1/cakes/" + created.id())
                        .with(httpBasic(USER_USER, USER_PASS)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Choco"));
    }

    @Test
    void deleteCake_thenNotFound() throws Exception {
        CakeEntity entity = repository.save(new CakeEntity(null, "Vanilla", "Nice", "https://img.com/v.png"));

        mockMvc.perform(delete("/api/v1/cakes/" + entity.getId())
                        .with(httpBasic(ADMIN_USER, ADMIN_PASS)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/cakes/" + entity.getId())
                        .with(httpBasic(USER_USER, USER_PASS)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCake_thenUpdatedFieldsReturned() throws Exception {
        CakeEntity entity = repository.save(new CakeEntity(null, "Old", "Desc", "https://img.com/old.png"));

        CakeDTO update = new CakeDTO(null, "New", "NewDesc", "https://img.com/new.png");
        String json = objectMapper.writeValueAsString(update);

        mockMvc.perform(put("/api/v1/cakes/" + entity.getId())
                        .with(httpBasic(ADMIN_USER, ADMIN_PASS))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New"));

        mockMvc.perform(get("/api/v1/cakes/" + entity.getId())
                        .with(httpBasic(USER_USER, USER_PASS)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("NewDesc"));
    }

    @Test
    void listCakes_withPagination_returnsPagedResults() throws Exception {
        repository.save(new CakeEntity(null, "Cake1", "Desc1", "img1"));
        repository.save(new CakeEntity(null, "Cake2", "Desc2", "img2"));
        repository.save(new CakeEntity(null, "Cake3", "Desc3", "img3"));

        mockMvc.perform(get("/api/v1/cakes?page=0&size=2")
                        .with(httpBasic(USER_USER, USER_PASS)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    void addCake_withInvalidData_returnsBadRequest() throws Exception {
        CakeDTO dto = new CakeDTO(null, "", "D", "not-a-url");
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/cakes")
                        .with(httpBasic(ADMIN_USER, ADMIN_PASS))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCake_withoutAuth_returnsUnauthorized() throws Exception {
        CakeEntity entity = repository.save(new CakeEntity(null, "T", "D", "img"));

        mockMvc.perform(get("/api/v1/cakes/" + entity.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getCake_whenNotExists_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/cakes/999")
                        .with(httpBasic(USER_USER, USER_PASS)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCake_whenNotExists_returnsNotFound() throws Exception {
        CakeDTO update = new CakeDTO(null, "Missing", "Desc", "https://img.com/missing.png");
        String json = objectMapper.writeValueAsString(update);

        mockMvc.perform(put("/api/v1/cakes/999")
                        .with(httpBasic(ADMIN_USER, ADMIN_PASS))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCake_whenNotExists_returnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/cakes/999")
                        .with(httpBasic(ADMIN_USER, ADMIN_PASS)))
                .andExpect(status().isNotFound());
    }

    @Test
    void listCakes_invalidPageSize_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/cakes?page=0&size=0")
                        .with(httpBasic(USER_USER, USER_PASS)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addCake_withoutAuth_returnsUnauthorized() throws Exception {
        CakeDTO dto = new CakeDTO(null, "T", "D", "img");
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/cakes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }
}
