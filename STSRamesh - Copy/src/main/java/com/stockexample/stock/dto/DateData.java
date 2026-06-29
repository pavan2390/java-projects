package com.stockexample.stock.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateData {

	private LocalDate loadDate;
	private int dayOfWeek;
	private String dayOfWeekName;
	private int weekOfMonth;
	private int weekOfYear;
}
