package gradingTools.comp401f15.assignment11.testcases;

import java.lang.reflect.Method;

import grader.basics.config.BasicProjectExecution;

public class FutureTester {
	public void test() {
		System.out.println("Future tester");
		System.out.println("Thread name:" + Thread.currentThread().getName());
	}
	public static void main (String[] args) {
		try {
		FutureTester futureTester = new FutureTester();
		Method test = FutureTester.class.getMethod("test");
		BasicProjectExecution.timedInvoke(futureTester, test, new Object[]{});
		} catch (Exception e) {
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
