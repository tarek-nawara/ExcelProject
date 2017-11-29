package com.erricson.tttool.model.handlers;

import com.erricson.tttool.model.entities.Criteria;
import com.erricson.tttool.model.entities.ExcelRecord;

import java.util.Map;

public final class ExcelEntityProcessor {
	private static final String TITLE = "Title KeyWord";
	private static final String DESCRIPTION = "Description Keyword";
	private static final String CRITERIA = "Comment";


	private ExcelEntityProcessor() {
	}

	public static void processExcelEntity(final ExcelRecord excelRecord,
                                          final Map<String, Criteria> allCriteria) {
		processTitleColumn(excelRecord, allCriteria.get(TITLE));
		processDescriptionColumn(excelRecord, allCriteria.get(DESCRIPTION));
		processCriteriaColumn(excelRecord, allCriteria.get(CRITERIA));
	}

	private static void processTitleColumn(final ExcelRecord excelRecord,
                                           final Criteria criteria) {
		if (excelRecord == null || excelRecord.getTitle() == null) {
			return;
		}
		excelRecord.setTitleComment(criteria.findMatchingValue(excelRecord.getTitle()));
	}

	private static void processDescriptionColumn(final ExcelRecord excelRecord,
                                                 final Criteria criteria) {
		if (excelRecord == null || excelRecord.getDescription() == null) {
			return;
		}
		excelRecord.setDescriptionComment(criteria.findMatchingValue(excelRecord.getDescription()));
	}

	private static void processCriteriaColumn(final ExcelRecord excelRecord, final Criteria criteria) {
		if (isExist(excelRecord.getDescriptionComment()) && !isExist(excelRecord.getTitleComment())) {
			excelRecord.setTitleComment(excelRecord.getDescriptionComment());
		} else if (isExist(excelRecord.getTitleComment()) && !isExist(excelRecord.getDescriptionComment())) {
			excelRecord.setDescriptionComment(excelRecord.getTitleComment());
		} else {
			excelRecord.setTitleComment(excelRecord.getTitleComment());
			excelRecord.setDescriptionComment(excelRecord.getDescriptionComment());
		}

		if (excelRecord.getDescriptionComment() != null && excelRecord.getTitleComment() != null
				&& excelRecord.getDescriptionComment().equalsIgnoreCase(excelRecord.getTitleComment())) {
			excelRecord.setCriteria(criteria.findMatchingValue(excelRecord.getTitleComment()));

		}
	}

	private static boolean isExist(String s) {
		return (s != null && !s.isEmpty());
	}
}
