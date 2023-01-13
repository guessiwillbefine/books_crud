package assignment_five.services;

import assignment_five.entity.Author;
import assignment_five.entity.dto.AuthorDto;
import assignment_five.services.repositories.AuthorRepository;
import assignment_five.utils.exceptions.AuthorDuplicateException;
import assignment_five.utils.exceptions.AuthorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public AuthorDto findById(long id) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if (authorOptional.isPresent()) {
            Author author = authorOptional.get();
            return AuthorDto.builder()
                    .name(author.getName())
                    .surname(author.getSurname())
                    .age(author.getAge())
                    .build();
        }
        throw new AuthorNotFoundException(String.format("Author with id[%s] wasn't found", id));
    }

    @Transactional
    public void save(AuthorDto dto) {
        Optional<Author> existingAuthor = findByNameAndSurname(dto.getName(), dto.getSurname());
        if (existingAuthor.isPresent()) {
            throw new AuthorDuplicateException(String.format("Author[%s %s] already exists",
                    dto.getName(), dto.getSurname()));
        }
        Author author = new Author();
        author.setName(dto.getName());
        author.setSurname(dto.getSurname());
        author.setAge(dto.getAge()); //TODO mapper
        authorRepository.save(author);
    }

    @Transactional
    public void update(AuthorDto dto) {
        Optional<Author> author = authorRepository.findByNameAndSurname(dto.getName(), dto.getSurname());
        if (author.isPresent()) {
            Author authorToUpdate = author.get();
            authorToUpdate.setName(dto.getName());
            authorToUpdate.setSurname(dto.getSurname());
            authorToUpdate.setAge(dto.getAge());
            authorRepository.save(authorToUpdate);
        }
    }

    @Transactional
    public void delete(AuthorDto dto) {
        Optional<Author> author = authorRepository.findByNameAndSurname(dto.getName(), dto.getSurname());
        author.ifPresent(authorRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<AuthorDto> getAll() {
        return authorRepository.findAll().stream()
                .map(author -> AuthorDto.builder()
                        .age(author.getAge())
                        .name(author.getName())
                        .surname(author.getSurname())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<Author> findByNameAndSurname(String name, String surname) {
        return authorRepository.findByFullName(name, surname);
    }
}
