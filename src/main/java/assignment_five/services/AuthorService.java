package assignment_five.services;

import assignment_five.entity.Author;
import assignment_five.entity.dto.AuthorDto;
import assignment_five.services.repositories.AuthorRepository;
import assignment_five.utils.AuthorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        throw new AuthorNotFoundException(String.format("Author with id[%s] not found", id));
    }

    @Transactional
    public void save(AuthorDto dto) {
        Author author = new Author();
        author.setName(dto.getName());
        author.setSurname(dto.getSurname());
        author.setAge(dto.getAge()); //TODO mapper
        authorRepository.save(author);
    }

    @Transactional
    public void update(AuthorDto dto) {
        Optional<Author> author = authorRepository.findByNameAndSurname(dto.getName(), dto.getSurname());
        author.ifPresent(authorRepository::save);
    }

    @Transactional
    public void delete(AuthorDto dto) {
        Optional<Author> author = authorRepository.findByNameAndSurname(dto.getName(), dto.getSurname());
        author.ifPresent(authorRepository::delete);
    }
}
