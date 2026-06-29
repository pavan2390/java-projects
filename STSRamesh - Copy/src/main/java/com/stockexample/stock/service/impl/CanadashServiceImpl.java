package com.stockexample.stock.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stockexample.stock.dto.DateData;
import com.stockexample.stock.dto.RecordsDto;
import com.stockexample.stock.dto.RequestDto;
import com.stockexample.stock.dto.TemplateDto;
import com.stockexample.stock.entity.CanadashEntity;
import com.stockexample.stock.entity.CanadashUniqueEntity;
import com.stockexample.stock.repository.CanadashRepository;
import com.stockexample.stock.repository.CanadashUniqueRepository;
import com.stockexample.stock.service.StockService;
import com.stockexample.stock.util.CSVUtil;
import com.stockexample.stock.util.ServiceUtil;

@Service
public class CanadashServiceImpl implements StockService {

	@Autowired
	private CanadashRepository stockRepository;
	@Autowired
	private CanadashUniqueRepository stockUniqueRepository;

	@Override
	@Transactional
	public void uploadData(RequestDto requestDto) throws IOException {

		Files.walk(Paths.get(requestDto.getImportPath())).filter(Files::isRegularFile)
				.filter(path -> path.toString().endsWith(".csv")).forEach(path -> {
					List<CanadashEntity> lsData;
					try {
						lsData = (List<CanadashEntity>) ServiceUtil.extracted(path, this);
						if (lsData != null && !lsData.isEmpty()) {
							stockRepository.saveAll(lsData);
							ServiceUtil.sendFiles(path, requestDto.getExportPath() + "/Canadash/Processed/");
						} else {
							ServiceUtil.sendFiles(path, requestDto.getExportPath() + "/Canadash/UnProcessed/");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

		if (requestDto.isPrint()) {
			exportToCsv(requestDto);
		}
	}

	public static CanadashEntity setCsvEnityValues(DateData extractedDate, String fileName, CSVRecord record) {
		DecimalFormat df = new DecimalFormat("#.##");
		CanadashEntity canStk = new CanadashEntity();
		canStk.set_date(extractedDate.getLoadDate());
		canStk.setDayOfWeekName(extractedDate.getDayOfWeek());
		canStk.setDayOfWeek(extractedDate.getDayOfWeekName());
		canStk.setWeekOfMonth(extractedDate.getWeekOfMonth());
		canStk.setWeekOfYear(extractedDate.getWeekOfYear());
		canStk.setWatchList(fileName.replace(".csv", ""));
		canStk.setSymbol(ServiceUtil.isNullOrEmpty(record.get("Symbol")) ? null : record.get("Symbol"));
		canStk.setVolume(
				ServiceUtil.isNullOrEmpty(record.get("Volume")) ? null : record.get("Volume").replace("|", ""));
		canStk.setAverageVolumeOfSixMonth(ServiceUtil.isNullOrEmpty(record.get("AVG Volume 6 Month")) ? null
				: record.get("AVG Volume 6 Month").replace("|", ""));
		canStk.setBlkVolume(
				ServiceUtil.isNullOrEmpty(record.get("Blk Vol")) ? null : record.get("Blk Vol").replace("|", ""));
		canStk.setPercentChange(ServiceUtil.isNullOrEmpty(record.get("% Chg")) ? null
				: Double.parseDouble(record.get("% Chg")));
		canStk.set_change(ServiceUtil.isNullOrEmpty(record.get("Change")) ? null
				: Double.parseDouble(record.get("Change")));
		canStk.setTicker(ServiceUtil.isNullOrEmpty(record.get("Ticker")) ? null : record.get("Ticker"));
		canStk.setC(ServiceUtil.isNullOrEmpty(record.get("C")) ? null : record.get("C"));
		canStk.setQ(ServiceUtil.isNullOrEmpty(record.get("Q")) ? null : record.get("Q"));
		canStk.setName(ServiceUtil.isNullOrEmpty(record.get("Name")) ? null : record.get("Name"));
		canStk.setEps(ServiceUtil.isNullOrEmpty(record.get("EPS")) ? null : Double.parseDouble(record.get("EPS")));
		canStk.setPe(ServiceUtil.isNullOrEmpty(record.get("PE")) ? null : Double.parseDouble(record.get("PE")));
		canStk.setYield(
				ServiceUtil.isNullOrEmpty(record.get("Yield")) ? null : Double.parseDouble(record.get("Yield")));
		canStk.setPrice(
				ServiceUtil.isNullOrEmpty(record.get("Price")) ? null : Double.parseDouble(record.get("Price")));
		canStk.setAsk(ServiceUtil.isNullOrEmpty(record.get("Ask")) ? null : Double.parseDouble(record.get("Ask")));
		canStk.setBid(ServiceUtil.isNullOrEmpty(record.get("Bid")) ? null : Double.parseDouble(record.get("Bid")));
		canStk.setTrdSz(ServiceUtil.isNullOrEmpty(record.get("Trd Sz")) ? null
				: Double.parseDouble(record.get("Trd Sz").replace("|", "")));
		canStk.setAskSz(
				ServiceUtil.isNullOrEmpty(record.get("Ask Sz")) ? null : Double.parseDouble(record.get("Ask Sz")));
		canStk.setBidSz(
				ServiceUtil.isNullOrEmpty(record.get("Bid Sz")) ? null : Double.parseDouble(record.get("Bid Sz").replace("|", "")));
		canStk.set_open(ServiceUtil.isNullOrEmpty(record.get("Open")) ? null : Double.parseDouble(record.get("Open")));
		canStk.setClose(
				ServiceUtil.isNullOrEmpty(record.get("Close")) ? null : Double.parseDouble(record.get("Close")));
		canStk.setFiftyTwotWeekHigh(
				ServiceUtil.isNullOrEmpty(record.get("52 High")) ? null : Double.parseDouble(record.get("52 High")));
		canStk.setFiftyTwotWeekLow(
				ServiceUtil.isNullOrEmpty(record.get("52 Low")) ? null : Double.parseDouble(record.get("52 Low")));
		canStk.setFiftyTwoWeekRange(ServiceUtil.isNullOrEmpty(record.get("52 Week Range")) ? null
				: Double.parseDouble(record.get("52 Week Range")));
		canStk.setHigh(ServiceUtil.isNullOrEmpty(record.get("High")) ? null : Double.parseDouble(record.get("High")));
		canStk.setLow(ServiceUtil.isNullOrEmpty(record.get("Low")) ? null : Double.parseDouble(record.get("Low")));
		canStk.setEps(ServiceUtil.isNullOrEmpty(record.get("EPS")) ? null : Double.parseDouble(record.get("EPS")));
		canStk.setCallPut(ServiceUtil.isNullOrEmpty(record.get("EPS")) ? null : record.get("EPS"));
		canStk.setImpVol(
				ServiceUtil.isNullOrEmpty(record.get("Imp Vol")) ? null : Double.parseDouble(record.get("Imp Vol")));
		canStk.setMark(ServiceUtil.isNullOrEmpty(record.get("Mark")) ? null : Double.parseDouble(record.get("Mark")));
		canStk.setExch(ServiceUtil.isNullOrEmpty(record.get("Exch")) ? null : record.get("Exch"));
		canStk.setMarketVal(
				ServiceUtil.isNullOrEmpty(record.get("Mkt Val")) ? null : Double.parseDouble(record.get("Mkt Val")));
		canStk.setMarketCap(ServiceUtil.isNullOrEmpty(record.get("Market Cap")) ? null
				: Double.parseDouble(record.get("Market Cap").replace("|", "")));
		canStk.setMarketCapinMillions(ServiceUtil.isNullOrEmpty(record.get("Market Cap")) ? null
				: Double.parseDouble(record.get("Market Cap").replace("|", "")) * 1000000);
		canStk.setShrOs(ServiceUtil.isNullOrEmpty(record.get("Shr Os")) ? null
				: Double.parseDouble(record.get("Shr Os").replace("|", "")));
		canStk.setOpenInt(
				ServiceUtil.isNullOrEmpty(record.get("Opn Int")) ? null : Double.parseDouble(record.get("Opn Int")));
		canStk.setDayPercentagePl(ServiceUtil.isNullOrEmpty(record.get("Day % P/L")) ? null
				: Double.parseDouble(record.get("Day % P/L")));
		canStk.setDayPl(
				ServiceUtil.isNullOrEmpty(record.get("Day P/L")) ? null : Double.parseDouble(record.get("Day P/L")));
		canStk.setPercentagePl(
				ServiceUtil.isNullOrEmpty(record.get("% P/L")) ? null : Double.parseDouble(record.get("% P/L")));
		canStk.setPl(ServiceUtil.isNullOrEmpty(record.get("P/L")) ? null : Double.parseDouble(record.get("P/L")));
		canStk.setBookCost(ServiceUtil.isNullOrEmpty(record.get("Book Cost")) ? null
				: Double.parseDouble(record.get("Book Cost")));
		canStk.setAvgCost(
				ServiceUtil.isNullOrEmpty(record.get("Avg Cost")) ? null : Double.parseDouble(record.get("Avg Cost")));
		canStk.setEntryDate(
				ServiceUtil.isNullOrEmpty(record.get("Entry Date")) ? null : LocalDate.parse(record.get("Entry Date"), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
		canStk.setQuantity(
				ServiceUtil.isNullOrEmpty(record.get("Quantity")) ? null : Integer.parseInt(record.get("Quantity")));
		canStk.setTrade(
				ServiceUtil.isNullOrEmpty(record.get("Trade")) ? null : Double.parseDouble(record.get("Trade")));
		canStk.setTrend(
				ServiceUtil.isNullOrEmpty(record.get("Trend")) ? null : Double.parseDouble(record.get("Trend")));
		canStk.setOpenIntChange(ServiceUtil.isNullOrEmpty(record.get("Opn Int Chg")) ? null
				: Double.parseDouble(record.get("Opn Int Chg")));
		canStk.setCurr(ServiceUtil.isNullOrEmpty(record.get("Curr")) ? null : record.get("Curr"));
		canStk.set_div(ServiceUtil.isNullOrEmpty(record.get("Div")) ? null : Double.parseDouble(record.get("Div")));
		canStk.setDivPayDt(
				ServiceUtil.isNullOrEmpty(record.get("Div Pay Dt")) ? null : LocalDate.parse(record.get("Div Pay Dt"), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
		canStk.setExDiv(ServiceUtil.isNullOrEmpty(record.get("Ex-Div")) ? null : LocalDate.parse(record.get("Ex-Div"), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
		canStk.setExpDate(
				ServiceUtil.isNullOrEmpty(record.get("Exp Dt")) ? null : LocalDate.parse(record.get("Exp Dt"), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
		canStk.setNumTrd(ServiceUtil.isNullOrEmpty(record.get("Num Trd")) ? null
				: Double.parseDouble(record.get("Num Trd").replace("|", "")));
		canStk.setPrcTck(ServiceUtil.isNullOrEmpty(record.get("Prc Tck")) ? null : record.get("Prc Tck"));
		canStk.setQuoteTrend(ServiceUtil.isNullOrEmpty(record.get("Quote Trend")) ? null : record.get("Quote Trend"));
		canStk.set_range(
				ServiceUtil.isNullOrEmpty(record.get("Range")) ? null : Double.parseDouble(record.get("Range")));
		canStk.setRelRange(ServiceUtil.isNullOrEmpty(record.get("Rel Range")) ? null : record.get("Rel Range"));
		canStk.setStrike(
				ServiceUtil.isNullOrEmpty(record.get("Strike")) ? null : Double.parseDouble(record.get("Strike")));

		return canStk;
	}
	
	
	/**
	 * Method to export data to csv from sql server table.
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public void exportToCsv(RequestDto requestDto) throws IOException {

		int pageSize = 10000; // Number of rows per page
		int pageNumber = 0;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		int recordsPerFile = requestDto.getNoOfRecords();
		String startD = requestDto.getStartDate();
		String endD = requestDto.getEndDate();
		LocalDate startDate = null, endDate = null;
		if (!startD.isBlank() && !endD.isBlank()) {
			startDate = ServiceUtil.getDate(requestDto.getStartDate());
			endDate = ServiceUtil.getDate(requestDto.getEndDate());
		}
		String symbol = requestDto.getSymbol();
		int fileCount = 1, recordsInCurrentFile = 0;
		String filePath = requestDto.getExportPath() + "/";
		File file = CSVUtil.createCSVFile(filePath, "Canadash-", fileCount);
		if (file == null) {
			System.out.println("No Directory present");
			return;
		}
		
		String[] headers = requestDto.getColumnNames();
		if (headers == null || headers.length == 0)
			headers = CSVUtil.getEntityHeaders(CanadashEntity.class);
		Page<CanadashEntity> sourcePage;

		CSVPrinter csvPrinter = null;
		try {
			csvPrinter = new CSVPrinter(new FileWriter(file), CSVFormat.DEFAULT.withHeader(headers));
			do {
				if (!startD.isBlank() && !endD.isBlank()) {
					if (!symbol.isBlank()) {
						sourcePage = stockRepository.findAllBySymbolAndDateRange(symbol, startDate,
								endDate, pageable);
					} else {
						sourcePage = stockRepository.findAllByDateRange(startDate, endDate, pageable);
					}
				} else if (!symbol.isBlank()) {
					sourcePage = stockRepository.findAllBySymbol(symbol, pageable);
				} else {
					sourcePage = stockRepository.findAll(pageable);
				}
				List<CanadashEntity> sourceData = sourcePage.getContent();

				for (CanadashEntity source : sourceData) {
					CSVUtil.writeEntityToCSV(csvPrinter, source, headers);

					recordsInCurrentFile++;

					if (!(recordsPerFile == 0)) {
						if (recordsInCurrentFile >= recordsPerFile) {
							csvPrinter.flush(); // Ensure the current data is written
							csvPrinter.close(); // Close the current file
							fileCount++; // Increment the file count
							file = CSVUtil.createCSVFile(filePath, "Canadash-", fileCount); // New file name
							csvPrinter = new CSVPrinter(new FileWriter(file), CSVFormat.DEFAULT.withHeader(headers)); // Open the next file
							recordsInCurrentFile = 0; // Reset record count for new file
						}
					}
				}

				pageNumber++; // Move to the next page
				pageable = PageRequest.of(pageNumber, pageSize); // Get next page
			} while (sourcePage.hasNext()); // Continue until there are no more pages
		} finally {
			if (csvPrinter != null) {
				csvPrinter.flush();
				csvPrinter.close(); // Ensure writer is closed in case of exceptions
			}
		}
	}

	
	/**
	 * This method is for getting unique symbols based on inputs
	 */
	@Override
	public void getTemplate(TemplateDto templateDto) throws IOException {

		int pageSize = 10000; // Number of rows per page
		int pageNumber = 0;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		int recordsPerFile = templateDto.getNoOfRecords();
		int fileCount = 1, recordsInCurrentFile = 0;
		String watchList = templateDto.getWatchList();
		String fileName = watchList + "-";
		String filePath = templateDto.getExportPath() + "/Canadash/Template/";
		File file = CSVUtil.createCSVFile(filePath, fileName, fileCount);
		if (file == null) {
			System.out.println("No Directory present");
			return;
		}
		String startD = templateDto.getStartDate();
		String endD = templateDto.getEndDate();
		LocalDate startDate = null, endDate = null;
		if (!startD.isBlank() && !endD.isBlank()) {
			startDate = ServiceUtil.getDate(templateDto.getStartDate());
			endDate = ServiceUtil.getDate(templateDto.getEndDate());
		}
		
		String[] headers = { "watchlist '" + watchList + "'" };
		List<String[]> nextLines = new ArrayList<>();
		nextLines.add(new String[] { "" });
		nextLines.add(new String[] { watchList });
		nextLines.add(new String[] { "Symbol" });
		Page<String> sourcePage;

		CSVPrinter csvPrinter = null;
		if (templateDto.isPrint()) {
			try {
				csvPrinter = new CSVPrinter(new FileWriter(file), CSVFormat.DEFAULT.withHeader(headers));
				csvPrinter.printRecords(nextLines);
				do {
					if (!startD.isBlank() && !endD.isBlank()) {
						sourcePage = stockRepository.findAllDistinctSymbolByDateRange(startDate, endDate, pageable);
					} else {
						sourcePage = stockRepository.findAllDistinctSymbol(pageable);
					}
					List<String> sourceData = sourcePage.getContent();

					for (String source : sourceData) {
						csvPrinter.printRecord(source);

						recordsInCurrentFile++;

						if (!(recordsPerFile == 0)) {
							if (recordsInCurrentFile >= recordsPerFile) {
								csvPrinter.flush(); // Ensure the current data is written
								csvPrinter.close(); // Close the current file
								fileCount++; // Increment the file count
								file = CSVUtil.createCSVFile(filePath, fileName, fileCount); // New file name
								csvPrinter = new CSVPrinter(new FileWriter(file), CSVFormat.DEFAULT.withHeader(headers)); // Open the next file
								csvPrinter.printRecords(nextLines);
								recordsInCurrentFile = 0; // Reset record count for new file
							}
						}
					}

					pageNumber++; // Move to the next page
					pageable = PageRequest.of(pageNumber, pageSize); // Get next page
				} while (sourcePage.hasNext()); // Continue until there are no more pages
			} finally {
				if (csvPrinter != null) {
					csvPrinter.flush();
					csvPrinter.close(); // Ensure writer is closed in case of exceptions
				}
			}
		}
	}

	/**
	 * This method is for getting all or unique data based on input from canadaat table 
	 */
	@Override
	public void getRecords(RecordsDto recordsDto) throws IOException {

		RequestDto requestDto = new RequestDto();
		requestDto.setExportPath(recordsDto.getExportPath());
		requestDto.setStartDate(recordsDto.getAllData().getStartDate());
		requestDto.setEndDate(recordsDto.getAllData().getEndDate());
		requestDto.setSymbol(recordsDto.getAllData().getSymbol());
		requestDto.setNoOfRecords(recordsDto.getAllData().getNoOfRecords());
		requestDto.setColumnNames(recordsDto.getAllData().getColumnNames());

		if (recordsDto.isUnique() && recordsDto.isAll()) {
			handleUniqueData(recordsDto);
			exportToCsv(requestDto);
		} else if (recordsDto.isUnique()) {
			handleUniqueData(recordsDto);
		} else {
			exportToCsv(requestDto);
		}
	}

	/**
	 * This method is for getting unique data from canadaat table based on inputs
	 * 
	 * @param recordsDto
	 * @throws IOException
	 */
	@Transactional
	public void handleUniqueData(RecordsDto recordsDto) throws IOException {
		int pageSize = 1000; // Number of rows per page
		int pageNumber = 0;
		int recordsPerFile = recordsDto.getUniqueData().getNoOfRecords();
		String startD = recordsDto.getUniqueData().getStartDate();
		String endD = recordsDto.getUniqueData().getEndDate();
		LocalDate startDate = null, endDate = null;
		if (!startD.isBlank() && !endD.isBlank()) {
			startDate = ServiceUtil.getDate(recordsDto.getUniqueData().getStartDate());
			endDate = ServiceUtil.getDate(recordsDto.getUniqueData().getEndDate());
		}
		String symbol = recordsDto.getUniqueData().getSymbol();
		boolean isWriteToDb = recordsDto.getUniqueData().isWriteToDb();
		if(!isWriteToDb) {
			pageSize = 10000;
		}
		boolean isWriteToLocal = recordsDto.getUniqueData().isWriteToLocal();
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		
		int fileCount = 1, recordsInCurrentFile = 0;
		String filePath = recordsDto.getExportPath() + "/Canadash/Records/";
		File file = CSVUtil.createCSVFile(filePath, "Unique-", fileCount);
		if (file == null) {
			System.out.println("No Directory present");
			return;
		}
		
		String[] headers = recordsDto.getUniqueData().getColumnNames();
		if (headers == null || headers.length == 0)
			headers = CSVUtil.getEntityHeaders(CanadashEntity.class);
		ModelMapper modelMapper = new ModelMapper();
		Page<CanadashEntity> sourcePage;

		CSVPrinter csvPrinter = null;
		try {
			csvPrinter = new CSVPrinter(new FileWriter(file), CSVFormat.DEFAULT.withHeader(headers));
			do {
				if (!startD.isBlank() && !endD.isBlank()) {
					if (!symbol.isBlank()) {
						sourcePage = stockRepository.findAllUniqueRecordsBySymbolAndDateRange(
								symbol, startDate, endDate, pageable);
					} else {
						sourcePage = stockRepository.findAllUniqueRecordsByDateRange(startDate, endDate, pageable);
					}
				} else if (!symbol.isBlank()) {
					sourcePage = stockRepository.findAllUniqueRecordsBySymbol(symbol,
							pageable);
				} else {
					sourcePage = stockRepository.findAllUniqueRecords(pageable);
				}
				List<CanadashEntity> sourceData = sourcePage.getContent();

				for (CanadashEntity source : sourceData) {
					if (isWriteToLocal) {
						CSVUtil.writeEntityToCSV(csvPrinter, source, headers);
					}
					recordsInCurrentFile++;

					if (isWriteToDb) {
						// Transform and save data to target table
						CanadashUniqueEntity targetEntity = modelMapper.map(source, CanadashUniqueEntity.class);
						stockUniqueRepository.save(targetEntity);
					}
					if (!(recordsPerFile == 0)) {
						if (recordsInCurrentFile >= recordsPerFile) {
							csvPrinter.flush(); // Ensure the current data is written
							csvPrinter.close(); // Close the current file
							fileCount++; // Increment the file count
							file = CSVUtil.createCSVFile(filePath, "Unique-", fileCount); // New file name
							csvPrinter = new CSVPrinter(new FileWriter(file), CSVFormat.DEFAULT.withHeader(headers)); // Open the next file
							recordsInCurrentFile = 0; // Reset record count for new file
						}
					}
				}

				pageNumber++; // Move to the next page
				pageable = PageRequest.of(pageNumber, pageSize); // Get next page
			} while (sourcePage.hasNext()); // Continue until there are no more pages
		} finally {
			if (csvPrinter != null) {
				csvPrinter.flush();
				csvPrinter.close(); // Ensure writer is closed in case of exceptions
			}
		}
	}

}
