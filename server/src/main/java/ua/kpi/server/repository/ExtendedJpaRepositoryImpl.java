package ua.kpi.server.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

public class ExtendedJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID>
        implements ExtendedJpaRepository<T, ID> {

    public ExtendedJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }
}
