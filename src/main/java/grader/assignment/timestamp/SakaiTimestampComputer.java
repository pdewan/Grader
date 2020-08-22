package grader.assignment.timestamp;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

import framework.navigation.SakaiStudentFolder;
import grader.basics.util.Option;
import grader.util.SakaiDateUtil;
import util.trace.Tracer;

public class SakaiTimestampComputer implements TimestampComputer{

	@Override
	public DateTime computeStudentFolderTimeStamp(SakaiStudentFolder aStudentFolder) {
		File timestampFile = new File(aStudentFolder.getFolder(), "timestamp.txt");
        if (timestampFile.exists()) {
        	Tracer.info(this, "Found timestamp file:" + timestampFile.getAbsolutePath());

//        	System.out.println(("Found timestamp file:" + timestampFile.getAbsolutePath()));
            try {
                String timestampText = FileUtils.readFileToString(timestampFile);
            	Tracer.info(this, "Found timestamp:" + timestampText);

//            	System.out.println("Found timestamp:" + timestampText);
            	Date aDate = SakaiDateUtil.toDate(timestampText);

            	Tracer.info (this, "Timestamp Date:" + aDate );
//            	System.out.println ("Timestamp Date:" + aDate );

            	DateTime aDateTime = new DateTime(aDate);
            	Tracer.info (this, "Date time:" + aDateTime );

//            	System.out.println ("Date time:" + aDateTime );
            	return aDateTime;


            //    return Option.apply(new DateTime(Common.toDate(timestampText)));
            } catch (IOException e) {
                return null;
            }
        } else
            return null;
    }
	

}
