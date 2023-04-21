package org.vrr.simplecloudservice.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.vrr.simplecloudservice.domain.MinioFile;
import org.vrr.simplecloudservice.dto.response.FileResponseDto;
import org.vrr.simplecloudservice.excecption.InvalidFileIdentifierException;
import org.vrr.simplecloudservice.mapper.FileMapper;
import org.vrr.simplecloudservice.security.AuthProvider;
import org.vrr.simplecloudservice.service.CloudStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FileFacade {

    private final CloudStorageService cloudStorageService;

    private final AuthProvider authProvider;

    private final FileMapper fileMapper;

    public Iterable<FileResponseDto> getLimitedList(int limit){
        List<MinioFile> listObjects = cloudStorageService
                .getLimitedList(getBucketName(), limit);
        return listObjects.stream().map(s -> fileMapper.mapMinioToFileResponseDto(s)).collect(Collectors.toList());
    }

    public void uploadFile(String fileName, MultipartFile file){
        cloudStorageService.uploadFile(file, getBucketName(), fileName);
    }

    public void deleteFile(String fileName){
        cloudStorageService.deleteFile(getBucketName(), fileName);
    }

    public Resource downloadObject(String fileName) {
        try (InputStream inputStream = cloudStorageService.getObject(fileName, getBucketName())) {
            byte[] res = inputStream.readAllBytes();
            ByteArrayResource resource = new ByteArrayResource(res);
            return resource;
        } catch (IOException e) {
            throw new InvalidFileIdentifierException();
        }
    }

    public void renameFile(String fileName, String newFileName){
        cloudStorageService.renameFile(getBucketName(), fileName, newFileName);
    }

    private String getBucketName(){
        return String.valueOf(authProvider.getAuthorizedUserUuid());
    }
}
