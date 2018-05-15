package ua.kpi.correction.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ExtendedJpaRepository<T, ID> extends CrudRepository<T, ID> {

}
