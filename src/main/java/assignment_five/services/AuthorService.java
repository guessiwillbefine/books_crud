package assignment_five.services;

import assignment_five.entity.Author;
import assignment_five.entity.dto.AuthorDto;
import assignment_five.services.repositories.AuthorRepository;
import assignment_five.services.repositories.CrudService;
import assignment_five.utils.exceptions.AuthorDuplicateException;
import assignment_five.utils.exceptions.AuthorNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorService implements CrudService<AuthorDto,Long> {
    private final AuthorRepository authorRepository;

    @Override
    @Transactional(readOnly = true)
    public Author findById(@NotNull Long id) {
        final Optional<Author> authorOptional = authorRepository.findById(id);
        if (authorOptional.isPresent()) {
            return authorOptional.get();
        }
        throw new AuthorNotFoundException(format("Author with id[%s] wasn't found", id));
    }
    @Override
    public void save(AuthorDto dto) {
        if (authorRepository.findByNameAndSurname(dto.getName(), dto.getSurname()).isPresent()) {
            throw new AuthorDuplicateException(format("Author[%s %s] already exists",
                    dto.getName(), dto.getSurname()));
        }
        final Author author = new Author();
        author.setName(dto.getName());
        author.setSurname(dto.getSurname());
        author.setAge(dto.getAge());
        authorRepository.save(author);
    }

    @Override
    public void update(AuthorDto dto) {
        final Optional<Author> author = authorRepository.findById(dto.getId());
        if (author.isPresent()) {
            final Author authorToUpdate = author.get();
            authorToUpdate.setName(dto.getName());
            authorToUpdate.setSurname(dto.getSurname());
            authorToUpdate.setAge(dto.getAge());
            authorRepository.save(authorToUpdate);
        }
    }
    @Override
    public void delete(AuthorDto dto) {
        final Optional<Author> author = authorRepository.findByFullName(dto.getName(), dto.getSurname());
        author.ifPresent(authorRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<Author> getAll() {
        return authorRepository.findAll();
    }


    @Transactional(readOnly = true)
    public Optional<Author> findAuthor(AuthorDto authorDto) {
        return authorRepository.findByFullName(authorDto.getName(), authorDto.getSurname());
    }

    @Transactional(readOnly = true)
    public Author findAuthorByFullName(String name, String surname) {
        Optional<Author> byFullName = authorRepository.findByFullName(name, surname);
        if (byFullName.isPresent()) {
            return byFullName.get();
        }
        throw new AuthorNotFoundException(format("Author[%s %s] wasn't found", name, surname));
    }
}
