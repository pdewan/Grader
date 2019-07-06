package framework.logging.loggers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import framework.grading.testing.CheckResult;
import framework.logging.recorder.RecordingSession;
import framework.logging.serializers.SerializationUtils;
import framework.navigation.NotValidDownloadFolderException;
import framework.navigation.SakaiBulkDownloadFolder;
import framework.utils.GraderSettings;
import grader.sakai.project.ASakaiProjectDatabase;
import grader.sakai.project.SakaiProjectDatabase;
import grader.spreadsheet.FinalGradeRecorder;
import grader.spreadsheet.FinalGradeRecorderSelector;
import grader.spreadsheet.TotalScoreRecorderSelector;
import grader.spreadsheet.csv.ASakaiCSVFeatureGradeManager;
// this is logging both the feedback and final grade to through my Final grade recorder, which seems to be adding an extra line
public class CsvLogger implements Logger {

	@Override
    public void save(RecordingSession recordingSession) {
		 if (recordingSession.getGradingFeatures() == null) {
	        	 resultsBasedSave(recordingSession);        	
	        } else {
	        	 featuresBasedSave(recordingSession);
	        }
	}
	
//	public String logFileName(RecordingSession recordingSession) {
//		return GraderSettings.get().get("path") + "/" + recordingSession.getUserId() + "/Feedback Attachment(s)/feedback.txt";
//	}
//	
	public String logFileName(String aUserId) {
		File file = logFile(aUserId);
		if (file != null && file.exists()) {
			return file.getAbsolutePath();
		}
		return null;
	}
	
	 File logFile(String aUserId) {
		SakaiBulkDownloadFolder sakaiFolder;
		try {
			sakaiFolder = new SakaiBulkDownloadFolder(GraderSettings.get().get("path"));
			File file = sakaiFolder.getGrades();
			return file; 
		} catch (NotValidDownloadFolderException e) {
			return null;
		}
		
	}
	
