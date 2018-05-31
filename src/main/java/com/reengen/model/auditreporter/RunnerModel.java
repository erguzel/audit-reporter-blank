package com.reengen.model.auditreporter;

import java.util.Comparator;

//enclosing poco type to control my back-data-models
public class RunnerModel {
	
	public static class InputPaths {
		private static String UserPath;
		private static String FilePath;
		
		public InputPaths(){}
		
		public static String getUserPath(){
			return UserPath;
		} 
		public static String getFilesPath(){
			return FilePath;
		}
		public static void setUserPath(String path){
			UserPath = path;			
		}
		public static void setFilesPath(String path){
			FilePath = path;
		}
	}
	
	//To extend and use the methods of IComparable 
	public static class TopDisplay implements Comparable<RunnerModel.TopDisplay> {
		//I encapsulate my fields
		private String fileName;
		private String userName;
		private long size;
		//
		//some parameter constructor for collection cases
		public  TopDisplay(String fileName, String UserName, long Size){
			this.fileName = fileName; this.userName = UserName; this.size = Size;
		}
		// empty constructor for array cases
		public TopDisplay(){}
		//  gets sets static for easy access
		public void setFileName(String FileName) {this.fileName = FileName;}
		public void setUserName(String UserName) {this.userName = UserName;}
		public void setSize(long Size) {this.size = Size;}
		// sets
		public String getFileName() {return this.fileName;}
		public String getUserName() {return this.userName ;}
		public long getFilesize() {return this.size;}
		
		//comparing attributes
		public static class SizeOrderUnit implements Comparator<RunnerModel.TopDisplay>{

			@Override
			public int compare(TopDisplay o1, TopDisplay o2) {
				//comparing by size
				return o1.getFilesize() > o2.getFilesize() ? -1 : (o1.getFilesize() == o2.getFilesize() ? 0 : 1 );
			}
			
		}

		@Override
		public int compareTo(TopDisplay o) {
			
			return this.getFilesize() > o.getFilesize() ? -1 : (this.getFilesize() == o.getFilesize()? 0 : 1 );
		}
	}
}
	
