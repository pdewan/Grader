package gradingTools.assignment9.testCases;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import tools.CompilationNavigation;

//import com.github.antlrjavaparser.api.body.ClassOrInterfaceDeclaration;
//import com.github.antlrjavaparser.api.body.MethodDeclaration;

import framework.grading.testing.BasicTestCase;
import framework.project.ParsableClassDescription;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.ClassDescription;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 11/6/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class PaintListenerPaintOnEventTestCase extends BasicTestCase {

    public PaintListenerPaintOnEventTestCase() {
        super("PaintListener calls paint on events test case");
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        // Make sure we can get the class description
        if (project.getClassesManager().isEmpty())
            throw new NotGradableException();
        return pass();
//        Set<ClassDescription> classDescriptions = project.getClassesManager().get().findByTag("PaintListener");
//        if (classDescriptions.isEmpty())
//            return fail("No class tagged \"PaintListener\"");
//        ClassDescription classDescription = new ArrayList<>(classDescriptions).get(0);
//
//        // Get the views
//        Class<?> paintListener = classDescription.getJavaClass();
//        Set<ClassDescription> views = new HashSet<>();
//        for (ClassDescription description : project.getClassesManager().get().getClassDescriptions()) {
//            if (!description.getJavaClass().isInterface() && paintListener.isAssignableFrom(description.getJavaClass())) {
//                views.add(description);
//            }
//        }
//
//        // Count how many views call paint or repaint in propertyChange
//        double paintCount = 0;
//        String notes = "";
//        for (ClassDescription view : views) {
//            // Get the constructors
//            try {
//                ClassOrInterfaceDeclaration classDef = CompilationNavigation.getClassDef(((ParsableClassDescription) view).parse());
//                MethodDeclaration method = CompilationNavigation.getMethod(classDef, "propertyChange");
//                if (method == null) {
//                    notes += "Paint listener view " + view.getJavaClass().getSimpleName() + " doesn't have a propertyChange method.";
//                    continue;
//                }
//
//                String code = method.toString();
//
//                if (code.contains("paint();"))
//                    paintCount++;
//                else
//                    notes += "Paint listener view " + view.getJavaClass().getSimpleName() + " doesn't call paint() or repaint() when events happen.\n";
//
//            } catch (Exception e) {
//                // Don't do anything here.
//            }
//        }
//
//        double count = views.size();
//        return partialPass(paintCount / count, notes);
    }
}

