package grader.spreadsheet.display;

import bus.uigen.ObjectEditor;

public class SpreadsheetDisplayerFactory {
	static SpreadsheetDisplay spreadsheetDisplayer;
	public static SpreadsheetDisplay getOrCreateSpreadsheetDisplayer() {
		if (spreadsheetDisplayer == null) {
			spreadsheetDisplayer = new ASpreadsheetDisplay();
		}
		return spreadsheetDisplayer;
	}
	public static void displaySpreadsheet() {
    	SpreadsheetDisplay aSpreadsheetDisplayer = SpreadsheetDisplayerFactory.getOrCreateSpreadsheetDisplayer();
    	ObjectEditor.tableEdit(aSpreadsheetDisplayer);

	}
}
