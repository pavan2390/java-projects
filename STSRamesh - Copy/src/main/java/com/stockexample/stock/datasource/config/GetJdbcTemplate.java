/*
 * package com.stockexample.stock.datasource.config;
 * 
 * import javax.sql.DataSource;
 * 
 * import org.springframework.beans.factory.annotation.Qualifier; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.jdbc.core.JdbcTemplate;
 * 
 * @Configuration public class GetJdbcTemplate {
 * 
 * @Bean(name="jdbcTemplate2") public JdbcTemplate
 * JdbcTemplate2(@Qualifier("dataSource2") DataSource dataSource) { return new
 * JdbcTemplate(dataSource); }
 * 
 * @Bean(name="jdbcTemplate3") public JdbcTemplate
 * JdbcTemplate3(@Qualifier("dataSource3") DataSource dataSource) { return new
 * JdbcTemplate(dataSource); } }
 */