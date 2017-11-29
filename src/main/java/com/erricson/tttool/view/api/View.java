package com.erricson.tttool.view.api;

import com.erricson.tttool.controllers.NotificationMessage;

public interface View {
    String getSourcePath();
    String getCriteriaPath();
    String getDestinationPath();
    void showMessage(String message);
    void showErrorMessage(NotificationMessage message);
    void setProgress(Double progress);
}
