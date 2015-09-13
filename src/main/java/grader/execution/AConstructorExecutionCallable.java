package grader.execution;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import util.misc.Common;

public class AConstructorExecutionCallable implements Callable{
	Constructor constructor;
//	Object object;
	Object[] args;
	public AConstructorExecutionCallable(Constructor aConstructor, Object[] anArgs) {
		constructor = aConstructor;
//		object = anObject;
		args = anArgs;
	}
	@Override
	public Object call() throws Exception {
		System.out.println ("calling method: " + constructor + " " + Common.toString(args));
		Object retVal = constructor.newInstance(args);
		System.out.println ("called method: " + constructor + " " + Common.toString(args));

		return retVal;
	}
	

}