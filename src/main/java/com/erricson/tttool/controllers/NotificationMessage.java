package com.erricson.tttool.controllers;

public enum NotificationMessage {
    FAILED_SOURCE("Failed to read from the source file",
            "This problem could be caused by:\n\tno file provided\n\tinvalid file extension\n\tinvalid file formate\n\tfile is opened by other application"),
    FAILED_CRITERIA(
            "Failed to read from the criteria file",
            "This problem could be cause by:\n\tinvalid file extension\n\tinvalid file formate\n\tfile doesn't have the required columns\n\tfile is opened by other application"),
    FAILED_OUTPUT(
            "Failed to write in the output file",
            "This problem could be cause by:\n\tinvalid file extension"), SUCCESS("Finished Successfully",
            ""),
    FAILED_OVERWRITE("Failed to overwrite to source file",
            "This problem could be cause by:\n\tsource file is opened by another application\n\tsource file has corrupted formate\n\tfile is opened by other application");

    private String message;
    private String commonErrors;

    NotificationMessage(String message, String commonErrors) {
        this.message = message;
        this.commonErrors = commonErrors;
    }

    @Override
    public String toString() {
        return message;
    }

    public String getMessage() {
        return message;
    }

    public String getCommonErrors() {
        return commonErrors;
    }

}
