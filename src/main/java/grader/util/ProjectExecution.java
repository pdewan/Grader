package grader.util;

import framework.execution.ARunningProject;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.Project;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import util.misc.Common;
import util.trace.Tracer;

public class ProjectExecution extends BasicProjectExecution {
	public static Map<String, Object> testBean(String aFeatureName,
			String aCheckName, Project aProject, String[] aBeanDescriptions,
			Class[] aConstructorArgTypes, Object[] aConstructorArgs,
			Map<String, Object> anInputs, String[] anOutputProperties) {
		String anOutput;
		Map<String, Object> anActualOutputs = new HashMap();

		try {
			// String[] aBeanDescriptions = aBeanDescription.split(",");
			if (aBeanDescriptions.length != 4) {
				Tracer.error("Bean description  in testBean should have 4 elements instead of: "
						+ aBeanDescriptions.length);
			}
			redirectOutput();
			System.out.println("Testcase:" + aCheckName);
			System.out.println("Finding class matching:"
					+ Common.toString(aBeanDescriptions));
			Class aClass = BasicProjectIntrospection.findClass(aProject,
					aBeanDescriptions[0], aBeanDescriptions[1],
					aBeanDescriptions[2], aBeanDescriptions[3]);

			if (aClass == null) {
				System.out.println("No class matching: "
						+ Common.toString(aBeanDescriptions));
				anActualOutputs.put(MISSING_CLASS, true);
				// anActualOutputs = null;
			} else {
				System.out.println("Finding constructor matching:"
						+ Common.toString(aConstructorArgTypes));
				// anActualOutputs.put(CLASS_MATCHED,
				// aClass.getCanonicalName());
				anActualOutputs.put(CLASS_MATCHED, aClass);

				Constructor aConstructor = aClass
						.getConstructor(aConstructorArgTypes);
				Object anObject = timedInvoke(aConstructor, aConstructorArgs,
						600);
				for (String aPropertyName : anInputs.keySet()) {
					if (aPropertyName == null)
						continue;
					PropertyDescriptor aProperty = BasicProjectIntrospection
							.findProperty(aClass, aPropertyName);
					if (aProperty == null) {
						anActualOutputs.put(MISSING_PROPERTY, true);
						anActualOutputs.put(MISSING_PROPERTY + "."
								+ aPropertyName, true);
						// System.out.println("Property " + aPropertyName +
						// "not found");
						continue;
					}
					Method aWriteMethod = aProperty.getWriteMethod();
					if (aWriteMethod == null) {
						anActualOutputs.put(MISSING_WRITE, true);
						anActualOutputs.put(
								MISSING_WRITE + "." + aPropertyName, true);
						System.out.println("Missing write method for property "
								+ aPropertyName);
						continue;
					}
					Object aValue = anInputs.get(aPropertyName);
//					timedInvoke(anObject, aWriteMethod, getMethodTimeOut(),
//							new Object[] { aValue });
					try {
						timedInvoke(anObject, aWriteMethod, 
								new Object[] { aValue }, getMethodTimeOut());
					} catch (Throwable e) {
						e.printStackTrace();
						anActualOutputs.put(MISSING_WRITE, true);
						anActualOutputs.put(
								MISSING_WRITE + "." + aPropertyName, true);
						System.out.println("Erroneous write method for property "
								+ aPropertyName);
						continue;
						// TODO Auto-generated catch block
					}
				}
				for (String anOutputPropertyName : anOutputProperties) {
					if (anOutputPropertyName == null)
						continue;
					PropertyDescriptor aProperty = BasicProjectIntrospection
							.findProperty(aClass, anOutputPropertyName);
					if (aProperty == null) {

						// System.out.println("Property " + aPropertyName +
						// "not found");
						continue;
					}
					Method aReadMethod = aProperty.getReadMethod();
					if (aReadMethod == null) {
						System.out.println("Missing read method for property "
								+ anOutputPropertyName);
						anActualOutputs.put(MISSING_READ, true);
						anActualOutputs.put(MISSING_READ + "."
								+ anOutputPropertyName, true);
						continue;
					}
//					Object result = timedInvoke(anObject, aReadMethod,
//							getMethodTimeOut(), emptyArgs);
					Object result = timedInvoke(anObject, aReadMethod,
							 emptyArgs, getMethodTimeOut());
					anActualOutputs.put(anOutputPropertyName, result);
				}
			}

		} catch (NoSuchMethodException e) {
			System.out.println("Constructor not found:" + e.getMessage());
			anActualOutputs.put(MISSING_CONSTRUCTOR, true);
			// e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			anActualOutputs = null;

			e.printStackTrace();
		} catch (Throwable e) {
			anActualOutputs = null;

			e.printStackTrace();
			// TODO Auto-generated catch block
		} finally {
			anOutput = restoreOutputAndGetRedirectedOutput();
			if (anOutput != null && !anOutput.isEmpty()) {
				ARunningProject.appendToTranscriptFile(aProject, aFeatureName,
						anOutput);
			}
			anActualOutputs.put(PRINTS, anOutput);

		}
		boolean getsReturnSets = BasicProjectExecution.getsReturnedSets(
				anInputs, anActualOutputs);
		anActualOutputs.put(GETS_EQUAL_SETS, getsReturnSets);
		return anActualOutputs;
	}

