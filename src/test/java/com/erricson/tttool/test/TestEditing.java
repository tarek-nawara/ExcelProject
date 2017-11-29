package com.erricson.tttool.test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestEditing {
	public static void main(String[] args) throws Exception {
		String fileName = "C:\\Users\\Tarek Nawara\\Desktop\\TT Tool\\New TT Tool Key Words.xlsx";
		InputStream inputStream = new FileInputStream(fileName);
		Workbook wb = new XSSFWorkbook(inputStream);
		Sheet sheet = wb.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			short size = row.getLastCellNum();
			Cell cell = row.createCell(size + 1);
			cell.setCellValue(new XSSFRichTextString("hamada"));
		}
	}
}
