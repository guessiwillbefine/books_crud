package assignment_five;

import assignment_five.entity.Author;
import assignment_five.entity.dto.AuthorDto;
import assignment_five.services.AuthorService;
import assignment_five.services.repositories.AuthorRepository;
import assignment_five.utils.exceptions.AuthorNotFoundException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookServiceTest {
    final Logger logger = LoggerFactory.getLogger("BookServiceTest");
    @MockBean
    AuthorRepository authorRepository;
    @Autowired
    AuthorService authorService;

    @Test
    void findByIdShouldReturnValidData() {
        logger.info("testing if service will return DTO with the same data as repository returned to it");
        Author author = new Author();
        author.setAge(10);
        author.setName("testName");
        author.setSurname("testSurname");

        when(authorRepository.findById(any(Long.class))).thenReturn(Optional.of(author));

        AuthorDto authorFromService = authorService.findById(1L);
        assertEquals(author.getName(), authorFromService.getName());
        assertEquals(author.getSurname(), authorFromService.getSurname());
        assertEquals(author.getAge(), authorFromService.getAge());
    }

    @Test
    void invalidIdShouldThrowException() {
        logger.info("testing if service method will throw correct exception if id is not present id database");
        assertThrows(AuthorNotFoundException.class, () -> authorService.findById(-1L));
    }
}
