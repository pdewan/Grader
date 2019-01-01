package grader.steppers;

import framework.grading.ProjectRequirements;
import framework.grading.testing.CheckResult;
import framework.grading.testing.Checkable;
import framework.navigation.StudentFolder;
import grader.assignment.AGradingFeature;
import grader.assignment.GradingFeature;
import grader.assignment.GradingFeatureList;
import grader.auto_notes.NotesGenerator;
import grader.basics.execution.RunningProject;
import grader.basics.settings.BasicGradingEnvironment;
import grader.basics.util.Option;
import grader.language.LanguageDependencyManager;
import grader.sakai.project.SakaiProject;
import grader.sakai.project.SakaiProjectDatabase;
import grader.spreadsheet.FeatureGradeRecorder;
import grader.spreadsheet.csv.ASakaiCSVFeatureGradeManager;
import grader.spreadsheet.csv.ASakaiCSVFinalGradeManager;
import grader.trace.feature.auto_result_format.FeatureAutoResultFormatSaved;
import grader.trace.feature.score.FeatureScoreSaved;
import grader.trace.multiplier.MultiplierAutoChange;
import grader.trace.multiplier.MultiplierLoaded;
import grader.trace.source_points.SourcePointsLoaded;
import grader.trace.steppers.AutoAutoGradeSet;
import grader.trace.steppers.AutoVisitFailedException;
import grader.trace.steppers.FeaturesAutoGraded;
import grader.trace.steppers.ProjectRun;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

import util.annotations.ComponentWidth;
import util.annotations.Row;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
import util.introspect.ClassLoaderFactory;
import wrappers.framework.project.ProjectWrapper;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;

@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class AnAutoVisitBehavior implements
        AutoVisitBehavior {

    public static int PAUSE_AFTER_RUN = 3000;
    PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);

    String COMMENTS_FILE_PREFIX = "Comments";
    ;
	SakaiProjectDatabase projectDatabase;

	// ideally this stuff should really be done through property change as
    // Josh's wrapper does
    FeatureGradeRecorder featureGradeRecorder;

    SakaiProject project;
    OverviewProjectStepper projectStepper;
    grader.basics.project.Project wrappedProject;

    List<CheckResult> featureResults;
    List<CheckResult> restrictionResults;
    StudentFolder studentFolder;
    boolean autoRunDeferred = false;

    public AnAutoVisitBehavior() {

    }

    public void setProjectDatabase(SakaiProjectDatabase aProjectDatabase) {
        projectDatabase = aProjectDatabase;
        projectStepper = (OverviewProjectStepper) aProjectDatabase.getProjectStepper();
        featureGradeRecorder = aProjectDatabase.getFeatureGradeRecorder();

    }

    boolean runExecuted;
   
    @Override
    public boolean runAttempted() {
        return runExecuted || isAutoRun() || isAutoAutoGrade();
    }
    @Override
    public StudentFolder getStudentFolder() {
    	return studentFolder;
    }
    String getCommentsFileName(SakaiProject aProject) {
        return AGradingFeature.getFeedbackFolderName(aProject)
                + COMMENTS_FILE_PREFIX + AGradingFeature.FEEDBACK_FILE_SUFFIX;

    }

