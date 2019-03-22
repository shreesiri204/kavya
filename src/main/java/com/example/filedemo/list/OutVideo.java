package com.example.filedemo.list;

import java.util.List;

public class OutVideo {
	
	private String videoName;
	private String size;
	public String getVideoName() {
		return videoName;
	}
	public String getSize() {
		return size;
	}
	public String getTime() {
		return time;
	}
	public List<VideoResolution> getResolutionFileList() {
		return resolutionFileList;
	}

	private String time;
	private List<VideoResolution> resolutionFileList;
	
public OutVideo() {
		
	}
public OutVideo(String videoName, String size, String time, List<VideoResolution> resolutionFileList ) {
	super();
	this.videoName=videoName;
	this.size=size;
	this.time=time;
	this.resolutionFileList = resolutionFileList;
	
}
	public void setOutVideo(String videoName, String size, String time, List<VideoResolution> resolutionFileList ) {
		this.videoName=videoName;
		this.size=size;
		this.time=time;
		this.resolutionFileList = resolutionFileList;
		
	}
	
	@Override
    public String toString() {
        return "[videoName=" + videoName + ", size=" + size + ", updatedtime=" + time + ",ResolutionFileList" + resolutionFileList.toString() + "]";
    }
	

}
