package gradingTools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;



/*
 * 
 * Research: Make the anonimizer platform agnostic....make it work with folders w/o headers....and w them....also anonimize grades.csv!!! 
 * Pehaps make anonimizing process more readable too
 * ...student 1, student 2 etc...needs to work on windows....mac....linux....

After this i can get aikat and whoever else to use this and hand me sakai folders w/ grades.csv
 so I can then do a high-low-median in similiarity 
 scores for a semester's course and finally get some graphs going!!
 */
public class Anon {
	static boolean courseMode;
	static boolean deleteTXTAndHTML = false;
	static String currIden;
	static int depth;
	static HashMap<String,String>CommentsIdenMap;
	static File log_file;
	static FileWriter logger;
	static int counter; //used for differentiating students
	public static void main(String[] args) throws IOException, InterruptedException {
		//instantiate vars
		CommentsIdenMap=new HashMap<String,String>();
		depth = 1;
		counter = 0;
		String folderName = ""; //will either act as the singleton assignment name....or the course folder name
		log_file=new File("anon_log");
		log_file.delete();
		log_file.createNewFile();
		logger = new FileWriter(log_file);
		System.out.println("made stuff!");
		if (args.length == 1) {
			folderName = args[0];
//			zipFileName = args[0];
			courseMode=false;
			deleteTXTAndHTML = false;
			logger.write("In single assignment mode. Delete txt and html files = false\n");
		} else if(args.length == 2) {
			folderName = args[0];
//			zipFileName = args[0];
			courseMode=false;
			if (args[1].equals("true")) {
				deleteTXTAndHTML = true;
			}
			logger.write("In single assignment mode. Delete txt and html files = " + deleteTXTAndHTML + "\n");
		}
		else if (args.length == 3){
			folderName = args[0];
//			zipFileName = args[0];
			courseMode=true;
			if (args[1].equals("true")) {
				deleteTXTAndHTML = true;
			}
			logger.write("In course folder mode. Delete txt and html files = " + deleteTXTAndHTML + "\n");
		} else {
			System.out.println("Enter main args: folderName (deleteTXTAndHTMLFiles) (CourseMode)");
			System.exit(0);
		}
		//determine which version to run based on OS
		String os=System.getProperty("os.name").toLowerCase();
		if (folderName.contains(".zip")) {
			File zipFile = new File(folderName);
			folderName = unzip(folderName, zipFile.getParent());
		}
		File folder = findFolderWithCSV(new File(folderName));
		if (!folder.exists()) {
			System.out.println("Assignment folder not found.");
			System.exit(0);
		}
		folder = findFolderWithCSV(folder);
		if (folder == null) {
			System.out.println("Assignment folder not found.");
			System.exit(0);
		}
		if(os.contains("window")){
			if(!courseMode){
				folderName = findFolderWithCSV(new File(folderName)).getPath();
				unzipAllZipFiles(new File(folderName));
				clearHeaders_Windows(folderName);
				Anon_ize_Windows(1,folderName);
				Anon_ize_grades_Windows(folderName);
			}
			else{
				folderName = findFolderWithCSV(new File(folderName)).getParentFile().getPath();
				unzipAllZipFiles(new File(folderName));
				Anon_ize_Course_Windows(folderName);
			}
		}
		else if(os.contains("mac")){ //currently not supported
			Process testMac=new ProcessBuilder(new String[]{"/bin/bash","-c","mkdir", "goo", "&", "touch", "goo/hi.txt", "&", "ls" ,"|" ,"grep", "goo"}).start();
			BufferedReader r = new BufferedReader(new InputStreamReader(testMac.getInputStream()));
			while(true){
				String line=r.readLine();
				if(line==null)break;
				System.out.println(line);
			}
		}
		else if(os.contains("linux")){ //MUST CLOSE LOG so it can show for LINUX!!!
			//for this have to basically hand off the scripts too for running in unix
		//	Process testlinux=Runtime.getRuntime().exec(new String[]{"doStuff.sh"});
			if(!courseMode){
				clearHeaders_Linux(folderName);
				Anon_ize_Linux(1,folderName);
				Anon_ize_grades_Linux(folderName);
			}
			else{
				Anon_ize_Course_Linux(folderName);
			}
		}
		else{
			System.out.println("Can't Figure out your os!");
		}
		logger.close();//must close upon completion for linux to show this stuff.
	}

