package com.example.filedemo.list;

import java.util.ArrayList;
import java.util.List;

public class VideoList {
	
	private List<ListFiles> listVideos;
    
    public List<ListFiles> getVideoList() {
        if(listVideos == null) {
        	listVideos = new ArrayList<>();
        }
        return listVideos;
    }
  
    public void setVideoList(List<ListFiles> listVideos) {
        this.listVideos = listVideos;
    }

}
