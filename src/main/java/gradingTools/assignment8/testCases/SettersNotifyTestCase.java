package gradingTools.assignment8.testCases;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.Method;

import tools.CompilationNavigation;

//import com.github.antlrjavaparser.JavaParser;
//import com.github.antlrjavaparser.api.CompilationUnit;
//import com.github.antlrjavaparser.api.body.ClassOrInterfaceDeclaration;

import framework.grading.testing.BasicTestCase;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.ClassDescription;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 10/29/13
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class SettersNotifyTestCase extends BasicTestCase {

    public SettersNotifyTestCase() {
        super("Setters notify observers of change test case");
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        if (project.getClassesManager().isEmpty())
            throw new NotGradableException();
        return pass();
//
//        // Find the classes which have addPropertyChangeListener
//        double listenerSetters = 0;
//        double listenerNotifies = 0;
//        String notes = "";
//        for (ClassDescription description : project.getClassesManager().get().getClassDescriptions()) {
//
//            // Does the class have a the method?
//            try {
//                if (!description.getJavaClass().isInterface()) {
//                    description.getJavaClass().getDeclaredMethod("addPropertyChangeListener", PropertyChangeListener.class);
//                    listenerSetters++;
//
//                    // Parse the code
//                    CompilationUnit compilation = JavaParser.parse(description.getSource());
//                    ClassOrInterfaceDeclaration classDef = CompilationNavigation.getClassDef(compilation);
//
//                    // Look at each setter
//                    int hasNotify = 0;
//                    for (Method m : description.getJavaClass().getMethods()) {
//                        if (m.getName().startsWith("set")) {
//
//                            // Get the setter body
//                            try {
//                                String code = CompilationNavigation.getMethod(classDef, m.getName()).toString();
//                                // The setter should notify. Look for notify, propertyChange(, firePropertyChange, PropertyChangeEvent
//                                if (code.contains("notify") || code.contains("propertyChange(") || code.contains("firePropertyChage") ||
//                                        code.contains("PropertyChangeEvent")) {
//                                    hasNotify = 1;
//                                }
//                            } catch (Exception e) {
//                                // Probably an inherited method
//                            }
//                        }
//                    }
//                    if (hasNotify == 0) {
//                        notes += "Class " + description.getJavaClass().getName() + " has addPropertyChangeListener but never notifies listeners;";
//                    }
//                    listenerNotifies += hasNotify;
//                }
//            } catch (IOException e) {
//                throw new NotGradableException();
//            } catch (NoSuchMethodException e) {
//                // Move along
//            }
//        }
//        return partialPass(listenerNotifies / listenerSetters, notes);
    }
}

