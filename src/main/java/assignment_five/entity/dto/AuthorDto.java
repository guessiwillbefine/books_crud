package assignment_five.entity.dto;

import assignment_five.utils.ApplicationConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@ToString
public class AuthorDto {
    @Length(min = ApplicationConstants.Validation.MIN_AUTHOR_NAME_SIZE,
            max = ApplicationConstants.Validation.MAX_AUTHOR_NAME_SIZE)
    private String name;
    @Length(min = ApplicationConstants.Validation.MIN_AUTHOR_SURNAME_SIZE,
            max = ApplicationConstants.Validation.MAX_AUTHOR_SURNAME_SIZE)
    private String surname;
    @Min(ApplicationConstants.Validation.MIN_AGE)
    @Max(ApplicationConstants.Validation.MAX_AGE)
    private Integer age;
}
