package com.stockexample.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordsDto {

	private String exportPath;
	private boolean all;
	private AllData allData;
	private boolean unique;
	private UniqueData uniqueData;
}
