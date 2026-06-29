package com.stockexample.stock.service;

import java.io.IOException;

import com.stockexample.stock.dto.RecordsDto;
import com.stockexample.stock.dto.RequestDto;
import com.stockexample.stock.dto.TemplateDto;

public interface StockService {

	void uploadData(RequestDto requestDto) throws IOException;

	void getTemplate(TemplateDto templateDto) throws IOException;

	void getRecords(RecordsDto recordsDto) throws IOException;
}
