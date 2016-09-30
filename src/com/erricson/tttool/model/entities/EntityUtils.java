package com.erricson.tttool.model.entities;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Row;

public final class EntityUtils {
	// -------------------||
	// class variables ---||
	// -------------------||
	private static final String TITLE = "Title";
	private static final String DESCRIPTION = "Description";
	private static final String NUMBER = "Number";
	private static final String ACI = "Affected CI";

	private EntityUtils() {
	}

	public static ExcelEntity getEntityFromRow(Map<String, Integer> requiredCols, Row row) {
		ExcelEntity entity = new ExcelEntity();
		for (Entry<String, Integer> entry : requiredCols.entrySet()) {
			if (entry.getValue() == null || entry.getValue().equals(-1)) {
				continue;
			}
			String columnValue = row.getCell(entry.getValue()).getRichStringCellValue().getString();
			switch (entry.getKey()) {
			case TITLE:
				entity.setTitle(columnValue);
				break;
			case DESCRIPTION:
				entity.setDescription(columnValue);
				break;
			case NUMBER:
				entity.setNumber(columnValue);
				break;
			case ACI:
				entity.setACI(columnValue);
				break;
			default:
				break;
			}
		}
		return entity;
	}

}
