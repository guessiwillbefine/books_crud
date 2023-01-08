package assignment_five.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class AuthorDto {
    private String name;
    private String surname;
    private int age;
}
