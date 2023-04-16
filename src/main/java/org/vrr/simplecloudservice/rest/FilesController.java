package org.vrr.simplecloudservice.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.vrr.simplecloudservice.dto.response.FileResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/list")
public class FilesController {

    @GetMapping
    public ResponseEntity<Iterable<FileResponseDto>> getFiles(@RequestParam(name = "limit") Integer limit){
        Random random = new Random();
        System.out.println(limit);
        List<FileResponseDto> qq = Stream.generate(() -> {
            var s = new FileResponseDto();
            s.setFileName("qq" + random.nextInt(44) + ".pdf");
            s.setSize(random.nextInt(25));
            return s;
        }).limit(limit).toList();
//        FileResponseDto d = new FileResponseDto();
//        d.setSize(25);
//        d.setFileName("25");
        return ResponseEntity.ok(qq);
    }
}
