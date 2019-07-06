package grader.spreadsheet.display;

import java.util.jar.Attributes;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;

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
    	ObjectEditor.setAttribute(ASpreadsheetItemDisplay.class, AttributeNames.LABELLED, true);
    	ObjectEditor.tableEdit(aSpreadsheetDisplayer);

	}
}
