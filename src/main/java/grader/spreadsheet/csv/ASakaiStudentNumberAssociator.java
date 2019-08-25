package grader.spreadsheet.csv;

import grader.basics.file.FileProxy;
import grader.basics.file.filesystem.AFileSystemFileProxy;
import grader.sakai.project.SakaiProjectDatabase;
import grader.spreadsheet.FinalGradeRecorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.misc.Common;
import util.trace.Tracer;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
//import bus.uigen.Message;
// This is the class that manages the shifts.csv file. 
public class ASakaiStudentNumberAssociator implements SakaiStudentNumberAssociator {
	public static final int ONYEN_COLUMN = 0;
	public static final int ONYEN_COLUMN_2 = 1;
	public static final int LAST_NAME_COLUMN = 2;
	public static final int FIRT_NAME_COLUMN = 3;
	public static final int NUMBER_COLUMN = 4;
	public static final int ROW_SIZE = NUMBER_COLUMN + 1;
	public static final int TITLE_ROW = 2;
	public static final int FIRST_STUDENT_ROW = TITLE_ROW + 1;
	 public static final String DEFAULT_CHAR = "";
	 public static final double  DEFAULT_VALUE = 0.0;

//	InputStream input; // this may have to be reinitialized each time
//	OutputStream output; // may have to reinitialized and closed each time
	File numberSpreadsheet;
	
	List<String[]>  table;
  
	int originalTableSize;
	
	public ASakaiStudentNumberAssociator(File aNumberSpreadsheet) {
		numberSpreadsheet = aNumberSpreadsheet	;	
	}
	public ASakaiStudentNumberAssociator(String aFileName) {
		numberSpreadsheet = new File(aFileName);	
		
	}
	
	public List<String[]> getTable() {
		return table;
	}
	public int size() {
		return table.size() - FIRST_STUDENT_ROW;
	}
	protected int toActualRow(int aRowNum) {
		return aRowNum + FIRST_STUDENT_ROW;
	}
	public String getOnyen(int aRowIndex) {
		return table.get(toActualRow(aRowIndex))[ONYEN_COLUMN];		
	}
	public String getFirstName(int aRowIndex) {
		return table.get(toActualRow(aRowIndex))[FIRT_NAME_COLUMN];
	}
	public String getLastName(int aRowIndex) {
		return table.get(toActualRow(aRowIndex))[LAST_NAME_COLUMN];
	}
	public String getFullName(int aRowIndex) {
		return getFirstName(aRowIndex) + " " + getLastName(aRowIndex);
	}
	public double getNumber (int aRowIndex) {
		int anActualRow = toActualRow(aRowIndex);
		if (anActualRow >= table.size())
			return 0;
		return getDouble(table.get(anActualRow), NUMBER_COLUMN);

	}
	public String[] getStudentRow(int aRowIndex) {
		return table.get(toActualRow(aRowIndex));
	}
	
	protected void maybeCreateTable() {
		if (table != null)
			return;
		createTable();
		
	}
   protected String[] createNewRow(String anOnyen, String aStudentName) {
		String aLastName = DEFAULT_CHAR;
		String aFirstName = DEFAULT_CHAR;
		String aNumber = DEFAULT_CHAR;
		
		if (aStudentName != null) {
			String[] aNames = aStudentName.split(" ");
			if (aNames.length != 0)
				aLastName = aNames[0];
			if (aNames.length != 1)
				aFirstName = aNames[1];
		}
		String[] aNewRow = new String[ROW_SIZE];
		aNewRow[ONYEN_COLUMN] = anOnyen;
		aNewRow[ONYEN_COLUMN_2] = anOnyen;
		aNewRow[LAST_NAME_COLUMN] = aLastName;
		aNewRow[FIRT_NAME_COLUMN] = aFirstName;
		aNewRow[NUMBER_COLUMN] = aNumber;
		table.add(aNewRow);
		return aNewRow;
	}
	
