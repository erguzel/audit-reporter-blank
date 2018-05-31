package com.reengen.utils.auditreporter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.erg.abst.cpaar.prepare.IParserStarter;
import com.erg.cpaar.prepare.ParseStarter;
import com.erg.abst.cpaar.prepare.IParserStarter;
import com.reengen.model.auditreporter.RunnerModel;
import com.reengen.model.auditreporter.RunnerModel.InputPaths;


public class Runner {

	public static enum MainModeTypes {
		DEFAULT, MIXED
	}

	private List<List<String>> users;

	private List<List<String>> files;

	private static InputPaths paths;;

	private List<RunnerModel.TopDisplay> DisplayModelData = new ArrayList<RunnerModel.TopDisplay>();

	// translated commandline arguments
	private static List<String> arguments = new ArrayList<String>();
	
	//filtering order
	private static int topFilterThreshold;

	// empty ctor for default action
	public Runner() {

	}

	// ctor for csv report in non filter mode //note: boolean parameters for ctors are
	// just for creating new method signature. main logic handled in main()
	public Runner(boolean mode) throws IOException {
		loadData(paths.getUserPath(), paths.getFilesPath());
		this.run(true);
	}

	// for filterable txt reports // !!Caution, print orders different
	public Runner(boolean mode1, boolean mode2) throws IOException {
		
		loadData(paths.getUserPath(), paths.getFilesPath());
		this.run(true, true);
		// run(true,true);
	}
	
	// for filter mode tables csv reports 
	public Runner(boolean mode1, boolean mode2, boolean mode3) throws IOException{
		
		loadData(paths.getUserPath(), paths.getFilesPath());
		this.run(true, true, true);
	}
	
	// ===========MAIN==============//
	public static void main(String[] args) throws Exception {
		// initialization

		IParserStarter ps = new ParseStarter();
		ps.addOption("--top",Integer.class,false)
				.submit("ListTop")
				.addFlag("-c")
				.submit("IsCsvMode")
				.parse(args);



		Initialize(args);
		Configure();
		
		// normal mode
		if (Runner.RunnerEnums.RunModeTypes.txtMode && Runner.RunnerEnums.RunModeTypes.nonFilterMode) { //
			Runner r = new Runner();
			r.loadData(paths.getUserPath(), paths.getFilesPath()); // just a
																	// little
																	// modified
																	// from the
																	// original
			r.run();
		}
		// non filtered report mode in cvsv format
		if (Runner.RunnerEnums.RunModeTypes.csvMode && Runner.RunnerEnums.RunModeTypes.nonFilterMode) {
			Runner csNfl = new Runner(true);
		}
		//filtered report in txt frmat
		if (Runner.RunnerEnums.RunModeTypes.txtMode && Runner.RunnerEnums.RunModeTypes.filterMode) {
			Runner txFl = new Runner(true, true);
		}
		//filtered reports in csv format
		if(Runner.RunnerEnums.RunModeTypes.csvMode && Runner.RunnerEnums.RunModeTypes.filterMode){
			Runner csFl = new Runner(true,true,true);
		}

	}

	// ===============================
	// for validation cases

	// sets and prepares arguments field
	private static void Initialize(String[] args) {

		// validation fields
		boolean anyExtraCharacter = true;
		boolean isEmpty = true;
		// for using in loops
		int maxIndex = args.length - 1;

		// for easy validating parameter kinds
		Arrays.sort(args);

		// validation booleans
		anyExtraCharacter = (args.length > 5) ? true : false;
		isEmpty = (args.length == 0) ? true : false;
		//
		if (anyExtraCharacter == true || isEmpty == true) {
			System.out.println("Runner is exiting... please check messages");
			System.out.println("Unsupported parameters detected.. please check the command line argument set");
		}
		// * if loop direction reverses, index out of bounds exception
		// sorted values
		for (int i = maxIndex; i >= 0; i--) {
			// defaults values should remain for each item
			boolean isPath = false;
			boolean isOption = false;
			boolean isEmptyOrNull = true;
			boolean isNumeric = true;
			boolean isUndefined = true;

			//System.out.println("Processing input argument --> " + args[i]);
			// // i
			// know
			// where
			// the
			// paths
			// are
			// after
			// sort
			if (i == maxIndex || i == maxIndex - 1) {
				isPath = (args[i].length() > 5
						&& args[i].substring(args[i].length() - 4, args[i].length()).startsWith(".") ? true : false);
			}

			isEmptyOrNull = (args[i].isEmpty() || args[i] == null) ? true : false;
			isNumeric = CheckParsability(args[i]) ? true : false;
			if (isNumeric) {
				topFilterThreshold = Integer.parseInt(args[i]);
			}
			
			isOption = args[i].equals("--top") || args[i].equals("-c") ? true : false;
			isUndefined = (isPath || isNumeric ? false : true) && (isOption || isEmptyOrNull ? false : true);
			if (isUndefined == true) {
				System.out.println("Runner is exiting... please check messages");
				System.out.println(
						"ERROR!!..Undefined input format.. /n" + "only paths numbers and -c , --top flags are defined");
			} else {
				
				arguments.add(args[i].toString());
			}
		}
	}

