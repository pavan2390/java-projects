/*
 * package com.stockexample.stock.datasource.config;
 * 
 * import java.util.Objects;
 * 
 * import javax.sql.DataSource;
 * 
 * import org.springframework.beans.factory.annotation.Qualifier; import
 * org.springframework.boot.context.properties.ConfigurationProperties; import
 * org.springframework.boot.jdbc.DataSourceBuilder; import
 * org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.data.jpa.repository.config.EnableJpaRepositories; import
 * org.springframework.orm.jpa.JpaTransactionManager; import
 * org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean; import
 * org.springframework.transaction.PlatformTransactionManager; import
 * org.springframework.transaction.annotation.EnableTransactionManagement;
 * 
 * 
 * @Configuration
 * 
 * @EnableTransactionManagement
 * 
 * @EnableJpaRepositories( basePackages = "com.stockexample.stock.repository",
 * entityManagerFactoryRef = "entityManagerFactory3", transactionManagerRef =
 * "transactionManager3" ) public class DataSource3Config {
 * 
 * @Bean(name = "dataSource3")
 * 
 * @ConfigurationProperties(prefix = "spring.datasource3") public DataSource
 * dataSource3() { return DataSourceBuilder.create().build(); }
 * 
 * @Bean(name = "entityManagerFactory3") public
 * LocalContainerEntityManagerFactoryBean entityManagerFactory3(
 * EntityManagerFactoryBuilder builder,
 * 
 * @Qualifier("dataSource3") DataSource dataSource) { return builder
 * .dataSource(dataSource) .packages("com.stockexample.stock.repository")
 * .persistenceUnit("datasource3") .build(); }
 * 
 * @Bean(name = "transactionManager3") public PlatformTransactionManager
 * transactionManager3(
 * 
 * @Qualifier("entityManagerFactory3") LocalContainerEntityManagerFactoryBean
 * entityManagerFactory) { return new
 * JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()
 * )); } }
 */