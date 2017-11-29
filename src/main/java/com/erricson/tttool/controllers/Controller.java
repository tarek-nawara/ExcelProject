package com.erricson.tttool.controllers;

import com.erricson.tttool.view.api.View;

public interface Controller {

    /**
     * The controller is start method, this method will start processing the
     * excel files and provide the output file
     *
     * @param view view which will invoke the controller
     */
    void start(View view, boolean overwriteOriginal, boolean writeOutSide);
}
