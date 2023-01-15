package assignment_five.utils.mapper;

import assignment_five.entity.Author;
import assignment_five.entity.Book;
import assignment_five.entity.dto.AuthorDto;
import assignment_five.entity.dto.BookDto;

public class BookMapper implements Mapper<BookDto, Book> {
    public static final BookMapper INSTANCE = new BookMapper();
    private static final Mapper<AuthorDto, Author> authMapper = AuthorMapper.INSTANCE;
    private BookMapper(){}

    public BookDto map(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .description(book.getDescription())
                .year(book.getYear())
                .pageAmount(book.getPageAmount())
                .author(authMapper.map(book.getAuthor()))
                .build();
    }
}
