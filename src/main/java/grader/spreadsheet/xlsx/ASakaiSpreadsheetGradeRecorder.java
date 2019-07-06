package grader.spreadsheet.xlsx;

import grader.file.FileProxy;
import grader.spreadsheet.FinalGradeRecorder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ASakaiSpreadsheetGradeRecorder implements FinalGradeRecorder {
	public static final int ONYEN_COLUMN = 0;
	public static final int GRADE_COLUMN = 4;
//	InputStream input; // this may have to be reinitialized each time
//	OutputStream output; // may have to reinitialized and closed each time
	FileProxy gradeSpreadsheet;
  

	public ASakaiSpreadsheetGradeRecorder(FileProxy aGradeSpreadsheet) {
		gradeSpreadsheet = aGradeSpreadsheet;		
	}
	
	public Row getStudentRow(Sheet aSheet, String aStudentName, String anOnyen) {
		 for (int rowNum = 0; rowNum <= aSheet.getLastRowNum(); rowNum ++) {
			 Row aRow = aSheet.getRow(rowNum);
			 if (aRow.getCell(ONYEN_COLUMN).equals(anOnyen))
				 return aRow;
		 }
//		 System.out.println("Could not find gradesheet row for:" + anOnyen);
		 return null;
		
	}
	
	public void recordGrade (Row aRow, double aScore) {
		recordGrade(aRow, GRADE_COLUMN, aScore);
//		Cell aGradeCell = aRow.getCell(GRADE_COLUMN);
//		if (aGradeCell == null) {
//			aGradeCell = aRow.createCell(GRADE_COLUMN);
//		}
//		aGradeCell.setCellValue(aScore);
	}
	
	public void recordGrade (Row aRow, int aColumn, double aScore) {
		Cell aGradeCell = aRow.getCell(aColumn);
		if (aGradeCell == null) {
			aGradeCell = aRow.createCell(aColumn);
		}
		aGradeCell.setCellValue(aScore);
	}
	
	public double getGrade (Row aRow, int aColumn) {
		Cell aGradeCell = aRow.getCell(aColumn);
		if (aGradeCell == null) {
			aGradeCell = aRow.createCell(aColumn);
		}
		return aGradeCell.getNumericCellValue();
	}
	
	public double getGrade (Row aRow) {
		return getGrade(aRow, GRADE_COLUMN);
//		Cell aGradeCell = aRow.getCell(GRADE_COLUMN);
//		if (aGradeCell == null) {
//			aGradeCell = aRow.createCell(GRADE_COLUMN);
//		}
//		aGradeCell.setCellValue(aScore);
	}
	
	
	public double getGrade(String aStudentName, String anOnyen) {
		try {
		InputStream input = gradeSpreadsheet.getInputStream();
	    Workbook wb = WorkbookFactory.create(input);			
	    

    Sheet aSheet = wb.getSheetAt(0);
    Row row = getStudentRow(aSheet, aStudentName, anOnyen);
    if (row == null) {
		System.out.println("Cannot find row for:" + aStudentName + " " + anOnyen);
		return -1;
    }
   double retVal =  getGrade(row);

//
//    Row row = sheet.getRow(2);
//    Cell cell1 = row.getCell(4);
//    if (cell1 == null)
//    	cell1 = row.createCell(5);
//    double doubleCell = cell1.getNumericCellValue();
//    int i = 0;
//    cell1.setCellValue (doubleCell + 2);
//    Cell cell2 = row.getCell(0);
//    if (cell2 != null) {
//    	String stringCell = cell2.getStringCellValue();
//    	System.out.println(stringCell);
//    }
   
    input.close();
    return retVal;
    /*
    if (cell == null)
        cell = row.createCell(3);
    cell.setCellType(Cell.CELL_TYPE_STRING);
    cell.setCellValue("a test");
    */
    /*
    // Write the output to a file
    FileOutputStream fileOut = new FileOutputStream("workbook.xls");
    wb.write(fileOut);
    fileOut.close();
    */
	} catch (Exception e) {
		e.printStackTrace();
		return -1;
		
	}
	
		
	}
	
	

	@Override
	public void setGrade(String aStudentName, String anOnyen, double aScore) {
		try {
		InputStream input = gradeSpreadsheet.getInputStream();
	    Workbook wb = WorkbookFactory.create(input);			
	    OutputStream output;
	
		output = gradeSpreadsheet.getOutputStream();
		if (output == null) {
			System.out.println("Cannot write grade as null output stream");
			return;
		}
		

    Sheet aSheet = wb.getSheetAt(0);
    Row row = getStudentRow(aSheet, aStudentName, anOnyen);
    if (row == null) {
		System.out.println("Cannot find row for:" + aStudentName + " " + anOnyen);
		return;
    }
    recordGrade(row, aScore);

//
//    Row row = sheet.getRow(2);
//    Cell cell1 = row.getCell(4);
//    if (cell1 == null)
//    	cell1 = row.createCell(5);
//    double doubleCell = cell1.getNumericCellValue();
//    int i = 0;
//    cell1.setCellValue (doubleCell + 2);
//    Cell cell2 = row.getCell(0);
//    if (cell2 != null) {
//    	String stringCell = cell2.getStringCellValue();
//    	System.out.println(stringCell);
//    }
    wb.write(output);
   
    output.close();
    input.close();
    /*
    if (cell == null)
        cell = row.createCell(3);
    cell.setCellType(Cell.CELL_TYPE_STRING);
    cell.setCellValue("a test");
    */
    /*
    // Write the output to a file
    FileOutputStream fileOut = new FileOutputStream("workbook.xls");
    wb.write(fileOut);
    fileOut.close();
    */
	} catch (Exception e) {
		e.printStackTrace();
		
	}
		
	}

	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return gradeSpreadsheet.getAbsoluteName();
	}

	@Override
	public FileProxy getGradeSpreadsheet() {
		// TODO Auto-generated method stub
		return gradeSpreadsheet;
	}

	@Override
	public String getFullName(String anOnyen) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public String getFullName(int aRowIndex) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	

}
