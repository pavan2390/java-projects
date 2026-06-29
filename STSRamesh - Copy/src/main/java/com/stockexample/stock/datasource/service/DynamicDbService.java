//package com.stockexample.stock.datasource.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
//import com.stockexample.stock.dto.DataSourceDto;
//import com.stockexample.stock.entity.CanEntity;
//
//@Service
//public class DynamicDbService {
//
//	/*
//	 * @Autowired
//	 * 
//	 * @Qualifier("jdbcTemplate2") private JdbcTemplate jdbcTemplate2; //
//	 * JdbcTemplate for DataSource2
//	 * 
//	 * @Autowired
//	 * 
//	 * @Qualifier("jdbcTemplate3") private JdbcTemplate jdbcTemplate3; //
//	 * JdbcTemplate for DataSource3
//	 */
//
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
//
//	public List<String> createDatabaseAndTable(DataSourceDto dataSourceDto) throws Exception {
//		System.out.println("Inside createDatabaseAndTable method");
//		/*
//		 * if(dataSourceDto.getDbType() == "MySql") { createDatabase(jdbcTemplate2,
//		 * dataSourceDto.getDbName());
//		 * 
//		 * try { jdbcTemplate2.execute("USE " + dataSourceDto.getDbName()); // Executes
//		 * the query to switch databases } catch (DataAccessException e) { // Handle
//		 * exception (e.g., database not found, access issues)
//		 * System.out.println("Error switching database: " + e.getMessage()); }
//		 * 
//		 * createTable(jdbcTemplate2, dataSourceDto.getDbName()); } else
//		 * if(dataSourceDto.getDbType() == "PSql") { createDatabase(jdbcTemplate3,
//		 * dataSourceDto.getDbName());
//		 * 
//		 * try { jdbcTemplate3.execute("USE " + dataSourceDto.getDbName()); // Executes
//		 * the query to switch databases } catch (DataAccessException e) { // Handle
//		 * exception (e.g., database not found, access issues)
//		 * System.out.println("Error switching database: " + e.getMessage()); }
//		 * 
//		 * createTable(jdbcTemplate3, dataSourceDto.getDbName()); }
//		 */
//
//		if (dataSourceDto.getDbName().isBlank() || dataSourceDto.getTableName().isBlank()) {
//			throw new Exception("Datebase or Table name is blank");
//		}
//		List<String> response = new ArrayList<String>();
//		try {
//			if (createDatabase(jdbcTemplate, dataSourceDto.getDbName())) {
//				response.add("Database already exits");
//			}
//			jdbcTemplate.execute("USE " + dataSourceDto.getDbName());
//			if (createTable(jdbcTemplate, dataSourceDto.getDbName(), dataSourceDto.getTableName())) {
//				response.add("Table already exits");
//			}
//		} catch (DataAccessException e) { // Handle exception (e.g., database not found, access issues)
//			System.out.println("Error switching database: " + e.getMessage());
//			throw e;
//		}
//		return response;
//
//	}
//
//	public boolean createDatabase(JdbcTemplate jdbcTemplate, String dbName) {
//		try {
//			String checkDbQuery = "SELECT 1 FROM pg_database WHERE datname = ?";
//			jdbcTemplate.queryForObject(checkDbQuery, Boolean.class, // We expect a Boolean result
//					dbName);
//		} catch (EmptyResultDataAccessException e) {
//			String sql = String.format("CREATE DATABASE \"%s\"", dbName);
//			jdbcTemplate.execute(sql); // Execute the CREATE DATABASE query
//			return false; // Database does not exist
//		}
//		return true;
//	}
//
//	public boolean createTable(JdbcTemplate jdbcTemplate, String dbName, String tableName) {
//		try {
//			String checkTbQuery = "SELECT EXISTS (SELECT 1 FROM pg_tables WHERE schemaname = '?' AND tablename = '?');";
//			jdbcTemplate.queryForObject(checkTbQuery, Boolean.class, // We expect a Boolean result
//					dbName, tableName);
//		} catch (EmptyResultDataAccessException e) {
//			TableCreator tableCreator = new TableCreator();
//			tableCreator.createTable(CanEntity.class, jdbcTemplate, tableName);
//			return false;
//		}
//		return true;
//	}
//}
