//non Java doc reg exp: (?s)/\*[^*](?:(?!\*/).)*\(non-javadoc\)(?:(?!\*/).)*\*/
package grader.project.flexible;

import grader.basics.file.FileProxy;
import grader.basics.settings.BasicGradingEnvironment;
import grader.execution.ProxyClassLoader;
import grader.trace.compilation.ClassFileCouldNotBeCompiled;
import grader.trace.compilation.ClassFileNotFound;
import grader.trace.compilation.ClassLoaded;
import grader.trace.compilation.CompilationUnitCreated;
import grader.trace.compilation.JavacSourceClassCreated;
import grader.trace.compilation.QDoxClassCreated;
import grader.trace.compilation.SourceTextCompiledInMemory;
import grader.trace.overall_transcript.OverallTranscriptSaved;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import util.annotations.EditablePropertyNames;
import util.annotations.PropertyNames;
import util.annotations.StructurePattern;
import util.annotations.Tags;
import util.javac.ParserMain;
import util.javac.SourceClass;
import util.javac.SourceClassManager;
import util.misc.Common;
import util.misc.TeePrintStream;
import util.trace.Tracer;
import util.trace.javac.CompilerNotFound;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;
// gets the source from AClassesManager
// converts class name to class object

import com.github.antlrjavaparser.JavaParser;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;

public class AClassDescription implements FlexibleClassDescription {

    ClassProxy classProxy;
    Class javaClass;
    StringBuffer text;
    long sourceTime;
    String packageName;
    String className;
    JavaClass qdoxClass;
    JavaSource javaSource;
    FlexibleProject project;
    FileProxy sourceFile;
    SourceClass javacSourceClass;
//	List<String> classNamesThatCouldNotBeCompiled = new ArrayList();
//	
//	List<String> classNamesCompiled = new ArrayList();

    private CompilationUnit compilationUnit;

