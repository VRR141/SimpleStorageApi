package org.vrr.simplecloudservice.service.impl;

import com.google.common.collect.Streams;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vrr.simplecloudservice.domain.MinioFile;
import org.vrr.simplecloudservice.excecption.InvalidFileIdentifierException;
import org.vrr.simplecloudservice.service.CloudStorageService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudStorageServiceImpl implements CloudStorageService {

    private final MinioClient minioClient;

    public List<MinioFile> getLimitedList(String bucketName, int limit) {
        List<MinioFile> objects = new ArrayList<>();
        try {
            Iterable<Result<Item>> result = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .recursive(true)
                            .build());
            Streams.stream(result).sorted((i1, i2) -> {
                try {
                    return i2.get().lastModified().compareTo(i1.get().lastModified());
                } catch (Exception e) {
                    log.error("Can't load file list for {}", bucketName);
                    throw new RuntimeException(e);
                }
            }).limit(limit).forEach((i) -> {
                try {
                    objects.add(MinioFile.builder()
                            .fileName(i.get().objectName())
                            .size(i.get().size())
                            .build());
                } catch (Exception e) {
                    log.error("Can't load file list for {}", bucketName);
                    throw new RuntimeException();
                }
            });
            return objects;
        } catch (Exception e) {
            log.error("Can't load file list for {}", bucketName);
            throw new InvalidFileIdentifierException();
        }
    }

    public InputStream getObject(String filename, String bucketName) {
        InputStream stream;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when get list objects from minio: ", e);
            throw new InvalidFileIdentifierException();
        }

        return stream;
    }

    public void uploadFile(MultipartFile file, String bucketName, String fileName) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when upload file: {} for bucket {}", file, bucketName);
            throw new InvalidFileIdentifierException();
        }
    }

    public void deleteFile(String bucketName, String fileName){
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName).build());
        } catch (Exception e) {
            log.error("Error while delete file {} from bucket {}", fileName, bucketName);
            throw new InvalidFileIdentifierException();
        }
    }

    public void renameFile(String bucketName, String fileName, String newFileName){
        try {
            minioClient.copyObject(CopyObjectArgs.builder()
                    .bucket(bucketName)
                    .object(newFileName)
                    .source(CopySource.builder()
                            .bucket(bucketName)
                            .object(fileName).build())
                    .build());
            deleteFile(bucketName, fileName);
        } catch (Exception e) {
            log.error("Error while rename file {} in bucket {}", fileName, bucketName);
            throw new InvalidFileIdentifierException();
        }
    }

    public void createBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when creat bucket {}", bucketName, e);
            throw new InvalidFileIdentifierException();
        }
    }
}
