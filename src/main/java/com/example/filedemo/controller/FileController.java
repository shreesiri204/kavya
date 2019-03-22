package com.example.filedemo.controller;

import com.example.filedemo.payload.UploadFileResponse;
import com.example.filedemo.service.FileStorageService;
import com.example.filedemo.list.ListFiles;
import com.example.filedemo.list.VideoList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.file.Path;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;
    
    private List<ListFiles> listFiles;


    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }
    
    
    @PostMapping("/transcodeFile")
    public  void transcodeFile(@RequestParam("filename") String filename) {
        String fileName = fileStorageService.getFilePath(filename);
        String processedFilePath = fileStorageService.getprocessedFilePath(filename);
        String command = "ffmpeg -y -i"+ fileName + "-c:v libx264 -x264opts \"keyint=24:min-keyint=24:no-scenecut\" -r 24 -c:a aac -b:a 128k -strict -2 -bf 1 -b_strategy 0 -sc_threshold 0 -pix_fmt yuv420p -map 0:v:0 -map 0:a:0 -map 0:v:0 -map 0:a:0 -map 0:v:0 -map 0:a:0 -b:v:0 400k  -filter:v:0 \"scale=-2:240\" -profile:v:0 baseline -b:v:1 700k  -filter:v:1 \"scale=-2:360\" -profile:v:1 main -b:v:1 1250k  -filter:v:1 \"scale=-2:480\" -profile:v:1 main -b:v:2 2500k -filter:v:2 \"scale=-2:720\" -profile:v:2 high" +processedFilePath;
        try {
			Runtime.getRuntime().exec(new String[] {"cmd", command, "Start"});
			
		} catch (IOException e) {

			logger.info("Problem while executing the command." + command);
		}
     }
    
    @GetMapping(path = "/listFiles")
    public VideoList listFiles() {
       VideoList lists= fileStorageService.getAllVideos();
       return lists;
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
