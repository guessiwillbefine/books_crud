package assignment_five.controllers;

import assignment_five.entity.Book;
import assignment_five.entity.dto.AuthorDto;
import assignment_five.entity.dto.BookDto;
import assignment_five.services.BookService;
import assignment_five.utils.exceptions.BookNotFoundException;
import assignment_five.utils.exceptions.BookValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public BookDto findBook(@RequestBody BookDto bookDto){
        Book book = bookService.findBook(bookDto);
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .description(book.getDescription())
                .author(AuthorDto.builder()
                        .name(book.getAuthor().getName())
                        .surname(book.getAuthor().getSurname())
                        .age(book.getAuthor().getAge())
                        .build())
                .build();
    }
    @GetMapping("/all")
    public List<BookDto> getAllBooks(@RequestParam(value = "size", required = false) Integer pageSize,
                                     @RequestParam(value = "page", required = false) Integer pageNum) {
        //todo
        return bookService.getBookList(pageSize, pageNum);
    }

    @GetMapping("/{id}")
    public BookDto findById(@PathVariable("id") long id) {
        Optional<Book> bookOptional = bookService.findById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            return BookDto.builder()
                    .name(book.getName())
                    .description(book.getDescription())
                    .author(AuthorDto.builder()
                            .name(book.getAuthor().getName())
                            .surname(book.getAuthor().getSurname())
                            .age(book.getAuthor().getAge())
                            .build())
                    .build(); //todo.........
        } //todo move to service
        throw new BookNotFoundException(String.format("Book with id[%d] wasn't found", id));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BookValidationException(bindingResult);
        }
        bookService.create(bookDto);
    }

    @PatchMapping("/update")
    public void update(@RequestBody @Valid BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BookValidationException(bindingResult.getAllErrors().toString());
        }
        bookService.update(bookDto);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody @Valid BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BookValidationException(bindingResult.getAllErrors().toString());
        }
        bookService.delete(bookDto);
    }
}