	// stores initial conditions // boolean switches are managed here
	private static void Configure() {
		// path info carriying obhect
		paths = new InputPaths();
		for (String pth : arguments) {
			if (pth.length() > 5) {
				if (pth.substring(pth.length() - 4, pth.length() - 1).startsWith(".")) { // maybe
																							// //
																							// other
																							// //
																							// format
																							// //
																							// etc.
					if (pth.contains("users")) {
						paths.setUserPath(pth);
					} else if (pth.contains("files")) {
						paths.setFilesPath(pth);
					}

				}
			}
		} //

		// arrange ui switches, FATAL Importance, for main logic defines
		if (!arguments.contains("-c") && !arguments.contains("--top")) {

			Runner.RunnerEnums.RunModeTypes.setTxtMode(true);
			Runner.RunnerEnums.RunModeTypes.setCsvMode(false);
			Runner.RunnerEnums.RunModeTypes.setNonFilterMode(true);
			Runner.RunnerEnums.RunModeTypes.setfilterMode(false);
		} else if (arguments.contains("-c") && arguments.contains("--top")) {
			Runner.RunnerEnums.RunModeTypes.setCsvMode(true);
			Runner.RunnerEnums.RunModeTypes.setfilterMode(true);
			Runner.RunnerEnums.RunModeTypes.setTxtMode(false);
			Runner.RunnerEnums.RunModeTypes.setNonFilterMode(false);
		} else if (arguments.contains("-c") && !arguments.contains("--top")) {
			Runner.RunnerEnums.RunModeTypes.setCsvMode(true);
			Runner.RunnerEnums.RunModeTypes.setTxtMode(false);
			Runner.RunnerEnums.RunModeTypes.setfilterMode(false);
			Runner.RunnerEnums.RunModeTypes.setNonFilterMode(true);
		} else {// !arguments.contains ("-c") and arguments.contains("--top")
			Runner.RunnerEnums.RunModeTypes.setCsvMode(false);
			Runner.RunnerEnums.RunModeTypes.setTxtMode(true);
			Runner.RunnerEnums.RunModeTypes.setfilterMode(true);
			Runner.RunnerEnums.RunModeTypes.setNonFilterMode(false);
		}
	}
	// loads for filter display model-dto data

	//* loads ui model data for ui filtering option 
	private void loadData(boolean mode1, boolean mode2, boolean yedek) {
		DisplayModelData = new LinkedList<RunnerModel.TopDisplay>();
		for (List<String> userRow : users) {
			long userId = Long.parseLong(userRow.get(0));
			String userName = userRow.get(1);
			for (List<String> fileRow : files) {
				String fileID = fileRow.get(0);
				long filesize = Long.parseLong(fileRow.get(1));
				String filename = fileRow.get(2);
				Integer ownerId = Integer.parseInt(fileRow.get(3));
				if (ownerId == userId) {
					RunnerModel.TopDisplay td = new RunnerModel.TopDisplay(filename, userName, filesize);
					DisplayModelData.add(td);
				}
			}
		}		
	}

	//loads users files
	private void loadData(String userFn, String filesFn) throws IOException {
		boolean isTopModelDataLoad = (Runner.RunnerEnums.RunModeTypes.getfilterMode() == true
				|| Runner.RunnerEnums.RunModeTypes.getNonFilterMode() == false) ? true : false;
		String line;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(userFn));
			users = new ArrayList<List<String>>();

			reader.readLine(); // skip header

