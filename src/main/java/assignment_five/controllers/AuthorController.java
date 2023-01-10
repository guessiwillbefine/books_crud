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
    @ResponseStatus(HttpStatus.OK)
    public List<AuthorDto> getAllAuthors() {
        var list =  authorService.getAll();
        System.out.println(list);
        return list;
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthorById(@PathVariable long id) {
        return authorService.findById(id);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid AuthorDto author, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new AuthorValidationException(bindingResult.getAllErrors().toString());
        }
        authorService.save(author);
    }

    @PatchMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody @Valid AuthorDto author, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new AuthorValidationException(bindingResult.getAllErrors().toString());
        }
        authorService.delete(author);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody AuthorDto author) {
        authorService.delete(author);
    }
}
