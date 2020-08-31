package gradingTools.comp401f15.assignment3;

import framework.grading.FrameworkProjectRequirements;
import gradingTools.comp401f15.assignment1.testcases.MainClassDefinedTestCase;
import gradingTools.comp401f15.assignment3.testcases.EndClassTestCase;
import gradingTools.comp401f15.assignment3.testcases.EndEchoTestCase;
import gradingTools.comp401f15.assignment3.testcases.EndTokenBeanTestCase;
import gradingTools.comp401f15.assignment3.testcases.HasInterfaceTestCase;
import gradingTools.comp401f15.assignment3.testcases.MinusClassTestCase;
import gradingTools.comp401f15.assignment3.testcases.MinusEchoTestCase;
import gradingTools.comp401f15.assignment3.testcases.MinusTokenBeanTestCase;
import gradingTools.comp401f15.assignment3.testcases.NumberClassTestCase;
import gradingTools.comp401f15.assignment3.testcases.NumberComputationTestCase;
import gradingTools.comp401f15.assignment3.testcases.NumberEchoTestCase;
import gradingTools.comp401f15.assignment3.testcases.NumberTokenBeanTestCase;
import gradingTools.comp401f15.assignment3.testcases.PlusClassTestCase;
import gradingTools.comp401f15.assignment3.testcases.PlusEchoTestCase;
import gradingTools.comp401f15.assignment3.testcases.PlusTokenBeanTestCase;
import gradingTools.comp401f15.assignment3.testcases.QuoteClassTestCase;
import gradingTools.comp401f15.assignment3.testcases.QuoteEchoTestCase;
import gradingTools.comp401f15.assignment3.testcases.QuoteTokenBeanTestCase;
import gradingTools.comp401f15.assignment3.testcases.StartClassTestCase;
import gradingTools.comp401f15.assignment3.testcases.StartEchoTestCase;
import gradingTools.comp401f15.assignment3.testcases.StartTokenBeanTestCase;
import gradingTools.comp401f15.assignment3.testcases.WordClassTestCase;
import gradingTools.comp401f15.assignment3.testcases.WordComputationTestCase;
import gradingTools.comp401f15.assignment3.testcases.WordEchoTestCase;
import gradingTools.comp401f15.assignment3.testcases.WordTokenBeanTestCase;
import gradingTools.sharedTestCase.ImageEnclosedTestCase;
import gradingTools.sharedTestCase.checkstyle.CheckStyleClassDefinedTestCase;
import gradingTools.sharedTestCase.checkstyle.CheckStyleIllegalImportOrCallTestCase;
import gradingTools.sharedTestCase.checkstyle.CheckStyleVariableHasClassTypeTestCase;


public class Assignment3Requirements extends FrameworkProjectRequirements {

    public Assignment3Requirements() {
    	addDueDate("09/09/2015 23:59:00", 1.05);
    	addDueDate("09/13/2015 23:59:00", 1);
    	addDueDate("09/16/2015 23:59:00", 0.9);
    	addDueDate("09/18/2015 23:59:00", 0.75);
    	addFeature("Multi Property Token Beans", 25,
    			new NumberTokenBeanTestCase(),
    			new WordTokenBeanTestCase()
    			);
    	
    	addFeature("Single Property Token Beans", 15,
    			new QuoteTokenBeanTestCase(),
    			new StartTokenBeanTestCase(),
    			new EndTokenBeanTestCase()
    			);
    	addFeature("Scanner Check", 25, 
    			new NumberEchoTestCase(),
    			new NumberComputationTestCase(),
    			new NumberClassTestCase(),
    			new WordEchoTestCase(),
    			new WordComputationTestCase(),
    			new WordClassTestCase(),
    			new QuoteEchoTestCase(),
    			new QuoteClassTestCase(),
    			new StartEchoTestCase(),
    			new StartClassTestCase(),
    			new EndEchoTestCase(),
    			new EndClassTestCase()
    			
    			);
    	addFeature("Class has interface check", 25, new HasInterfaceTestCase("Has interface"));
    	addFeature("Variable has class type", 10, new CheckStyleVariableHasClassTypeTestCase("Variable has class type"));

    	addFeature("Plus Minus Token Beans", 6, true,
    			new PlusTokenBeanTestCase(),
    			new MinusTokenBeanTestCase()
    			);
    	addFeature("Plus Minus Scanner Check", 4, true,
    			new PlusEchoTestCase(),
    			new PlusClassTestCase(),
    			new MinusEchoTestCase(),
    			new MinusClassTestCase()
    			
    			);

//         addFeature("Numbers and words", 20,
//                 new NumberTokensTestCase(),
//                 new WordTokensTestCase());
//         addFeature("Quoted string", 30, new QuotedStringTokensTestCase());
    	
//        // Functionality
//        addFeature("Process & print tokens", 40,
//                new SingleTokenTestCase(),
//                new MultipleTokensTestCase()
//              //  new RemovePrecedingZerosTestCase()
//                );
//        addFeature("Sum and product", 10,
//                new SumTestCase(),
//                new ProductTestCase());
//        addFeature("Terminates with period", 10, new TerminateWithPeriodTestCase());

        // Style
//        addManualFeature("One loop on string", 20, new QuestionTestCase("Is there only one loop over the input string?", "Input string one loop test case"));
//        addFeature("Two declared methods", 3, new MinDeclaredMethodsInSameOrDifferentClassTestCase(2));
//        addFeature("One called method", 7, new MinDeclaredMethodsInSameOrDifferentClassTestCase(1));
//        addFeature("Variable spaces", 5, true, new VariableSpacesTestCase());
//        addFeature("Handle invalid chars", 5, true, new VariableSpacesTestCase());
        addFeature("Screenshots enclosed", 10, new ImageEnclosedTestCase());
//        addManualFeature("Breakpoint step into/over/return screenhots", 20);
        
//        addFeature("Variable spaces", 3, true, new VarialbleSpaceTokensTestCase());
//        addFeature("Plus minus", 4, true, new PlusMinusTokensTestCase());
//      addFeature("Missing quote", 3, true, new MissingQuotedStringTokensTestCase());
//      addManualFeature("Own isLetter", 3, true);





//        addManualFeature("Use an extra class with iterator like interface", 10, true);


//        addManualFeature("Screenshots", 10, new QuestionTestCase("Screenshots included showing test data output?", "Screenshots testcase"));

        // TODO: Extra Credit
//        addManualFeature("Handle invalid chars", 5, true);
//        addManualFeature("No-array parser class", 10, true);
//        addManualFeature("Variable spaces", 5, true);
        addRestriction("Illegal import or call", 25, new CheckStyleIllegalImportOrCallTestCase());
        addRestriction("Single main.Assignment", 10, new MainClassDefinedTestCase("main.Assignment(.*)"));
        addRestriction("Classes Tagged ", 18, 
        		new CheckStyleClassDefinedTestCase("@ScannerBean"),
        		new CheckStyleClassDefinedTestCase("@Word"),
        		new CheckStyleClassDefinedTestCase("@Number"),
        		new CheckStyleClassDefinedTestCase("@Quote"),
        		new CheckStyleClassDefinedTestCase("@Start"),
        		new CheckStyleClassDefinedTestCase("@End"));


       
        

//        addManualFeature("Nice code", 10, true);

        // Restrictions
//        addRestriction("No .split allowed", -10, new NoSplitTestCase());

    }
}
