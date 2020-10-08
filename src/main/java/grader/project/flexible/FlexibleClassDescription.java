package grader.project.flexible;

import java.io.IOException;

import util.javac.SourceClass;
import bus.uigen.reflect.ClassProxy;
import grader.basics.file.FileProxy;

//import com.github.antlrjavaparser.api.CompilationUnit;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
// gets source and object file for a class
public interface FlexibleClassDescription {
	public ClassProxy getClassProxy() ;
	public void setClassProxy(ClassProxy classProxy) ;
	public StringBuffer getText() ;
	public void setText(StringBuffer text) ;	
	public long getSourceTime() ;
	public void setSourceTime(long newVal);
	public String getClassName();
	public String getPackageName();
	String[] getTags();
	String getStructurePatternName();
	String[] getPropertyNames();
	String[] getEditablePropertyNames();
	JavaClass getQdoxClass();
	JavaSource getQdoxSource();
	SourceClass getJavacSourceClass();
    Class<?> getJavaClass();

//    CompilationUnit getCompilationUnit() throws IOException;
    public FileProxy getSourceFile() ;
	public void setSourceFile(FileProxy sourceFile) ;
	FlexibleProject getProject();
	
	
//	public List<String> getClassNamesThatCouldNotBeCompiled();
//	public void setClassNamesThatCouldNotBeCompiled(List<String> classNamesToCompile) ;
//	public List<String> getClassNamesCompiled();
//	public void setClassNamesCompiled(List<String> classNamesCompiled);

}