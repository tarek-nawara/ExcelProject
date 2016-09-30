package com.erricson.tttool.view.impl;

import com.erricson.tttool.controllers.Controller;
import com.erricson.tttool.controllers.ControllerImp;
import com.erricson.tttool.controllers.NotificationMessage;
import com.erricson.tttool.view.api.View;

public class TestView implements View {
	public static void main(String[] args) {
		Controller controller = new ControllerImp();
		controller.start(new TestView(), false, true);
	}

	@Override
	public String getSourcePath() {
		return "C:\\Users\\Tarek Nawara\\Desktop\\TT Tool\\TX TT August.xlsx";
	}

	@Override
	public String getCriteriaPath() {
		return "C:\\Users\\Tarek Nawara\\Desktop\\TT Tool\\New TT Tool Key Words.xlsx";
	}

	@Override
	public String getDestinationPath() {
		return "C:\\Users\\Tarek Nawara\\Desktop\\output2.xlsb";
	}

	@Override
	public void showMessage(String message) {
		System.out.println(message);
	}

	@Override
	public void showErrorMessage(NotificationMessage message) {
		System.out.println(message);
	}

	@Override
	public void setProgress(Double progress) {		
	}
}
