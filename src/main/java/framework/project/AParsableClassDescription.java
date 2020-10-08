package framework.project;

import grader.basics.project.BasicClassDescription;

import java.io.File;
import java.io.IOException;

//import com.github.antlrjavaparser.JavaParser;
//import com.github.antlrjavaparser.api.CompilationUnit;


public class AParsableClassDescription  extends BasicClassDescription implements ParsableClassDescription{
//    private CompilationUnit compilationUnit;

	public AParsableClassDescription(Class<?> javaClass, File source) {
		super(javaClass, source);
		// TODO Auto-generated constructor stub
	}
//  @Override
//  public CompilationUnit parse() throws IOException {
//      if (compilationUnit == null)
//          compilationUnit = JavaParser.parse(source);
//      return compilationUnit;
//  }

}
