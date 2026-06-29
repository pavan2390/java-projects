/*
 * package com.stockexample.stock.controller;
 * 
 * import java.util.List;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.HttpStatus; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.stockexample.stock.datasource.service.DynamicDbService; import
 * com.stockexample.stock.dto.DataSourceDto;
 * 
 * @RestController
 * 
 * @RequestMapping("/stock/database") public class DatabaseController {
 * 
 * @Autowired private DynamicDbService dynamicDbService;
 * 
 * 
 * @PostMapping("/create") public ResponseEntity<String>
 * createDatabaseAndTable(@RequestBody DataSourceDto dataSourceDto) { try {
 * List<String> response =
 * dynamicDbService.createDatabaseAndTable(dataSourceDto);
 * if(response.isEmpty()) {
 * response.add("Database and table created sucessfully"); } return new
 * ResponseEntity<>(response.toString(), HttpStatus.CREATED); } catch (Exception
 * e) { e.printStackTrace(); return new ResponseEntity<>(e.getMessage(),
 * HttpStatus.INTERNAL_SERVER_ERROR); } }
 * 
 * }
 * 
 */