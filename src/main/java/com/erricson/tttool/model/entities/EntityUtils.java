package com.erricson.tttool.model.entities;

import java.util.Map;

import org.apache.poi.ss.usermodel.Row;

public final class EntityUtils {
    private static final String TITLE = "Title";
    private static final String DESCRIPTION = "Description";
    private static final String NUMBER = "Number";
    private static final String ACI = "Affected CI";

    private EntityUtils() {
    }

    public static ExcelRecord getEntityFromRow(final Map<String, Integer> requiredCols, final Row row) {
        final ExcelRecord.ExcelRecordBuilder excelRecordBuilder = ExcelRecord.anExcelRecord();
        requiredCols.entrySet().stream()
                .filter(EntityUtils::isValidRecord)
                .map(entry -> Map.entry(entry.getKey(), getColumnValue(row, entry)))
                .forEach(entry -> injectValue(excelRecordBuilder, entry));

        return excelRecordBuilder.build();
    }

    private static boolean isValidRecord(Map.Entry<String, Integer> entry) {
        return !(entry.getValue() == null || entry.getValue().equals(-1));
    }

    private static String getColumnValue(final Row row, final Map.Entry<String, Integer> entry) {
        return row.getCell(entry.getValue()).getRichStringCellValue().getString();
    }

    private static void injectValue(final ExcelRecord.ExcelRecordBuilder excelRecordBuilder,
                                    final Map.Entry<String, String> entry) {
        switch (entry.getKey()) {
            case TITLE:
                excelRecordBuilder.title(entry.getValue());
                break;
            case DESCRIPTION:
                excelRecordBuilder.description(entry.getValue());
                break;
            case NUMBER:
                excelRecordBuilder.number(entry.getValue());
                break;
            case ACI:
                excelRecordBuilder.aci(entry.getValue());
                break;
            default:
                break;
        }
    }

}
