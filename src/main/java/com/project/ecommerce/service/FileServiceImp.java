package com.project.ecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImp implements   FileService{
    public String uploadImage(String path, MultipartFile image) throws IOException {
        String oldImage=image.getOriginalFilename();
        String endName=oldImage.substring(oldImage.lastIndexOf('.'));
        String newFileName= UUID.randomUUID().toString()+endName;
        String filePath=path+ File.separator+newFileName;
        File file=new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        Files.copy(image.getInputStream(), Paths.get(filePath));
        return  newFileName;
    }
}
