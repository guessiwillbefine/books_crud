package assignment_five;

import assignment_five.entity.Author;
import assignment_five.services.AuthorService;
import assignment_five.services.repositories.AuthorRepository;
import assignment_five.utils.exceptions.AuthorNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class BookServiceTest {
    @MockBean
    AuthorRepository authorRepository;
    @Autowired
    AuthorService authorService;

    @Test
    void findByIdShouldReturnValidData() {
        Author author = new Author();
        author.setAge(10);
        author.setName("testName");
        author.setSurname("testSurname");

        when(authorRepository.findById(any(Long.class))).thenReturn(Optional.of(author));

        Author authorFromService = authorService.findById(1L);
        assertEquals(author.getName(), authorFromService.getName());
        assertEquals(author.getSurname(), authorFromService.getSurname());
        assertEquals(author.getAge(), authorFromService.getAge());
    }

    @Test
    void invalidIdShouldThrowException() {
        assertThrows(AuthorNotFoundException.class, () -> authorService.findById(-1L));
    }
}
