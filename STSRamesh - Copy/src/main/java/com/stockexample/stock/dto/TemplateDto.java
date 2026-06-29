package com.stockexample.stock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateDto {

	private String exportPath;
	private String watchList;
	private int noOfRecords;
	private boolean print;
	private String startDate;
	private String endDate;
}
