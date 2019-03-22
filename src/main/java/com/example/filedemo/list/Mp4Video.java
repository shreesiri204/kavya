package com.example.filedemo.list;

public class Mp4Video {
	
	private String videoName;
	private String size;
	private String time;
	
	public Mp4Video() {
		
	}
	
	public Mp4Video(String videoName, String size, String time) {
         super();
		this.videoName=videoName;
		this.size=size;
		this.time=time;
		
	}
	
	public void setMp4Video(String videoName, String size, String time) {

		this.videoName=videoName;
		this.size=size;
		this.time=time;
		
	}
	
	
	
	@Override
    public String toString() {
        return "[videoName=" + videoName + ", size=" + size + ", updatedtime=" + time + "]";
    }

	public String getVideoName() {
		return videoName;
	}

	public String getSize() {
		return size;
	}

	public String getTime() {
		return time;
	}
	


}
