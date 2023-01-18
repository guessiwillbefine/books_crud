package assignment_five.controllers;

import assignment_five.entity.Author;
import assignment_five.entity.dto.AuthorDto;
import assignment_five.services.AuthorService;
import assignment_five.utils.exceptions.AuthorValidationException;
import assignment_five.utils.mapper.AuthorMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

    @GetMapping
    public AuthorDto findAuthor(@RequestParam("name") @NotNull String name,
                                @RequestParam("surname") @NotNull String surname) {
        Author author = authorService.findAuthorByFullName(name, surname);
        return AuthorMapper.INSTANCE.map(author);
    }

    @GetMapping("/all")
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAll()
                .stream().map(AuthorMapper.INSTANCE::map)
                .toList();
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthorById(@PathVariable final long id) {
        return AuthorMapper.INSTANCE.map(authorService.findById(id));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @NotNull @Valid final AuthorDto author,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new AuthorValidationException(bindingResult.getAllErrors().toString());
        }
        authorService.save(author);
    }

    @PatchMapping("/update")
    public void update(@RequestBody @NotNull @Valid final AuthorDto author,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new AuthorValidationException(bindingResult.getAllErrors().toString());
        }
        authorService.update(author);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody @NotNull @Valid final AuthorDto author) {
        authorService.delete(author);
    }
}
