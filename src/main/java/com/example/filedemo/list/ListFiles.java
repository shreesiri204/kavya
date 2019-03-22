package com.example.filedemo.list;


import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import com.example.filedemo.property.FileStorageProperties;

public class ListFiles {
	
	public InVideo getInVideo() {
		return inVideo;
	}
	public void setInVideo(InVideo inVideo) {
		this.inVideo = inVideo;
	}
	public Mp4Video getMp4Video() {
		return mp4Video;
	}
	public void setMp4Video(Mp4Video mp4Video) {
		this.mp4Video = mp4Video;
	}
	public OutVideo getOutVideo() {
		return outVideo;
	}
	public void setOutVideo(OutVideo outVideo) {
		this.outVideo = outVideo;
	}
	private InVideo inVideo;
	private Mp4Video mp4Video;
	private OutVideo outVideo;
	
	public ListFiles() {
	}
	
	public ListFiles(InVideo inVideo, Mp4Video mp4Video, OutVideo outVideo) {
        super();
		this.inVideo=inVideo;
		this.mp4Video=mp4Video;
		this.outVideo=outVideo;
	}
	
	public void setListFiles(InVideo inVideo, Mp4Video mp4Video, OutVideo outVideo) {

		this.inVideo=inVideo;
		this.mp4Video=mp4Video;
		this.outVideo=outVideo;
	}
	
	

}
