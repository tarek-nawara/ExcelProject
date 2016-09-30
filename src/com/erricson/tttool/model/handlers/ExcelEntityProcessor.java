package com.erricson.tttool.model.handlers;

import java.util.Map;
import java.util.Map.Entry;

import com.erricson.tttool.model.entities.Criteria;
import com.erricson.tttool.model.entities.ExcelEntity;

public final class ExcelEntityProcessor {
	// ------------------||
	// class variables --||
	// ------------------||
	private static final String TITLE = "Title KeyWord";
	private static final String DESCRIPTION = "Description Keyword";
	private static final String CRITERIA = "Comment";

	/*
	 * Private constructor to prevent instantiation
	 */
	private ExcelEntityProcessor() {
	}

	public static void processExcelEntity(ExcelEntity entity, Map<String, Criteria> allCriteria) {
		processTitleColumn(entity, allCriteria.get(TITLE));
		processDescriptionColumn(entity, allCriteria.get(DESCRIPTION));
		processCriteriaColumn(entity, allCriteria.get(CRITERIA));
	}

	private static void processTitleColumn(ExcelEntity entity, Criteria criteria) {
		Map<String, String> keysAndComments = criteria.keysAndComments;
		if (entity == null || entity.getTitle() == null) {
			return;
		}
		String title = entity.getTitle();
		for (Entry<String, String> entry : keysAndComments.entrySet()) {
			if (title.toLowerCase().contains(entry.getKey().toLowerCase())) {
				entity.setTitleComment(entry.getValue());
				break;
			}
		}
	}

	private static void processDescriptionColumn(ExcelEntity entity, Criteria criteria) {
		Map<String, String> keysAndComments = criteria.keysAndComments;
		if (entity == null || entity.getDescription() == null) {
			return;
		}
		String description = entity.getDescription();
		for (Entry<String, String> entry : keysAndComments.entrySet()) {
			if (description.toLowerCase().contains(entry.getKey().toLowerCase())) {
				entity.setDescriptionComment(entry.getValue());
				break;
			}
		}
	}

	private static void processCriteriaColumn(ExcelEntity entity, Criteria criteria) {
		ExcelEntity otherEntity = new ExcelEntity();
		otherEntity.setTitleComment(entity.getTitleComment());
		otherEntity.setDescriptionComment(entity.getDescriptionComment());
		if (isExist(otherEntity.getDescriptionComment()) && !isExist(otherEntity.getTitleComment())) {
			otherEntity.setTitleComment(otherEntity.getDescriptionComment());
		} else if (isExist(otherEntity.getTitleComment()) && !isExist(otherEntity.getDescriptionComment())) {
			otherEntity.setDescriptionComment(otherEntity.getTitleComment());
		}
		if (otherEntity.getDescriptionComment() != null && otherEntity.getTitleComment() != null
				&& otherEntity.getDescriptionComment().equalsIgnoreCase(otherEntity.getTitleComment())) {
			Map<String, String> keysAndComments = criteria.keysAndComments;
			String titleComment = otherEntity.getTitleComment();
			for (Entry<String, String> entry : keysAndComments.entrySet()) {
				if (titleComment.toLowerCase().contains(entry.getKey().toLowerCase())) {
					otherEntity.setCriteria(entry.getValue());
					break;
				}
			}
		}
	}

	private static boolean isExist(String s) {
		return (s != null && !s.isEmpty());
	}
}
