package com.erricson.tttool.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.erricson.tttool.model.IO.ExcelCriteriaReader;
import com.erricson.tttool.model.IO.ExcelOriginalWriter;
import com.erricson.tttool.model.IO.ExcelReader;
import com.erricson.tttool.model.IO.ExcelWriter;
import com.erricson.tttool.model.entities.Criteria;
import com.erricson.tttool.model.entities.ExcelEntity;
import com.erricson.tttool.model.handlers.ExcelEntityProcessor;
import com.erricson.tttool.view.api.View;

public class ControllerImp implements Controller {
	// ------------------||
	// class variables --||
	// ------------------||
	private static final Logger LOG = Logger.getLogger(ControllerImp.class);
	static {
		 BasicConfigurator.configure();
	}

	@Override
	public void start(View view, boolean overwriteOriginal, boolean writeOutside) {
		LOG.info("start processing the excel sheet");
		String criteriaPath = view.getCriteriaPath();
		Optional<Map<String, Criteria>> criteriaResult = ExcelCriteriaReader.readCriterias(criteriaPath);
		if (!criteriaResult.isPresent()) {
			view.showErrorMessage(NotificationMessage.FAILED_CRITERIA);
			return;
		}
		Map<String, Criteria> allCriteria = criteriaResult.get();
		if (overwriteOriginal) {
			ExcelOriginalWriter writer = new ExcelOriginalWriter();
			String sourceFile = view.getSourcePath()
			boolean success = writer.write(sourceFile, allCriteria);
			if (!success) {
				view.showErrorMessage(NotificationMessage.FAILED_OVERWRITE);
				return;
			}
		}
		if (writeOutside) {
			String sourcePath = view.getSourcePath();
			Optional<List<ExcelEntity>> entitiesResult = ExcelReader.readFromExcelFile(sourcePath);
			if (!entitiesResult.isPresent()) {
				view.showErrorMessage(NotificationMessage.FAILED_SOURCE);
				return;
			}
			List<ExcelEntity> entities = entitiesResult.get();
			LOG.info("finished getting all the entities");
			LOG.info("finished loading all the criteria keys and comments");
			entities.forEach(entity -> ExcelEntityProcessor.processExcelEntity(entity, allCriteria));
			String destinationPath = view.getDestinationPath();
			boolean success = ExcelWriter.write(destinationPath, entities);
			LOG.info("finished writing to the destination folder");
			if (!success) {
				view.showErrorMessage(NotificationMessage.FAILED_OUTPUT);
				return;
			}
		}
		view.showMessage(NotificationMessage.SUCCESS.getMessage());
		LOG.info("finished successfully");
		return;
	}
}
