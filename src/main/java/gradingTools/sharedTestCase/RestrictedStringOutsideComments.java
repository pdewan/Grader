package gradingTools.sharedTestCase;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

//import com.github.antlrjavaparser.api.CompilationUnit;
//import com.github.antlrjavaparser.api.ImportDeclaration;
//import com.github.antlrjavaparser.api.body.BodyDeclaration;
//import com.github.antlrjavaparser.api.body.ConstructorDeclaration;
//import com.github.antlrjavaparser.api.body.InitializerDeclaration;
//import com.github.antlrjavaparser.api.body.MethodDeclaration;
//import com.github.antlrjavaparser.api.body.TypeDeclaration;
//import com.github.antlrjavaparser.api.expr.Expression;
//import com.github.antlrjavaparser.api.stmt.BlockStmt;
//import com.github.antlrjavaparser.api.stmt.DoStmt;
//import com.github.antlrjavaparser.api.stmt.ExpressionStmt;
//import com.github.antlrjavaparser.api.stmt.ForStmt;
//import com.github.antlrjavaparser.api.stmt.IfStmt;
//import com.github.antlrjavaparser.api.stmt.ReturnStmt;
//import com.github.antlrjavaparser.api.stmt.Statement;
//import com.github.antlrjavaparser.api.stmt.WhileStmt;

import framework.grading.testing.BasicTestCase;
import framework.project.ParsableClassDescription;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.ClassDescription;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;

public class RestrictedStringOutsideComments extends BasicTestCase {

	private final String restrictedString;

	public RestrictedStringOutsideComments(String restrictedString) {
		super("No " + restrictedString + " Restriction");
		this.restrictedString = restrictedString;
	}

	protected boolean containsRestrictedString(String str) {
		return str.contains(restrictedString);
	}

	protected TestCaseResult restrictedStringOccurred() {
		return fail(restrictedString + " not allowed");
	}

	protected TestCaseResult noRestrictedString() {
		return pass();
	}

//	private boolean containsRestrictedString(ImportDeclaration importDecl) {
//		return containsRestrictedString(importDecl.getName().toString());
//	}

//	private boolean containsRestrictedString(Expression expression) {
//		if (expression == null) {
//			return false;
//		}
//		return containsRestrictedString(expression.toString());
//	}
//
//	private boolean containsRestrictedString(List<Expression> expressions) {
//		if (expressions == null) {
//			return false;
//		}
//		for (Expression expression : expressions) {
//			if (containsRestrictedString(expression)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	private boolean containsRestrictedString(Statement statement) {
//		if (statement instanceof ExpressionStmt) {
//			return containsRestrictedString(((ExpressionStmt) statement).getExpression());
//		} else if (statement instanceof BlockStmt) {
//			if (((BlockStmt) statement).getStmts() != null) {
//				for (Statement subStatement : ((BlockStmt) statement).getStmts()) {
//					if (containsRestrictedString(subStatement)) {
//						return true;
//					}
//				}
//			}
//		} else if (statement instanceof IfStmt) {
//			if (containsRestrictedString(((IfStmt) statement).getCondition())
//					|| containsRestrictedString(((IfStmt) statement).getThenStmt())
//					|| containsRestrictedString(((IfStmt) statement).getElseStmt())) {
//				return true;
//			}
//		} else if (statement instanceof ForStmt) {
//			if (containsRestrictedString(((ForStmt) statement).getInit())
//					|| containsRestrictedString(((ForStmt) statement).getCompare())
//					|| containsRestrictedString(((ForStmt) statement).getUpdate())
//					|| containsRestrictedString(((ForStmt) statement).getBody())) {
//				return true;
//			}
//		} else if (statement instanceof WhileStmt) {
//			if (containsRestrictedString(((WhileStmt) statement).getCondition())
//					|| containsRestrictedString(((WhileStmt) statement).getBody())) {
//				return true;
//			}
//		} else if (statement instanceof DoStmt) {
//			if (containsRestrictedString(((DoStmt) statement).getCondition())
//					|| containsRestrictedString(((DoStmt) statement).getBody())) {
//				return true;
//			}
//		} else if (statement instanceof ReturnStmt) {
//			if (containsRestrictedString(((ReturnStmt) statement).getExpr())) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	private boolean containsRestrictedString(BlockStmt blockStatement) {
//		if (blockStatement == null || blockStatement.getStmts() == null) {
//			return false;
//		}
//		for (Statement statement : blockStatement.getStmts()) {
//			if (containsRestrictedString(statement)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	private boolean containsRestrictedString(TypeDeclaration type) {
//		for (BodyDeclaration body : type.getMembers()) {
//			BlockStmt blockStmt = null;
//			if (body instanceof ConstructorDeclaration) {
//				blockStmt = ((ConstructorDeclaration) body).getBlock();
//			} else if (body instanceof InitializerDeclaration) {
//				blockStmt = ((InitializerDeclaration) body).getBlock();
//			} else if (body instanceof MethodDeclaration) {
//				blockStmt = ((MethodDeclaration) body).getBody();
//			}
//
//			if (containsRestrictedString(blockStmt)) {
//				return true;
//			}
//
//		}
//		return false;
//	}

	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
			NotGradableException {
		if (project.getClassesManager().isEmpty())
			throw new NotGradableException();
		
		if (project.getClassesManager().get().getClassDescriptions().isEmpty())
				throw new NotGradableException();
		for (ClassDescription description : project.getClassesManager().get()
				.getClassDescriptions()) {
//			try {

//				// Get the comment free code
//				CompilationUnit compilationUnit = ((ParsableClassDescription) description).parse();
//
//				for (ImportDeclaration importDecl : compilationUnit.getImports()) {
//					if (containsRestrictedString(importDecl)) {
//						return restrictedStringOccurred();
//					}
//				}
//
//				Collection<TypeDeclaration> types = compilationUnit.getTypes();
//				for (TypeDeclaration type : types) {
//					if (containsRestrictedString(type)) {
//						return restrictedStringOccurred();
//					}
//				}

//			} catch (IOException e) {
//				throw new NotGradableException();
//			}
		}

		return noRestrictedString();
	}
}
