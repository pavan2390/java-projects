package com.stockexample.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

	private String importPath;
	private String exportPath;
	private int noOfRecords;
	private String[] columnNames;
	private boolean print;
	private String startDate;
	private String endDate;
	private String symbol;
}