//	String readComments(SakaiProject aProject) {
//		try {
//			return FileUtils.readFileToString(new File(
//					getCommentsFileName(aProject)));
//		} catch (IOException e) {
//			return "";
//		}
//	}
    boolean autoRun = false;

    public boolean isAutoRun() {
        return autoRun;

    }

    public void setAutoRun(boolean newVal) {
        autoRun = newVal;

    }

    public void autoRun() {
        autoRun = !autoRun;
    }

    boolean autoAutoGrade = false; // should we automatically do all the auto
    // grade

    public boolean isAutoAutoGrade() {
        return autoAutoGrade;

    }

    public void setAutoAutoGrade(boolean newVal) {
        boolean oldVal = autoAutoGrade;
        autoAutoGrade = newVal;
        propertyChangeSupport.firePropertyChange("autoAutoGrade", autoAutoGrade, projectStepper);
        AutoAutoGradeSet.newCase(projectDatabase, projectStepper, project, autoAutoGrade, this);

    }

    public void autoAutoGrade() {
        autoAutoGrade = !autoAutoGrade;
    }

    @Visible(false)
    public SakaiProject getProject() {
        return project;
    }

    boolean isNotRunnable() {
//        return LanguageDependencyManager.isJava() && (project.getClassLoader() == null || project.getClassesManager() == null);
        return LanguageDependencyManager.isJava() && (project.getClassLoader() == null) ; // let us not evaluate classes manager


    }

	// Josh: We want to know when a project is set, so I'm adding the project
    // property change event here.
    @Visible(false)
    public boolean setProject(SakaiProject newVal) {
        settingUpProject = true;
        propertyChangeSupport.firePropertyChange(OEFrame.SUPPRESS_NOTIFICATION_PROCESSING, false, true);

        runExecuted = false;
        boolean notRunnable = false;
        project = newVal;
        if (preTerminate()) {
        	terminate();
        }
//		if (project.getClassLoader() == null || project.getClassesManager() == null ) {
        if (isNotRunnable()) {

            AutoVisitFailedException.newCase("Not running or autograding project as no binary folder or classes found for" + projectStepper.getOnyen(), this);
            notRunnable = true;

//			projectStepper.setScore(0);
//			projectStepper.setMultiplier(0);
            //			Tracer.error("Not running or autograding project as no binary folder or classes found for" + projectStepper.getOnyen());
            if (AGradedProjectNavigator.doNotVisitNullProjects) {
                return false;
            }

        }

        try {
            if (project.isNoProjectFolder()) {
                return false;
            }
            wrappedProject = new ProjectWrapper(project, BasicGradingEnvironment
                    .get().getAssignmentName());
            ClassLoaderFactory.setCurrentClassLoader(
            		wrappedProject.getClassesManager().get().getClassLoader());
            studentFolder = ProjectWrapper.getStudentFolder(projectStepper.getOnyen());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false; // go to next project
        }

        if (isAutoRun() && 
        		(!projectStepper.getGradingFeatures().isAllAutoGraded() || projectStepper.getScore() <= 0)) {
//			projectDatabase.runProject(projectStepper.getOnyen(), project);
            if (frame == null) {
                autoRunDeferred = true;
            } else {
                run();
//				ThreadSupport.sleep(PAUSE_AFTER_RUN);
            }
        }

//		if (isAutoAutoGrade() && !projectStepper.getGradingFeatures().isAllAutoGraded()) {
        if (isAutoAutoGrade() && !projectStepper.getGradingFeatures().isSomeAutoGraded()) { // auto attempt was made

            autoGrade();

        } else {

            // in case the multiplier is to be used for navigation filter then we need to load it in the set project in project overviewer
            double aMultiplier = featureGradeRecorder.getEarlyLatePoints(projectStepper.getName(),
                    projectStepper.getOnyen());

            if (aMultiplier == ASakaiCSVFeatureGradeManager.DEFAULT_VALUE) {
                aMultiplier = 1;
            }
            projectStepper.internalSetMultiplier(aMultiplier);
            //have a method for getting the file name from featureGradeRecorder
            MultiplierLoaded.newCase(projectDatabase, projectStepper, project, featureGradeRecorder.getFileName(), aMultiplier, this);

			// not sure we need the next step, perhaps to let the other loggers to know this information
            // problem is that only one unparsers is a parser, so we transmit info from the unique parser
            // to all unparsers
            // internalSetMultiplier is doing the saving
//			featureGradeRecorder.setEarlyLatePoints(projectStepper.getName(), projectStepper.getOnyen(),
//					aMultiplier);
			//
            // cannot be used for filtering
            double savedSourcePoints = featureGradeRecorder.getSourcePoints(projectStepper.getName(), projectStepper.getOnyen());
            if (savedSourcePoints != ASakaiCSVFinalGradeManager.DEFAULT_VALUE) {
                projectStepper.internalSetSourcePoints(savedSourcePoints);
                SourcePointsLoaded.newCase(projectDatabase, projectStepper, project, featureGradeRecorder.getFileName(), savedSourcePoints, this);
            }

            projectStepper.setStoredFeedback();

            projectStepper.setStoredOutput();
        }
		// featureGradeRecorder.setEarlyLatePoints(name, onyen,
        // gradePercentage);

//		if (projectStepper.getSelectedGradingFeature() != null) {
//			projectStepper.internalSetNotes(getNotes(projectStepper.getSelectedGradingFeature()));
////			internalSetResult(getSavedResult(selectedGradingFeature));  // could  use cached result in selected feature
//			projectStepper.internalSetResult(projectStepper.getSelectedGradingFeature().getResult());  
//
//		} else {
//			projectStepper.internalSetNotes("");
//			projectStepper.internalSetAutoNotes("");
//		}
//
//		projectStepper.internalSetComments(readComments(project));
//		
//		
//		Icon studentPhoto = projectDatabase.getStudentPhoto(projectStepper.getOnyen(), project);
//				projectDatabase.getPhotoReader().getIcon(onyen);
//		photoLabelBeanModel.setIcon(studentPhoto);
//		if (studentPhoto != null){
//			projectStepper.getPhoto().set("", studentPhoto);
//		} else {
//			projectStepper.getPhoto().set(APhotoReader.NO_PHOTO_TITLE, studentPhoto);
//		}
//		settingUpProject = false;
////		setScore();
//		projectStepper.setColors();
////		if (!shouldVisit()) {
////			return false;
////		}
//		
//		propertyChangeSupport.firePropertyChange(OEFrame.SUPPRESS_NOTIFICATION_PROCESSING, true, false);
//		boolean changed = setCurrentColors();
//		if (changed)
//			displayColors();
        return true;
    }

