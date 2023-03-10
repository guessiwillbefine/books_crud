package assignment_five.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "author")
@RequiredArgsConstructor
public class Author {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private Integer age;
    @ToString.Exclude
    @OneToMany(mappedBy = "author")
    private Set<Book> books;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id) && Objects.equals(age, author.age)
                && Objects.equals(name, author.name)
                && Objects.equals(surname, author.surname);
    }

    @Override
    public int hashCode() {
        return Math.toIntExact(id);
    }
}

