package grader.steppers;

import java.util.ArrayList;
import java.util.List;

import util.annotations.Row;
import util.models.ADynamicEnum;
import bus.uigen.ObjectEditor;

public class AnArgsHolder implements ArgsHolder {
	String args = "";
	List<String> history = new ArrayList<>();
	@Row(3)
	public void useAsArg(String anArg) {
		setArgs(anArg);
	}
	@Row(0)
	@Override
	public String getArgs() {
		return args;
	}

	@Override
	public void setArgs(String newVal) {
		args = newVal;
		if (!history.contains(newVal)) {
			history.add(newVal);
		}		
	}
	@Row(1)
	public List<String> getArgsHistory() {
		return history;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main (String[] args) {
//		ObjectEditor.edit(new AnArgsHolder());
		ObjectEditor.edit (new ADynamicEnum());
	}
}
