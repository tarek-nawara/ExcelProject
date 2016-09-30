package com.erricson.tttool.model.IO;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.erricson.tttool.model.entities.ExcelEntity;

public class ExcelWriter {
	// ------------------||
	// class variables --||
	// ------------------||
	private static final Logger LOG = Logger.getLogger(ExcelWriter.class);
	static {
		BasicConfigurator.configure();
	}
	private static final int COLUMN_NUM = 5;

	// ---------------------||
	// instance variables --||
	// ---------------------||
	private String destinationFilePath;
	private XSSFWorkbook workBook;
	private XSSFSheet spreadSheet;

	public void open(String destinationFilePath) {
		this.destinationFilePath = destinationFilePath;
		this.workBook = new XSSFWorkbook();
		this.spreadSheet = workBook.createSheet();
		Row headerRow = spreadSheet.createRow(0);
		createCells(headerRow, "Number", "Affected CI", "Title Comment",
				"Description Comment", "Criteria");
	}
	
	public boolean write(ExcelEntity entity) {
		
	}
	
	public boolean write(String destinationFilePath,
			List<ExcelEntity> entities) {
		boolean result = false;
		try {
			FileOutputStream fout = new FileOutputStream(destinationFilePath);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			this.workBook = new XSSFWorkbook();
			XSSFSheet spreadSheet = workBook.createSheet();
			Row headerRow = spreadSheet.createRow(0);
			createCells(headerRow, "Number", "Affected CI", "Title Comment",
					"Description Comment", "Criteria");
			for (int i = 1; i <= entities.size(); ++i) {
				Row row = spreadSheet.createRow(i);
				ExcelEntity entity = entities.get(i - 1);
				createCells(row, entity.getNumber(), entity.getACI(),
						entity.getTitleComment(),
						entity.getDescriptionComment(), entity.getCriteria());
			}
			for (int i = 0; i < COLUMN_NUM; ++i) {
				spreadSheet.autoSizeColumn(i);
			}
			workBook.write(outputStream);
			outputStream.writeTo(fout);
			outputStream.close();
			fout.close();
			workBook.close();
			result = true;
		} catch (Exception e) {
			LOG.info("failed to write to destination file", e);
		}
		return result;
	}

	private static void createCells(Row row, String... cellValues) {
		for (int i = 0; i < cellValues.length; ++i) {
			Cell cell = row.createCell(i);
			cell.setCellValue(new XSSFRichTextString(cellValues[i]));
		}
	}
}
