package com.example.leica_refactoring.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.leica_refactoring.entity.Member;
import com.example.leica_refactoring.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3FileUploadService {

    private final AmazonS3 s3Client;
    private final MemberService memberService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.default-url}")
    private String defaultUrl;

    public String uploadFile(MultipartFile file, HttpServletRequest request) throws IOException {

        Member member = memberService.findMemberByToken(request);

        if (member.getUserRole() != UserRole.ADMIN) {
            throw new UsernameNotFoundException("유저가 존재하지 않습니다."); // 추후 ErrorCode 로직 수정
        } else {
            UUID uuid = UUID.randomUUID();

            String fileName = uuid.toString();

            String imageUrl = defaultUrl + fileName;
            try {
                s3Client.putObject(bucketName, fileName, file.getInputStream(), getObjectMetadata(file));
                return imageUrl;
            } catch (SdkClientException e) {
                throw new IOException("S3에 파일 업로드를 실패하였습니다.", e);
            }
        }
    }


    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

//    private String generateFileName(MultipartFile file){
//        return file.getOriginalFilename();
//    }
}