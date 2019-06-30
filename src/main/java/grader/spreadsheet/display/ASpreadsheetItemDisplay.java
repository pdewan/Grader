package grader.spreadsheet.display;

import util.annotations.EditablePropertyNames;
import util.annotations.Position;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;

@StructurePattern(StructurePatternNames.BEAN_PATTERN)
@EditablePropertyNames({})
public class ASpreadsheetItemDisplay implements SpreadsheetItemDisplay {
	protected String id;
	protected String name;
	protected String grade;
	public ASpreadsheetItemDisplay(String anID, String aName) {
		id = anID;
		name = aName;		
	}
	@Override
	@Position(2)
	public String getGrade() {
		return grade;
	}
	@Override
	@Visible(false)
	public void setGrade(String grade) {
		this.grade = grade;
	}
	@Override
	@Position(0)
	public String getId() {
		return id;
	}
	@Override
	@Position(1)
	public String getName() {
		return name;
	}


}
