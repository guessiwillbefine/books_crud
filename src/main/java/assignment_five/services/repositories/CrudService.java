package assignment_five.services.repositories;

public interface CrudService <E, I> {

    void delete(E entity);
    void update(E entity);
    void save(E entity);
    Object findById(I id);
}
