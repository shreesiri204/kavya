package com.example.filedemo.list;

public class InVideo {
	private String videoName;
	private String size;
	private String time;
	
	public InVideo() {
		
	}
	
	public InVideo(String videoName, String time, String size) {
		super();
		this.videoName=videoName;
		this.size=size;
		this.time=time;
		
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

	public void setInVideo(String videoName, String time, String size) {
		this.videoName=videoName;
		this.size=size;
		this.time=time;
		
	}
	
	@Override
    public String toString() {
        return "[videoName=" + videoName + ", size=" + size + ", updatedtime=" + time + "]";
    }
	

}
