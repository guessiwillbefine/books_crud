package assignment_five.utils.mapper;

import assignment_five.entity.Author;
import assignment_five.entity.dto.AuthorDto;

public class AuthorMapper implements Mapper<AuthorDto, Author> {
    public static final AuthorMapper INSTANCE = new AuthorMapper();
    private AuthorMapper() {}
    @Override
    public AuthorDto map(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .age(author.getAge())
                .build();
    }

}