//	boolean shouldVisit() {
//		if (manualOnyen)
//			return true;
//		GraderSettingsModel graderSettingsModel = projectDatabase.getGraderSettings();
//		if (graderSettingsModel == null) return true;
//		if (graderSettingsModel.getNavigationSetter().getNavigationKind() != NavigationKind.MANUAL)
//			return true;
//		BasicNavigationFilter navigationFilter = projectDatabase.getNavigationFilter();
//		return navigationFilter.includeProject(this, projectDatabase);
//	}
//	void refreshColors() {
//		// no incremental updates as score and other properties change during auto grade
//		if (settingUpProject) 
//		    return;
//		boolean changed = setCurrentColors();
//		if (changed)
//			displayColors();
//		
//	}
//	
//	void setGradingFeatureColor(int index) {
//		if (settingUpProject) return;
//
//		nextColors.set(index, projectDatabase.getGradingFeatureColorer().color(projectDatabase.getGradingFeatures().get(index)) );
//
//		if (currentColors.get(index) == nextColors.get(index)) return;
//		setColor ( "GradingFeatures." + index, nextColors.get(index));
//		currentColors.set(index, nextColors.get(index));		
//	}
//	void setColor(String aPath, Color aColor) {
//		propertyChangeSupport.firePropertyChange(aPath, null, 
//				new Attribute(AttributeNames.CONTAINER_BACKGROUND, aColor));
//	}
//	public void computeNextColors() {
////		List<Color> colors = new ArrayList();
//		int i = 0;
//		for (GradingFeature aGradingFeature:projectDatabase.getGradingFeatures()) {
//			nextColors.set(i, projectDatabase.getGradingFeatureColorer().color(aGradingFeature) );
//			i++;
//		}
//		nextMultiplierColor = projectDatabase.getMultiplierColorer().color(multiplier);
//		nextScoreColor = projectDatabase.getScoreColorer().color(score);
//		nextOverallNotesColor = projectDatabase.getOverallNotesColorer().color(overallNotes);
//		
//	}
//	void setMultiplierColor() {
//		if (settingUpProject) return;
//		nextMultiplierColor = projectDatabase.getMultiplierColorer().color(multiplier);
//		if (currentMultiplierColor == nextMultiplierColor ) return;
//		setColor("Multiplier",  nextMultiplierColor);
//		currentMultiplierColor = nextMultiplierColor;
//
//	}
//	void setScoreColor() {
//		if (settingUpProject) return;
//		nextScoreColor = projectDatabase.getScoreColorer().color(score);
//		if (currentScoreColor == nextScoreColor ) return;
//		setColor("Score",  nextScoreColor);
//		currentScoreColor = nextScoreColor;
//	}
//	
//	void setOverallNotesColor() {
//		if (settingUpProject) return;
//		nextOverallNotesColor = projectDatabase.getOverallNotesColorer().color(overallNotes);
//		if (currentOverallNotesColor == nextOverallNotesColor ) return;
//		setColor("OverallNotes",  nextOverallNotesColor);
//		currentOverallNotesColor = nextOverallNotesColor;
//	}
	// setCurrentColors and refreshColors should probably be combined
//		boolean setCurrentColors() {
//			 computeNextColors();
//			boolean changed = !currentColors.equals(nextColors) ||
//					(currentMultiplierColor != nextMultiplierColor) ||
//					(currentOverallNotesColor != nextOverallNotesColor) ||
//					(currentScoreColor != nextScoreColor);
//			if (changed) {
//				for (int i=0; i < currentColors.size();i++) {
//					currentColors.set(i, nextColors.get(i));
//				}
//				currentScoreColor = nextScoreColor;
//				currentMultiplierColor = nextMultiplierColor;
//				currentOverallNotesColor = nextOverallNotesColor;
//			}
//			return changed;
//		}
//	void setGradingFeatureColors() {
//		if (settingUpProject) 
//		    return;
////		for (int i = 0; i < gradingObjectAdapters.size(); i++) {
//		for (int i = 0; i < projectDatabase.getGradingFeatures().size(); i++) {
//
////			if (currentColors.get(i) != nextColors.get(i)) {
//				setGradingFeatureColor(i);
////				setGradingFeatureColor(i);
////				currentColors.set(i, nextColors.get(i));				
////			}			
//		}
//		
//	}
//	void setColors() {
//		// no incremental updates as score and other properties change during auto grade
//		if (settingUpProject) 
//		    return;
////		computeNextColors();
//		setGradingFeatureColors();
//		setMultiplierColor();
//		setOverallNotesColor();
//		setScoreColor();
//		
//	}
//	
//	void displayColors() {
//		for (int i = 0; i < gradingObjectAdapters.size(); i++) {
//			ObjectAdapter gradingAdapter =  gradingObjectAdapters.get(i);
//			gradingAdapter.setTempAttributeValue(AttributeNames.CONTAINER_BACKGROUND, currentColors.get(i));
//		}
//		
//		
//		stepperViewAdapter.get("score")
//		.setTempAttributeValue(AttributeNames.COMPONENT_BACKGROUND, currentScoreColor);
//
//		scoreAdapter.setTempAttributeValue(AttributeNames.COMPONENT_BACKGROUND, currentScoreColor);
//		multiplierAdapter.setTempAttributeValue(AttributeNames.COMPONENT_BACKGROUND, currentMultiplierColor);
//		overallNotesAdapter.setTempAttributeValue(AttributeNames.COMPONENT_BACKGROUND, currentOverallNotesColor);
//
//
//		oeFrame.refresh();
////		stepperViewAdapter.refreshAttributes();
//	}
//
//	
//
//	public boolean preOutput() {
//		// return project.canBeRun();
//		return runAttempted() && project.canBeRun();
//
//	}
//
    public boolean preRun() {
//        return project.canBeRun() && !autoRun // && !runExecuted
		return !project.isNoProjectFolder() && project.canBeRun();            
    }
    @Override
	public boolean preTerminate() {
		return runningProject != null && !runningProject.isDestroyed();
	}
    boolean lastTranscriptRecorded;
	@Override
	public void terminate() {
		if (!lastTranscriptRecorded) {
		Checkable anInteractiveFeature = projectDatabase.getProjectRequirements().getInteractiveRunFeature();
		project.setCurrentGradingFeature(anInteractiveFeature);
		lastTranscriptRecorded = true;
	 if (anInteractiveFeature != null) {
		 anInteractiveFeature.check(wrappedProject);
		 runningProject.appendCumulativeOutput();
	 }
		}
		if (!runningProject.isDestroyed()) {
			runningProject.destroy();
		}
	}

