package assignment_five;

import assignment_five.entity.dto.AuthorDto;
import assignment_five.entity.dto.BookDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ApiTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();
    private final AuthorDto testAuthor = AuthorDto.builder()
            .name("testName")
            .surname("testSurname")
            .age(21).build();
    private final BookDto testBook = BookDto.builder()
            .name("testBook")
            .description("very long description")
            .year(2000)
            .pageAmount(201)
            .author(testAuthor).build();

    @Test
    void testBookAndAuthorCreate() throws Exception {

        mockMvc.perform(post("/authors/create").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testAuthor)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/books/create").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testBook)))
                .andExpect(status().isCreated());
    }

    @Test
    void nullBodyShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/books/create").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/authors/create").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void NonExistingEntitiesShouldReturnNotFound() throws Exception {
        final AuthorDto nonExitingAuthor = AuthorDto.builder()
                .name("qweqwe")
                .surname("qweqwe")
                .build();
        final BookDto nonExitingBook = BookDto.builder()
                .name("wqewqek")
                .description("veqwdwqdwqption")
                .author(testAuthor).build();

        mockMvc.perform(get("/books").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(nonExitingBook)))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/authors").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(nonExitingAuthor)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGettingBooksAndAuthors() throws Exception {

        BookDto dto = BookDto.builder().name("the gunslinger")
                .author(AuthorDto.builder().name("steven").surname("king").build())
                .build();

        var response = mockMvc.perform(get("/books").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk()).andReturn();

        BookDto receivedBook = mapper.readValue(response.getResponse().getContentAsString(), BookDto.class);
        assertEquals(dto.getName().toLowerCase(), receivedBook.getName().toLowerCase());
    }

    @Test
    void testDeletingBooksAndAuthors() throws Exception {

        BookDto dto = BookDto.builder().name("The Name of Rose")
                .author(AuthorDto.builder().name("Umberto").surname("Eco").build())
                .build();

        mockMvc.perform(delete("/books/delete").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    //@Disabled("need to fix updating")
    void testBookAndAuthorUpdate() throws Exception {

        BookDto dto = BookDto.builder().name("The Name of Rose")
                .author(AuthorDto.builder().name("Umberto").surname("Eco").build())
                .build();

        var book = mockMvc.perform(get("/books").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk()).andReturn();

        BookDto updated = mapper.readValue(book.getResponse().getContentAsString(), BookDto.class);
        final String newName = "The Name of Rose 2";

        updated.setName(newName);
        mockMvc.perform(patch("/books/update").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        var response = mockMvc.perform(get("/books").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isOk()).andReturn();

        BookDto receivedBook = mapper.readValue(response.getResponse().getContentAsString(), BookDto.class);

        assertEquals(updated.getName(), receivedBook.getName());
    }
}
