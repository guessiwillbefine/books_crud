package assignment_five.controllers;

import assignment_five.entity.dto.AuthorDto;
import assignment_five.services.AuthorService;
import assignment_five.utils.exceptions.AuthorValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/all")
    public List<AuthorDto> getAllAuthors(@RequestParam(value = "size", required = false) Integer pageSize,
                                         @RequestParam(value = "page", required = false) Integer pageNum) {
        return authorService.getAll();
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthorById(@PathVariable long id) {
        return authorService.findById(id);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid AuthorDto author,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new AuthorValidationException(bindingResult.getAllErrors().toString());
        }
        authorService.save(author);
    }

    @PatchMapping("/update")
    public void update(@RequestBody @Valid AuthorDto author,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new AuthorValidationException(bindingResult.getAllErrors().toString());
        }
        authorService.update(author);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody AuthorDto author) {
        authorService.delete(author);
    }
}
