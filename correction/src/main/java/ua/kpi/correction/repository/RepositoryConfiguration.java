package ua.kpi.correction.repository;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
        basePackageClasses = ExtendedJpaRepository.class,
        repositoryBaseClass = ExtendedJpaRepositoryImpl.class
)
@EnableTransactionManagement
public class RepositoryConfiguration {
}
