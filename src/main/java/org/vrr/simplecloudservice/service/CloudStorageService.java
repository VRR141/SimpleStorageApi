package org.vrr.simplecloudservice.service;

import org.springframework.web.multipart.MultipartFile;
import org.vrr.simplecloudservice.domain.MinioFile;

import java.io.InputStream;
import java.util.List;

public interface CloudStorageService {

    List<MinioFile> getLimitedList(String bucketName, int limit);

    InputStream getObject(String filename, String bucketName);

    void createBucket(String bucketName);

    void uploadFile(MultipartFile file, String bucketName, String fileName);

    void deleteFile(String bucketName, String fileName);

    void renameFile(String bucketName, String fileName, String newFileName);
}
