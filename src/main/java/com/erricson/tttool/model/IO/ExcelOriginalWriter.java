package com.erricson.tttool.model.IO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;

import com.erricson.tttool.model.entities.Criteria;
import com.erricson.tttool.model.entities.EntityUtils;
import com.erricson.tttool.model.entities.ExcelRecord;
import com.erricson.tttool.model.handlers.ExcelEntityProcessor;

public class ExcelOriginalWriter {
    private static final Logger LOG = Logger.getLogger(ExcelOriginalWriter.class);

    private final Map<String, Integer> requiredCols = new HashMap<>();
    private final Map<String, Integer> alreadyExist = new HashMap<>();
    private final Map<String, Integer> originalCols = new HashMap<>();

    public ExcelOriginalWriter() {
        requiredCols.put("Title Comment", -1);
        requiredCols.put("Description Comment", -1);
        requiredCols.put("Criteria", -1);

        originalCols.put("Title", -1);
        originalCols.put("Description", -1);
        originalCols.put("Number", -1);
        originalCols.put("Affected CI", -1);
    }

    public boolean write(final String sourceFile, final Map<String, Criteria> allCriteria) {
        boolean result = false;
        try {
            final InputStream inputStream = new FileInputStream(sourceFile);
            final XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
            final XSSFSheet spreadSheet = workBook.getSheetAt(0);
            final Row headerRow = spreadSheet.getRow(0);
            figureRequiredCols(headerRow);
            createMissingCols(headerRow, alreadyExist, buildStyle(workBook.createCellStyle()));
            final Iterator<Row> rows = spreadSheet.rowIterator();
            if (rows.hasNext()) {
                rows.next();
            }
            while (rows.hasNext()) {
                Row row = rows.next();
                createMissingCols(row, buildStyle(workBook.createCellStyle()));
                ExcelRecord entity = EntityUtils.getEntityFromRow(originalCols, row);
                ExcelEntityProcessor.processExcelEntity(entity, allCriteria);
                writeEntity(entity, row);
            }
            for (Entry<String, Integer> entry : alreadyExist.entrySet()) {
                if (entry.getValue() == null || entry.getValue().equals(-1)) {
                    continue;
                }
                spreadSheet.autoSizeColumn(entry.getValue());
            }
            final OutputStream out = new FileOutputStream(sourceFile);
            workBook.write(out);
            workBook.close();
            result = true;
        } catch (Exception e) {
            LOG.info("failed to write to destination file", e);
        }
        return result;
    }

    private void writeEntity(ExcelRecord entity, Row row) {
        for (Entry<String, Integer> entry : alreadyExist.entrySet()) {
            Cell cell = row.getCell(entry.getValue());
            String value = "";
            switch (entry.getKey()) {
                case "Title Comment":
                    value = entity.getTitleComment();
                    break;
                case "Description Comment":
                    value = entity.getDescriptionComment();
                    break;
                case "Criteria":
                    value = entity.getCriteria();
                    break;
                default:
                    break;
            }
            cell.setCellValue(new XSSFRichTextString(value));
        }
    }

    private XSSFCellStyle buildStyle(XSSFCellStyle style) {
        XSSFColor color = new XSSFColor();
        style.setBorderColor(BorderSide.LEFT, color);
        style.setBorderColor(BorderSide.RIGHT, color);
        style.setBorderColor(BorderSide.BOTTOM, color);
        style.setBorderColor(BorderSide.TOP, color);
        return style;
    }

    private void createMissingCols(Row headerRow, Map<String, Integer> alreadyExist, XSSFCellStyle style) {
        short size = headerRow.getLastCellNum();
        int addedCols = size + 1;
        for (Entry<String, Integer> entry : requiredCols.entrySet()) {
            Cell cell = headerRow.createCell(addedCols);
            cell.setCellValue(new XSSFRichTextString(entry.getKey()));
            style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            cell.setCellStyle(style);
            alreadyExist.put(entry.getKey(), addedCols);
            requiredCols.put(entry.getKey(), addedCols);
            addedCols++;
        }
    }

    private void createMissingCols(Row row, XSSFCellStyle style) {
        for (Entry<String, Integer> entry : requiredCols.entrySet()) {
            if (entry.getValue() == null || entry.getValue().equals(-1)) {
                continue;
            }
            Cell cell = row.createCell(entry.getValue());
            cell.setCellStyle(style);
        }
    }

    private void figureRequiredCols(Row headerRow) {
        Iterator<Cell> cells = headerRow.cellIterator();
        while (cells.hasNext()) {
            Cell cell = cells.next();
            if (cell == null || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                continue;
            }
            String cellValue = cell.getRichStringCellValue().getString();
            if (cellValue != null && requiredCols.containsKey(cellValue)) {
                alreadyExist.put(cellValue, cell.getColumnIndex());
                requiredCols.remove(cellValue);
            }
            if (cellValue != null && originalCols.containsKey(cellValue)) {
                originalCols.put(cellValue, cell.getColumnIndex());
            }
        }
    }
}