     void resultsBasedSave(RecordingSession recordingSession) {
        String text = SerializationUtils.getSerializer("text").serialize(recordingSession);

        // Maybe write this to a file
        try {
			SakaiBulkDownloadFolder sakaiFolder = new SakaiBulkDownloadFolder(GraderSettings.get().get("path"));
			File file = sakaiFolder.getGrades();
			
			//Get the onyen
			String id = recordingSession.getUserId();
			String onyen = id.substring(id.lastIndexOf('(')+1, id.lastIndexOf(')'));
			
			//Get the rawScore
			double rawScore = 0;
			for (CheckResult result : recordingSession.getFeatureResults()) {
				rawScore += result.getScore();
			}
			for (CheckResult result : recordingSession.getRestrictionResults()) {
				rawScore += result.getScore();
			}
			
			List<String> lines = FileUtils.readLines(file);
			FileWriter writer = new FileWriter(file);			
			for(String line : lines) {
				if (line.startsWith(onyen + ",")) {
					String[] csvParts = line.split(",");
					for(int i=0; i<4; i++) {
						writer.write(csvParts[i]+",");
					}
//					writer.write("" + (rawScore * recordingSession.getLatePenalty()));
					double total = ASakaiCSVFeatureGradeManager.getTotalGrade(rawScore, recordingSession.getLatePenalty(), recordingSession.getSourcePoints());
					writer.write("" + total);

					writer.write("\n");
				}else{
					writer.write(line+"\n");
				}
			}
			writer.close();
			
		} catch (NotValidDownloadFolderException e) {
			System.err.println("Error saving csv log");
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println("Error saving csv log");
			System.err.println(e.getMessage());
		}
        File file = new File(GraderSettings.get().get("path") + "/" + recordingSession.getUserId() + "/Feedback Attachment(s)/feedback.txt");
//        File file = new File(logFileName(recordingSession));

        try {
            FileUtils.writeStringToFile(file, text);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
     void featuresBasedSaveJosh(RecordingSession recordingSession) {
         String text = SerializationUtils.getSerializer("text").serialize(recordingSession);

         // Maybe write this to a file
         try {
 			SakaiBulkDownloadFolder sakaiFolder = new SakaiBulkDownloadFolder(GraderSettings.get().get("path"));
 			File file = sakaiFolder.getGrades();
 			
 			//Get the onyen
 			String id = recordingSession.getUserId();
 			String onyen = id.substring(id.lastIndexOf('(')+1, id.lastIndexOf(')'));
 			
 			//Get the rawScore
// 			double rawScore = 0;
 			
 			
// 	         for (GradingFeature gradingFeature:recordingSession.getGradingFeatures()) {
// 	        	rawScore += gradingFeature.getScore();
// 	            
// 	         }
 			double rawScore = recordingSession.getScore();
 			
 			List<String> lines = FileUtils.readLines(file);
 			FileWriter writer = new FileWriter(file);			
 			for(String line : lines) {
 				if (line.startsWith("\""+onyen + "\",")) {
 					String[] csvParts = line.split(",");
 					for(int i=0; i<4; i++) {
 						writer.write(csvParts[i]+",");
 					}
// 					writer.write("" + (rawScore * recordingSession.getLatePenalty()));
 					double total = ASakaiCSVFeatureGradeManager.getTotalGrade(rawScore, recordingSession.getLatePenalty(), recordingSession.getSourcePoints());
					writer.write("" + total);

 					writer.write("\n");
 				}else{
 					writer.write(line+"\n");
 				}
 			}
 			writer.close();
 			
 		} catch (NotValidDownloadFolderException e) {
 			System.err.println("Error saving csv log");
 			System.err.println(e.getMessage());
 		} catch (IOException e) {
 			System.err.println("Error saving csv log");
 			System.err.println(e.getMessage());
 		}
         File file = new File(GraderSettings.get().get("path") + "/" + recordingSession.getUserId() + "/Feedback Attachment(s)/feedback.txt");
//         File file = new File(logFileName(recordingSession));
         try {
             FileUtils.writeStringToFile(file, text);
         } catch (IOException e) {
             e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }
     }
// this is not correct, should really look at specific entry 
	@Override
	public boolean isSaved(String aUserId) {
        File file = logFile(aUserId);
        return file != null && file.exists();
	}
	/*
	 * Writes the csv file and feedcback text it seems
	 */
	void featuresBasedSave(RecordingSession recordingSession) {
        String text = SerializationUtils.getSerializer("text").serialize(recordingSession);

        // Maybe write this to a file
       
        	SakaiProjectDatabase aSakaiProjectDatabase = ASakaiProjectDatabase.getCurrentSakaiProjectDatabase();
//			FinalGradeRecorder aRecorder = TotalScoreRecorderSelector.getFactory().getOrCreateFinalGradeRecorder(aSakaiProjectDatabase);
			FinalGradeRecorder aRecorder = FinalGradeRecorderSelector.getOrCreateFinalGradeRecorder();

			
			//Get the onyen
			String id = recordingSession.getUserId();
			String onyen = id.substring(id.lastIndexOf('(')+1, id.lastIndexOf(')'));
			
			//Get the rawScore
//			double rawScore = 0;
			
			
//	         for (GradingFeature gradingFeature:recordingSession.getGradingFeatures()) {
//	        	rawScore += gradingFeature.getScore();
//	            
//	         }
			double rawScore = recordingSession.getScore();
			System.out.println ("CSV logger raw score:" + rawScore);
			System.out.println("Late Penalty/Multiplier:" + recordingSession.getLatePenalty());
			double total = ASakaiCSVFeatureGradeManager.getTotalGrade(rawScore, recordingSession.getLatePenalty(), recordingSession.getSourcePoints());

			aRecorder.setGrade(id, onyen, total);
//			
//			List<String> lines = FileUtils.readLines(file);
//			FileWriter writer = new FileWriter(file);			
//			for(String line : lines) {
//				if (line.startsWith("\""+onyen + "\",")) {
//					String[] csvParts = line.split(",");
//					for(int i=0; i<4; i++) {
//						writer.write(csvParts[i]+",");
//					}
////					writer.write("" + (rawScore * recordingSession.getLatePenalty()));
//					double total = ASakaiCSVFeatureGradeManager.getTotalGrade(rawScore, recordingSession.getLatePenalty(), recordingSession.getSourcePoints());
//					writer.write("" + total);
//
//					writer.write("\n");
//				}else{
//					writer.write(line+"\n");
//				}
//			}
//			writer.close();
			
		
        File file = new File(GraderSettings.get().get("path") + "/" + recordingSession.getUserId() + "/Feedback Attachment(s)/feedback.txt");
//        File file = new File(logFileName(recordingSession));
        try {
            FileUtils.writeStringToFile(file, text);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
