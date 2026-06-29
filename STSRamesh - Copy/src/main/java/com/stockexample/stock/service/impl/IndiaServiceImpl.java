package com.stockexample.stock.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Row;
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
import com.stockexample.stock.entity.IndiaEntity;
import com.stockexample.stock.entity.IndiaUniqueEntity;
import com.stockexample.stock.repository.IndiaRepository;
import com.stockexample.stock.repository.IndiaUniqueRepository;
import com.stockexample.stock.service.StockService;
import com.stockexample.stock.util.CSVUtil;
import com.stockexample.stock.util.ServiceUtil;

@Service
public class IndiaServiceImpl implements StockService {

	@Autowired
	private IndiaRepository stockRepository;
	@Autowired
	private IndiaUniqueRepository stockUniqueRepository;

	/**
	 * This method is for uploading data to canadaat table
	 */
	@Override
	@Transactional
	public void uploadData(RequestDto requestDto) throws IOException {
		// Traverse the directory and its sub directories
		List<String> fileExtensions = List.of(".csv", ".xls");
		Files.walk(Paths.get(requestDto.getImportPath())).filter(Files::isRegularFile)
				.filter(path -> fileExtensions.stream().anyMatch(ext -> path.toString().endsWith(ext)))
				.forEach(path -> {
					List<IndiaEntity> lsData;
					try {
						lsData = (List<IndiaEntity>) ServiceUtil.extracted(path, this);
						if (lsData != null && !lsData.isEmpty()) {
							stockRepository.saveAll(lsData); // Batch save to DB
							ServiceUtil.sendFiles(path, requestDto.getExportPath() + "/India/Processed/");
						} else {
							ServiceUtil.sendFiles(path, requestDto.getExportPath() + "/India/UnProcessed/");
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
	public static IndiaEntity setCsvEnityValues(DateData extractedDate, String fileName, CSVRecord record)
			throws NumberFormatException {
		IndiaEntity indStk = new IndiaEntity();
		indStk.setDate(extractedDate.getLoadDate());
		indStk.setDayOfTheWeek(extractedDate.getDayOfWeek());
		indStk.setWeekOfTheMonth(extractedDate.getWeekOfMonth());
		indStk.setWeekOfTheYear(extractedDate.getWeekOfYear());
		indStk.setWatchList(fileName.replace(".csv", ""));
		indStk.setSelected(ServiceUtil.isNullOrEmpty(record.get("Selected")) ? null : record.get("Selected"));
		indStk.setShortExchangeName(ServiceUtil.isNullOrEmpty(record.get("Short Exchange Name")) ? null
				: record.get("Short Exchange Name"));
		indStk.setExchange(ServiceUtil.isNullOrEmpty(record.get("Exchange")) ? null : record.get("Exchange"));
		indStk.setNewsEvent(ServiceUtil.isNullOrEmpty(record.get("News / Event")) ? null : record.get("News / Event"));
		indStk.setScripCode(ServiceUtil.isNullOrEmpty(record.get("Scrip Code")) ? null : record.get("Scrip Code"));
		indStk.setScripName(ServiceUtil.isNullOrEmpty(record.get("Scrip Name")) ? null : record.get("Scrip Name"));
		indStk.setSectorIndustry(
				ServiceUtil.isNullOrEmpty(record.get("Sector / Industry")) ? null : record.get("Sector / Industry"));
		indStk.setPercentChange(
				ServiceUtil.isNullOrEmpty(record.get("% Change")) ? null : Double.parseDouble(record.get("% Change")));
		indStk.setLastTradedDate(
				ServiceUtil.isNullOrEmpty(record.get("Last Traded Qty")) ? null : record.get("Last Traded Qty"));
		indStk.setCurrent1(
				ServiceUtil.isNullOrEmpty(record.get("Current")) ? null : Double.parseDouble(record.get("Current")));
		indStk.setBidQty(
				ServiceUtil.isNullOrEmpty(record.get("Bid Qty")) ? null : Integer.parseInt(record.get("Bid Qty")));
		indStk.setBidPrice(ServiceUtil.isNullOrEmpty(record.get("Bid Price")) ? null
				: Double.parseDouble(record.get("Bid Price")));
		indStk.setOfferPrice(ServiceUtil.isNullOrEmpty(record.get("Offer Price")) ? null
				: Double.parseDouble(record.get("Offer Price")));
		indStk.setOfferQty(
				ServiceUtil.isNullOrEmpty(record.get("Offer Qty")) ? null : Integer.parseInt(record.get("Offer Qty")));
		indStk.setPOpen(ServiceUtil.isNullOrEmpty(record.get("Open")) ? null : Double.parseDouble(record.get("Open")));
		indStk.setPHigh(ServiceUtil.isNullOrEmpty(record.get("High")) ? null : Double.parseDouble(record.get("High")));
		indStk.setLow(ServiceUtil.isNullOrEmpty(record.get("Low")) ? null : Double.parseDouble(record.get("Low")));
		indStk.setClose1(
				ServiceUtil.isNullOrEmpty(record.get("Close")) ? null : Double.parseDouble(record.get("Close")));
		indStk.setLastUpdatedTime(
				ServiceUtil.isNullOrEmpty(record.get("Last Updated Time")) ? null : record.get("Last Updated Time"));
		indStk.setLastTradedTime(
				ServiceUtil.isNullOrEmpty(record.get("Last Traded Time")) ? null : record.get("Last Traded Time"));
		indStk.setLastTradedDate(
				ServiceUtil.isNullOrEmpty(record.get("Last Traded Date")) ? null : record.get("Last Traded Date"));
		indStk.setQty(ServiceUtil.isNullOrEmpty(record.get("Qty")) ? null : Integer.parseInt(record.get("Qty")));
		indStk.setDifference(ServiceUtil.isNullOrEmpty(record.get("Difference")) ? null
				: Double.parseDouble(record.get("Difference")));
		indStk.setTurnOver(
				ServiceUtil.isNullOrEmpty(record.get("TurnOver")) ? null : Double.parseDouble(record.get("TurnOver")));
		indStk.setAvgRate(
				ServiceUtil.isNullOrEmpty(record.get("Avg Rate")) ? null : Double.parseDouble(record.get("Avg Rate")));
		indStk.setPrevVal(
				ServiceUtil.isNullOrEmpty(record.get("PrevVal")) ? null : Double.parseDouble(record.get("PrevVal")));
		indStk.setCallPut(ServiceUtil.isNullOrEmpty(record.get("Call/Put")) ? null : record.get("Call/Put"));
		indStk.setStrikePrice(ServiceUtil.isNullOrEmpty(record.get("Strike Price")) ? null
				: Double.parseDouble(record.get("Strike Price")));
		indStk.setLotSize(
				ServiceUtil.isNullOrEmpty(record.get("Lot Size")) ? null : Integer.parseInt(record.get("Lot Size")));
		indStk.setFiftyTwoWeekHigh(ServiceUtil.isNullOrEmpty(record.get("52 Week High")) ? null
				: Double.parseDouble(record.get("52 Week High")));
		indStk.setFiftyTwoWeekLow(ServiceUtil.isNullOrEmpty(record.get("52 Week Low")) ? null
				: Double.parseDouble(record.get("52 Week Low")));
		indStk.setUpperCircuit(ServiceUtil.isNullOrEmpty(record.get("Upper Circuit")) ? null
				: Double.parseDouble(record.get("Upper Circuit")));
		indStk.setLowerCircuit(ServiceUtil.isNullOrEmpty(record.get("Lower Circuit")) ? null
				: Double.parseDouble(record.get("Lower Circuit")));
		indStk.setTotalBuyQty(ServiceUtil.isNullOrEmpty(record.get("Total Buy Qty")) ? null
				: Integer.parseInt(record.get("Total Buy Qty")));
		indStk.setTotalSellQty(ServiceUtil.isNullOrEmpty(record.get("Total Sell Qty")) ? null
				: Integer.parseInt(record.get("Total Sell Qty")));
		indStk.setOIDifference(ServiceUtil.isNullOrEmpty(record.get("OI Difference")) ? null
				: Integer.parseInt(record.get("OI Difference")));
		indStk.setOIDifferencePercentage(ServiceUtil.isNullOrEmpty(record.get("OI Difference Percentage")) ? null
				: Double.parseDouble(record.get("OI Difference Percentage")));
		indStk.setUnderlying(ServiceUtil.isNullOrEmpty(record.get("Underlying")) ? null : record.get("Underlying"));
		indStk.setPriceQuotation(
				ServiceUtil.isNullOrEmpty(record.get("Price Quotation")) ? null : record.get("Price Quotation"));
		indStk.setOpenInterest(ServiceUtil.isNullOrEmpty(record.get("Open Interest")) ? null
				: Integer.parseInt(record.get("Open Interest")));
		indStk.setExpiryDate(ServiceUtil.isNullOrEmpty(record.get("Expiry Date")) ? null : record.get("Expiry Date"));
		indStk.setCompanyName(
				ServiceUtil.isNullOrEmpty(record.get("Company Name")) ? null : record.get("Company Name"));
		indStk.setOpen1(
				ServiceUtil.isNullOrEmpty(record.get("P.Open")) ? null : Double.parseDouble(record.get("P.Open")));
		indStk.setPHigh(
				ServiceUtil.isNullOrEmpty(record.get("P.High")) ? null : Double.parseDouble(record.get("P.High")));
		indStk.setPLow(ServiceUtil.isNullOrEmpty(record.get("P.Low")) ? null : Double.parseDouble(record.get("P.Low")));
		indStk.setPClose(
				ServiceUtil.isNullOrEmpty(record.get("P.Close")) ? null : Double.parseDouble(record.get("P.Close")));
		indStk.setPQuantity(ServiceUtil.isNullOrEmpty(record.get("P.Quantity")) ? null
				: Integer.parseInt(record.get("P.Quantity")));
		indStk.setPivotRes3(ServiceUtil.isNullOrEmpty(record.get("Pivot Res 3")) ? null
				: Double.parseDouble(record.get("Pivot Res 3")));
		indStk.setPivotRes2(ServiceUtil.isNullOrEmpty(record.get("Pivot Res 2")) ? null
				: Double.parseDouble(record.get("Pivot Res 2")));
		indStk.setPivotRes1(ServiceUtil.isNullOrEmpty(record.get("Pivot Res 1")) ? null
				: Double.parseDouble(record.get("Pivot Res 1")));
		indStk.setPivot1(
				ServiceUtil.isNullOrEmpty(record.get("Pivot")) ? null : Double.parseDouble(record.get("Pivot")));
		indStk.setPivotSup1(ServiceUtil.isNullOrEmpty(record.get("Pivot Sup 1")) ? null
				: Double.parseDouble(record.get("Pivot Sup 1")));
		indStk.setPivotSup2(ServiceUtil.isNullOrEmpty(record.get("Pivot Sup 2")) ? null
				: Double.parseDouble(record.get("Pivot Sup 2")));
		indStk.setPivotSup3(ServiceUtil.isNullOrEmpty(record.get("Pivot Sup 3")) ? null
				: Double.parseDouble(record.get("Pivot Sup 3")));
		indStk.setSettlementPrice(ServiceUtil.isNullOrEmpty(record.get("Settlement Price")) ? null
				: Double.parseDouble(record.get("Settlement Price")));
		indStk.setIndexGroups(
				ServiceUtil.isNullOrEmpty(record.get("Index Groups")) ? null : record.get("Index Groups"));
		indStk.setInstrumentType(
				ServiceUtil.isNullOrEmpty(record.get("Instrument Type")) ? null : record.get("Instrument Type"));

		return indStk;
	}
	
	public static IndiaEntity setXslEnityValues(DateData extractedDate, String fileName, Row row, Map<String, Integer> headerMap)
			throws NumberFormatException {
		IndiaEntity indStk = new IndiaEntity();
		indStk.setDate(extractedDate.getLoadDate());
		indStk.setDayOfTheWeek(extractedDate.getDayOfWeek());
		indStk.setWeekOfTheMonth(extractedDate.getWeekOfMonth());
		indStk.setWeekOfTheYear(extractedDate.getWeekOfYear());
		indStk.setWatchList(fileName.replace(".csv", ""));
		indStk.setSelected(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Selected")))) ? null : String.valueOf(row.getCell(headerMap.get("Selected")).getBooleanCellValue()));
		indStk.setShortExchangeName(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Short Exchange Name")))) ? null
				: row.getCell(headerMap.get("Short Exchange Name")).getStringCellValue());
		indStk.setExchange(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Exchange")))) ? null : row.getCell(headerMap.get("Exchange")).getStringCellValue());
		indStk.setNewsEvent(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("News / Event")))) ? null : row.getCell(headerMap.get("News / Event")).getStringCellValue());
		indStk.setScripCode(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Scrip Code")))) ? null : row.getCell(headerMap.get("Scrip Code")).getStringCellValue());
		indStk.setScripName(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Scrip Name")))) ? null : row.getCell(headerMap.get("Scrip Name")).getStringCellValue());
		indStk.setSectorIndustry(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Sector / Industry")))) ? null : row.getCell(headerMap.get("Sector / Industry")).getStringCellValue());
		indStk.setPercentChange(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("% Change")))) ? null : row.getCell(headerMap.get("% Change")).getNumericCellValue());
		indStk.setLastTradedQty(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Last Traded Qty")))) ? null : (int)Math.round(row.getCell(headerMap.get("Last Traded Qty")).getNumericCellValue()));
		indStk.setCurrent1(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Current")))) ? null : row.getCell(headerMap.get("Current")).getNumericCellValue());
		indStk.setBidQty(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Bid Qty")))) ? null : (int)Math.round(row.getCell(headerMap.get("Bid Qty")).getNumericCellValue()));
		indStk.setBidPrice(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Bid Price")))) ? null
				: row.getCell(headerMap.get("Bid Price")).getNumericCellValue());
		indStk.setOfferPrice(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Offer Price")))) ? null
				: row.getCell(headerMap.get("Offer Price")).getNumericCellValue());
		indStk.setOfferQty(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Offer Qty")))) ? null : (int)Math.round(row.getCell(headerMap.get("Offer Qty")).getNumericCellValue()));
		indStk.setPOpen(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Open")))) ? null : row.getCell(headerMap.get("Open")).getNumericCellValue());
		indStk.setPHigh(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("High")))) ? null : row.getCell(headerMap.get("High")).getNumericCellValue());
		indStk.setLow(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Low")))) ? null : row.getCell(headerMap.get("Low")).getNumericCellValue());
		indStk.setClose1(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Close")))) ? null : row.getCell(headerMap.get("Close")).getNumericCellValue());
		indStk.setLastUpdatedTime(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Last Updated Time")))) ? null : row.getCell(headerMap.get("Last Updated Time")).getStringCellValue());
		indStk.setLastTradedTime(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Last Traded Time")))) ? null : row.getCell(headerMap.get("Last Traded Time")).getStringCellValue());
		indStk.setLastTradedDate(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Last Traded Date")))) ? null : String.valueOf(row.getCell(headerMap.get("Last Traded Date")).getNumericCellValue()));
		indStk.setQty(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Qty")))) ? null : (int)Math.round(row.getCell(headerMap.get("Qty")).getNumericCellValue()));
		indStk.setDifference(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Difference")))) ? null
				: row.getCell(headerMap.get("Difference")).getNumericCellValue());
		indStk.setTurnOver(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("TurnOver")))) ? null : row.getCell(headerMap.get("TurnOver")).getNumericCellValue());
		indStk.setAvgRate(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Avg Rate")))) ? null : row.getCell(headerMap.get("Avg Rate")).getNumericCellValue());
		indStk.setPrevVal(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("PrevVal")))) ? null : row.getCell(headerMap.get("PrevVal")).getNumericCellValue());
		indStk.setCallPut(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Call/Put")))) ? null : row.getCell(headerMap.get("Call/Put")).getStringCellValue());
		indStk.setStrikePrice(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Strike Price")))) ? null
				: row.getCell(headerMap.get("Strike Price")).getNumericCellValue());
		indStk.setLotSize(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Lot Size")))) ? null : (int)Math.round(row.getCell(headerMap.get("Lot Size")).getNumericCellValue()));
		indStk.setFiftyTwoWeekHigh(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("52 Week High")))) ? null
				: row.getCell(headerMap.get("52 Week High")).getNumericCellValue());
		indStk.setFiftyTwoWeekLow(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("52 Week Low")))) ? null
				: row.getCell(headerMap.get("52 Week Low")).getNumericCellValue());
		indStk.setUpperCircuit(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Upper Circuit")))) ? null
				: row.getCell(headerMap.get("Upper Circuit")).getNumericCellValue());
		indStk.setLowerCircuit(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Lower Circuit")))) ? null
				: row.getCell(headerMap.get("Lower Circuit")).getNumericCellValue());
		indStk.setTotalBuyQty(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Total Buy Qty")))) ? null
				: (int)Math.round(row.getCell(headerMap.get("Total Buy Qty")).getNumericCellValue()));
		indStk.setTotalSellQty(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Total Sell Qty")))) ? null
				: (int)Math.round(row.getCell(headerMap.get("Total Sell Qty")).getNumericCellValue()));
		indStk.setOIDifference(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("OI Difference")))) ? null
				: (int)Math.round(row.getCell(headerMap.get("OI Difference")).getNumericCellValue()));
		indStk.setOIDifferencePercentage(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("OI Difference Percentage")))) ? null
				: row.getCell(headerMap.get("OI Difference Percentage")).getNumericCellValue());
		indStk.setUnderlying(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Underlying")))) ? null : String.valueOf(row.getCell(headerMap.get("Underlying")).getNumericCellValue()));
		indStk.setPriceQuotation(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Price Quotation")))) ? null : row.getCell(headerMap.get("Price Quotation")).getStringCellValue());
		indStk.setOpenInterest(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Open Interest")))) ? null
				: (int)Math.round(row.getCell(headerMap.get("Open Interest")).getNumericCellValue()));
		indStk.setExpiryDate(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Expiry Date")))) ? null : row.getCell(headerMap.get("Expiry Date")).getStringCellValue());
		indStk.setCompanyName(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Company Name")))) ? null : row.getCell(headerMap.get("Company Name")).getStringCellValue());
		indStk.setOpen1(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("P.Open")))) ? null : row.getCell(headerMap.get("P.Open")).getNumericCellValue());
		indStk.setPHigh(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("P.High")))) ? null : row.getCell(headerMap.get("P.High")).getNumericCellValue());
		indStk.setPLow(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("P.Low")))) ? null : row.getCell(headerMap.get("P.Low")).getNumericCellValue());
		indStk.setPClose(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("P.Close")))) ? null : row.getCell(headerMap.get("P.Close")).getNumericCellValue());
		indStk.setPQuantity(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("P.Quantity")))) ? null
				: (int)Math.round(row.getCell(headerMap.get("P.Quantity")).getNumericCellValue()));
		indStk.setPivotRes3(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Pivot Res 3")))) ? null
				: row.getCell(headerMap.get("Pivot Res 3")).getNumericCellValue());
		indStk.setPivotRes2(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Pivot Res 2")))) ? null
				: row.getCell(headerMap.get("Pivot Res 2")).getNumericCellValue());
		indStk.setPivotRes1(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Pivot Res 1")))) ? null
				: row.getCell(headerMap.get("Pivot Res 1")).getNumericCellValue());
		indStk.setPivot1(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Pivot")))) ? null : row.getCell(headerMap.get("Pivot")).getNumericCellValue());
		indStk.setPivotSup1(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Pivot Sup 1")))) ? null
				: row.getCell(headerMap.get("Pivot Sup 1")).getNumericCellValue());
		indStk.setPivotSup2(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Pivot Sup 2")))) ? null
				: row.getCell(headerMap.get("Pivot Sup 2")).getNumericCellValue());
		indStk.setPivotSup3(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Pivot Sup 3")))) ? null
				: row.getCell(headerMap.get("Pivot Sup 3")).getNumericCellValue());
		indStk.setSettlementPrice(ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Settlement Price")))) ? null
				: row.getCell(headerMap.get("Settlement Price")).getNumericCellValue());
		indStk.setIndexGroups(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Index Groups")))) ? null : row.getCell(headerMap.get("Index Groups")).getStringCellValue());
		indStk.setInstrumentType(
				ServiceUtil.isNullOrEmpty(String.valueOf(row.getCell(headerMap.get("Instrument Type")))) ? null : row.getCell(headerMap.get("Instrument Type")).getStringCellValue());

		return indStk;
		
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
		File file = CSVUtil.createCSVFile(filePath, "India-", fileCount);
		if (file == null) {
			System.out.println("No Directory present");
			return;
		}

		String[] headers = requestDto.getColumnNames();
		if (headers == null || headers.length == 0)
			headers = CSVUtil.getEntityHeaders(IndiaEntity.class);
		Page<IndiaEntity> sourcePage;

		CSVPrinter csvPrinter = null;
		try {
			csvPrinter = new CSVPrinter(new FileWriter(file), CSVFormat.DEFAULT.withHeader(headers));
			do {
				if (!startD.isBlank() && !endD.isBlank()) {
					if (!symbol.isBlank()) {
						sourcePage = stockRepository.findAllByScripNameAndDateRange(symbol, startDate, endDate, pageable);
					} else {
						sourcePage = stockRepository.findAllByDateRange(startDate, endDate, pageable);
					}
				} else if (!symbol.isBlank()) {
					sourcePage = stockRepository.findAllByScripName(symbol, pageable);
				} else {
					sourcePage = stockRepository.findAll(pageable);
				}
				List<IndiaEntity> sourceData = sourcePage.getContent();

				for (IndiaEntity source : sourceData) {
					CSVUtil.writeEntityToCSV(csvPrinter, source, headers);

					recordsInCurrentFile++;

					if (!(recordsPerFile == 0)) {
						if (recordsInCurrentFile >= recordsPerFile) {
							csvPrinter.flush(); // Ensure the current data is written
							csvPrinter.close(); // Close the current file
							fileCount++; // Increment the file count
							file = CSVUtil.createCSVFile(filePath, "India-", fileCount); // New file name
							csvPrinter = new CSVPrinter(new FileWriter(file), CSVFormat.DEFAULT.withHeader(headers));
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

	@Override
	public void getTemplate(TemplateDto templateDto) throws IOException {
		// TODO Auto-generated method stub
		throw new IOException();
	}

	/**
	 * This method is for getting all or unique data based on input from canadaat
	 * table
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
		if (!isWriteToDb) {
			pageSize = 10000;
		}
		boolean isWriteToLocal = recordsDto.getUniqueData().isWriteToLocal();
		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		int fileCount = 1, recordsInCurrentFile = 0;
		String filePath = recordsDto.getExportPath() + "/India/Records/";
		File file = CSVUtil.createCSVFile(filePath, "Unique-", fileCount);
		if (file == null) {
			System.out.println("No Directory present");
			return;
		}

		String[] headers = recordsDto.getUniqueData().getColumnNames();
		if (headers == null || headers.length == 0)
			headers = CSVUtil.getEntityHeaders(IndiaEntity.class);
		ModelMapper modelMapper = new ModelMapper();
		Page<IndiaEntity> sourcePage;

		CSVPrinter csvPrinter = null;
		try {
			csvPrinter = new CSVPrinter(new FileWriter(file), CSVFormat.DEFAULT.withHeader(headers));
			do {
				if (!startD.isBlank() && !endD.isBlank()) {
					if (!symbol.isBlank()) {
						sourcePage = stockRepository.findAllUniqueRecordsByScripNameAndDateRange(symbol, startDate,
								endDate, pageable);
					} else {
						sourcePage = stockRepository.findAllUniqueRecordsByDateRange(startDate, endDate, pageable);
					}
				} else if (!symbol.isBlank()) {
					sourcePage = stockRepository.findAllUniqueRecordsByScripName(symbol, pageable);
				} else {
					sourcePage = stockRepository.findAllUniqueRecords(pageable);
				}
				List<IndiaEntity> sourceData = sourcePage.getContent();

				for (IndiaEntity source : sourceData) {
					if (isWriteToLocal) {
						CSVUtil.writeEntityToCSV(csvPrinter, source, headers);
					}
					recordsInCurrentFile++;

					if (isWriteToDb) {
						// Transform and save data to target table
						IndiaUniqueEntity targetEntity = modelMapper.map(source, IndiaUniqueEntity.class);
						stockUniqueRepository.save(targetEntity);
					}
					if (!(recordsPerFile == 0)) {
						if (recordsInCurrentFile >= recordsPerFile) {
							csvPrinter.flush(); // Ensure the current data is written
							csvPrinter.close(); // Close the current file
							fileCount++; // Increment the file count
							file = CSVUtil.createCSVFile(filePath, "Unique-", fileCount); // New file name
							csvPrinter = new CSVPrinter(new FileWriter(file), CSVFormat.DEFAULT.withHeader(headers)); 
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
