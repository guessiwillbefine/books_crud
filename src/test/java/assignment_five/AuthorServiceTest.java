package assignment_five;


import assignment_five.entity.Author;
import assignment_five.entity.dto.AuthorDto;
import assignment_five.services.AuthorService;
import assignment_five.services.repositories.AuthorRepository;
import assignment_five.utils.exceptions.AuthorDuplicateException;
import assignment_five.utils.exceptions.AuthorNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorServiceTest {
    @MockBean
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorService authorService;

    @Test
    void findByIdShouldReturnValidData() {
        Author author = new Author();
        author.setAge(10);
        author.setName("testName");
        author.setSurname("testSurname");

        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));

        final var authorFromService = authorService.findById(1L);
        assertEquals(author.getName(), authorFromService.getName());
        assertEquals(author.getSurname(), authorFromService.getSurname());
        assertEquals(author.getAge(), authorFromService.getAge());
    }

    @Test
    void invalidIdShouldThrowException() {
        assertThrows(AuthorNotFoundException.class, () -> authorService.findById(-1L));
    }

    @Test
    void getAllAuthorsShouldReturnList() {
        final List<Author> authorList = new ArrayList<>();
        when(authorRepository.findAll()).thenReturn(authorList);
        final var result = authorService.getAll();

        assertEquals(authorList, result);
    }

    @Test
    void findByDtoShouldReturnEntity() {
        final Author author = new Author();
        when(authorRepository.findByFullName(anyString(), anyString()))
                .thenReturn(Optional.of(author));
        final var result = authorService.findAuthor(AuthorDto.builder()
                .name("somename")
                .surname("somesurname").build());
        assertEquals(result.get(), author);
    }

    @Test
    void testSavingEntitiesThatPresent() {
        final Author author = new Author();
        final AuthorDto authorDto = AuthorDto.builder()
                .name("somename")
                .surname("somesurname").build();
        when(authorRepository.findByFullName(anyString(), anyString()))
                .thenReturn(Optional.of(author));
        assertThrows(AuthorDuplicateException.class, ()-> authorService.save(authorDto));
    }
    @Test
    void testSavingEntitiesThatNotPresent() {
        final AuthorDto authorDto = AuthorDto.builder()
                .name("somename")
                .surname("somesurname").build();
        when(authorRepository.findByFullName(anyString(), anyString()))
                .thenReturn(Optional.empty());
        assertDoesNotThrow(()-> authorService.save(authorDto));
    }
}
