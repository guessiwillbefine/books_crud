package assignment_five.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import java.util.List;
@Jacksonized
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDto {
    @NotNull
    private List<Param> params;

        public record Param(@NotNull String name, @NotNull String value) {
    }
}
