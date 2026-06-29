package com.stockexample.stock.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.stockexample.stock.entity.CanadaatEntity;
import com.stockexample.stock.entity.CanadaatUniqueEntity;
import com.stockexample.stock.repository.CanadaatRepository;
import com.stockexample.stock.repository.CanadaatUniqueRepository;
import com.stockexample.stock.service.StockService;
import com.stockexample.stock.util.CSVUtil;
import com.stockexample.stock.util.ServiceUtil;

@Service
public class CanadaatServiceImpl implements StockService {

	Logger LOGER = LoggerFactory.getLogger(CanadaatServiceImpl.class);

	@Autowired
	private CanadaatRepository stockRepository;
	@Autowired
	private CanadaatUniqueRepository stockUniqueRepository;

	/**
	 * This method is for uploading data to canadaat table
	 */
	@Override
	@Transactional
	public void uploadData(RequestDto requestDto) throws IOException {
		// Traverse the directory and its sub directories
		Files.walk(Paths.get(requestDto.getImportPath())).filter(Files::isRegularFile)
				.filter(path -> path.toString().endsWith(".csv")) // Filter for CSV files
				.forEach(path -> {
					List<CanadaatEntity> lsData;
					try {
						lsData = (List<CanadaatEntity>) ServiceUtil.extracted(path, this);
						if (lsData != null && !lsData.isEmpty()) {
							stockRepository.saveAll(lsData); // Batch save to DB
							ServiceUtil.sendFiles(path, requestDto.getExportPath() + "/Canadaat/Processed/"); //send files to processed folder
						} else {
							ServiceUtil.sendFiles(path, requestDto.getExportPath() + "/Canadaat/UnProcessed/"); //send files to unprocessed folder
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

		if (requestDto.isPrint()) {
			exportToCsv(requestDto);
		}
	}

	/**
	 * @param extractedDate
	 * @param fileName
	 * @param record
	 * @return
	 * @throws NumberFormatException
	 */
	public static CanadaatEntity setCsvEnityValues(DateData extractedDate, String fileName, CSVRecord record)
			throws NumberFormatException {
		DecimalFormat df = new DecimalFormat("#.##");
		CanadaatEntity canStk = new CanadaatEntity();
		canStk.setDate(extractedDate.getLoadDate());
		canStk.setDayOfTheWeek(extractedDate.getDayOfWeek());
		canStk.setWeekOfTheMonth(extractedDate.getWeekOfMonth());
		canStk.setWeekOfTheYear(extractedDate.getWeekOfYear());
		canStk.setWatchList(fileName.replace(".csv", ""));
		canStk.setSymbol(record.get("Symbol"));
		canStk.setDescription(record.get("Description"));
		canStk.setVolume(ServiceUtil.isNullOrEmpty(record.get("Volume")) ? null : record.get("Volume"));
		canStk.setPercentChange(ServiceUtil.isNullOrEmpty(record.get("% Change")) ? null
				: Double.parseDouble(record.get("% Change").replace(",", "")));
		canStk.setMarketCap(record.get("Market Cap."));
		canStk.setMarketCapInMNumbers(ServiceUtil.isNullOrEmpty(record.get("Market Cap.")) ? null
				: Double.parseDouble(record.get("Market Cap.").replace("M", "")));
		canStk.setMarketCapNinMillions(ServiceUtil.isNullOrEmpty(record.get("Market Cap.")) ? null
				: Double.parseDouble(record.get("Market Cap.").replace("M", "")) * 1000000);
		canStk.setShares(record.get("Shares"));
		canStk.setBeta(
				ServiceUtil.isNullOrEmpty(record.get("Beta")) ? null : Double.parseDouble(record.get("Beta").replace(",", "")));
		canStk.setMark(
				ServiceUtil.isNullOrEmpty(record.get("Mark")) ? null : Double.parseDouble(record.get("Mark").replace(",", "")));
		canStk.setLastR(
				ServiceUtil.isNullOrEmpty(record.get("Last")) ? null : Double.parseDouble(record.get("Last").replace(",", "")));
		canStk.setOpenInt(
				ServiceUtil.isNullOrEmpty(record.get("Open Int")) ? null : Long.parseLong(record.get("Open Int").replace(",", "")));
		canStk.setSpread(
				ServiceUtil.isNullOrEmpty(record.get("Spread")) ? null : Double.parseDouble(record.get("Spread").replace(",", "")));
		canStk.setNetChange(ServiceUtil.isNullOrEmpty(record.get("Net Change")) ? null
				: Double.parseDouble(record.get("Net Change").replace(",", "")));
		canStk.setBidSize(
				ServiceUtil.isNullOrEmpty(record.get("Bid Size")) ? null : Long.parseLong(record.get("Bid Size").replace(",", "")));
		canStk.setSize(ServiceUtil.isNullOrEmpty(record.get("Size")) ? null : record.get("Size"));
		canStk.setAskSize(
				ServiceUtil.isNullOrEmpty(record.get("Ask Size")) ? null : Long.parseLong(record.get("Ask Size").replace(",", "")));
		canStk.setLastSize(ServiceUtil.isNullOrEmpty(record.get("Last Size")) ? null
				: Long.parseLong(record.get("Last Size").replace(",", "")));
		canStk.setInstrument(record.get("Instrument"));
		canStk.setOpen1(
				ServiceUtil.isNullOrEmpty(record.get("Open")) ? null : Double.parseDouble(record.get("Open").replace(",", "")));
		canStk.setFiftyTwotWeekLow(ServiceUtil.isNullOrEmpty(record.get("52 week Low")) ? null
				: Double.parseDouble(record.get("52 week Low").replace(",", "")));
		canStk.setLow(ServiceUtil.isNullOrEmpty(record.get("Low")) ? null : Double.parseDouble(record.get("Low").replace(",", "")));
		canStk.setBid(ServiceUtil.isNullOrEmpty(record.get("Bid")) ? null : Double.parseDouble(record.get("Bid").replace(",", "")));
		canStk.setAsk(ServiceUtil.isNullOrEmpty(record.get("Ask")) ? null : Double.parseDouble(record.get("Ask").replace(",", "")));
		canStk.setHigh(
				ServiceUtil.isNullOrEmpty(record.get("High")) ? null : Double.parseDouble(record.get("High").replace(",", "")));
		canStk.setPrevClose(ServiceUtil.isNullOrEmpty(record.get("Prev Close")) ? null
				: Double.parseDouble(record.get("Prev Close").replace(",", "")));
		canStk.setFiftyTwotWeekHigh(ServiceUtil.isNullOrEmpty(record.get("52 week High")) ? null
				: Double.parseDouble(record.get("52 week High").replace(",", "")));
		canStk.setFiftyTwoWeekDiff(ServiceUtil.isNullOrEmpty(record.get("52 week High").replace(",", "")) ? null
				: Double.parseDouble(df.format(canStk.getFiftyTwotWeekHigh() - canStk.getFiftyTwotWeekLow())));
		canStk.setCusip(record.get("CUSIP"));
		canStk.setMaintenaceMargin(ServiceUtil.isNullOrEmpty(record.get("Maintenance Margin")) ? null
				: Double.parseDouble(record.get("Maintenance Margin").replace(",", "")));
		canStk.setDaysToExpiration(ServiceUtil.isNullOrEmpty(record.get("Days To Expiration")) ? null
				: Integer.parseInt(record.get("Days To Expiration").replace(",", "")));
		canStk.setDelta(
				ServiceUtil.isNullOrEmpty(record.get("Delta")) ? null : Double.parseDouble(record.get("Delta").replace(",", "")));
		canStk.setExpirationDate(record.get("Expiration Date"));
		canStk.setFirstNoticeDate(record.get("First Notice Date"));
		canStk.setImpliedVolatility(ServiceUtil.isNullOrEmpty(record.get("Impl Vol")) ? null
				: Double.parseDouble(record.get("Impl Vol").replace(",", "")));
		canStk.setGamma(
				ServiceUtil.isNullOrEmpty(record.get("Gamma")) ? null : Double.parseDouble(record.get("Gamma").replace(",", "")));
		canStk.setTradingHalt(record.get("Trading Halt"));
		canStk.setLastTradeDate(record.get("Last Trade Date"));
		canStk.setLimitDown(ServiceUtil.isNullOrEmpty(record.get("Limit Down")) ? null
				: Double.parseDouble(record.get("Limit Down").replace(",", "")));
		canStk.setLimitUp(ServiceUtil.isNullOrEmpty(record.get("Limit Up")) ? null
				: Double.parseDouble(record.get("Limit Up").replace(",", "")));
		canStk.setRho(ServiceUtil.isNullOrEmpty(record.get("Rho")) ? null : Double.parseDouble(record.get("Rho").replace(",", "")));
		canStk.setTheoPrice(ServiceUtil.isNullOrEmpty(record.get("Theo Price")) ? null
				: Double.parseDouble(record.get("Theo Price").replace(",", "")));
		canStk.setStrike(
				ServiceUtil.isNullOrEmpty(record.get("Strike")) ? null : Double.parseDouble(record.get("Strike").replace(",", "")));
		canStk.setTheta(
				ServiceUtil.isNullOrEmpty(record.get("Theta")) ? null : Double.parseDouble(record.get("Theta").replace(",", "")));
		canStk.setTickValue(ServiceUtil.isNullOrEmpty(record.get("Tick Value")) ? null
				: Double.parseDouble(record.get("Tick Value").replace(",", "")));
		canStk.setVega(
				ServiceUtil.isNullOrEmpty(record.get("Vega")) ? null : Double.parseDouble(record.get("Vega").replace(",", "")));
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
		File file = CSVUtil.createCSVFile(filePath, "Canadaat-", fileCount);
		if (file == null) {
			System.out.println("No Directory present");
			return;
		}
		
		String[] headers = requestDto.getColumnNames();
		if (headers == null || headers.length == 0)
			headers = CSVUtil.getEntityHeaders(CanadaatEntity.class);
		Page<CanadaatEntity> sourcePage;

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
				List<CanadaatEntity> sourceData = sourcePage.getContent();

				for (CanadaatEntity source : sourceData) {
					CSVUtil.writeEntityToCSV(csvPrinter, source, headers);

					recordsInCurrentFile++;

					if (!(recordsPerFile == 0)) {
						if (recordsInCurrentFile >= recordsPerFile) {
							csvPrinter.flush(); // Ensure the current data is written
							csvPrinter.close(); // Close the current file
							fileCount++; // Increment the file count
							file = CSVUtil.createCSVFile(filePath, "Canadaat-", fileCount); // New file name
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
		String filePath = templateDto.getExportPath() + "/Canadaat/Template/";
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
		String filePath = recordsDto.getExportPath() + "/Canadaat/Records/";
		File file = CSVUtil.createCSVFile(filePath, "Unique-", fileCount);
		if (file == null) {
			System.out.println("No Directory present");
			return;
		}
		
		String[] headers = recordsDto.getUniqueData().getColumnNames();
		if (headers == null || headers.length == 0)
			headers = CSVUtil.getEntityHeaders(CanadaatEntity.class);
		ModelMapper modelMapper = new ModelMapper();
		Page<CanadaatEntity> sourcePage;

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
				List<CanadaatEntity> sourceData = sourcePage.getContent();

				for (CanadaatEntity source : sourceData) {
					if (isWriteToLocal) {
						CSVUtil.writeEntityToCSV(csvPrinter, source, headers);
					}
					recordsInCurrentFile++;

					if (isWriteToDb) {
						// Transform and save data to target table
						CanadaatUniqueEntity targetEntity = modelMapper.map(source, CanadaatUniqueEntity.class);
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
