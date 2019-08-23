package org.minioasis.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.minioasis.library.repository",
        repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class
)
public class RepositoryConfig {

}
