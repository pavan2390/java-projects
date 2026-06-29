package com.stockexample.stock.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.stockexample.stock.dto.DateData;
import com.stockexample.stock.entity.CanadaatEntity;
import com.stockexample.stock.entity.CanadashEntity;
import com.stockexample.stock.entity.IndiaEntity;
import com.stockexample.stock.service.impl.CanadaatServiceImpl;
import com.stockexample.stock.service.impl.CanadashServiceImpl;
import com.stockexample.stock.service.impl.IndiaServiceImpl;

public class ServiceUtil {

	/**
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public static List<?> extracted(Path path, Object service) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path.toString(), StandardCharsets.UTF_8));

		DateData extractedDate = new DateData();
		extractedDate.setLoadDate(extractDateFromFilePath(path.toString()));
		System.out.println("extractedDate : " + extractedDate.getLoadDate());
		extractedDate = dateTimeExtractor(extractedDate);
		// Extract the file name from the full path
		String fileName = Paths.get(path.toString()).getFileName().toString();

		if (service.getClass() == CanadaatServiceImpl.class) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(',').withFirstRecordAsHeader().parse(reader);
			List<CanadaatEntity> lsData = new ArrayList<>();
			for (CSVRecord record : records) {
				CanadaatEntity canStk;
				try {
					canStk = CanadaatServiceImpl.setCsvEnityValues(extractedDate, fileName, record);
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
					System.out.println("Issue filename-" + fileName);
					return null;
				}
				lsData.add(canStk);
			}
			return lsData;
		} else if (service.getClass() == CanadashServiceImpl.class) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('|').withFirstRecordAsHeader().parse(reader);
			List<CanadashEntity> lsData = new ArrayList<>();
			for (CSVRecord record : records) {
				CanadashEntity canStk;
				try {
					canStk = CanadashServiceImpl.setCsvEnityValues(extractedDate, fileName, record);
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
					ex.printStackTrace();
					System.out.println("Issue filename-" + fileName);
					return null;
				}
				lsData.add(canStk);
			}
			return lsData;
		} else if (service.getClass() == IndiaServiceImpl.class) {
			if (path.toString().endsWith(".csv")) {
				Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(',').withFirstRecordAsHeader()
						.parse(reader);
				List<IndiaEntity> lsData = new ArrayList<>();
				for (CSVRecord record : records) {
					IndiaEntity canStk;
					try {
						canStk = IndiaServiceImpl.setCsvEnityValues(extractedDate, fileName, record);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
						ex.printStackTrace();
						System.out.println("Issue filename-" + fileName);
						return null;
					}
					lsData.add(canStk);
				}
				return lsData;
			}

			else if (path.toString().endsWith(".xls")) {
				HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(path.toString())));
				Sheet sheet = workbook.getSheetAt(0);
				Row headerRow = sheet.getRow(0);
	            Map<String, Integer> headerMap = new HashMap<>();

	            Iterator<Cell> headerIterator = headerRow.cellIterator();
	            while (headerIterator.hasNext()) {
	                Cell cell = headerIterator.next();
	                headerMap.put(cell.getStringCellValue(), cell.getColumnIndex());
	            }
				List<IndiaEntity> lsData = new ArrayList<>();
				for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
					Row row = sheet.getRow(i);
					if (row == null) continue;
					IndiaEntity canStk;
					try {
						canStk = IndiaServiceImpl.setXslEnityValues(extractedDate, fileName, row, headerMap);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
						ex.printStackTrace();
						System.out.println("Issue filename-" + fileName);
						return null;
					}
					lsData.add(canStk);
				}
				return lsData;
			}
		}

		return null;

	}

	/**
	 * This method for copying files from one place to another
	 * 
	 * @param fromPath
	 * @param toPath
	 */
	public static void sendFiles(Path fromPath, String toPath) {
		DateData extractedDate = new DateData();
		extractedDate.setLoadDate(ServiceUtil.extractDateFromFilePath(fromPath.toString()));
		String dirPath = toPath + extractedDate.getLoadDate() + "/";
		File file = new File(dirPath);
		String fileName = Paths.get(fromPath.toString()).getFileName().toString();
		file.mkdirs();
		try {
			Files.copy(Paths.get(fromPath.toString()), Paths.get(dirPath + fileName),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to extract load date from file path.
	 * 
	 * @param filePath
	 * @return
	 */
	public static LocalDate extractDateFromFilePath(String filePath) {
		// Regular expression for date format (yyyy-MM-dd)
		Pattern datePattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})");
		Matcher matcher = datePattern.matcher(filePath);

		// If a match is found, return the date
		if (matcher.find()) {
			return LocalDate.parse(matcher.group(1), DateTimeFormatter.ofPattern("yyyy-MM-dd")); // The first captured
																									// group (the date)
		}
		return null; // Return null if no date is found
	}

	/**
	 * Method to calculate day of the week, week of the month, week of the year.
	 * 
	 * @param extractedDate
	 * @return
	 */
	public static DateData dateTimeExtractor(DateData extractedDate) {

		// Parse the string into a LocalDate object
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = extractedDate.getLoadDate();

		// Extract day of the week
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		extractedDate.setDayOfWeek(dayOfWeek.getValue());
		extractedDate.setDayOfWeekName(dayOfWeek.name());

		// Extract week of the month
		int weekOfMonth = date.get(WeekFields.of(Locale.getDefault()).weekOfMonth());
		extractedDate.setWeekOfMonth(weekOfMonth);

		// Extract week of the year
		int weekOfYear = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
		extractedDate.setWeekOfYear(weekOfYear);

		return extractedDate;
	}

	/**
	 * Method to check whether string is empty or null.
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isNullOrEmpty(String field) {
		return field == null || field.strip().isEmpty() || field.strip().equals("-") || field.strip().equals("―") || field == "null";
	}

	/**
	 * @param date
	 * @return Localdate generated from string date
	 */
	public static LocalDate getDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
}
