package com.example.filedemo.list;

import java.util.ArrayList;
import java.util.List;

public class VideoResolution {
	
	private String fileName;
	private List<String> chunksList;
	
	public VideoResolution() {
	}
	
	public String getFileName() {
		return fileName;
	}

	public List<String> getChunksList() {
		return chunksList;
	}

	public VideoResolution(String fileName, List<String> chunksList) {
		super();
		this.fileName=fileName;
		this.chunksList=chunksList;
	}
	
	public void  setVideoResolution(String fileName, List<String> chunksList) {
		this.fileName=fileName;
		this.chunksList=chunksList;
	}
	
	

}
