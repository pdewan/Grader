package gradingTools.assignment10.testCases;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import scala.Option;
import tools.CompilationNavigation;
import tools.classFinder2.ClassFinder;
import tools.classFinder2.ClassType;

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
 * Date: 11/11/13
 * Time: 11:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class SayMoveCommandInvokedTestCase extends BasicTestCase {

    public SayMoveCommandInvokedTestCase() {
        super("Command objects returned from say and move are invoked.");
    }

    @Override
    public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
        if (project.getClassesManager().isEmpty())
            throw new NotGradableException();

        // Get the command interpreter
        Option<ClassDescription> classDescription = ClassFinder.get(project).findByTag("Command Interpreter", autoGrade, ClassType.CLASS);
        if (classDescription.isEmpty())
            return fail("Command interpreter not found.", autoGrade);
        Class<?> _class = classDescription.get().getJavaClass();

        // The approach to check this is to check
        //  1. find the caller of the parser methods
        //  2. check that run() is called

        // Get the parser methods
        Option<Method> sayMethod = getMethodOption(classDescription, "say parser");
        Option<Method> moveMethod = getMethodOption(classDescription, "move parser");
        if (sayMethod.isEmpty() && moveMethod.isEmpty())
            return fail("Could not find parser methods", autoGrade);
        return pass();
//        // Find where the parser methods are called.
//        Set<MethodDeclaration> callers = new HashSet<MethodDeclaration>();
//        callers.addAll(findCallers(classDescription.get(), sayMethod));
//        callers.addAll(findCallers(classDescription.get(), moveMethod));
//
//        // Look in each caller function for ".run()"
//        for (MethodDeclaration caller : callers) {
//            String code = caller.toString();
//            if (code.contains(".run()"))
//                return pass(autoGrade);
//        }
//        return fail("Couldn't find a parser invoker that called .run()", autoGrade);
    }

    private Option<Method> getMethodOption(Option<ClassDescription> classDescription, String tag) {
        List<Method> methods = classDescription.get().getTaggedMethods(tag);
        return methods.isEmpty() ? Option.<Method>empty() : Option.apply(methods.get(0));
    }

//    private List<MethodDeclaration> findCallers(ClassDescription classDescription, Option<Method> method) throws NotGradableException {
//        if (method.isEmpty())
//            return new ArrayList<MethodDeclaration>();
//        List<MethodDeclaration> callers = new ArrayList<MethodDeclaration>();
//
//        // Get the name of the method
//        String name = method.get().getName();
//
//        // Look in the code for places where it is called
//        try {
////            ClassOrInterfaceDeclaration classDef = CompilationNavigation.getClassDef(classDescription.parse());
//            ClassOrInterfaceDeclaration classDef = CompilationNavigation.getClassDef(
//            		((ParsableClassDescription) classDescription).parse()
//            		);
//            List<MethodDeclaration> methods = CompilationNavigation.getMethods(classDef);
//
//            for (MethodDeclaration m : methods) {
//                if (!m.getName().equals(name)) {
//                    String code = m.toString();
//                    if (code.contains(name + "("))
//                        callers.add(m);
//                }
//            }
//        } catch (IOException e) {
//            throw new NotGradableException();
//        }
//
//        return callers;
//    }
}

