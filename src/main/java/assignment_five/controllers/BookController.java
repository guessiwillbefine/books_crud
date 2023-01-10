package assignment_five.controllers;

import assignment_five.entity.dto.BookDto;
import assignment_five.services.BookService;
import assignment_five.utils.exceptions.BookValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    //todo DTO for Pageable?
    @GetMapping
    public List<BookDto> getAllBooks(@RequestParam(value = "size", required = false)Integer pageSize,
                                     @RequestParam(value = "page", required = false)Integer pageNum,
                                     @RequestParam(value = "sortBy", required = false)String fieldSort) {
        return bookService.getBookList(pageSize, pageNum, fieldSort);
    }
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BookValidationException(bindingResult.getAllErrors().toString());
        }
        bookService.create(bookDto);
    }
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.CREATED)
    public void delete(@RequestBody @Valid BookDto bookDto, BindingResult bindingResult) {
        System.err.println(bookDto.getAuthor().getName());
        if (bindingResult.hasErrors()) {
            throw new BookValidationException(bindingResult.getAllErrors().toString());
        }
        bookService.delete(bookDto);
    }
}
