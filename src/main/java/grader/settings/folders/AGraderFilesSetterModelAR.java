package grader.settings.folders;

import java.awt.Color;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class AGraderFilesSetterModelAR implements ExecutableCommand{
	public Object execute(Object theFrame) {


		ObjectEditor.setAttribute(AGraderFilesSetterModel.class,  AttributeNames.STRETCHABLE_BY_PARENT, true);
		ObjectEditor.setAttribute(AGraderFilesSetterModel.class,  AttributeNames.LAYOUT, AttributeNames.GRID_BAG_LAYOUT);

		ObjectEditor.setPropertyAttribute(AGraderFilesSetterModel.class, "DownloadFolder", AttributeNames.LABEL_POSITION, AttributeNames.LABEL_IS_LEFT);
		ObjectEditor.setPropertyAttribute(AGraderFilesSetterModel.class, "DownloadFolder", AttributeNames.CONTAINER_BACKGROUND, Color.PINK);

		ObjectEditor.setPropertyAttribute(AGraderFilesSetterModel.class, "TextEditor", AttributeNames.LABEL_POSITION, AttributeNames.LABEL_IS_LEFT);
		ObjectEditor.setPropertyAttribute(AGraderFilesSetterModel.class, "Diff", AttributeNames.LABEL_POSITION, AttributeNames.LABEL_IS_LEFT);

		ObjectEditor.setPropertyAttribute(AGraderFilesSetterModel.class, "TestProjectSrc", AttributeNames.LABEL_POSITION, AttributeNames.LABEL_IS_LEFT);

		return null;
	}
}
