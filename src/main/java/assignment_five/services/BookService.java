package assignment_five.services;

import assignment_five.entity.Author;
import assignment_five.entity.Book;
import assignment_five.entity.dto.AuthorDto;
import assignment_five.entity.dto.BookDto;
import assignment_five.services.repositories.BookRepository;
import assignment_five.utils.exceptions.AuthorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;

    @Transactional
    public void create(BookDto bookDto) {
        Book book = new Book();
        book.setName(bookDto.getName());
        book.setDescription(bookDto.getDescription());
        AuthorDto authorDto = bookDto.getAuthor();
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
    public void delete(BookDto bookDto) {
        AuthorDto authorDto = bookDto.getAuthor();
        Optional<Book> book = bookRepository.findByNameAndAuthorName(bookDto.getName(),
                authorDto.getName(), authorDto.getSurname());
        book.ifPresent(bookRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<BookDto> getBookList(Integer pageSize, Integer pageNum, String fieldSort) {
        List<Book> books = bookRepository.findAll(buildPageRequest(pageSize, pageNum, fieldSort))
                .getContent();
        return books.stream().map(book -> BookDto.builder()
                .name(book.getName())
                .description(book.getDescription())
                .author(AuthorDto.builder()
                        .name(book.getAuthor().getName())
                        .surname(book.getAuthor().getSurname())
                        .age(book.getAuthor().getAge())
                        .build())
                .build()).toList(); //todo ..........
    }

    private Pageable buildPageRequest(Integer pageSize, Integer pageNum, String sort) {
        int size = (pageSize == null || pageSize == 0) ? 15 : pageSize;
        int page = (pageNum < 0) ? 0 : pageNum;
        String sortField = isValid(sort) ? sort : "name";
        return PageRequest.of(page, size, Sort.by(sortField));
    }

    private boolean isValid(String s) {
        return s.equals("name") || s.equals("year");
    }
}
