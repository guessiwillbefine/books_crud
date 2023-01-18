package assignment_five;

import assignment_five.entity.Author;
import assignment_five.entity.Book;
import assignment_five.entity.dto.AuthorDto;
import assignment_five.entity.dto.BookDto;
import assignment_five.services.AuthorService;
import assignment_five.services.BookService;
import assignment_five.services.repositories.BookRepository;
import assignment_five.utils.exceptions.BookDuplicateException;
import assignment_five.utils.exceptions.BookNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest
class BookServiceTest {
    @Autowired
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private BookRepository bookRepository;

    private final AuthorDto mockBook = AuthorDto.builder()
            .name("Umberto")
            .surname("Eco").build();
    private final BookDto mockAuthor = BookDto.builder()
            .name("The Name of Rose")
            .author(mockBook).build();

    @Test
    void findByIdShouldReturnEntityIfPresent(){
        final Book book = new Book();
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        assertEquals(book, bookService.findById(1L));
    }
    @Test
    void findByIdShouldThrowExceptionIfNotPresent(){
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.findById(1L));
    }
    @Test
    void testSaveCaseWhenShouldBeException(){
        final Book book = new Book();
        final AuthorDto existingAuthor = AuthorDto.builder()
                .name("Umberto")
                .surname("Eco").build();
        final BookDto existingBook = BookDto.builder()
                .name("The Name of Rose")
                .author(existingAuthor).build();

        when(bookRepository.findByNameAndAuthorName(anyString(),anyString(),anyString()))
                .thenReturn(Optional.of(book));

        assertThrows(BookDuplicateException.class, () -> bookService.save(existingBook));
    }
    @Test
    void testSaveCaseWhenShouldBeSaved(){
        final Author author = new Author();


        when(bookRepository.findByNameAndAuthorName(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(authorService.findAuthor(mockBook)).thenReturn(Optional.of(author));

        assertDoesNotThrow(() -> bookService.save(mockAuthor));
    }
    @Test
    void testFindingBooks(){
        when(bookRepository.findByNameAndAuthorName(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.findBook(mockAuthor));
    }
    @Test
    void testFindingBooksThrowException(){
        when(bookRepository.findByNameAndAuthorName(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(new Book()));

        assertDoesNotThrow(() -> bookService.findBook(mockAuthor));
    }
}