    public AClassDescription(String aClassName, StringBuffer aText, long aSourceTime, ProxyClassLoader aClassLoader, FlexibleProject aProject, FileProxy aFileProxy) {
//		text = Common.toText(aClassName);
        sourceFile = aFileProxy;
        project = aProject;
        text = aText;
        sourceTime = aSourceTime;
        if (aClassLoader != null) {
            PrintStream teeout = null;
            PrintStream teeerr = null;
            PrintStream stdout = System.out;
            PrintStream stderr = System.err;
            try {
//			javaClass = Class.forName(aClassName);
                if (BasicGradingEnvironment.get().isLoadClasses()) {
                    javaClass = aClassLoader.loadClass(aClassName);
                    if (javaClass == null) {
                        ClassFileNotFound classFileNotfound = ClassFileNotFound.newCase(aClassName, this);

                        String outputFileName = aProject.getOutputFileName();
                        ByteArrayOutputStream outStream = null;
                        if (outputFileName != null) {
                            outStream = new ByteArrayOutputStream();
//					FileOutputStream outStream = new FileOutputStream(outputFile, true);
//					ByteArrayOutputStream outStream = new ByteArrayOutputStream();

                            teeout = new TeePrintStream(outStream, stdout);
                            teeerr = new TeePrintStream(outStream, stderr);
                            System.setOut(teeout);
                            System.setErr(teeerr);
                        }
                        classFileNotfound.printStackTrace();
                        Tracer.error(classFileNotfound.getMessage());

                        if (BasicGradingEnvironment.get().isCompileMissingObjectCode()) {
                            String compileClassMessage = "Attempting to compile class:" + aClassName;
                            Tracer.error(compileClassMessage);
				// need to compile multiple files in one shot because of dependencies
                            // in any case am duplicating work of Josh's compilation
                            // better to have Josh's code run through the directory, unzipping and compiling things, and then 
                            // read the entire directory
                            // compilation errors
                            // for now letting it be
                            // so as long as we have one class programs, we are fine it seems
                            byte[] classBytes = ParserMain.compile(aClassName, aText, BasicGradingEnvironment
                                    .get().getClassPath());
                            if (classBytes != null) {
                                SourceTextCompiledInMemory.newCase(aClassName, classBytes, this);
                                javaClass = aClassLoader.defineDynamicallyCompiledClass(aClassName, classBytes);
                            }
//				teeout.close();
//				teeerr.close();
//				System.setOut(stdout);
//				System.setErr(stderr);
                            if (javaClass != null) {
                                ClassLoaded.newCase(aClassName, this);
                                project.addCompiledClass(aClassName);
                            } else {
                                Tracer.error("remove 2: " + ClassFileCouldNotBeCompiled.newCase(aClassName, this).getMessage());
                                project.addNonCompiledClass(aClassName);

//					Tracer.error("Could not compile");
                            }
                            String transcript = outStream.toString();
                            outStream.close();
                            try {
                                String original = Common.toText(new File(outputFileName));
                                if (!original.contains(compileClassMessage)) { // otherwise we have already put the text in a previous pass
                                    FileWriter fileWriter = new FileWriter(outputFileName);
                                    fileWriter.append(transcript + "\n" + "----------------------------------------\n" + original);
                                    OverallTranscriptSaved.newCase(null, null, null, outputFileName, transcript, this);
//					if (project.getCurrentGradingFeature() != null)
//					FeatureTranscriptSaved.newCase(null, null, project,  project.getCurrentGradingFeature()., outputFileName, transcript, this);;
                                    fileWriter.close();
                                }
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

//				Tracer.error("Missing class file for:" + aClassName);
                    } else {

                        ClassLoaded.newCase(aClassName, this);
//			javaClass = Class.forName(aClassName);
//			javaClass = aClassLoader.findClass(aClassName);
                        classProxy = AClassProxy.classProxy(javaClass);
                    }
                }

            } catch (CompilerNotFound cnf) {
                System.out.println(cnf.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                ClassFileNotFound.newCase(aClassName, this);

//			Tracer.error("Missing class file for:" + aClassName);
//			e.printStackTrace();
            } catch (Error e) { // Added by Josh, the loadClass method may throw a IncompatibleClassChangeError
                ClassFileNotFound.newCase(aClassName, this);

//			Tracer.error("Missing class file for:" + aClassName);
            } finally {
                if (teeout != null) {
                    teeout.close();
                    System.setOut(stdout);
                }
                if (teeerr != null) {
                    teeerr.close();
                    System.setErr(stderr);
                }

            }
        }

        className = aClassName;
        if (BasicGradingEnvironment.get().isLoadClasses()) {
            packageName = Common.classNameToPackageName(aClassName);
        }
//		qdoxClass = getQdoxClass();
    }

    public String toString() {
        if (classProxy != null) {
            return classProxy.toString();
        } else {
            return className;
        }
    }

    public String getClassName() {
        return className;
    }

    public ClassProxy getClassProxy() {
        return classProxy;
    }

    public void setClassProxy(ClassProxy classProxy) {
        this.classProxy = classProxy;
    }

    public StringBuffer getText() {
        return text;
    }

    public void setText(StringBuffer text) {
        this.text = text;
    }

    public long getSourceTime() {
        return sourceTime;
    }

    public void setSourceTime(long newVal) {
        this.sourceTime = newVal;
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public FlexibleProject getProject() {
        return project;
    }

    @Override
    public String[] getTags() {
        if (classProxy == null) {
            return new String[]{};
        }
        try {
        	Tags tags = classProxy.getAnnotation(Tags.class);
        
        return tags == null ? new String[]{} : tags.value();
        }
        catch(Throwable e) {
        	e.printStackTrace();
        	return new String[]{};
        }
    }

    @Override
    public String getStructurePatternName() {
        if (classProxy == null) {
            return null;
        }
        StructurePattern structurePattern = classProxy.getAnnotation(StructurePattern.class);
        return structurePattern == null ? null : structurePattern.value();
    }

    @Override
    public String[] getPropertyNames() {
        if (classProxy == null) {
            return new String[]{};
        }
        PropertyNames propertyNames = classProxy.getAnnotation(PropertyNames.class);
        return propertyNames == null ? new String[]{} : propertyNames.value();
    }

    @Override
    public String[] getEditablePropertyNames() {
        if (classProxy == null) {
            return new String[]{};
        }
        EditablePropertyNames editablePropertyNames = classProxy.getAnnotation(EditablePropertyNames.class);
        return editablePropertyNames == null ? new String[]{} : editablePropertyNames.value();
    }

    @Override
    public JavaClass getQdoxClass() {
        if (qdoxClass == null) {
            initializeQdoxData();
//			FileProxy fileProxy = project.
        }
        return qdoxClass;
    }

    @Override
    public JavaSource getQdoxSource() {
        if (javaSource == null) {
            initializeQdoxData();
//			FileProxy fileProxy = project.
        }
        return javaSource;
    }

    public void initializeQdoxData() {
        JavaDocBuilder builder = project.getJavaDocBuilder();
        javaSource = builder.addSource(new InputStreamReader(sourceFile.getInputStream()));
        qdoxClass = builder.getClassByName(className);
        QDoxClassCreated.newCase(className, this);
    }

    @Override
    public SourceClass getJavacSourceClass() {
        if (javacSourceClass == null) {
            // added classpath, should not really be accessed
            javacSourceClass = SourceClassManager.getInstance().getOrCreateClassInfo(className, BasicGradingEnvironment
                    .get().getClassPath());
            JavacSourceClassCreated.newCase(className, this);

        }
        return javacSourceClass;
    }

    @Override
    public Class<?> getJavaClass() {
        return javaClass;
    }

//	public String getComment() {
//		return getQdoxClass().getComment();
//	}
    @Override
    public CompilationUnit getCompilationUnit() throws IOException {
        if (compilationUnit == null) {
            compilationUnit = JavaParser.parse(sourceFile.getInputStream());
            CompilationUnitCreated.newCase(sourceFile.getAbsoluteName(), this);
        }
        return compilationUnit;
    }

    public FileProxy getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(FileProxy sourceFile) {
        this.sourceFile = sourceFile;
    }
//	public List<String> getClassNamesThatCouldNotBeCompiled() {
//		return classNamesThatCouldNotBeCompiled;
//	}
//	public void setClassNamesThatCouldNotBeCompiled(List<String> classNamesToCompile) {
//		this.classNamesThatCouldNotBeCompiled = classNamesToCompile;
//	}
//	public List<String> getClassNamesCompiled() {
//		return classNamesCompiled;
//	}
//	public void setClassNamesCompiled(List<String> classNamesCompiled) {
//		this.classNamesCompiled = classNamesCompiled;
//	}

}
