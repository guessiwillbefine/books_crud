package assignment_five.controllers;

import assignment_five.entity.Book;
import assignment_five.entity.dto.BookDto;
import assignment_five.entity.dto.SearchRequestDto;
import assignment_five.services.BookService;
import assignment_five.utils.exceptions.BookValidationException;
import assignment_five.utils.mapper.BookMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/search")
    public List<BookDto> findByParams(@RequestBody @NotNull @Valid SearchRequestDto searchRequestDto) {
        return bookService.getBookList(searchRequestDto).stream()
                .map(BookMapper.INSTANCE::map)
                .toList();
    }

    @GetMapping
    public BookDto findBook(@RequestBody @NotNull final BookDto bookDto) {
        Book book = bookService.findBook(bookDto);
        return BookMapper.INSTANCE.map(book);
    }

    @GetMapping("/all")
    public List<BookDto> getAllBooks(@RequestParam(value = "size", required = false) final Integer pageSize,
                                     @RequestParam(value = "page", required = false) final Integer pageNum) {
        return bookService.getBookList(pageSize, pageNum).stream()
                .map(BookMapper.INSTANCE::map)
                .toList();
    }

    @GetMapping("/{id}")
    public BookDto findById(@PathVariable("id") final long id) {
        return BookMapper.INSTANCE.map(bookService.findById(id));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @NotNull @Valid final BookDto bookDto,
                       final BindingResult bindingResult) {
        log.error(String.valueOf(bindingResult.hasErrors()));
        if (bindingResult.hasErrors()) {
            throw new BookValidationException(bindingResult);
        }
        bookService.save(bookDto);
    }

    @PatchMapping("/update")
    public void update(@RequestBody @NotNull @Valid final BookDto bookDto,
                       final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BookValidationException(bindingResult.getAllErrors().toString());
        }
        bookService.update(bookDto);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody @NotNull @Valid final BookDto bookDto,
                       final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BookValidationException(bindingResult.getAllErrors().toString());
        }
        bookService.delete(bookDto);
    }
}