			while ((line = reader.readLine()) != null) {
				users.add(Arrays.asList(line.split(",")));
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		reader = null;
		try {
			reader = new BufferedReader(new FileReader(filesFn));
			files = new ArrayList<List<String>>();

			reader.readLine(); // skip header

			while ((line = reader.readLine()) != null) {
				files.add(Arrays.asList(line.split(",")));
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		
		// i chosed to read stream from the same method to fill the model object
		if (isTopModelDataLoad) {
			RunnerModel.TopDisplay topDisplayPoco;
			for (List<String> userRow : users) {

				String userName = userRow.get(1);
				long userId = Long.parseLong(userRow.get(0));
				for (List<String> fileRow : files) {
					long size = Long.parseLong(fileRow.get(1));
					String fileName = fileRow.get(2);
					long ownerUserId = Long.parseLong(fileRow.get(3));
					if (ownerUserId == userId) {
						topDisplayPoco = new RunnerModel.TopDisplay();
						topDisplayPoco.setFileName(fileName);
						topDisplayPoco.setUserName(userName);
						topDisplayPoco.setSize(size);
						DisplayModelData.add(topDisplayPoco);
					}
				}

			}

		}
	}

	// for csv report in non filtered mode
	private void run(boolean mode) {
		for (List<String> userRow : users) {
			long userId = Long.parseLong(userRow.get(0));
			String userName = userRow.get(1);
			for (List<String> fileRow : files) {
				String fileID = fileRow.get(0);
				long filesize = Long.parseLong(fileRow.get(1));
				String filename = fileRow.get(2);
				Integer ownerId = Integer.parseInt(fileRow.get(3));
				if (ownerId == userId) {
					printFile(userName, filename, Long.toString(filesize));
				}
			}
		}
	}

	// for text report for non filtered mode
	private void run() {
		printHeader();
		for (List<String> userRow : users) {
			long userId = Long.parseLong(userRow.get(0));
			String userName = userRow.get(1);

			printUserHeader(userName);

			for (List<String> fileRow : files) {
				String fileId = fileRow.get(0);
				long size = Long.parseLong(fileRow.get(1));
				String fileName = fileRow.get(2);
				long ownerUserId = Long.parseLong(fileRow.get(3));
				if (ownerUserId == userId) {
					printFile(fileName, size);
				}

			}
		}
	}

	// this main method for txt reports in filter mode
	private void run(boolean mode, boolean mode2) {
		List<RunnerModel.TopDisplay> topDispData = getModelDataByOrder(topFilterThreshold);
		printHeader(true,true);
		for(RunnerModel.TopDisplay td : topDispData){
			printFile(td.getFileName(), td.getUserName(), td.getFilesize());
		}
	}

	// for csv methods in filter mode
	private void run(boolean mode1, boolean mode2, boolean mode3) {
		List<RunnerModel.TopDisplay> topDispData = getModelDataByOrder(topFilterThreshold);
		for(RunnerModel.TopDisplay td : topDispData){
			printFile(td.getFileName(), td.getUserName(), Long.toString(td.getFilesize()));
		}
	}

	// gets ui data poco for top list report
	private List<RunnerModel.TopDisplay> getModelDataByOrder(int topOrder) {
		RunnerModel.TopDisplay.SizeOrderUnit sizeUnit = new RunnerModel.TopDisplay.SizeOrderUnit(); // to
																									// create
																									// compare
																									// implementation
																									// unit
																									// implements
																									// (IComparator<RunnerModel.TopDisplay>)
		Collections.sort(DisplayModelData, sizeUnit); // use predefined sorting
														// protocol
		int upperBound = DisplayModelData.size() - 1;

		List<RunnerModel.TopDisplay> sublist = new ArrayList<RunnerModel.TopDisplay>();
		for (int i = 0; i < topOrder; i++) { // toporder is a global
			sublist.add(DisplayModelData.get(i));
		}
		
		return sublist;
	}

	private void printHeader() {
		System.out.println("Audit Report");
		System.out.println("============");
	}

	// for displaying txt reports in filtered mode
	private void printHeader(boolean mode, boolean mode1) {
		System.out.println("  Top #" + topFilterThreshold + " Report");
		System.out.println("  =============");
	}

	private void printUserHeader(String userName) {
		System.out.println("## User: " + userName);
	}

	private void printFile(String fileName, long fileSize) {		
		System.out.println("* " + fileName + " ==> " + fileSize + " bytes");
	}

	// txt mode filtered report templete
	private void printFile(String fileName, String userName, long fileSize) {		
		System.out.println("* " + fileName + " ==> user " + userName + ", " + fileSize + " bytes");
	}

	// filtered mode csv report templete
	private void printFile(String col1, String col2, String col3) { // careful
																	// line
																	// order is
																	// different
																	// between
																	// filter
																	// and
																	// nonFilterModes
																	// modes
		System.out.println(col1 + "," + col2 + "," + col3);
	}

	public static Integer tryIntParse(String text) {
		int result = Integer.parseInt(text);
		return result;
	}

	public static boolean CheckParsability(String text) throws java.lang.NumberFormatException {
		// iterate each char in string to see if all chars are sub sets of basic
		// digits
		// this was a redundant try for fun
		boolean isParsable = true;
		int checked = 0;
		int total = 0;
		char[] asciis = text.toCharArray();
		for (char c : asciis) {
			total++;
			String C = Character.toString(c);
			for (int i = 9; i >= 9; i--) {
				try {
					int parsed = Integer.parseInt(C);
					isParsable = true;
					return isParsable;
				} catch (java.lang.NumberFormatException e) {
					// System.out.println("Warning..!" + text + " is not a
					// numeric character.");
					isParsable = false;
					return isParsable;
				}
			}
		}

		return isParsable;

	}

	// my boolean enums to control master logic in Runner main domain
	public static class RunnerEnums {

		public static class RunModeTypes {

			// encapsulate them
			private static boolean csvMode;
			private static boolean filterMode;
			private static boolean txtMode;
			private static boolean nonFilterMode;

			public static void setCsvMode(boolean mode) {
				csvMode = mode;
			}

			public static boolean getCsvMode() {
				return csvMode;
			}

			//
			public static void setfilterMode(boolean mode) {
				filterMode = mode;
			}

			public static boolean getfilterMode() {
				return filterMode;
			}

			//
			public static void setTxtMode(boolean mode) {
				txtMode = mode;
			}

			public static boolean getTxtMode() {
				return txtMode;
			}

			public static void setNonFilterMode(boolean mode) {
				nonFilterMode = mode;
				// SetMultipleModes(optionMode);
			}

			public static boolean getNonFilterMode() {
				return nonFilterMode;

			}
		}
	}
}
