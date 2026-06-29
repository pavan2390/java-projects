package com.stockexample.stock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllData {

	private String startDate;
	private String endDate;
	private String symbol;
	private int noOfRecords;
	private String[] columnNames;
}
