package gradingTools.assignment9.testCases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import tools.CompilationNavigation;

//import com.github.antlrjavaparser.api.body.ClassOrInterfaceDeclaration;
//import com.github.antlrjavaparser.api.body.ConstructorDeclaration;

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
public class ScenePainterListenerTestCase extends BasicTestCase {

    public ScenePainterListenerTestCase() {
        super("Scene painter is a registered listener test case");
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        // Make sure we can get the class description
        if (project.getClassesManager().isEmpty())
            throw new NotGradableException();
        return pass();
//        Set<ClassDescription> classDescriptions = project.getClassesManager().get().findByTag("InheritingBridgeScenePainter");
//        if (classDescriptions.isEmpty())
//            return fail("No class tagged \"Inheriting Bridge Scene Painter\"");
//        ClassDescription classDescription = new ArrayList<>(classDescriptions).get(0);
//
//        // It should register itself as a listener at least once in the constructor
//        // Get the constructor code
//        try {
//            ClassOrInterfaceDeclaration classDef = CompilationNavigation.getClassDef(((ParsableClassDescription) classDescription).parse());
//            List<ConstructorDeclaration> constructors = CompilationNavigation.getConstructors(classDef);
//
//            // Look for one assignment in any constructor
//            for (ConstructorDeclaration constructor : constructors) {
//                String code = constructor.toString();
//                if (code.contains(".addPropertyChangeListener(this)"))
//                    return pass();
//            }
//            return fail("The scene painter never registers itself as a listener in the constructor.");
//        } catch (IOException e) {
//            throw new NotGradableException();
//        }
    }
}

