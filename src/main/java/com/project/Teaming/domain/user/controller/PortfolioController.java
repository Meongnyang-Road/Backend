package com.project.Teaming.domain.user.controller;

import com.project.Teaming.domain.user.dto.request.PortfolioDto;
import com.project.Teaming.domain.user.entity.Portfolio;
import com.project.Teaming.domain.user.service.PortfolioService;
import com.project.Teaming.global.jwt.dto.SecurityUserDto;
import com.project.Teaming.global.result.ResultCode;
import com.project.Teaming.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Portfolio", description = "사용자 정보 관련 API")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping("/user/portfolio/save")
    @Operation(summary = "포트폴리오 저장", description = "로그인 한 사용자는 본인의 자기소개와 사용 가능 스택 등을 기술한다. 기술 스택은 일단은 String으로 저장 예정")
    public ResultResponse<Void> saveInfo(@RequestBody PortfolioDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUserDto securityUser = (SecurityUserDto) authentication.getPrincipal();
        String email = securityUser.getEmail();
        log.info("SecurityContext Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
        portfolioService.savePortfolio(email, dto);
        return new ResultResponse<>(ResultCode.REGISTER_PORTFOLIO, null);
    }

    @GetMapping("/user/portfolio")
    @Operation(summary = "본인 포트폴리오 조회", description = "로그인 한 사용자는 본인의 포트폴리오를 조회할 수 있다. 개인 조회 및 수정 시 사용")
    public ResultResponse<PortfolioDto> getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUserDto securityUser = (SecurityUserDto) authentication.getPrincipal();
        String email = securityUser.getEmail();
        log.info("SecurityContext Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
        PortfolioDto dto = portfolioService.getPortfolio(email);
        return new ResultResponse<>(ResultCode.GET_USER_PORTFOLIO, List.of(dto));
    }
}
