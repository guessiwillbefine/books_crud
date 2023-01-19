package assignment_five.services;

import assignment_five.entity.Author;
import assignment_five.entity.Book;
import assignment_five.entity.dto.AuthorDto;
import assignment_five.entity.dto.BookDto;
import assignment_five.entity.dto.SearchRequestDto;
import assignment_five.services.repositories.BookRepository;
import assignment_five.services.repositories.CrudService;
import assignment_five.utils.ApplicationConstants;
import assignment_five.utils.exceptions.AuthorNotFoundException;
import assignment_five.utils.exceptions.BookDuplicateException;
import assignment_five.utils.exceptions.BookNotFoundException;
import assignment_five.entity.dto.SearchRequestDto.Param;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService implements CrudService<BookDto, Long> {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Book findById(@NotNull Long id) {
        Optional<Book> byId = bookRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new BookNotFoundException(format("Book with id[%d] wasn't found", id));
    }

    @Override
    public void save(BookDto bookDto) {
        final AuthorDto authorDto = bookDto.getAuthor();
        Optional<Book> existingBook = bookRepository.findByNameAndAuthorName(bookDto.getName(),
                authorDto.getName(), authorDto.getSurname());
        if (existingBook.isPresent()) {
            throw new BookDuplicateException(format("Book with name[%s] already exists", bookDto.getName()));
        }
        Optional<Author> author = authorService.findAuthor(authorDto);
        if (author.isPresent()) {
            Book book = new Book();
            book.setName(bookDto.getName());
            book.setDescription(bookDto.getDescription());
            book.setYear(bookDto.getYear());
            book.setPageAmount(book.getPageAmount());
            book.setAuthor(author.get());
            bookRepository.save(book);
        } else {
            throw new AuthorNotFoundException(format("Author [%s %s] wasn't found", authorDto.getName(),
                    authorDto.getSurname()));
        }
    }

    @Override
    public void update(BookDto bookDto) {
        Optional<Book> foundedBook = bookRepository.findById(bookDto.getId());
        if (foundedBook.isPresent()) {
            Book bookToUpdate = foundedBook.get();
            Book updatedBook = updateBookFields(bookToUpdate, bookDto);
            bookRepository.save(updatedBook);
        }
    }

    @Override
    public void delete(BookDto bookDto) {
        final AuthorDto authorDto = bookDto.getAuthor();
        Optional<Book> book = bookRepository.findByNameAndAuthorName(bookDto.getName(),
                authorDto.getName(), authorDto.getSurname());
        book.ifPresent(bookRepository::delete);
    }

    @Transactional(readOnly = true)
    public Book findBook(BookDto bookDto) {
        final AuthorDto authorDto = bookDto.getAuthor();
        Optional<Book> foundedBook = bookRepository.findByNameAndAuthorName(bookDto.getName(),
                authorDto.getName(), authorDto.getSurname());
        if (foundedBook.isPresent()) {
            return foundedBook.get();
        }
        throw new BookNotFoundException(format("Book [%s] wasn't found", bookDto.getName()));
    }

    private Book updateBookFields(Book book, BookDto dto) {
        book.setDescription(dto.getDescription() == null ? book.getDescription() : dto.getDescription());
        book.setName(dto.getName() == null ? book.getName() : dto.getName());
        book.setYear(dto.getYear() == null ? book.getYear() : dto.getYear());
        book.setPageAmount(dto.getPageAmount() == null ? book.getPageAmount() : dto.getPageAmount());
        return book;
    }

    @Transactional(readOnly = true)
    public List<Book> getBookList(Integer pageSize, Integer pageNum) {
        return bookRepository.findAll(buildPageRequest(pageSize, pageNum))
                .getContent();
    }

    @Transactional(readOnly = true)
    public List<Book> getBookList(SearchRequestDto searchRequestDto) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
        final Root<Book> root = query.from(Book.class);

        int paramAmount = searchRequestDto.getParams().size();
        List<Param> params = searchRequestDto.getParams();
        Predicate[] predicates = new Predicate[paramAmount];

        for (int i = 0; i < predicates.length; i++) {
            Param param = params.get(i);
            predicates[i] = criteriaBuilder.equal(root.get(param.name()), param.value());
        }
        query.select(root).where(predicates);
        return entityManager.createQuery(query).getResultList();
    }

    private Pageable buildPageRequest(Integer pageSize, Integer pageNum) {
        int size = (pageSize == null || pageSize == 0) ? ApplicationConstants.Pageable.DEFAULT_PAGE_SIZE : pageSize;
        int page = (pageNum == null || pageNum < 0) ? ApplicationConstants.Pageable.DEFAULT_PAGE : pageNum;
        return PageRequest.of(page, size);
    }
}