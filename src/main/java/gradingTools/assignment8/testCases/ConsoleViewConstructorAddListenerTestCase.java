package gradingTools.assignment8.testCases;

import java.io.IOException;
import java.util.List;

import scala.Option;
import tools.CompilationNavigation;
import tools.classFinder.ManualClassFinder;
import tools.classFinder.RootTagFinder;
//
//import com.github.antlrjavaparser.JavaParser;
//import com.github.antlrjavaparser.api.CompilationUnit;
//import com.github.antlrjavaparser.api.body.ClassOrInterfaceDeclaration;
//import com.github.antlrjavaparser.api.body.ConstructorDeclaration;

import framework.grading.testing.BasicTestCase;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.ClassDescription;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 10/30/13
 * Time: 3:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleViewConstructorAddListenerTestCase extends BasicTestCase {
    public ConsoleViewConstructorAddListenerTestCase() {
        super("Console view add as listener in constructor test case");
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        if (project.getClassesManager().isEmpty())
            throw new NotGradableException();
        Option<ClassDescription> classDescription = new RootTagFinder(project).findClass("ConsoleSceneView");
        if (classDescription.isEmpty()) {
            if (autoGrade)
                throw new NotAutomatableException();
            classDescription = ManualClassFinder.find(project, "ConsoleSceneView");
        }

//        try {
            Class<?> _class = classDescription.get().getJavaClass();
//            CompilationUnit compilation = JavaParser.parse(classDescription.get().getSource());
//            ClassOrInterfaceDeclaration classDef = CompilationNavigation.getClassDef(compilation);
//
//            // Find the constructors and look for .addPropertyChangeListener(this) in any of them
//            List<ConstructorDeclaration> constructors = CompilationNavigation.getConstructors(classDef);
//            for (ConstructorDeclaration constructor : constructors) {
//                String code = constructor.toString();
//                if (code.contains(".addPropertyChangeListener(this)"))
//                    return pass();
//            }

            return fail("Console view should add itself as a listener in the constructor.");
//        } catch (IOException e) {
//            throw new NotGradableException();
//        }
    }
}