	public void createTable() {
		
		try {
			if ( !numberSpreadsheet.exists()) { // file has not been created so far
				table = new ArrayList();
				String[] aGradebookItem = {"Gradebook Item", "Points", "", "", ""};
				String[] aBlankRow = {"", "", "", "", ""};
				//Display ID,ID,Last Name,First Name,grade
				String[] aTitleRow = {"Display ID", "ID", "Last Name", "First Name", "grade"};
				table.add(aGradebookItem);
				table.add(aBlankRow);
				table.add(aTitleRow);
				return;
			}
			InputStream input = new FileInputStream(numberSpreadsheet);
					
			CSVReader csvReader 	=	new CSVReader(new InputStreamReader(input));
		     table = csvReader.readAll();
		     System.out.println ("Read spreadsheet table of size:" + table.size());
		     originalTableSize = table.size();
		     
			csvReader.close();
			input.close();
			
		   
	    
	    
		} catch (Exception e) {
			e.printStackTrace();
		
			
		}
		
	}
	
	public String[] getRow(String anOnyen) {
		maybeCreateTable();
		return getRow(table, anOnyen);
	}
	
	public String[] getRow(List<String[]> aSheet, String anOnyen) {
		 for (int rowNum = FIRST_STUDENT_ROW; rowNum < aSheet.size(); rowNum ++) {
			 String[] aRow = aSheet.get(rowNum);
			 if (aRow[ONYEN_COLUMN].equals(anOnyen))
				 return aRow;
		 }
		 return null;
	}
	
	public String[] getStudentRow(List<String[]> aSheet, String anOnyen, String aStudentName) {
//		 for (int rowNum = 0; rowNum < aSheet.size(); rowNum ++) {
//			 String[] aRow = aSheet.get(rowNum);
//			 if (aRow[ONYEN_COLUMN].equals(anOnyen))
//				 return aRow;
//		 }
		 String[] retVal = getRow(aSheet, anOnyen);
		 return retVal;
//		 if (retVal != null) {
//			 return retVal;
//		 }
//		
//		System.err.println("Cannot find row for:" + aStudentName + " " + anOnyen);
//		System.err.println("Creating row for:" + aStudentName + " " + anOnyen);
//		return createNewRow(anOnyen, aStudentName);
//		 return null;
		
	}
	
	public void clearStudentRow(List<String[]> aSheet, String aStudentName, String anOnyen) {
		 String[] aRow = getStudentRow(aSheet, anOnyen, aStudentName);
		 if (aRow != null) {
			 for (int aColumn = 0; aColumn < aRow.length; aColumn++) {
//				 if (aColumn == ONYEN_COLUMN) continue;
				 if (aColumn < NUMBER_COLUMN) continue;

				 aRow[aColumn] = DEFAULT_CHAR;
			 }
		 }
		
	}
	
	public void recordNumber (String[] aRow, double aNumber) {
		recordNumber(aRow, NUMBER_COLUMN, aNumber);
//		writeTable();

	}
	
	public void recordName(String[] aRow, String aFullName) {
		String[] aNames = aFullName.split(" ");
		String aFirstName = aNames[0];
		String aLastName = aNames[1];
		aRow[FIRT_NAME_COLUMN] = aFirstName;
		aRow[LAST_NAME_COLUMN] = aLastName;
	}
	
	public void recordNumber (String[] aRow, int aColumn, double aNumber) {
		
		String aGradeCell = aRow[aColumn];
		if (aColumn >= aRow.length) {
			System.err.println("No column:" + aColumn + " in row:" + 
					Arrays.toString(aRow));
		}
//		
		aRow[aColumn] = Double.toString(aNumber);
		
		
	}
	
	public void recordResult (String[] aRow, int aColumn, String aResult) {
		String aGradeCell = aRow[aColumn];
		aRow[aColumn] = aResult;
		
	}
	
	public double getDouble (String[] aRow, int aColumn) {
		try {
		String aGradeCell = aRow[aColumn];
		if (aGradeCell.equals(DEFAULT_CHAR))
			return DEFAULT_VALUE; 
		
		return Double.parseDouble(aGradeCell);
		} catch (Exception e) {
//			e.printStackTrace();
			return DEFAULT_VALUE;
		}
	}
	
