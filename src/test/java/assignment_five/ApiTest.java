package assignment_five;

import assignment_five.entity.dto.AuthorDto;
import assignment_five.entity.dto.BookDto;
import assignment_five.entity.dto.SearchRequestDto;
import assignment_five.entity.dto.SearchRequestDto.Param;
import assignment_five.services.repositories.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional // <--- to  make rollback after every test
@SpringBootTest
@ActiveProfiles("test") // <--- spring profile from application.yaml
@AutoConfigureMockMvc
class ApiTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private final MediaType mediaJson = MediaType.APPLICATION_JSON;
    private final AuthorDto nonExistingAuthor = AuthorDto.builder()
            .name("testName").surname("testSurname")
            .age(21).build();
    private final BookDto nonExistingBook = BookDto.builder()
            .name("testBook").description("very long description")
            .year(2000).pageAmount(201)
            .author(nonExistingAuthor).build();
    private final AuthorDto existingAuthor = AuthorDto.builder()
            .name("Umberto")
            .surname("Eco").build();
    private final BookDto existingBook = BookDto.builder()
            .name("The Name of Rose")
            .author(existingAuthor).build();

    private final AuthorDto invalidAuthor = AuthorDto.builder()
            .name("very large non-existing name for author")
            .surname("surname").age(15000)
            .build();
    private final BookDto invalidBook = BookDto.builder()
            .name("too large invalid name of book...").description("some description")
            .year(2025).pageAmount(-10)
            .author(invalidAuthor).build();

    private final SearchRequestDto validSearchRequest = new SearchRequestDto(
            List.of(
                    new Param("name", "The Name of Rose"),
                    new Param("year", "1980")));
    private final SearchRequestDto invalidSearchRequest = new SearchRequestDto(
            List.of(
                    new Param("some trash", "Gunslinger"),
                    new Param("what?", "¯\\_(ツ)_/¯")));

    @Test
    @DisplayName("test searching by valid Book entity fields")
    void testSearchingByParams() throws Exception {
        String requestBody = mapper.writeValueAsString(validSearchRequest);
        mockMvc.perform(get("/books/search").contentType(mediaJson)
                .content(requestBody)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("test searching with invalid Book entity fields")
    void testSearchingByIncorrectParams() throws Exception {
        String requestBody = mapper.writeValueAsString(invalidSearchRequest);
        mockMvc.perform(get("/books/search").contentType(mediaJson)
                        .content(requestBody)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("response").exists());
    }

    @Test
    @DisplayName("book and author creation operations test")
    void testBookAndAuthorCreate() throws Exception {
        final String bookJson = mapper.writeValueAsString(nonExistingBook);
        final String authJson = mapper.writeValueAsString(nonExistingAuthor);

        mockMvc.perform(post("/authors/create")
                        .contentType(mediaJson)
                        .content(authJson))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/books/create").contentType(mediaJson)
                        .content(bookJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("test if duplicates are throwing exception with correct status code")
    void testBookAndAuthorCreateDuplicationError() throws Exception {

        mockMvc.perform(post("/authors/create")
                        .contentType(mediaJson)
                        .content(mapper.writeValueAsString(nonExistingAuthor)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/authors/create")
                        .contentType(mediaJson)
                        .content(mapper.writeValueAsString(nonExistingAuthor)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("response").exists());

        mockMvc.perform(post("/books/create")
                        .contentType(mediaJson)
                        .content(mapper.writeValueAsString(nonExistingBook)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/books/create")
                        .contentType(mediaJson)
                        .content(mapper.writeValueAsString(nonExistingBook)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("response").exists());
    }

    @Test
    @DisplayName("test validation error will return bad request")
    void nullBodyShouldReturnBadRequest() throws Exception {
        final String dtoJson = mapper.writeValueAsString(invalidBook);
        mockMvc.perform(post("/books/create").contentType(mediaJson)
                        .content(dtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("response").exists());

        mockMvc.perform(post("/authors/create").contentType(mediaJson)
                        .content(dtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("response").exists());
    }

    @Test
    @DisplayName("test getting data that isn't present in DB")
    void nonExistingEntitiesShouldReturnNotFound() throws Exception {
        final String dtoJson = mapper.writeValueAsString(nonExistingBook);
        final String existingAuthorName = nonExistingAuthor.getName();
        final String existingAuthorSurname = nonExistingAuthor.getSurname();

        mockMvc.perform(get("/books").contentType(mediaJson)
                        .content(dtoJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("response").exists());

        mockMvc.perform(get("/authors")
                        .param("name", existingAuthorName)
                        .param("surname", existingAuthorSurname))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("response").exists());
    }

    @Test
    @DisplayName("test getting data from DB")
    void testGettingBooksAndAuthors() throws Exception {
        final String requestBody = mapper.writeValueAsString(existingBook);
        final var response = mockMvc.perform(get("/books")
                        .contentType(mediaJson)
                        .content(requestBody))
                .andExpect(status().isOk()).andReturn();

        final String responseBody = response.getResponse().getContentAsString();
        final BookDto receivedBook = mapper.readValue(responseBody, BookDto.class);

        assertEquals(existingBook.getName().toLowerCase(), receivedBook.getName().toLowerCase());
    }

    @Test
    @DisplayName("testing updating books")
    void testBookUpdate() throws Exception {
        final String newBookName = "The Name of Rose 2";

        final var book = mockMvc.perform(get("/books")
                        .contentType(mediaJson)
                        .content(mapper.writeValueAsString(existingBook)))
                .andExpect(status().isOk()).andReturn();

        final String responseBody = book.getResponse().getContentAsString();
        final BookDto updated = mapper.readValue(responseBody, BookDto.class);
        updated.setName(newBookName);

        mockMvc.perform(patch("/books/update")
                        .contentType(mediaJson)
                        .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        final var response = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isOk()).andReturn();

        final String updatedResponse = response.getResponse().getContentAsString();
        final BookDto receivedBook = mapper.readValue(updatedResponse, BookDto.class);

        assertEquals(updated.getName(), receivedBook.getName());
    }

    @Test
    @DisplayName("test deleting entities")
    void testDeletingBooksAndAuthors() throws Exception {
        final String existingBookJson = mapper.writeValueAsString(existingBook);
        mockMvc.perform(delete("/books/delete")
                        .contentType(mediaJson)
                        .content(existingBookJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("testing updating authors")
    void testAuthorUpdate() throws Exception {
        final String existingAuthorName = existingAuthor.getName();
        final String existingAuthorSurname = existingAuthor.getSurname();
        final String newAuthorName = "Steven Prince";

        final var author = mockMvc.perform(get("/authors")
                        .param("name", existingAuthorName)
                        .param("surname", existingAuthorSurname))
                .andExpect(status().isOk()).andReturn();

        final String responseBody = author.getResponse().getContentAsString();
        final AuthorDto updated = mapper.readValue(responseBody, AuthorDto.class);
        updated.setName(newAuthorName);

        mockMvc.perform(patch("/authors/update")
                        .contentType(mediaJson)
                        .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        final var response = mockMvc.perform(get("/authors")
                        .param("name", updated.getName())
                        .param("surname", updated.getSurname()))
                .andExpect(status().isOk()).andReturn();

        final AuthorDto receivedAuthor = mapper.readValue(response.getResponse().getContentAsString(), AuthorDto.class);

        assertEquals(updated.getName(), receivedAuthor.getName());
    }

    @Test
    @DisplayName("getting all authors test")
    void getAllShouldReturnList() throws Exception {
        final var response = mockMvc.perform(get("/authors/all")).andExpect(status().isOk()).andReturn();
        final String responseBody = response.getResponse().getContentAsString();
        var authorDtoList = mapper.readValue(responseBody, List.class);
        assertNotNull(authorDtoList);
        assertFalse(authorDtoList.isEmpty());
    }

    @Test
    @DisplayName("test getting authors by id")
    void getByIdShouldReturnAuthorDto() throws Exception {
        final var response = mockMvc.perform(get("/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("surname").exists())
                .andReturn();
        final String responseBody = response.getResponse().getContentAsString();
        final var authorDtoList = mapper.readValue(responseBody, AuthorDto.class);
        assertNotNull(authorDtoList);
    }

    @Test
    @DisplayName("getting all authors test")
    void getByIdShouldReturnBookDto() throws Exception {
        final var response = mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("description").exists())
                .andReturn();
        final String responseBody = response.getResponse().getContentAsString();
        final var BookDtoList = mapper.readValue(responseBody, BookDto.class);
        assertNotNull(BookDtoList);
    }
}