	public static Map<String, Object> testBean(String aFeatureName,
			String aTestCase, Project aProject, String[] aBeanDescriptions,
			Class[] aConstructorArgTypes, Object[] aConstructorArgs,
			Map<String, Object> anInputs, String[] anOutputProperties,
			Object[] anExpectedValues) {
		if (anOutputProperties.length != anExpectedValues.length) {
			Tracer.error("output properties length not the same as expected values length");
			return null;
		}
		Map<String, Object> anActualOutputs = testBean(aFeatureName, aTestCase,
				aProject, aBeanDescriptions, aConstructorArgTypes,
				aConstructorArgs, anInputs, anOutputProperties);
		for (int i = 0; i < anOutputProperties.length; i++) {
			Object anExpectedOutput = anExpectedValues[i];
			Object anActualOutput;
			Object outputProperty = anOutputProperties[i];
			anActualOutput = outputProperty == null ? null : anActualOutputs
					.get(outputProperty);
			// anActualOutput = anActualOutputs.get(outputProperty);
			if (!Common.equal(anExpectedOutput, anActualOutput)) {
				anActualOutputs.put(EXPECTED_EQUAL_ACTUAL, false);
				anActualOutputs.put(EXPECTED_EQUAL_ACTUAL + "."
						+ anOutputProperties[i], false);
			}

		}
		return anActualOutputs;

	}

	public static Map<String, Object> testBeanWithStringConstructor(
			String aFeatureName, String aTestCase, Project aProject,
			String[] aBeanDescriptions, String aConstructorArg,
			String anIndependentPropertyName, Object anIndepentValue,
			String anOutputPropertyName, Object anExpectedOutputValue) {
		Class[] aConstructorArgTypes = new Class[] { String.class };
		Object[] aConstructorArgs = new String[] { aConstructorArg };
		String[] anOutputProperties = new String[] { anOutputPropertyName };
		Object[] anExpectedValue = new Object[] { anExpectedOutputValue };
		Map<String, Object> anInputs = new HashMap();
		anInputs.put(anIndependentPropertyName, anIndepentValue);
		return testBean(aFeatureName, aTestCase, aProject, aBeanDescriptions,
				aConstructorArgTypes, aConstructorArgs, anInputs,
				anOutputProperties);

	}

	public static Map<String, Object> testBeanWithNoConstructor(
			String aFeatureName, String aTestCase, Project aProject,
			String[] aBeanDescriptions, String anIndependentPropertyName,
			Object anIndepentValue, String anOutputPropertyName,
			Object anExpectedOutputValue) {
		Class[] aConstructorArgTypes = new Class[] { String.class };
		Object[] aConstructorArgs = new Object[] {};
		String[] anOutputProperties = new String[] { anOutputPropertyName };
		Object[] anExpectedValue = new Object[] { anExpectedOutputValue };
		Map<String, Object> anInputs = new HashMap();
		anInputs.put(anIndependentPropertyName, anIndepentValue);
		return testBean(aFeatureName, aTestCase, aProject, aBeanDescriptions,
				aConstructorArgTypes, aConstructorArgs, anInputs,
				anOutputProperties);

	}

	public static Map<String, Object> testBeanWithStringConstructor(
			String aFeatureName, String aTestCase, Project aProject,
			String[] aBeanDescriptions, String aConstructorArg) {
		Class[] aConstructorArgTypes = new Class[] { String.class };
		Object[] aConstructorArgs = new String[] { aConstructorArg };
		String[] anOutputProperties = new String[] {};
		Object[] anExpectedValue = new Object[] {};
		Map<String, Object> anInputs = new HashMap();
		return testBean(aFeatureName, aTestCase, aProject, aBeanDescriptions,
				aConstructorArgTypes, aConstructorArgs, anInputs,
				anOutputProperties);

	}
}
