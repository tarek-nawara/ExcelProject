package com.erricson.tttool.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestParsing {
	public static void main(String[] args) throws IOException {
//		String fileName = "C:\\Users\\Tarek Nawara\\Desktop\\TT Tool\\TX TT August.xlsx";
		String fileName = "C:\\Users\\Tarek Nawara\\Desktop\\output2.xlsb";
		InputStream inputStream = new FileInputStream(fileName);
		Workbook wb = new XSSFWorkbook(inputStream);
		Sheet sheet = wb.getSheetAt(0);
		/*
		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			Iterator<Cell> cells = row.cellIterator();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					System.out.print(cell.getStringCellValue() + " ");
				}
			}
			System.out.println();
		}
		*/
		Row row = sheet.getRow(0);
		Iterator<Cell> cells = row.cellIterator();
		while (cells.hasNext()) {
			Cell cell = cells.next();
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				System.out.println(cell.getStringCellValue());
			}
		}
		wb.close();
	}
}
