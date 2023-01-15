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

@Jacksonized
@Builder
@Getter
@Setter
@ToString
public class AuthorDto implements Serializable {
    @Nullable
    private Long id;
    @Length(min = ApplicationConstants.Validation.MIN_AUTHOR_NAME_SIZE,
            max = ApplicationConstants.Validation.MAX_AUTHOR_NAME_SIZE,
            message = ApplicationConstants.Validation.AUTHOR_NAME_MSG)
    private String name;
    @Length(min = ApplicationConstants.Validation.MIN_AUTHOR_SURNAME_SIZE,
            max = ApplicationConstants.Validation.MAX_AUTHOR_SURNAME_SIZE,
            message = ApplicationConstants.Validation.AUTHOR_SURNAME_MSG)
    private String surname;
    @Min(ApplicationConstants.Validation.MIN_AGE)
    @Max(ApplicationConstants.Validation.MAX_AGE)
    private Integer age;
}
