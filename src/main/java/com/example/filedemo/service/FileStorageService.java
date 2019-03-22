package com.example.filedemo.service;

import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.exception.MyFileNotFoundException;
import com.example.filedemo.property.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.example.filedemo.list.ListFiles;
import com.example.filedemo.list.InVideo;
import com.example.filedemo.list.Mp4Video;
import com.example.filedemo.list.OutVideo;
import com.example.filedemo.list.VideoList;
import com.example.filedemo.list.VideoResolution;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.DirectoryStream;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final Path processedFileLocation;
    private final Path transcodedFileLocation;
    private ListFiles listFiles;
    private InVideo inVideo;
    private OutVideo outVideo;
    private Mp4Video mp4Video;
    private VideoResolution videoResolution;
    private List<VideoResolution> resolutionFileList = null;
    private List<ListFiles> mediaLibrary=null;
    private VideoList list = null;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.processedFileLocation = Paths.get(fileStorageProperties.getProcessedDir())
                .toAbsolutePath().normalize();
        this.transcodedFileLocation = Paths.get(fileStorageProperties.gettranscodedDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.processedFileLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    
    public String getFilePath(String file) {
       
        try {
            if(file.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + file);
            }
            String targetLocation = this.fileStorageLocation+"/"+file;
            System.out.println(targetLocation);
           
            return targetLocation;
        } catch (Exception ex) {
            throw new FileStorageException("Could not find file " + file + ". Please try again!", ex);
        }
    }
    
    public String getprocessedFilePath(String file) {
        
        try {
            if(file.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + file);
            }
            String targetLocation = this.processedFileLocation+"/"+file;
           
            return targetLocation;
        } catch (Exception ex) {
            throw new FileStorageException("Could not find file " + file + ". Please try again!", ex);
        }
    }
    
    public void addAndlistFile() {
        try {
        	listFiles = new ListFiles();
        	mediaLibrary=new ArrayList<>();
        	list = new VideoList();
        	DirectoryStream<Path> inFolderStream = Files.newDirectoryStream(this.fileStorageLocation);
            for (Path path : inFolderStream) {
                File infile = path.toFile();
                listFiles.setInVideo(new InVideo(infile.getName(),getDateTime(infile.lastModified()),getFilesize(infile)));
                int index= infile.getName().lastIndexOf(".");
                String inFistpart=  infile.getName().substring(0,index);              
                DirectoryStream<Path> mp4FolderStream = Files.newDirectoryStream(this.processedFileLocation);
                for (Path mp4path : mp4FolderStream) {
                    File mp4file = mp4path.toFile();
                    if(mp4file.getName().contains(inFistpart))
                    {
                    	listFiles.setMp4Video(new Mp4Video(mp4file.getName(),getDateTime(mp4file.lastModified()),getFilesize(mp4file)));
                    	Path outFileLocation = this.transcodedFileLocation.resolve(inFistpart);
                    	handleOutFolder(outFileLocation);
                    	break;
                    }
                }
                mediaLibrary.add(listFiles);
               
            }
            list.setVideoList(mediaLibrary);
        } catch (IOException ex) {
            throw new FileStorageException("Could not list file " + fileStorageLocation + ". Please try again!", ex);
        }
    }
    
    public VideoList getAllVideos() {
    	addAndlistFile();
    	return list;
    }

	private void handleOutFolder(Path outFileLocation) throws IOException {
		if(Files.exists(outFileLocation)) {
			ArrayList<String> list360=new ArrayList<String>();
			ArrayList<String> list480=new ArrayList<String>();
			ArrayList<String> list720=new ArrayList<String>();
			ArrayList<String> list1020=new ArrayList<String>();
			resolutionFileList = new ArrayList<>();
			File outFile = null;
			DirectoryStream<Path> outFolderStream = Files.newDirectoryStream(outFileLocation);
			for (Path outpath : outFolderStream) { 
				String filename=outpath.toFile().getName();
			  if(filename.startsWith("360p_")){
				  list360.add(filename);
			   }
			  else if(filename.startsWith("480p_")){
				  list480.add(filename);
			   }
			  else if(filename.startsWith("720p_")){
				  list720.add(filename);
			   }
			  else if(filename.startsWith("1080p_")){
				  list1020.add(filename);
			  }
			  else if(filename.startsWith("playlist")){
				  outFile = outpath.toFile();
			  }
				  
			}
		 if(!list360.isEmpty())	{
			 resolutionFileList.add(new VideoResolution("360p.m3u8", list360));
		 }
		 if(!list480.isEmpty())	{
			 resolutionFileList.add(new VideoResolution("480p.m3u8", list480));
		 }
		 if(!list720.isEmpty())	{
			 resolutionFileList.add(new VideoResolution("720p.m3u8", list720));
		 }
		 if(!list1020.isEmpty())	{
			 resolutionFileList.add(new VideoResolution("1080p.m3u8", list1020));
		 }
		 listFiles.setOutVideo(new OutVideo("playlist.m3u8", getFilesize(outFile),getDateTime(outFile.lastModified()) , resolutionFileList));

			
		}
	}
	private String getDateTime(Long millis) {
   	 Date date = new Date(millis);
   	 return getStringFromDate(date);
   	 
    }
	public static String getStringFromDate(Date date){
		String dateString = null;
		if(date!=null) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		dateString = dateFormat.format(date); 
		}
		return dateString;

	}
	
    public String getFilesize(File file) {
    	long size = file.length();
    	if(size< 1024)
    		return size + "bytes";
    	else 
		return file.length()/1024 + "kb";
	}

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
   
}
