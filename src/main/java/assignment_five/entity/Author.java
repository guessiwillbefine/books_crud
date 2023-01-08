package assignment_five.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table
public class Author {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @Length(max = 20)
    private String name;
    @Column
    @Length(max = 20)
    private String surname;
    @Column
    @Min(18)
    @Max(100)
    private int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id == author.id && age == author.age
                && Objects.equals(name, author.name)
                && Objects.equals(surname, author.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, age);
    }
}

