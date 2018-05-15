package ua.kpi.correction.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class ExtendedJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID>
        implements ExtendedJpaRepository<T, ID> {

    private final EntityManager entityManager;

    public ExtendedJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public List<T> saveAndFlushAll(Iterable<T> entities) {
        final List<T> result = saveAll(entities);

        entityManager.flush();
        entityManager.clear();

        return result;
    }

}
