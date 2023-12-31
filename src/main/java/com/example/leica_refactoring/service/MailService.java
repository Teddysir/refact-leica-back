package com.example.leica_refactoring.service;

import com.example.leica_refactoring.dto.mail.RequestMailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;

    @Transactional
    public String sendMailReject(RequestMailDto dto) throws Exception {

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setTo("smwfkim2@naver.com");
            simpleMailMessage.setSubject("[NTS] 견적 요청 메일입니다.");
            simpleMailMessage.setText(dto.getContent());

            javaMailSender.send(simpleMailMessage);

            return "Success!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail!";
        }
    }
}