	public static void unzipAllZipFiles(File folder) {
		for (File zipFile : folder.listFiles()) {
			if (zipFile.isDirectory()) {
				unzipAllZipFiles(zipFile);
			} else if (zipFile.getName().endsWith(".zip")) {
				try {
					unzip(zipFile.getPath(), zipFile.getParent());
					zipFile.delete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String unzip(String zipFilePath, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		String folderName = null;
		// iterates over entries in the zip file
		while (entry != null) {
			if (folderName == null && entry.getName().indexOf("/") != -1) {
				folderName = entry.getName().substring(0,entry.getName().indexOf("/"));
			}
			String filePath = destDirectory + File.separator + entry.getName().replace("/", "\\");
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				File dir = new File(filePath);
				dir.mkdirs();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
		return folderName == null ? destDirectory : destDirectory + "\\" + folderName;
	}

	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		new File(filePath).getParentFile().mkdirs();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[4096];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	public static void Anon_ize_Course_Linux(String folderName) throws IOException, InterruptedException {
		Process p = null;
		logger.flush();
		logger.write("PROCCESSING COURSE FOLDER\n");
		logger.flush();
		//look at course directory names
		try{
			p=Runtime.getRuntime().exec(new String[]{"getDirs.sh",folderName});
			Thread.sleep(2000);
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
		//reader for it
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while(true){
			line = r.readLine();
			if (line == null) {
				break;
			}
			String newPath = folderName + "/" + line;
			logger.flush();
			logger.write("processing " + line);
			logger.flush();
			clearHeaders_Linux(newPath);
			Anon_ize_Linux(1,newPath);
			Anon_ize_grades_Linux(newPath);
		}
	}




	public static void Anon_ize_Course_Windows(String folderName) throws IOException, InterruptedException {
//		Process p = null;
		logger.flush();
		logger.write("PROCCESSING COURSE FOLDER\n");
		logger.flush();
		File courseFolder = new File(folderName);
		for(File AssignmentFolder: courseFolder.listFiles()){
			if (AssignmentFolder.isDirectory()) {
				String newPath = AssignmentFolder.getPath();
				clearHeaders_Windows(newPath);
				Anon_ize_Windows(1,newPath);
				Anon_ize_grades_Windows(newPath);
			}
		}
//		try{
//			//look at course directory name
//			String[]command = new String[8];
//			command[0]="cmd.exe";
//			command[1]="/c";
//				command[2]="cd";
//			command[3]=folderName;
//			command[4]="&";
//			command[5]="ls";
//			command[6]="&";
//			command[7]="exit";
//			p=new ProcessBuilder(command).start();
//			Thread.sleep(2000);
//		}
//		catch(Exception e){
//			e.printStackTrace();
//			System.exit(0);
//		}
		//reader for it
//		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
//		String line;
//		while(true){
//			line = r.readLine();
//			if (line == null) {
//				break;
//			}
//			String newPath = folderName + "/" + line;
//			clearHeaders_Windows(newPath);
//			Anon_ize_Windows(1,newPath);
//			Anon_ize_grades_Windows(newPath);
//		}
		
	}




	public static void Anon_ize_grades_Windows(String folderName) throws IOException, InterruptedException{
		logger.write("CLEARING GRADES.CSV\n");
		//get csv file
		File csv=new File(folderName+"/grades.csv");
		//reader for it
		BufferedReader r = new BufferedReader(new FileReader(csv));
		//file to be the anoncsv
		File temp = new File(folderName+"/ANONGrades.csv");
		temp.createNewFile();
		//writer for it
		BufferedWriter w = new BufferedWriter(new FileWriter(temp));
		//first three lines do not contain any student stuff
		w.write(r.readLine()+"\n");
		w.flush();
		w.write(r.readLine()+"\n");
		w.flush();
		w.write(r.readLine()+"\n");
		
		while(true){
			w.flush();
			String line=r.readLine();
			if (line == null) {
				break;
			}
			line=line.replaceAll(" ", "");//string spaces
			line = line.replaceAll("\"",""); //get rid of all "
			String[]names = line.split(",");
			//replace each occurence of name w its shuffled version, passing in prefix
			line=line.replaceAll(names[0], shuffle(names[0],"ID"));
			line=line.replaceAll(names[1], shuffle(names[1],"ID"));
			line=line.replaceAll(names[2], shuffle(names[2],"fName"));
			line=line.replaceAll(names[3], shuffle(names[3],"lName"));
			//write to the new file
			w.write(line+"\n");
		}
		w.close(); 
		//remove old csv and rename the new one
		Process rmcsv =new ProcessBuilder(new String[]{"cmd.exe","/c","cd",folderName,"&","rm","grades.csv","&","exit"}).start();
		Thread.sleep(100);
		//if(!temp.renameTo(csv)){System.out.println("Couldn't replace file!?");System.exit(0);}
		r.close();
	}
	
	private static void Anon_ize_grades_Linux(String folderName) throws IOException, InterruptedException {
		logger.write("CLEARING GRADES.CSV\n");
		//get csv file
		File csv=new File(folderName+"/grades.csv");
		//reader for it
		BufferedReader r = new BufferedReader(new FileReader(csv));
		//file to be the anoncsv
		File temp = new File(folderName+"/ANONGrades.csv");
		temp.createNewFile();
		//writer for it
		BufferedWriter w = new BufferedWriter(new FileWriter(temp));
		//first three lines do not contain any student stuff
		w.write(r.readLine()+"\n");
		w.flush();
		w.write(r.readLine()+"\n");
		w.flush();
		w.write(r.readLine()+"\n");
		
		while(true){
			String line=r.readLine();
			if (line == null) {
				break;
			}
			line=line.replaceAll(" ", "");//string spaces
			line = line.replaceAll("\"",""); //get rid of all "
			String[]names = line.split(",");
			//replace each occurence of name w its shuffled version
			line=line.replaceAll(names[0], shuffle(names[0],"ID"));
			line=line.replaceAll(names[1], shuffle(names[1],"ID"));
			line=line.replaceAll(names[2], shuffle(names[2],"fName"));
			line=line.replaceAll(names[3], shuffle(names[3],"lName"));
			//write to the new file
			w.write(line+"\n");
		}
		w.close(); 
		//remove old csv and rename the new one
		Process rmcsv =Runtime.getRuntime().exec(new String[]{"rmCSV.sh",folderName});
		Thread.sleep(100);
		//if(!temp.renameTo(csv)){System.out.println("Couldn't replace file!?");System.exit(0);}
		r.close();
		
	}
	
	public static void Anon_ize_Windows(int depth, String folderName) throws IOException, InterruptedException{//depth is the depth of the name: 0 is base folder
		Process p = null;
		logger.flush();
		logger.write("CLEARING TOP-LEVEL DIRECTORY NAMES\n");
		logger.flush();
		File folder = new File(folderName);
//		try{
//			//look at each student directory
//			String[]command = new String[8];
//			command[0]="cmd.exe";
//			command[1]="/c";
//				command[2]="cd";
//			command[3]=folderName;
//			command[4]="&";
//			command[5]="ls";
//			command[6]="&";
//			command[7]="exit";
//			p=new ProcessBuilder(command).start();
//			Thread.sleep(2000);
//		}
//		catch(Exception e){
//			e.printStackTrace();
//			System.exit(0);
//		}
//		//reader for it
//		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
//		String line;
//		while(true){
		
		for(File file: folder.listFiles()){
//			line = r.readLine();
			//csv handled elsewhere
			String line = file.getName();
			if(line.contains(".csv"))continue;//skip csv file
//			Process rm = (new ProcessBuilder(new String[]{"cmd.exe","/c","cd","\""+folderName+"/"+line+"\"","&","rm","*.txt","*.html","&","exit"}).start());//kill txt and html...could have names
//			Thread.sleep(300);
			if (!file.isDirectory()) {
				continue;
			}
			//get lastname,firstname,onyen
			String lastName=line.substring(0, line.indexOf(","));
			String firstName=line.substring(line.indexOf(",")+2,line.indexOf("("));
			String onyen=line.substring(line.indexOf("(")+1,line.indexOf(")"));
			//toReplace has anon versions
			String toReplace=shuffle(lastName,"lName")+", "+shuffle(firstName,"fName")+"("+shuffle(onyen,"ID")+")";
			//System.out.println("\"" + folderName + "/" +"\"");
			findTXTAndHTMLFiles_Windows(file, firstName, lastName, onyen);
			//rename the directory
			file.renameTo(new File(file.getParent() + "/" + toReplace));
//			Process rename=(new ProcessBuilder(new String[]{"cmd.exe","/c","cd",folderName,"&","rename","\""+line+"\"","\""+toReplace+"\"","&","exit"}).start());
//			Thread.sleep(100);
			logger.write("renamed directory "+line+" to "+toReplace+"\n");
			if (deleteTXTAndHTML) {
				logger.write("removed all txt files and html files from directory "+line+"\n");
			}
			//log.flush();
		}
	}
	
	private static File findFolderWithCSV(File folder) {
		for (File file : folder.listFiles()) {
			if (file.getName().contains("grades.csv")) {
				return folder;
			}
		}
		File csvFolder = null;
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				csvFolder = findFolderWithCSV(file);
				if(csvFolder != null)
					return csvFolder;
			}
		}
		return null;
	}
	
	private static void findTXTAndHTMLFiles_Windows(File folder, String firstName, String lastName, String ID) throws IOException{
//		File folder = new File(folderName);
		if (folder.listFiles() == null) {
			return;
		}
		for(File file: folder.listFiles()){
			if (file.isDirectory()) {
				String filePath = file.getPath();
				String fileName = file.getName();
				if (!(fileName.endsWith(".gif") || fileName.endsWith(".jpg")) && fileName.contains(firstName) || fileName.contains(lastName) || fileName.contains(ID)) {
					String newFileName = fileName.replace(lastName, shuffle(lastName,"lName")).replace(firstName, shuffle(firstName,"fName")).replace(ID, shuffle(ID,"ID"));
					File newFile = new File(filePath.replace(fileName, newFileName));
					file.renameTo(newFile);
					logger.write("renamed Folder "+ fileName +" to "+ newFileName +"\n");
				}
				findTXTAndHTMLFiles_Windows(file, firstName, lastName, ID);
			} else {
				if (deleteTXTAndHTML) {
					if (file.getName().endsWith(".txt") || file.getName().endsWith(".html")) {
						file.delete();
					} else {
						String filePath = file.getPath();
						String fileName = file.getName();
						if (!(fileName.endsWith(".gif") || fileName.endsWith(".jpg")) && fileName.contains(firstName) || fileName.contains(lastName) || fileName.contains(ID)) {
							String newFileName = fileName.replace(lastName, shuffle(lastName,"lName")).replace(firstName, shuffle(firstName,"fName")).replace(ID, shuffle(ID,"ID"));
							File newFile = new File(filePath.replace(fileName, newFileName));
							file.renameTo(newFile);
							logger.write("renamed file "+ fileName +" to "+ newFileName +"\n");
						}
					}
				} else {
					String filePath = file.getPath();
					String fileName = file.getName();
					if (!(fileName.endsWith(".gif") || fileName.endsWith(".jpg")) && fileName.contains(firstName) || fileName.contains(lastName) || fileName.contains(ID)) {
						String newFileName = fileName.replace(lastName, shuffle(lastName,"lName")).replace(firstName, shuffle(firstName,"fName")).replace(ID, shuffle(ID,"ID"));
						File newFile = new File(filePath.replace(fileName, newFileName));
						file.renameTo(newFile);
						logger.write("renamed file "+ fileName +" to "+ newFileName +"\n");
					}
				}
			}
		}
	}
	
	
	private static void Anon_ize_Linux(int i, String folderName) throws IOException, InterruptedException {
		Process p = null;
		logger.write("CLEARING TOP-LEVEL DIRECTORY NAMES\n");
		try{
			p=Runtime.getRuntime().exec(new String[]{"getDirs.sh",folderName});
			Thread.sleep(2000);
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
		//reader for it
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while(true){
			line = r.readLine();
			if (line == null) {
				break;
			}
			//csv handled elsewhere
			//System.out.println(line);continue;
			if(line.contains(".csv"))continue;//skip csv file
			Process rm =Runtime.getRuntime().exec(new String[]{"delJunk.sh",folderName,line});//kill txt and html...could have names
			Thread.sleep(300);
			//get lastname,firstname,onyen
			String lastName=line.substring(0, line.indexOf(","));
			String firstName=line.substring(line.indexOf(",")+2,line.indexOf("("));
			String onyen=line.substring(line.indexOf("(")+1,line.indexOf(")"));
			//toReplace has anon versions
			String toReplace=shuffle(lastName,"lName")+", "+shuffle(firstName,"fName")+"("+shuffle(onyen,"ID")+")";
			//System.out.println("\"" + folderName + "/" +"\"");
			//rename the directory
			//Process rename=(new ProcessBuilder(new String[]{"cmd.exe","/c","cd",folderName,"&","rename","\""+line+"\"","\""+toReplace+"\"","&","exit"}).start());
			Process rename = p=Runtime.getRuntime().exec(new String[]{"renameDir.sh",folderName,line,toReplace});
			Thread.sleep(100);
			logger.write("renamed directory "+line+" to "+toReplace+"\n");
			logger.write("removed all txt files and html files from directory "+line+"\n");
			logger.flush();
		}
	}
	
	public static void clearHeaders_Windows(String folderName) throws IOException{
//		Process p = null;
		logger.write("CLEARING JAVA FILES OF NAMES\n");
		findJavaFiles_Windows(new File(folderName), folderName.substring(0,folderName.lastIndexOf("\\")+1));
//		try {
			//get path to each java file
		
//			if(!courseMode)p=new ProcessBuilder(new String[]{"cmd.exe","/c","find",folderName,"|","grep",".java","&","exit"}).start();
//			else{
//				String course = folderName.substring(0, folderName.indexOf("/"));
//				folderName = folderName.substring(folderName.indexOf("/")+1);
//				p=new ProcessBuilder(new String[]{"cmd.exe","/c","cd",course,"&","find",folderName,"|","grep",".java","&","exit"}).start();
//			}

//			Thread.sleep(2000);
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//			System.exit(0);
//		}
		//reader for it
//		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
//		String line;
//		while (true) {
//			line = r.readLine();
//			if (line == null) {
//				break;
//			}
	//		System.out.println(line);
			//hold on to orig line for cd later
		
//		r.close();
	}
	
	
	private static void findJavaFiles_Windows(File folder, String topFolderName) throws IOException{
//		File folder = new File(folderName);
		for(File file: folder.listFiles()){
			if (file.isDirectory()) {
				findJavaFiles_Windows(file, topFolderName);
			} else {
				String fileName = file.getName();
				if (fileName.endsWith(".java") || fileName.endsWith(".xml") || (fileName.endsWith(".csv") && !fileName.equals("grades.csv")) || fileName.endsWith(".txt")) {
					replaceHeaders_Windows(file, topFolderName);
				}
			}
		}
	}
	
	private static void replaceHeaders_Windows(File file, String topFolderName) throws IOException{
		
		String line = file.getPath().replace(topFolderName, "");
		String orig_line=file.getPath();
		line=line.replaceAll("\\\\", "/"); //sanitize
		String[] split = line.split("/");
		//load up our known names
		ArrayList<String>names=new ArrayList<String>();
		//should be last name
		names.add(split[depth].substring(0, split[depth].indexOf(",")));
		//should be first name
		names.add(split[depth].substring(split[depth].indexOf(",")+2,split[depth].indexOf("(")));
		//should be onyen
		names.add(split[depth].substring(split[depth].indexOf("(")+1,split[depth].indexOf(")")));
		//make a new file to write to
		File f = new File(orig_line);
		if(!f.canWrite()){
			System.out.println("can't write file "+line);
			return;
		}
		File temp = new File("TEMP_GOO");
		temp.createNewFile();
		//writer for new file...and reader for our orig java file
		BufferedWriter w = new BufferedWriter(new FileWriter(temp));
		BufferedReader r_1=new BufferedReader(new FileReader(f));
		int line_num=0;
		String [] prefixes = {"lName", "fName", "ID"};
		while(true){
			String line_1=r_1.readLine();
			if(line_1==null)break;
			//replace all instances of names with anon version
			for (int i=0; i<names.size(); i++) {
				String name = names.get(i);
				if(line_1.toLowerCase().contains(name.toLowerCase())){
					logger.write("changed "+name+" on line "+line_num+" of "+f.getName()+"\n");
					line_1=line_1.replaceAll(name, shuffle(name, prefixes[i])).replaceAll(name.toLowerCase(), shuffle(name, prefixes[i]));//shuffle all names
				}
				
			}
			//write it to new file
			w.write(line_1+"\n");
			line_num++;
		}
		w.close();
		r_1.close();
		//delete orig java file
		f.delete();
		//rename our new file to orig name
		if(!temp.renameTo(f)){System.out.println("Couldn't replace file!?");System.exit(0);}
	}
	
	private static void clearHeaders_Linux(String folderName) throws IOException {
		Process p = null;
		logger.write("CLEARING JAVA FILES OF NAMES\n");
		try {
			//get path to each java file
			if(!courseMode)p=Runtime.getRuntime().exec(new String[]{"getPaths.sh",folderName});
			else{
				String course = folderName.substring(0,folderName.indexOf("/"));
				folderName = folderName.substring(folderName.indexOf("/") + 1);
				p = Runtime.getRuntime().exec(new String[]{"getPathsCourse.sh",course,folderName});
			}
			Thread.sleep(2000);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
		//reader for it
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			//System.out.println(line);
			//hold on to orig line for cd later
			String orig_line=line;
			line=line.replaceAll("\\\\", "/"); //sanitize
			String[] split = line.split("/");
			//load up our known names
			ArrayList<String>names=new ArrayList<String>();
			//should be last name
			names.add(split[depth].substring(0, split[depth].indexOf(",")));
			//should be first name
			names.add(split[depth].substring(split[depth].indexOf(",")+2,split[depth].indexOf("(")));
			//should be onyen
			names.add(split[depth].substring(split[depth].indexOf("(")+1,split[depth].indexOf(")")));
			//make a new file to write to
			File f = new File(orig_line);
			if(!f.canWrite()){
				System.out.println("can't write file "+line);
				continue;
			}
			File temp = new File("TEMP_GOO");
			temp.createNewFile();
			//writer for new file...and reader for our orig java file
			BufferedWriter w = new BufferedWriter(new FileWriter(temp));
			BufferedReader r_1=new BufferedReader(new FileReader(f));
			int line_num=0;
			String [] prefixes = {"lName", "fName", "ID"};
			while(true){
				String line_1=r_1.readLine();
				if(line_1==null)break;
				//replace all instances of names with anon version
				for (int i =0; i< names.size(); i++) {
					String name = names.get(i);
					if(line_1.contains(name)){
						logger.write("changed "+name+" on line "+line_num+" of "+f.getName()+"\n");
						logger.flush();
						line_1=line_1.replaceAll(name, shuffle(name, prefixes[i]));//shuffle all names
					}
					
				}
				//write it to new file
				w.write(line_1+"\n");
				line_num++;
			}
			w.close();
			r_1.close();
			//delete orig java file
			f.delete();
			//rename our new file to orig name
		if(!temp.renameTo(f)){System.out.println("Couldn't replace file!?");System.exit(0);}
		}
		r.close();
	}
	
	public static String shuffle(String text, String prefix) {
		//we see if we have seen the name before, returning its mapped anon version if it exists
		if(CommentsIdenMap.get(text)!=null)return CommentsIdenMap.get(text);
		//otherwise we generate 5 char suffix for 'salting' the identifier...don't want to create something used already
//	    int leftLimit = 97; // letter 'a'
//	    int rightLimit = 122; // letter 'z'
//	    int targetStringLength = 5;
//	    StringBuilder buffer = new StringBuilder(targetStringLength);
//	    for (int i = 0; i < targetStringLength; i++) {
//	        int randomLimitedInt = leftLimit + (int) 
//	          (new Random().nextFloat() * (rightLimit - leftLimit + 1));
//	        buffer.append((char) randomLimitedInt);
//	    }
//	    String generatedSuffix = buffer.toString();
//	    String generatedString;
//	    if(prefix.equals("ID")) generatedString = prefix + ++counter + "_" + generatedSuffix; //only append counter to onyen
//	    else generatedString = prefix + "_" + generatedSuffix;
//		
		
//sha256	
		String generatedString = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(text.getBytes("UTF-8"));
			byte[] generatedBytes = digest.digest();
			StringBuffer sb = new StringBuffer();
            for (int i = 0; i < generatedBytes.length; i++) 
                sb.append(Integer.toHexString(0xff & generatedBytes[i]));
			generatedString = sb.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //load the generated identifier into hash map
	    CommentsIdenMap.put(text, generatedString);
	    return generatedString;
	    
	}

}
