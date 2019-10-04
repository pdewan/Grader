package grader.settings.folders;

import javax.swing.JFrame;

public interface GraderFilesSetterModel {
	public FileSetterModel getDownloadFolder() ;
	public FileSetterModel getTextEditor() ;
	public FileSetterModel getDiff() ;
	
	public void initFrame(JFrame aFrame) ;
	FileSetterModel getTestProjectSrc();
}
