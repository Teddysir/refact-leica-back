package com.example.leica_refactoring.controller;

import com.example.leica_refactoring.service.S3FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://localhost:3000", "https://www.nts-microscope.com"})
@Tag(name = "S3 Controller", description = "S3 API")
public class FileUploadController {

    private final S3FileUploadService s3FileUploadService;

    @PostMapping("/upload")
    @Operation(summary = "이미지 업로드 (유저권한 필요)")
    public String uploadFile(@RequestPart("file") MultipartFile[] files, HttpServletRequest request) throws IOException {
        String key = s3FileUploadService.uploadFile(files[0], request);
        return key; // key값 리턴해줄거임

        // postman으로 form-data 형식으로 key는 "file"로 해주고 이미지 파일 선택해서 post로 데이터 전송하면 key-"파일명.type" 형식으로 반환될거임

    }
}
