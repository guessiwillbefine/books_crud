package assignment_five.services.repositories;

import assignment_five.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByNameAndSurname(String name, String surname);
    @Query("from Author auth where " +
            "lower(auth.name) = lower(:name) " +
            "and lower(auth.surname) = lower(:surname)")
    Optional<Author> findByFullName(@Param("name") String name,
                                    @Param("surname") String surname);
}
