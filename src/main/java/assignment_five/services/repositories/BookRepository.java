package assignment_five.services.repositories;

import assignment_five.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>  {

    @Query("from Book b where lower(b.name) = lower(:name) " +
            "and lower(b.author.name) = lower(:aName)" +
            "and lower(b.author.surname) = lower(:aSurname)")
    Optional<Book> findByNameAndAuthorName(@Param("name")String name,
                                           @Param("aName")String aName,
                                           @Param("aSurname")String aSurname);

    Page<Book> findAll(Pageable pageable);

}
