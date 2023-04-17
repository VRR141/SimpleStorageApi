package org.vrr.simplecloudservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vrr.simplecloudservice.dto.request.RenameFileRequestDto;
import org.vrr.simplecloudservice.dto.response.FileResponseDto;
import org.vrr.simplecloudservice.facade.FileFacade;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FilesController {
    private final FileFacade fileFacade;

    @GetMapping("/list")
    public ResponseEntity<Iterable<FileResponseDto>> getFiles(@RequestParam(name = "limit") Integer limit){
        return ResponseEntity.ok(fileFacade.getLimitedList(limit));
    }

    @PostMapping("/file")
    public ResponseEntity<Void> uploadFile(@RequestPart MultipartFile file,
                                           @RequestParam(name = "filename") String fileName){
        fileFacade.uploadFile(fileName, file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@RequestParam(name = "filename") String fileName) {
        fileFacade.deleteFile(fileName);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> downloadFile(@RequestParam(name = "filename") String fileName) throws IOException {
        Resource resource = fileFacade.downloadObject(fileName);
        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @PutMapping("/file")
    public ResponseEntity<Void> renameFile(@RequestParam(name = "filename") String fileName,
                                           @RequestBody RenameFileRequestDto dto){
        fileFacade.renameFile(fileName, dto.getNewFileName());
        return ResponseEntity.ok().build();
    }
}
