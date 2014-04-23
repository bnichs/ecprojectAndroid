package com.example.filereconciler;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FileNode {

	private String path;
	private long size;
	private File fil;
	private String printedName;
	//private FileNode parent;
	
	
	
	public FileNode(File f){
		fil=f;
		size=f.length();
		path=f.getPath();
	}
	
	public FileNode(String path){
		
	}

	public String getPath(){
		try {
			return fil.getCanonicalPath();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "null fuck";
	}
	
	public FileNode getParent(){
		return new FileNode(fil.getParentFile());
	}
	
	//used for seting the name to .. and .
	public void setName(String str){
		printedName=str;
	}
	
	public boolean isDir(){
		return fil.isDirectory();
	}
	
	public String getFileName(){
		
		return printedName==null ? fil.getName():printedName;
	}
	
	public String getSize(){
		if(size <= 0) return "0";
	    String[] units = new String[] { "B", "Kb", "Mb", "Gb", "Tb" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1000));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1000, digitGroups)) + " " + units[digitGroups];
	}
	
	public List<FileNode> getContents(){
		if (!isDir()){
			return null;
		}

		List<FileNode> fils= new LinkedList<FileNode>();
		for (File f: fil.listFiles()){
			fils.add(new FileNode(f));
		}
		return fils;
	}
	
	public boolean equals(FileNode fn){
		try {
			return this.fil.getCanonicalPath().equals(fn.fil.getCanonicalPath());
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	
}
