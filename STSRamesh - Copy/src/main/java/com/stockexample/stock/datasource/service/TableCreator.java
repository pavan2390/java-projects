package com.stockexample.stock.datasource.service;

import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class TableCreator {

	// Method to create a table based on the entity class
	public void createTable(Class<?> entityClass, JdbcTemplate jdbcTemplate, String tableName) {
		String sql = generateCreateTableQuery(entityClass, tableName);

		try {
			// Execute the CREATE TABLE query
			jdbcTemplate.update(sql);
			System.out.println("Table " + tableName + " created successfully.");
		} catch (DataAccessException e) {
			// Handle exceptions (e.g., table already exists)
			System.out.println("Error creating table: " + e.getMessage());
		}
	}

	// Generate the CREATE TABLE query based on the entity class
	private String generateCreateTableQuery(Class<?> entityClass, String tableName) {
		StringBuilder query = new StringBuilder("CREATE TABLE " + tableName + " (");

		// Iterate over fields to generate column definitions
		for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {
			String columnName = field.getName();
			String columnType = mapJavaTypeToSqlType(field.getType());
			query.append(columnName).append(" ").append(columnType).append(", ");
		}

		// Remove the last comma and space, then close the parentheses
		query.setLength(query.length() - 2);
		query.append(");");

		return query.toString();
	}

	// Map Java types to SQL types
	private String mapJavaTypeToSqlType(Class<?> javaType) {
		if (javaType == int.class || javaType == Integer.class) {
			return "INTEGER";
		} else if (javaType == Long.class) {
			return "BIGINT";
		} else if (javaType == Double.class) {
			return "DECIMAL(18,2)";
		} else if (javaType == String.class) {
			return "VARCHAR(255)";
		} else if (javaType == Date.class) {
			return "TIMESTAMP";
		}
		// Add other type mappings as needed
		return "VARCHAR(255)"; // Default fallback
	}
}
