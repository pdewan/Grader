package grader.settings.folders;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import util.annotations.Explanation;
import util.annotations.Row;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class AGraderFilesSetterModel implements GraderFilesSetterModel {
	FileSetterModel downloadPathModel = new AFileSetterModel(JFileChooser.DIRECTORIES_ONLY);
	FileSetterModel testProjectModel = new AFileSetterModel(JFileChooser.DIRECTORIES_ONLY);

	FileSetterModel textEditorPathModel = new AFileSetterModel(JFileChooser.DIRECTORIES_ONLY);
	FileSetterModel diffPathModel = new AFileSetterModel(JFileChooser.FILES_ONLY);
	@Row(0)
	@Explanation("The sakai bulk folder of student projects. This value must be specied once per module. Other entries are derived from the problem choice. It is assumed that download folders for diferent problems of a module are in a common folder.")
	public FileSetterModel getDownloadFolder() {
		return downloadPathModel;
	}
	@Row(1)
//	@Visible(false)
	@Explanation("The OS-speific editor for displaying source files.")
	public FileSetterModel getTextEditor() {
		return textEditorPathModel;
	}

	@Row(2)
//	@Visible(false)
	@Explanation("The OS-speific toof for diffing files.")
	public FileSetterModel getDiff() {
		return diffPathModel;
	}
	@Row(3)
//	@Visible(false)
	@Override
	@Explanation("The folder into which a student's source files are downloaded/uploaded.")
	public FileSetterModel getTestProjectSrc() {
		return testProjectModel;
	}
	@Visible(false)
	public void initFrame(JFrame aFrame) {
		downloadPathModel.initFrame(aFrame);
		textEditorPathModel.initFrame(aFrame);
	}
	
	public static void main (String[] args) {
		AGraderFilesSetterModel fileSettersModel = new AGraderFilesSetterModel();
		OEFrame oeFrame = ObjectEditor.edit(fileSettersModel);
		fileSettersModel.initFrame((JFrame) oeFrame.getFrame().getPhysicalComponent());
		oeFrame.setSize(600, 300);
	}

	

}
