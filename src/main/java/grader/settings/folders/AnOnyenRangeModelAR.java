package grader.settings.folders;

import java.awt.Color;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class AnOnyenRangeModelAR implements ExecutableCommand{
	public Object execute(Object theFrame) {
		ObjectEditor.setPropertyAttribute(AnOnyenRangeModel.class, "displayedStartingOnyen",  AttributeNames.STRETCHABLE_BY_PARENT, true);
		ObjectEditor.setPropertyAttribute(AnOnyenRangeModel.class, "displayedEndingOnyen",  AttributeNames.STRETCHABLE_BY_PARENT, true);
		ObjectEditor.setPropertyAttribute(AnOnyenRangeModel.class, "onyenList",  AttributeNames.STRETCHABLE_BY_PARENT, true);
		ObjectEditor.setPropertyAttribute(AnOnyenRangeModel.class, "*",  AttributeNames.CONTAINER_BACKGROUND, Color.PINK);
		ObjectEditor.setAttribute(AnOnyenRangeModel.class,  AttributeNames.LAYOUT, AttributeNames.GRID_BAG_LAYOUT);
		ObjectEditor.setAttribute(AnOnyenRangeModel.class,  AttributeNames.STRETCHABLE_BY_PARENT, true);
		return null;
	}

}
