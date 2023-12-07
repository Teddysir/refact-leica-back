package com.example.leica_refactoring.service;

import com.example.leica_refactoring.dto.member.MemberLoginRequestDto;
import com.example.leica_refactoring.dto.member.MemberLoginResponseDto;
import com.example.leica_refactoring.entity.Member;
import com.example.leica_refactoring.jwt.JwtTokenProvider;
import com.example.leica_refactoring.repository.MemberRepository;
import com.example.leica_refactoring.service.jwt.RedisService;
import com.example.leica_refactoring.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    public MemberLoginResponseDto login(MemberLoginRequestDto requestDto, HttpServletResponse response) {

        Member member = memberRepository.findByMemberId(requestDto.getMemberId());

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException();
        }

        this.setJwtTokenInHeader(requestDto.getMemberId(), response);
        return MemberLoginResponseDto.builder()
                .responseCode("200")
                .build();
    }

    private void setJwtTokenInHeader(String memberId, HttpServletResponse response) {
        UserRole userRole = memberRepository.findByMemberId(memberId).getUserRole();

        String accessToken = jwtTokenProvider.createAccessToken(memberId, userRole);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId, userRole);

        jwtTokenProvider.setHeaderAccessToken(response, accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);
        redisService.setValues(refreshToken, memberId);

    }

    public Member findMemberByToken(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveAccessToken(request);
        return token == null ? null : memberRepository.findByMemberId(jwtTokenProvider.getMemberId(token));
    }

    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        String newAccessToken = jwtTokenProvider.reissueAccessToken(refreshToken);
        String newRefreshToken = jwtTokenProvider.reissueRefreshToken(refreshToken);

        jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, newRefreshToken);
    }

}