	public String getResult (String[] aRow, int aColumn) {
		try {
		return aRow[aColumn];
		
		} catch (Exception e) {
//			e.printStackTrace();
			return "";
		}
	}
	
	
	public double getNumber (String[] aRow) {
		return getDouble(aRow, NUMBER_COLUMN);

	}
	
	public double getNumber(String anOnyen) {
		return getNumber (anOnyen, null);
	}

	
	public double getNumber(String anOnyen, String aStudentName) {
		if (!numberSpreadsheet.exists()) { //no one has asked for a Number so far
			return 0;
		}
		try {
//		InputStream input = gradeSpreadsheet.getInputStream();
//		CSVReader csvReader 	=	new CSVReader(new InputStreamReader(input));
//		List<String[]>  table = csvReader.readAll();
//		csvReader.close();
			maybeCreateTable();
		
	   
    String[] row = getStudentRow(table, anOnyen, aStudentName);
    if (row == null) {
		System.out.println("Cannot find row for:" + anOnyen);
		return 0;
    }
   double retVal =  getNumber(row);

//
//    input.close();
    return retVal;
    
	} catch (Exception e) {
		e.printStackTrace();
		return -1;
		
	}
	
		
	}
	protected void checkSizes() {
		if (table.size() != originalTableSize) {
			System.err.println ("Spreadsheet table size:" + table.size() + " != " + originalTableSize); // should we delete extra rows?
		}
	}
	void writeTable() {
		try {
		if (!numberSpreadsheet.exists()) {
			numberSpreadsheet.createNewFile();
		}
		OutputStream output = new FileOutputStream(numberSpreadsheet);
		if (output == null) {
			System.out.println("Cannot write Number as null output stream");
			return;
		}
		CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(output));
//		checkSizes();
//		if (table.size() != originalTableSize) {
//			System.err.println ("Spreadsheet table size:" + table.size() + " != " + originalTableSize); // should we delete extra rows?
//		}
		csvWriter.writeAll(table);
		
			csvWriter.close();
			output.close();
			removeQuotesAndTrim();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	}
	
	public void setNumber( String anOnyen,double aNumber) {
		setNumber(anOnyen, "", aNumber);
	}
	@Override
	public void setNumber(String anOnyen, String aStudentName, double aNumber) {
		try {
			if (aNumber < 0) {
				Tracer.error("negative Number!");
//				JOptionPane.showMessageDialog(null, "Negative score! Not saving it.");
				return;
				
			}
//			InputStream input = gradeSpreadsheet.getInputStream();
//			CSVReader csvReader 	=	new CSVReader(new InputStreamReader(input));
//			List<String[]>  table = csvReader.readAll();
//			csvReader.close();
			maybeCreateTable();
			
		    String[] row = getStudentRow(table, anOnyen, aStudentName);
		    if (row == null) {
				System.out.println("Cannot find row for:" + aStudentName + " " + anOnyen);
				;
				System.out.println("Creating row for:" + aStudentName + " " + anOnyen);
				row = createNewRow(anOnyen, aStudentName);
		    }
//		    recordName(row, aStudentName);
		    recordNumber(row, aNumber);
		    writeTable();

//		OutputStream output = gradeSpreadsheet.getOutputStream();
//		if (output == null) {
//			System.out.println("Cannot write grade as null output stream");
//			return;
//		}
//		CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(output));
//		csvWriter.writeAll(table);
//		csvWriter.close();
	    
    

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
	
	void removeQuotesAndTrim() {
//		String aFileName = gradeSpreadsheet.getAbsoluteName();
		String aFileName = numberSpreadsheet.getAbsolutePath();
		StringBuffer aText = Common.toText(aFileName);
		String aNewText = aText.toString().replaceAll("\"", "");
//		 aNewText = aNewText.toString().replaceAll("\\n\\n", "\\n");
		 aNewText = aNewText.trim(); //an extra line is added when graded
		try {
			Common.writeText(aFileName, aNewText);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	public String getFileName() {
		return numberSpreadsheet.getName();
	}
	

}
