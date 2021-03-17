package socialnetwork.repository.memory;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.RepoException;
import socialnetwork.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private final Validator<E> validator;
    Map<ID, E> entities;

    public InMemoryRepository( Validator<E> validator ) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public E findOne( ID id ) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");
        if (entities.get(id) == null) {
            throw new RepoException("Nu exista entitatea cu id-ul dat!");
        }
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save( E entity ) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        if (entities.get(entity.getId()) != null) {
            //return entity;
            String clasa = entity.getClass().getSimpleName();
            throw new RepoException("Exista deja " + clasa + " cu id-ul dat!");
        } else entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public E delete( ID id ) {
        E e = entities.remove(id);
        if (e == null) {
            throw new RepoException("Entitatea cu id-ul dat nu exista!");
        }
        return e;
    }

    @Override
    public E update( E entity ) {

        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        entities.put(entity.getId(), entity);

        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return null;
        } else {

            throw new RepoException("Nu exista entitatea cu id-ul dat!");

        }

    }

}
