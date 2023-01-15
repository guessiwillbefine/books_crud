package assignment_five.entity.dto;

import assignment_five.utils.ApplicationConstants;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@ToString
@Jacksonized
@Builder
@Getter
@Setter
public class BookDto implements Serializable {
    private Long id;
    @Nullable
    @Max(value = ApplicationConstants.Validation.MAX_YEAR,
            message = ApplicationConstants.Validation.BOOK_YEAR_MSG)
    private Integer year;
    @Nullable
    @Min(value = ApplicationConstants.Validation.MIN_BOOK_PAGES,
            message = ApplicationConstants.Validation.BOOK_PAGE_MSG)
    private Integer pageAmount;
    @Length(min = ApplicationConstants.Validation.MIN_BOOK_NAME_SIZE,
            max = ApplicationConstants.Validation.MAX_BOOK_NAME_SIZE,
            message = ApplicationConstants.Validation.BOOK_NAME_MSG)
    private String name;
    @Length(max = ApplicationConstants.Validation.DESCRIPTION_SIZE,
    message = ApplicationConstants.Validation.BOOK_DESCRIPTION_MSG)
    private String description;
    private AuthorDto author;
}
