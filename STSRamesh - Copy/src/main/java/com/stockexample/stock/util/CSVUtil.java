package com.stockexample.stock.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.csv.CSVPrinter;

import com.stockexample.stock.entity.CanadaatEntity;
import com.stockexample.stock.entity.CanadashEntity;
import com.stockexample.stock.entity.IndiaEntity;

public class CSVUtil {

	/**
	 * This method will fetch the entity class column headers
	 * 
	 * @param entityClass
	 * @return
	 */
	public static String[] getEntityHeaders(Class<?> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();
		String[] headers = new String[fields.length];

		for (int i = 0; i < fields.length; i++) {
			headers[i] = fields[i].getName(); // Use field names as headers
		}
		return headers;
	}

	/**
	 * This method will write the entity object to csv file
	 * 
	 * @param csvWriter
	 * @param entity
	 * @param headers
	 * @throws IOException
	 */
	public static void writeEntityToCSV(CSVPrinter csvPrinter, Object entity, String[] headers) throws IOException {
		String[] values = new String[headers.length];

		// Use reflection to extract values based on headers (field names)
		for (int i = 0; i < headers.length; i++) {
			try {
				Field field;
				Object value = null;
				if (entity.getClass() == CanadaatEntity.class) {
					field = CanadaatEntity.class.getDeclaredField(headers[i]);
					field.setAccessible(true);
					value = field.get(entity);
				}
				else if (entity.getClass() == CanadashEntity.class) {
					field = CanadashEntity.class.getDeclaredField(headers[i]);
					field.setAccessible(true);
					value = field.get(entity);
				}
				else if (entity.getClass() == IndiaEntity.class) {
					field = IndiaEntity.class.getDeclaredField(headers[i]);
					field.setAccessible(true);
					value = field.get(entity);
				}
				values[i] = value != null ? value.toString() : ""; // Null safe
			} catch (NoSuchFieldException | IllegalAccessException e) {
				values[i] = ""; // If a field is not found or inaccessible, leave it blank
			}
		}

		// Print the record (values) to the CSV
		csvPrinter.printRecord((Object[]) values);
	}

	/**
	 * This method will create the csv file on demand
	 * 
	 * @param filePath
	 * @param name
	 * @param noOfFile
	 * @return
	 */
	public static File createCSVFile(String filePath, String name, int noOfFile) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String filename = name + dateFormat.format(date) + "--" + noOfFile + ".csv";
		File file = new File(filePath);
		file.mkdirs();
		if (!file.isDirectory()) {
			return null;
		}
		File file1 = new File(filePath + filename);
		return file1;
	}
}
