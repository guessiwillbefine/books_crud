package assignment_five.services;

import assignment_five.entity.Author;
import assignment_five.entity.Book;
import assignment_five.entity.dto.AuthorDto;
import assignment_five.entity.dto.BookDto;
import assignment_five.services.repositories.BookRepository;
import assignment_five.utils.exceptions.AuthorNotFoundException;
import assignment_five.utils.exceptions.BookDuplicateException;
import assignment_five.utils.exceptions.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;

    @Transactional(readOnly = true)
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }
    @Transactional(readOnly = true)
    public Book findBook(BookDto bookDto) {
        AuthorDto authorDto = bookDto.getAuthor();
        Optional<Book> foundedBook = bookRepository.findByNameAndAuthorName(bookDto.getName(),
                authorDto.getName(), authorDto.getSurname());
        if (foundedBook.isPresent()) {
            return foundedBook.get();
        }
        throw new BookNotFoundException(String.format("Book [%s] wasn't found", bookDto.getName()));
    }

    @Transactional
    public void create(BookDto bookDto) {
        AuthorDto authorDto = bookDto.getAuthor();
        Optional<Book> existingBook = bookRepository.findByNameAndAuthorName(bookDto.getName(),
                authorDto.getName(), authorDto.getSurname());
        if (existingBook.isPresent()) {
            throw new BookDuplicateException(String.format("Book with name [%s] already exists", bookDto.getName()));
        }
        Book book = new Book();
        book.setName(bookDto.getName());
        book.setDescription(bookDto.getDescription());
        book.setYear(bookDto.getYear());
        book.setPageAmount(book.getPageAmount());
        Optional<Author> author = authorService.findByNameAndSurname(authorDto.getName(), authorDto.getSurname());
        System.err.println(author.isPresent());
        if (author.isEmpty()) {
            throw new AuthorNotFoundException(String.format("Author[%s %s] was not found",
                    authorDto.getName(),
                    authorDto.getSurname()));
        }
        book.setAuthor(author.get());
        bookRepository.save(book);
    }

    @Transactional
    public void update(BookDto bookDto) {
        Optional<Book> book = bookRepository.findById(bookDto.getId());
        if (book.isPresent()) {
            Book bookToUpdate = book.get();
            bookToUpdate.setDescription(bookDto.getDescription() == null ? bookToUpdate.getDescription() : bookDto.getDescription());
            bookToUpdate.setName(bookDto.getName() == null ? bookToUpdate.getName() : bookDto.getName());
            bookToUpdate.setYear(bookDto.getYear() == null ? bookToUpdate.getYear() : bookDto.getYear());
            bookToUpdate.setPageAmount(bookDto.getPageAmount() == null ? bookToUpdate.getPageAmount() : bookDto.getPageAmount());
            bookRepository.save(bookToUpdate);
        }
    }

    @Transactional
    public void delete(BookDto bookDto) {
        AuthorDto authorDto = bookDto.getAuthor();
        Optional<Book> book = bookRepository.findByNameAndAuthorName(bookDto.getName(),
                authorDto.getName(), authorDto.getSurname());
        book.ifPresent(bookRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<BookDto> getBookList(Integer pageSize, Integer pageNum) {
        List<Book> books = bookRepository.findAll(buildPageRequest(pageSize, pageNum))
                .getContent();
        return books.stream().map(book -> BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .description(book.getDescription())
                .pageAmount(book.getPageAmount())
                .year(book.getYear())
                .author(AuthorDto.builder()
                        .name(book.getAuthor().getName())
                        .surname(book.getAuthor().getSurname())
                        .age(book.getAuthor().getAge())
                        .build())
                .build()).toList(); //todo ..........
    }

    private Pageable buildPageRequest(Integer pageSize, Integer pageNum) {
        int size = (pageSize == null || pageSize == 0) ? 15 : pageSize;
        int page = (pageNum < 0) ? 0 : pageNum;
        return PageRequest.of(page, size);
    }
}
