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
import com.erricson.tttool.model.entities.ExcelRecord;
import com.erricson.tttool.model.handlers.ExcelEntityProcessor;
import com.erricson.tttool.view.api.View;

public class ControllerImp implements Controller {
    private static final Logger LOG = Logger.getLogger(ControllerImp.class);

    static {
        BasicConfigurator.configure();
    }

    @Override
    public void start(final View view, final boolean overwriteOriginal, final boolean writeOutside) {
        LOG.info("start processing the excel sheet");
        final String criteriaPath = view.getCriteriaPath();
        final Optional<Map<String, Criteria>> criteriaResult = ExcelCriteriaReader.readCriteria(criteriaPath);
        if (!criteriaResult.isPresent()) {
            view.showErrorMessage(NotificationMessage.FAILED_CRITERIA);
            return;
        }

        final Map<String, Criteria> allCriteria = criteriaResult.get();
        if (overwriteOriginal) {
            OverwriteOriginal(view, allCriteria);
        } else if (writeOutside) {
            WriteOutside(view, allCriteria);
        }
    }

    private void OverwriteOriginal(final View view, final Map<String, Criteria> allCriteria) {
        ExcelOriginalWriter writer = new ExcelOriginalWriter();
        final String sourceFile = view.getSourcePath();
        final boolean writeResult = writer.write(sourceFile, allCriteria);
        showMessage(view, writeResult);
    }

    private void WriteOutside(View view, Map<String, Criteria> allCriteria) {
        final String sourcePath = view.getSourcePath();
        final Optional<List<ExcelRecord>> entitiesResult = ExcelReader.readFromExcelFile(sourcePath);
        if (!entitiesResult.isPresent()) {
            view.showErrorMessage(NotificationMessage.FAILED_SOURCE);
            return;
        }

        final List<ExcelRecord> entities = entitiesResult.get();
        LOG.info("finished getting all the entities");
        LOG.info("finished loading all the criteria keys and comments");
        entities.forEach(excelRecord -> ExcelEntityProcessor.processExcelEntity(excelRecord, allCriteria));
        final String destinationPath = view.getDestinationPath();
        final boolean writeResult = new ExcelWriter().write(destinationPath, entities);
        LOG.info("finished writing to the destination folder");
        showMessage(view, writeResult);
    }

    private void showMessage(final View view, final boolean writeResult) {
        if (writeResult) {
            view.showMessage(NotificationMessage.SUCCESS.getMessage());
            LOG.info("finished successfully");
        } else {
            view.showErrorMessage(NotificationMessage.FAILED_OUTPUT);
        }
    }
}
