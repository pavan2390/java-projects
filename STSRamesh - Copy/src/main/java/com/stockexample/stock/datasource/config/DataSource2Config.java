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
 * org.springframework.context.annotation.Primary; import
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
 * entityManagerFactoryRef = "entityManagerFactory2", transactionManagerRef =
 * "transactionManager2" ) public class DataSource2Config {
 * 
 * @Bean(name = "dataSource2")
 * 
 * @ConfigurationProperties(prefix = "spring.datasource2") public DataSource
 * dataSource2() { return DataSourceBuilder.create().build(); }
 * 
 * @Bean(name = "entityManagerFactory2")
 * 
 * @Primary public LocalContainerEntityManagerFactoryBean entityManagerFactory2(
 * EntityManagerFactoryBuilder builder,
 * 
 * @Qualifier("dataSource2") DataSource dataSource) { return builder
 * .dataSource(dataSource) .packages("com.stockexample.stock.repository")
 * .persistenceUnit("datasource2") .build(); }
 * 
 * @Bean(name = "transactionManager2") public PlatformTransactionManager
 * transactionManager2(
 * 
 * @Qualifier("entityManagerFactory2") LocalContainerEntityManagerFactoryBean
 * entityManagerFactory) { return new
 * JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()
 * )); } }
 */