//	@Row(3)
//    @ComponentWidth(100)
    public void runInSameVM() {
        if (isNotRunnable()) {
            notRunnableProjectFeedback();
            return;
        }
        runExecuted = true;
        projectDatabase.runProject(projectStepper.getOnyen(), project);
        project.setHasBeenRun(true);
        for (GradingFeature gradingFeature : projectDatabase
                .getGradingFeatures()) {
            if (gradingFeature.isAutoGradable()) {
                gradingFeature.firePropertyChange("this", null, gradingFeature);
            }
        }

    }
    RunningProject runningProject;
	
	public static String[] computeArgs(String subjectString) {
		List<String> matchList = new ArrayList<String>();
		Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
		Matcher regexMatcher = regex.matcher(subjectString);
		while (regexMatcher.find()) {
		    if (regexMatcher.group(1) != null) {
		        // Add double-quoted string without the quotes
		        matchList.add(regexMatcher.group(1));
		    } else if (regexMatcher.group(2) != null) {
		        // Add single-quoted string without the quotes
		        matchList.add(regexMatcher.group(2));
		    } else {
		        // Add unquoted word
		        matchList.add(regexMatcher.group());
		    }
		} 
		String[] result = new String[matchList.size()];
		return matchList.toArray(result);

	}

    public void run() {
		if (preTerminate())
			terminate();
		project.setHasBeenRun(true);
        runExecuted = true;
        lastTranscriptRecorded = false;
//		runExecuted = true;
//		projectDatabase.runProject(getOnyen(), project);
//		// should this go in the code doing the running?
		ProjectRun.newCase(projectDatabase, projectStepper, project, this);
//		project.setHasBeenRun(true);
//		for (GradingFeature gradingFeature : projectDatabase
//				.getGradingFeatures()) {
//			if (gradingFeature.isAutoGradable()) {
//				gradingFeature.firePropertyChange("this", null, gradingFeature);
//			}
//		}
//		project.setCurrentGradingFeature(newVal);
		String anArgString = projectStepper.runArgs().getValue();
		if (anArgString == null) {
			 runningProject = project.getWrapper().launchInteractive();
		} else {
//			 String[] anArgs = new String[]{anArgString};
			String[] anArgs = computeArgs(anArgString);
			 runningProject = project.getWrapper().launchInteractive(anArgs);
		}

//		 String[] anArgs = new String[]{};
//		 runningProject = project.getWrapper().launchInteractive(anArgs);
		 project.setCurrentRunningProject(runningProject);
//			Feature anInteractiveFeature = projectDatabase.getProjectRequirements().getInteractiveRun();
//			project.setCurrentGradingFeature(anInteractiveFeature);
//		 if (anInteractiveFeature != null) {
//			 runningProject.appendCumulativeOutput();
//		 }
		 
		 
		 
//		runningProject.destroy();
//		TimedProcess aProcess = runningProject.getCurrentTimedProcess();
//		aProcess.destroy();
//		runningProject.end();
//		 runningProject = project.getWrapper().launchInteractive();
//		 
//		runningProject.destroy();

		
//        String output = runningProject.await();

	}

    @Override
    public boolean preAutoGrade() {
		// return project.runChecked() && project.canBeRun() &&
        // preGetGradingFeatures();
        return /* project.runChecked() && project.canBeRun() && */ projectStepper.preGetGradingFeatures();

    }
    boolean settingUpProject;

    void notRunnableProjectFeedback() {

        projectStepper.internalSetScore(0);
        NotesGenerator notesGenerator = projectDatabase.getNotesGenerator();

        String newNotes = notesGenerator.missingProjectNotes(projectStepper);
        projectStepper.setOverallNotes(notesGenerator.appendNotes(
                projectStepper.getOverallNotes(),
                newNotes));

    }

    //

    @Row(8)
    @ComponentWidth(100)
    @Override
    public void autoGrade() {
//		project.setHasBeenRun(true);
        projectStepper.setChanged(true);
        if (isNotRunnable()) {
            notRunnableProjectFeedback();
            for (GradingFeature gradingFeature : projectDatabase
                    .getGradingFeatures()) {
                if (gradingFeature.isAutoGradable()) {
                    double score = 0;
                    gradingFeature.internalSetScore(score);
                    featureGradeRecorder.setGrade(projectStepper.getName(), projectStepper.getOnyen(), gradingFeature.getFeatureName(), score);
                }
            }
//			for (GradingFeature gradingFeature : projectDatabase
//					.getGradingFeatures()) {
//				if (gradingFeature.isAutoGradable()) {
//					gradingFeature.internalSetScore(0);
//				}
//			}
//			return;
        } else {

            // we may have compile errors in output, so do not clear it
//		project.clearOutput();
            for (GradingFeature gradingFeature : projectDatabase
                    .getGradingFeatures()) {
                if (gradingFeature.isAutoGradable()) {
                    gradingFeature.pureSetGraded(true);
                }
            }

//		if (!isNotRunnable()) {
            ProjectRequirements aProjectRequirements = projectDatabase.getProjectRequirements();
            System.out.println ("Project requirements:" + aProjectRequirements);
//            System.out.println("SLEEPING");
//            try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
            featureResults = aProjectRequirements
                    .checkFeatures(wrappedProject); // this where we will need to fork a process if required in configuration

            restrictionResults = aProjectRequirements
                    .checkRestrictions(wrappedProject);
//            featureResults = projectDatabase.getProjectRequirements()
//                    .checkFeatures(wrappedProject);
//
//            restrictionResults = projectDatabase.getProjectRequirements()
//                    .checkRestrictions(wrappedProject);

            FeaturesAutoGraded.newCase(projectDatabase, projectStepper, project, this);
//		}
            GradingFeatureList features = projectDatabase.getGradingFeatures();
//		projectStepper.setComputedScore(); // will trigger change occurred
            for (int i = 0; i < features.size(); i++) {
                // Figure out the score for the feature/restriction
                double score = (i < featureResults.size()) ? featureResults.get(i)
                        .getScore() : restrictionResults.get(
                                i - featureResults.size()).getScore();

			// Save the comments. We save them in the ConglomerateRecorder so
                // that, if it is being used as the
                // manual feedback, they will be pulled in.
			// correcting josh's code to separate feature comments and results
                // featureGradeRecorder.setFeatureComments(featureResults.get(i).getNotes());
                // featureGradeRecorder.setFeatureResults(featureResults.get(i).getResults());
			//not sure why we are doing the following two steps as previous feature data are overridden by the next one
                // a bit of investigation tells me these steps are useless but leaving the code around
                String notes = (i < featureResults.size()) ? featureResults
                        .get(i).getNotes() : restrictionResults.get(
                                i - featureResults.size()).getNotes();
//			featureGradeRecorder
//					.setFeatureComments((i < featureResults.size()) ? featureResults
//							.get(i).getNotes() : restrictionResults.get(
//							i - featureResults.size()).getNotes());

                featureGradeRecorder
                        .setFeatureComments(notes);

                // this is a useful step
                if (!notes.isEmpty()) {
                    features.get(i).setManualNotes(notes);
                }

                featureGradeRecorder
                        .setFeatureResults((i < featureResults.size()) ? featureResults
                                        .get(i).getResults() : restrictionResults.get(
                                                i - featureResults.size()).getResults());
			// do not reset notes read from files
//			features.get(i).setNotes(
//					(i < featureResults.size()) ? featureResults.get(i)
//							.getNotes() : restrictionResults.get(
//							i - featureResults.size()).getNotes());

                String resultFormat = (i < featureResults.size()) ? featureResults.get(i)
                        .getTarget().getSummary() : restrictionResults
                        .get(i - featureResults.size()).getTarget()
                        .getSummary();
//			// in memory save
                features.get(i).setResultFormat(resultFormat);
                String autoNotes = (i < featureResults.size()) ? featureResults.get(i).getAutoNotes() : restrictionResults
                        .get(i - featureResults.size()).getAutoNotes();
//			// in memory save
                // in memory save
                features.get(i).setAutoNotes(autoNotes);

                // save to the excel file so we can read it later
                featureGradeRecorder.setResultFormat(projectStepper.getName(), projectStepper.getOnyen(), features.get(i).getFeatureName(),
                        resultFormat);
                FeatureAutoResultFormatSaved.newCase(projectDatabase, projectStepper, project, features.get(i), featureGradeRecorder.getFileName(), resultFormat, this);
//			features.get(i).setScore(score);
                if (!features.get(i).isManual()) {
                    features.get(i).internalSetScore(score);

                    List<CheckResult> results = new ArrayList<>(featureResults.size() + restrictionResults.size());
                    results.addAll(featureResults);
                    results.addAll(restrictionResults);
                    
                    // Save the score
                    featureGradeRecorder.setGrade(projectStepper.getName(), projectStepper.getOnyen(), features.get(i)
                            .getFeatureName(), score, results);
                    FeatureScoreSaved.newCase(projectDatabase, projectStepper, project, features.get(i), featureGradeRecorder.getFileName(), score, this);
                }

            }
        }
        projectStepper.setComputedScore(); // will trigger change occurred

        projectStepper.setComputedFeedback();
        projectStepper.setStoredOutput();
		// Josh's code from ProjectStepperDisplayerWrapper
        // Figure out the late penalty
        Option<DateTime> timestamp = studentFolder.getTimestamp();
				// double gradePercentage = timestamp.isDefined() ?
        // projectDatabase.getProjectRequirements().checkDueDate(timestamp.get())
        // : 0;
        System.out.println("time stamp defined:" + timestamp.get());
        double aMultiplier = timestamp.isDefined()
                ? projectDatabase.getProjectRequirements().checkDueDate(wrappedProject, timestamp.get())
                : 0;
        MultiplierAutoChange.newCase(projectDatabase, projectStepper, project, projectStepper.getScore(), this);

        projectStepper.internalSetMultiplier(aMultiplier);

//		featureGradeRecorder.setEarlyLatePoints(name, onyen, aMultiplier);
        // setSummary();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener aListener) {
        propertyChangeSupport.addPropertyChangeListener(aListener);
    }

    CheckResult checkableToResult(Checkable aCheckable) {
        try {
            for (CheckResult checkResult : featureResults) {
                if (checkResult.getTarget() == aCheckable) {
                    return checkResult;
                }
            }

            for (CheckResult checkResult : restrictionResults) {
                if (checkResult.getTarget() == aCheckable) {
                    return checkResult;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;

    }

    CheckResult gradingFeatureToCheckResult(GradingFeature aGradingFeature) {
        Checkable checkable = projectDatabase.getRequirement(aGradingFeature);
        if (checkable != null) {
            return checkableToResult(checkable);
        }
        return null;
    }

    String getSavedResult(GradingFeature aGradingFeature) {
        return featureGradeRecorder.getResult(projectStepper.getName(), projectStepper.getOnyen(), aGradingFeature.getFeatureName());
    }

//	String getInMemoryResult(GradingFeature aGradingFeature) {
//		return aGradingFeature.getAutoNotes();
////		CheckResult checkResult = gradingFeatureToCheckResult(aGradingFeature);
////		if (checkResult != null) {
////			return checkResult.getMessage();
////		}
////
////		return "";
//	}
//	void setNotes(GradingFeature aGradingFeature, String aNotes) {
//		featureGradeRecorder.setFeatureComments(aNotes);
//		featureGradeRecorder.comment(aGradingFeature);
//		aGradingFeature.setNotes(aNotes);
//		// CheckResult checkResult =
//		// gradingFeatureToCheckResult(aGradingFeature);
//		// if (checkResult != null) {
//		// checkResult.setNotes(aNotes);
//		// }
//
//	}
    String getNotes(GradingFeature aGradingFeature) {
        String retVal = aGradingFeature.getManualNotes();
		// CheckResult checkResult =
        // gradingFeatureToCheckResult(aGradingFeature);
        // if (checkResult != null) {
        // return checkResult.getNotes(); }
        if (retVal == null) {
            retVal = "";
        }

        return retVal;
    }

//	void refreshSelectedFeature() {
//		if (selectedGradingFeature != null)
//		manualNotes = getNotes(selectedGradingFeature);
//	}
//	
//	void setSelectedFeature (GradingFeature gradingFeature) {
//		
//			manualNotes = getNotes(gradingFeature);
//			autoNotes = getInMemoryResult(gradingFeature);
//			// log = gradingFeature.getFeature();
//			selectedGradingFeature = gradingFeature;
//			output = selectedGradingFeature.getOutput();
//			unSelectOtherGradingFeatures(gradingFeature);
//		
//	}
//	@Override
//	public void propertyChange(PropertyChangeEvent evt) {
//		if (evt.getSource() instanceof GradingFeature
//				&& evt.getPropertyName().equalsIgnoreCase("score")) {
//			GradingFeature aGradingFeature = (GradingFeature) evt.getSource();
//			// setInternalScore(aGradingFeature.getScore());
//			if (!settingUpProject)
//			setComputedScore();
//			setGrade(aGradingFeature.getFeature(), aGradingFeature.getScore());
////			setSelectedFeature(aGradingFeature);// auto select
//			if (!settingUpProject) {
//			changed = true;
//			setComputedFeedback();
//			setGradingFeatureColors();
//			aGradingFeature.setSelected(true); 
//			
//			}
//
//			
//		} else if (evt.getSource() instanceof GradingFeature
//				&& evt.getPropertyName().equalsIgnoreCase("selected") && !settingUpProject) {
//			GradingFeature gradingFeature = (GradingFeature) evt.getSource();
//			if ((Boolean) evt.getNewValue()) {
//				setSelectedFeature(gradingFeature);
////				manualNotes = getNotes(gradingFeature);
////				autoNotes = getInMemoryResult(gradingFeature);
////				// log = gradingFeature.getFeature();
////				selectedGradingFeature = gradingFeature;
////				output = selectedGradingFeature.getOutput();
////				unSelectOtherGradingFeatures(gradingFeature);
//			} else {
//				// this may be a bounced feature
//				// selectedGradingFeature = null;
//				// unSelectOtherGradingFeatures(null);
//				// setNotes("");
//			}
//
//		} else if (evt.getSource() == this) {
//			// will get even name and onyen changes - let us focus on the changes that really need to be saved in the setters
//			if (!settingUpProject)
//
//			changed = true;
//			return; // do not want to execute the statement below as it  will cause infinite recursion
//			
//		} else if (evt.getSource() == getNavigationSetter().getNavigationFilterSetter()) {
//			noNextFilteredRecords = false;
//			noPreviousFilteredRecords = false;
//		}
//		if (!settingUpProject) { 
//		refreshSelectedFeature(); // we know a user action occurred
//	
//		propertyChangeSupport.firePropertyChange("this", null, this); // an
//																		// event
//																		// from
//																		// grading
//																		// features,
//																		// perhaps
//																		// our
//																		// precondition
//																		// chnaged
//																		// such
//																		// as
//																		// auoGraded
//		}
//
//	}
	// List<OEFrame> newList = new ArrayList( uiFrameList.getList());
	//
    // for (OEFrame frame:newList) {
    // if (oldList.contains(frame))
    // continue;
    // frame.dispose(); // will this work
    // }
    // Window[] newWindows = Window.getWindows();
    //
    //
    // for (Window frame:newWindows) {
    // if (Common.containsReference(oldWindows, frame)) {
    // continue;
    // }
    // frame.dispose();
    // }
    @Override
    @Visible(false)
    public SakaiProjectDatabase getProjectDatabase() {
        // TODO Auto-generated method stub
        return projectDatabase;
    }

    List<String> onyens;
    int currentOnyenIndex = 0;
    int filteredOnyenIndex = 0;
    String nextOnyen;

//	@Override
//	public void configureNavigationList() {
//		onyens = projectDatabase.getOnyenNavigationList();
//		currentOnyenIndex = 0;
//		hasMoreSteps = true;
//	}
//
//	@Override
//	public boolean preRunProjectsInteractively() {
//		return onyens != null && currentOnyenIndex < onyens.size();
//	}
//	
//	void setObjectAdapters() {
//		if (oeFrame == null) return;
//		for (GradingFeature gradingFeature:projectDatabase.getGradingFeatures()) {
//			ObjectAdapter featureAdapter = oeFrame.getObjectAdapter(gradingFeature);	
////			featureAdapter.setTempAttributeValue(AttributeNames.CONTAINER_BACKGROUND, Color.PINK);
////			featureAdapter.refresh();
//			gradingObjectAdapters.add(featureAdapter);
//		}
//		gradingFeaturesAdapter = oeFrame.getObjectAdapter(projectDatabase.getGradingFeatures());
//		if (gradingFeaturesAdapter == null) return;
//		stepperViewAdapter = (ClassAdapter) gradingFeaturesAdapter.getParentAdapter(); // this might be a delegator
//		scoreAdapter = stepperViewAdapter.get("score");
//		multiplierAdapter = stepperViewAdapter.get("multiplier");
//		overallNotesAdapter = stepperViewAdapter.get("overallNotes");
//		
//
//	}
//	public boolean runProjectsInteractively() {
//	
//			try {
//				return runProjectsInteractively("");
//			} catch (MissingOnyenException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace(); // this cannot happen
//				return false;
//			}
//	}
//	
//
//	@Override
//	public boolean runProjectsInteractively(String aGoToOnyen) throws MissingOnyenException {
//		
//		if (!preRunProjectsInteractively()) {
//			Tracer.error("Projects not configured");
//			hasMoreSteps = false;
//			return false;
//		}
//		
//		String anOnyen = aGoToOnyen;
//		if (aGoToOnyen.isEmpty()) {
//		
//			anOnyen= onyens.get(currentOnyenIndex);
//		} else {
//			currentOnyenIndex = onyens.indexOf(anOnyen);
//			if (currentOnyenIndex == -1) {
//				throw new MissingOnyenException(anOnyen);
//			}
//		}
//		SakaiProject aProject = projectDatabase.getProject(anOnyen);
//		projectDatabase.initIO();
//
//		projectDatabase.recordWindows();
//		featureGradeRecorder.setGradingFeatures(projectDatabase
//				.getGradingFeatures());
//		for (int i = 0; i < projectDatabase.getGradingFeatures().size(); i++) {
//			currentColors.add(null);
//			nextColors.add(null);
//		}
//		setObjectAdapters();
//		boolean retVal = setProject(anOnyen);
//		if (!retVal) {
//			 next();
//			 if (!hasMoreSteps) {
//				 currentOnyenIndex = filteredOnyenIndex;
//				 return false;
//			 }
//		}
//		filteredOnyenIndex = currentOnyenIndex;
//		return true;
//		
//
//	}
//	
//
//	@Override
//	public boolean preDone() {
//		// return preProceed();
//		// we are done but do not have another step
//		return preProceed() && !preNext();
//	}
//	void maybeSaveState() {
//		// josh's code
//				// no serialization otherwise
//				if (changed || 
//						!featureGradeRecorder.logSaved()) 
//				featureGradeRecorder.finish();
//
//				// my original code
//				projectDatabase.resetIO();
//				projectDatabase.clearWindows();
//	}
//	void redirectProject() {
//		projectDatabase.initIO();
//		projectDatabase.recordWindows();
//	}
//	
//	void setFailedMoveFlags(boolean forward) {
//		if (forward)
//			noNextFilteredRecords = true;
//		else
//			noPreviousFilteredRecords = true;
//	}
//	
//	void setSuccessfulMoveFlags(boolean forward) {
//		if (forward)
//			noNextFilteredRecords = false;
//		else
//			noPreviousFilteredRecords = false;
//	}
//	
//	
//
//	@Override
//	public synchronized boolean move(boolean forward) {
//		maybeSaveState();
////		// josh's code
////		// no serialization otherwise
////		if (changed || 
////				!featureGradeRecorder.logSaved()) 
////		featureGradeRecorder.finish();
////
////		// my original code
////		projectDatabase.resetIO();
////		projectDatabase.clearWindows();
//		if (forward) {
//			currentOnyenIndex++;
//
//			if (currentOnyenIndex >= onyens.size()) {
//				hasMoreSteps = false;
//				return false;
//			}
//		} else {
//			currentOnyenIndex--;
//			if (currentOnyenIndex < 0) {
//				hasMoreSteps = false;
//				return false;
//			}
//
//		}
//		redirectProject();
//		String anOnyen = onyens.get(currentOnyenIndex);
//		SakaiProject aProject = projectDatabase.getProject(anOnyen);
////		redirectProject();
////		projectDatabase.initIO();
////		projectDatabase.recordWindows();
//		boolean projectSet = setProject(anOnyen);
//		if (!projectSet) {
//			boolean retVal = move(forward);
//			if (!retVal && filteredOnyenIndex != currentOnyenIndex) {
//				currentOnyenIndex = filteredOnyenIndex;
//				try {
//					internalSetOnyen(onyens.get(filteredOnyenIndex));
//				} catch (MissingOnyenException e) {
//					e.printStackTrace(); // this should never be executed
//				}
//				Tracer.error("Cannot move as no more records that satisfy selection condition");
//				setFailedMoveFlags(forward);
//			} else {
//				filteredOnyenIndex = currentOnyenIndex;
//				setSuccessfulMoveFlags(forward);
//			}
//			return retVal;
//		}
//		filteredOnyenIndex = currentOnyenIndex;
//		return true;
//			
//		// these two steps should go into setProject unless there is something
//		// subttle here, specially as the stepProject step below is commented
//		// put
//		// if (isAutoRun())
//		// projectDatabase.runProject(anOnyen, aProject);
//		// if (isAutoAutoGrade())
//		// autoGrade();
//
//		// setProject(anOnyen);
//	}
//
//	@Override
//	@Row(11)
//	@ComponentWidth(100)
//	public synchronized void done() {
//
//		if (manualOnyen)
//			writeScores(this);
//		try {
//			// Common.appendText(logFile, onyen + " Skipped " +
//			// Common.currentTimeAsDate() + "\n\r");
//			Common.appendText(gradedFile, onyen + "\n");
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		move(true);
//	
//
//	}
//
    Object frame;

    @Visible(false)
    @Override
    public void setFrame(Object aFrame) {
        frame = aFrame;
        if (autoRunDeferred) {
            run();
            autoRunDeferred = false;
        }

    }
//
//	@Visible(false)
//	@Override
//	public Object getFrame() {
//		return frame;
//	}
//	
//	
//	public void computeNextColors() {
////		List<Color> colors = new ArrayList();
//		int i = 0;
//		for (GradingFeature aGradingFeature:projectDatabase.getGradingFeatures()) {
//			nextColors.set(i, projectDatabase.getGradingFeatureColorer().color(aGradingFeature) );
//			i++;
//		}
//		nextMultiplierColor = projectDatabase.getMultiplierColorer().color(multiplier);
//		nextScoreColor = projectDatabase.getScoreColorer().color(score);
//		nextOverallNotesColor = projectDatabase.getOverallNotesColorer().color(overallNotes);
//		
//	}

    public static void main(String[] args) {
        ObjectEditor.edit(new AnAutoVisitBehavior());
    }

}
