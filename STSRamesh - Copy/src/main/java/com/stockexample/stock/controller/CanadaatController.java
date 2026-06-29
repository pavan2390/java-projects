package com.stockexample.stock.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockexample.stock.dto.RecordsDto;
import com.stockexample.stock.dto.RequestDto;
import com.stockexample.stock.dto.TemplateDto;
import com.stockexample.stock.service.StockService;
import com.stockexample.stock.service.impl.CanadaatServiceImpl;

@RestController
@RequestMapping("/canadaat/loader")
public class CanadaatController {

	private StockService stockService;
	
	@Autowired
	public CanadaatController(CanadaatServiceImpl stockService) {
		super();
		this.stockService = stockService;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadCanadaatData(@RequestBody RequestDto requestDto) {
		System.out.println("!!! Inside Controller !!!");
		System.out.println("uploadCanadaData - Method");

		try {
			stockService.uploadData(requestDto);
			return new ResponseEntity<>("Data uploaded successfully!", HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error reading the file.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/template")
	public ResponseEntity<String> getTemplate(@RequestBody TemplateDto TemplateDto) {
		System.out.println("!!! Inside Controller !!!");
		System.out.println("getTemplate - Method");

		try {
			stockService.getTemplate(TemplateDto);
			return new ResponseEntity<>("Template created successfully!", HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/records")
	public ResponseEntity<String> getRecords(@RequestBody RecordsDto  recordsDto) {
		System.out.println("!!! Inside Controller !!!");
		System.out.println("getRecords - Method");

		try {
			stockService.getRecords(recordsDto);
			return new ResponseEntity<>("Records created successfully!", HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
