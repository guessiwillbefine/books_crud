package assignment_five.entity.dto;

import assignment_five.utils.ApplicationConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@ToString
@Builder
@Getter
public class BookDto {
    @Max(ApplicationConstants.Validation.MAX_YEAR)
    private int year;
    @Min(ApplicationConstants.Validation.MIN_BOOK_PAGES)
    private int pageAmount;
    @Length(min = ApplicationConstants.Validation.MIN_BOOK_NAME_SIZE,
            max = ApplicationConstants.Validation.MAX_BOOK_NAME_SIZE)
    private String name;
    @Length(max = ApplicationConstants.Validation.DESCRIPTION_SIZE)
    private String description;
    private AuthorDto author;
}
