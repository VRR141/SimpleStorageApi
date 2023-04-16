package org.vrr.simplecloudservice.service.impl;

import com.google.common.collect.Streams;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vrr.simplecloudservice.domain.MinioFile;
import org.vrr.simplecloudservice.service.CloudStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

            //TODO add exception

            Streams.stream(result).sorted((i1, i2) -> {
                try {
                    return i2.get().lastModified().compareTo(i1.get().lastModified());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).limit(limit).forEach((i) -> {
                try {
                    objects.add(MinioFile.builder()
                            .fileName(i.get().objectName())
                            .size(i.get().size())
                            .build());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            return objects;
        } catch (Exception e) {
            log.error("Happened error when get list objects from minio: ", e);
        }
        return objects;
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
            return null;
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
            log.error("Happened error when upload file: ", e);
        }
    }

    public void deleteFile(String bucketName, String fileName){
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            deleteOldFile(bucketName, fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteOldFile(String bucketName, String fileName) throws Exception{
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName).build());
    }

    public void createBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when get creat bucket {}", bucketName, e);
        }
    }
}
