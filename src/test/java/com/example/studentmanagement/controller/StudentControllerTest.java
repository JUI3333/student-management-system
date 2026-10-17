package com.example.studentmanagement.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Integration tests: real Spring app + real H2 database, driven through HTTP-like requests.
 * The table is emptied before each test so tests never depend on each other.
 */
@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    private static final String BASE_URL = "/api/students";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    private String json(String firstName, String lastName, String email, String course) {
        return """
                {"firstName": "%s", "lastName": "%s", "email": "%s", "course": "%s"}
                """.formatted(firstName, lastName, email, course);
    }

    private ResultActions createStudent(String email) throws Exception {
        return mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json("Asha", "Patil", email, "Information Technology")));
    }

    /** Creates a student and returns the generated id. */
    private long createAndGetId(String email) throws Exception {
        MvcResult result = createStudent(email).andExpect(status().isCreated()).andReturn();
        String location = result.getResponse().getHeader("Location");
        return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
    }

    // ---------- Add student ----------

    @Test
    void createValidStudentReturns201() throws Exception {
        createStudent("asha@example.com")
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName", is("Asha")))
                .andExpect(jsonPath("$.email", is("asha@example.com")));
    }

    @Test
    void createWithMissingFieldsReturns400AndFieldErrors() throws Exception {
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.fieldErrors.firstName").exists())
                .andExpect(jsonPath("$.fieldErrors.lastName").exists())
                .andExpect(jsonPath("$.fieldErrors.email").exists())
                .andExpect(jsonPath("$.fieldErrors.course").exists());
    }

    @Test
    void createWithBlankFirstNameReturns400() throws Exception {
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(json("   ", "Patil", "asha@example.com", "IT")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.firstName", is("First name is required")));
    }

    @Test
    void createWithInvalidEmailReturns400() throws Exception {
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(json("Asha", "Patil", "not-an-email", "IT")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email", is("Email must be a valid address")));
    }

    @Test
    void createWithMalformedJsonReturns400() throws Exception {
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("{not json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithDuplicateEmailReturns409() throws Exception {
        createStudent("asha@example.com").andExpect(status().isCreated());

        // Same address with different capitalisation must still count as a duplicate.
        createStudent("ASHA@example.com")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)));
    }

    // ---------- Retrieve ----------

    @Test
    void getAllReturnsEveryStudent() throws Exception {
        createStudent("one@example.com").andExpect(status().isCreated());
        createStudent("two@example.com").andExpect(status().isCreated());

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getAllReturnsEmptyListWhenNoStudents() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getByIdReturnsStudent() throws Exception {
        long id = createAndGetId("asha@example.com");

        mockMvc.perform(get(BASE_URL + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.email", is("asha@example.com")));
    }

    @Test
    void getByUnknownIdReturns404() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void getByNonNumericIdReturns400() throws Exception {
        mockMvc.perform(get(BASE_URL + "/abc")).andExpect(status().isBadRequest());
    }

    // ---------- Update ----------

    @Test
    void updateChangesStudentDetails() throws Exception {
        long id = createAndGetId("asha@example.com");

        mockMvc.perform(put(BASE_URL + "/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content(json("Asha", "Deshmukh", "asha@example.com", "Computer Engineering")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is("Deshmukh")))
                .andExpect(jsonPath("$.course", is("Computer Engineering")));

        mockMvc.perform(get(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.lastName", is("Deshmukh")));
    }

    @Test
    void updateUnknownStudentReturns404() throws Exception {
        mockMvc.perform(put(BASE_URL + "/9999").contentType(MediaType.APPLICATION_JSON)
                        .content(json("Asha", "Patil", "asha@example.com", "IT")))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateWithInvalidDataReturns400() throws Exception {
        long id = createAndGetId("asha@example.com");

        mockMvc.perform(put(BASE_URL + "/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content(json("Asha", "Patil", "bad-email", "IT")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateToEmailOfAnotherStudentReturns409() throws Exception {
        createStudent("first@example.com").andExpect(status().isCreated());
        long secondId = createAndGetId("second@example.com");

        mockMvc.perform(put(BASE_URL + "/" + secondId).contentType(MediaType.APPLICATION_JSON)
                        .content(json("Asha", "Patil", "first@example.com", "IT")))
                .andExpect(status().isConflict());
    }

    // ---------- Delete ----------

    @Test
    void deleteRemovesStudentAndReturns204() throws Exception {
        long id = createAndGetId("asha@example.com");

        mockMvc.perform(delete(BASE_URL + "/" + id)).andExpect(status().isNoContent());
        mockMvc.perform(get(BASE_URL + "/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void deleteUnknownStudentReturns404() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/9999")).andExpect(status().isNotFound());
    }
}
