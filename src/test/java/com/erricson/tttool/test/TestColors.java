package com.erricson.tttool.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellFill;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;

public class TestColors {
	public static void main(String[] args) throws Exception {
		String fileName = "C:\\Users\\Tarek Nawara\\Desktop\\TT Tool\\output.xlsx";
		InputStream inputStream = new FileInputStream(fileName);
		Workbook wb = new XSSFWorkbook(inputStream);
		Sheet sheet = wb.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			Iterator<Cell> cells = row.cellIterator();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
				XSSFColor color = new XSSFColor(IndexedColors.BLACK);
				style.setBorderColor(BorderSide.LEFT, color);
				style.setBorderColor(BorderSide.RIGHT, color);
				style.setBorderColor(BorderSide.BOTTOM, color);
				style.setBorderColor(BorderSide.TOP, color);
				style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				style.setFillBackgroundColor(IndexedColors.AUTOMATIC.getIndex());
				cell.setCellStyle(style);
			}
		}
		OutputStream outputStream = new FileOutputStream(fileName);
		wb.write(outputStream);
		wb.close();
	}
}
