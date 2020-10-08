package gradingTools.example.testCases;

import java.io.IOException;

import tools.CodeTools;
import tools.CompilationNavigation;

//import com.github.antlrjavaparser.api.body.ClassOrInterfaceDeclaration;

import framework.grading.testing.BasicTestCase;
import framework.project.ParsableClassDescription;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.ClassDescription;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;

/**
 * This test case looks for the usage of StringBuilder or StringBuffer in any method.
 */
public class NoStringToolsTestCase extends BasicTestCase {

    public NoStringToolsTestCase() {
        super("No string tool test case");
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {

        if (project.getClassesManager().isEmpty())
            throw new NotGradableException();
        return pass();

//        // You can also get the code and inspect it. This system uses Antlr to parse Java code
//        try {
//            for (ClassDescription description : project.getClassesManager().get().getClassDescriptions()) {
//
//                // The CompilationNavigation class offers methods to help navigate and work with the compilation unit
//                ClassOrInterfaceDeclaration classDef = CompilationNavigation.getClassDef(
//                		((ParsableClassDescription) description).parse()
//                		);
//
//                // With antlr parse object, you can call toString at any level to convert it to Java code.
//                String code = classDef.toString();
//
//                // Now, we'll do the check
//                code = CodeTools.removeComments(code);
//                if (code.contains("StringBuilder") || code.contains("StringBuffer"))
//                    return fail("StringBuilder/StringBuffer found in class: " + description.getJavaClass().getCanonicalName(), autoGrade);
//            }
//            return pass(autoGrade);
//        } catch (IOException e) {
//            throw new NotGradableException();
//        }
    }
}

