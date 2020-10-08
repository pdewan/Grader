package gradingTools.sharedTestCase;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.github.antlrjavaparser.api.CompilationUnit;
//import com.github.antlrjavaparser.api.ImportDeclaration;
//import com.github.antlrjavaparser.api.body.BodyDeclaration;
//import com.github.antlrjavaparser.api.body.ClassOrInterfaceDeclaration;
//import com.github.antlrjavaparser.api.body.ConstructorDeclaration;
//import com.github.antlrjavaparser.api.body.InitializerDeclaration;
//import com.github.antlrjavaparser.api.body.MethodDeclaration;
//import com.github.antlrjavaparser.api.body.ModifierSet;
//import com.github.antlrjavaparser.api.body.Parameter;
//import com.github.antlrjavaparser.api.body.TypeDeclaration;
//import com.github.antlrjavaparser.api.body.VariableDeclarator;
//import com.github.antlrjavaparser.api.expr.Expression;
//import com.github.antlrjavaparser.api.expr.MethodCallExpr;
//import com.github.antlrjavaparser.api.expr.VariableDeclarationExpr;
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

public abstract class CodeInspectorTestCase extends BasicTestCase{//mdaum mod

	public CodeInspectorTestCase(String name) {
		super(name);
	}
	
//	protected String getImportStatement(ImportDeclaration decl) {
//		String str = decl.toString();
//		str = str.replaceAll("\u00A0", " ");
//		
//		String pattern = "import\\s+.*" + decl.getName().getName() + "\\s*;";
//		Matcher matcher = Pattern.compile(pattern).matcher(str);
//		
//		String result = null;
//		while (matcher.find()) {
//			result = matcher.group();
//		}
//		return result;
//	}
//	
//	// Overwrite this method to inspect imports
//	protected void inspectImportDecl(ImportDeclaration importDecl) {
//		String importStmt = getImportStatement(importDecl);
//		if (importStmt != null) {
//			inspectImport(importStmt);
//		}
//	}
//	
//	protected String getClassSignature(ClassOrInterfaceDeclaration decl) {
//		String str = decl.toString();
//		str = str.replaceAll("\u00A0", " ");
//		
//		String pattern = "(((public)|(private)|(protected))\\s+)?";
//		if (decl.isInterface()) {
//			pattern += "interface";
//		} else {
//			pattern += "class";
//		}
//		pattern += "\\s+" + decl.getName() + ".*[{]";
//		
//		
//		Matcher matcher = Pattern.compile(pattern).matcher(str);
//		if (matcher.find()) {
//			return matcher.group();
//		} else {
//			return null;
//		}
//	}
//	
//	protected void inspectClassOrInterfaceDeclaration(ClassOrInterfaceDeclaration decl) {
//		
//		String classSignature = getClassSignature(decl);
//		inspectClassOrInterfaceSignature(classSignature);
//		
//		String name = decl.getName();
//		inspectClassOrInterfaceName(name);
//	}
//	
//	protected String getConstructorSignature(ConstructorDeclaration decl) {
//		String str = decl.toString();
//		str = str.replaceAll("\u00A0", " ");
//		
//		String pattern = "";//(((public)|(private)|(protected))\\s+)?" + decl.getName() +"\\s*[(]\\s*";
//		
//		if(ModifierSet.isPublic(decl.getModifiers())) {
//			pattern += "public\\s+";
//		} else if(ModifierSet.isProtected(decl.getModifiers())) {
//			pattern += "protected\\s+";
//		} else if(ModifierSet.isPrivate(decl.getModifiers())) {
//			pattern += "private\\s+";
//		}
//
//		String params = "";
//		if (decl.getParameters() != null) {
//			for (Iterator<Parameter> i = decl.getParameters().iterator(); i
//					.hasNext();) {
//				Parameter p = i.next();
//				params += "\\Q"+p.getType() + "\\E\\s+"+p.getId()+"+\\s*";
//				inspectVariableName(p.getId().getName());
//				if (i.hasNext()) {
//					params += "," + "\\s*";
//				}
//			}
//		}
//
//		pattern += params;
//		
//		pattern += "\\s*[)]";
//		
//		Matcher matcher = Pattern.compile(pattern).matcher(str);
//		if (matcher.find()) {
//			return matcher.group();
//		} else {
//			return null;
//		}
//	}
//	
//	protected void inspectConstuctorDeclaration(ConstructorDeclaration constructorDecl) {
//		String signature = getConstructorSignature(constructorDecl);
//		inspectConstructorSignature(signature);
//		inspectBlockStmt(constructorDecl.getBlock());
//	}
//	
//	protected String getMethodSignature(MethodDeclaration decl) {
//		String str = decl.toString();
//		str = str.replaceAll("\u00A0", " ");
//		
//		String pattern = "";//"(((public)|(private)|(protected))\\s+)?";
//		
//		if(ModifierSet.isPublic(decl.getModifiers())) {
//			pattern += "public\\s+";
//		} else if(ModifierSet.isProtected(decl.getModifiers())) {
//			pattern += "protected\\s+";
//		} else if(ModifierSet.isPrivate(decl.getModifiers())) {
//			pattern += "private\\s+";
//		}
//		
//		if(ModifierSet.isStatic(decl.getModifiers())) {
//			pattern += "static\\s+";
//		}
//
//		String type = decl.getType() != null ? decl.getType().toString()+"\\s+" : "";
//		pattern += type + decl.getName() +"\\s*[(]\\s*";
//		
//		String params = "";
//		if (decl.getParameters() != null) {
//			for (Iterator<Parameter> i = decl.getParameters().iterator(); i
//					.hasNext();) {
//				Parameter p = i.next();
//				params += "\\Q"+p.getType() + "\\E\\s+"+p.getId()+"+\\s*";
//				inspectVariableName(p.getId().getName());
//				if (i.hasNext()) {
//					params += "," + "\\s*";
//				}
//			}
//		}
//
//		pattern += params;
//		
//		pattern += "\\s*[)]";
//		
//		Matcher matcher = Pattern.compile(pattern).matcher(str);
//		if (matcher.find()) {
//			return matcher.group();
//		} else {
//			return null;
//		}
//	}
//	
//	protected void inspectMethodDeclaration(MethodDeclaration methodDecl) {
//		String signature = getMethodSignature(methodDecl);
//		inspectMethodSignature(signature);
//		inspectMethodName(methodDecl.getName());
//		inspectBlockStmt(methodDecl.getBody());
//	}
//	
//	protected void inspectIntializerDeclaration(InitializerDeclaration decl) {
//		inspectBlockStmt(decl.getBlock());
//	}
//	
//	protected void inspectBlockStmt(BlockStmt blockStatement) {
//		if (blockStatement == null || blockStatement.getStmts() == null) {
//			return;
//		}
//		for (Statement statement : blockStatement.getStmts()) {
//			inspectStatement(statement);
//		}
//	}
//	
//	protected void inspectStatement(Statement statement) {
//		if (statement instanceof ExpressionStmt) {
//			inspectExpression(((ExpressionStmt) statement).getExpression());
//		} else if (statement instanceof BlockStmt) {
//			inspectBlockStmt((BlockStmt) statement);
//		} else if (statement instanceof IfStmt) {
//			inspectIfStatement((IfStmt) statement);
//		} else if (statement instanceof ForStmt) {
//			inspectForStatement((ForStmt) statement);
//		} else if (statement instanceof WhileStmt) {
//			inspectWhileStatement((WhileStmt) statement);
//		} else if (statement instanceof DoStmt) {
//			inspectDoStatement((DoStmt) statement);
//		} else if (statement instanceof ReturnStmt) {
//			inspectReturnStatement((ReturnStmt) statement);
//		}
//	}
//	
//	protected void inspectIfStatement(IfStmt statement) {
//		inspectCondition(statement.getCondition());
//		inspectThenStatement(statement.getThenStmt());
//		inspectElseStatement(statement.getElseStmt());
//	}
//	
//	protected void inspectForStatement(ForStmt statement) {
//		for (Expression expression : statement.getInit()) {
//			inspectLoopInit(expression);
//		}		
//		inspectCondition(statement.getCompare());
//		for (Expression expression: statement.getUpdate()){
//			inspectLoopUpdate(expression);
//		}
//		inspectLoopBody(statement.getBody());
//	}
//	
//	protected void inspectWhileStatement(WhileStmt statement) {
//		inspectCondition(statement.getCondition());
//		inspectLoopBody(statement.getBody());
//	}
//	
//	protected void inspectDoStatement(DoStmt statement) {
//		inspectCondition(statement.getCondition());
//		inspectLoopBody(statement.getBody());
//	}
//	
//	protected void inspectReturnStatement(ReturnStmt statement) {
//		inspectExpression(statement.getExpr());
//	}
//	
//	protected void inspectThenStatement(Statement statement) {
//		inspectStatement(statement);
//	}
//	
//	protected void inspectElseStatement(Statement statement) {
//		inspectStatement(statement);
//	}
//	
//	protected void inspectCondition(Expression condition) {
//		inspectExpression(condition);
//	}
//	
//	protected void inspectLoopInit(Expression expression) {
//		inspectExpression(expression);
//	}
//	
//	protected void inspectLoopUpdate(Expression expression) {
//		inspectExpression(expression);
//	}
//	
//	protected void inspectLoopBody(Statement body) {
//		inspectStatement(body);
//	}
//	
//	protected void inspectExpression(Expression expression) {
//		if  (expression instanceof VariableDeclarationExpr) {
//			inspectVariableDeclarationExpr((VariableDeclarationExpr) expression);
//		} else if (expression instanceof MethodCallExpr) {
//			inspectMethodCallExpr((MethodCallExpr) expression);
//		} else {
//			//TODO: add more types of expressions
//		}
//	}
//	
//	protected void inspectVariableDeclarationExpr(VariableDeclarationExpr expr) {
//		for (VariableDeclarator var : expr.getVars()) {
//			inspectVariableName(var.getId().toString());
//		}
//	}
//	
//	protected void inspectMethodCallExpr(MethodCallExpr expr) {
//		//TODO: fill in
//	}
//	
//	protected void inspectTypeDeclaration(TypeDeclaration type) {
//		if (type instanceof ClassOrInterfaceDeclaration) {
//			inspectClassOrInterfaceDeclaration((ClassOrInterfaceDeclaration) type);
//		}
//		
//		for (BodyDeclaration body : type.getMembers()) {
//			if (body instanceof ConstructorDeclaration) {
//				inspectConstuctorDeclaration((ConstructorDeclaration) body);
//			} else if (body instanceof InitializerDeclaration) {
//				inspectIntializerDeclaration((InitializerDeclaration) body);
//			} else if (body instanceof MethodDeclaration) {
//				inspectMethodDeclaration((MethodDeclaration) body);
//			}
//
//		}
//	}

	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
			NotGradableException {
		
		resetVariablesForEachProject();
		
		if (project.getClassesManager().isEmpty())
			throw new NotGradableException();

		for (ClassDescription description : project.getClassesManager().get()
				.getClassDescriptions()) {
			Class<?> javaClass = description.getJavaClass();
			if(javaClass.getName().toLowerCase().contains("m"))continue;//this line meant for picking out bad classes....
//			try {
//
//				// Get the comment free code
//				CompilationUnit compilationUnit = ((ParsableClassDescription) description).parse();
//
//				for (ImportDeclaration importDecl : compilationUnit.getImports()) {
//					inspectImportDecl(importDecl);
//				}
//
//				Collection<TypeDeclaration> types = compilationUnit.getTypes();
//				for (TypeDeclaration type : types) {
//					inspectTypeDeclaration(type);
//				}
//
//			} catch (IOException e) {
//				throw new NotGradableException();
//			}
		}

		return codeInspectionResult();
	}

	
	public void inspectImport(String importStatement) {}
	
	public void inspectClassOrInterfaceSignature(String signature) {}
	
	public void inspectClassOrInterfaceName(String name) {}
	
	public void inspectConstructorSignature(String signature) {}
	
	public void inspectMethodSignature(String signature) {}

	public void inspectMethodName(String name) {}
	
	public void inspectVariableName(String variable) {}
	
	public abstract void resetVariablesForEachProject();

	public abstract TestCaseResult codeInspectionResult();
}