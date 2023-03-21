package com.practice.spring.security.controller;

import com.practice.spring.security.model.vo.NoticeVO;
import com.practice.spring.security.service.NoticeService;
import lombok.RequiredArgsConstructor;
import com.practice.spring.security.model.dto.NoteRegisterDto;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 공지사항 서비스 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 공지사항 조회
     *
     * @return notice/index.html
     */
    @GetMapping
    public String getNotice(Model model) {
        SecurityContext securityContextx = SecurityContextHolder.getContext();
        List<NoticeVO> notices = noticeService.findAll();
        model.addAttribute("notices", notices);
        return "notice/index";
    }

    /**
     * 공지사항 등록
     *
     * @param noteDto 노트 등록 Dto
     * @return notice/index.html refresh
     */
    @PostMapping
    public String postNotice(@ModelAttribute NoteRegisterDto noteDto) {
        noticeService.saveNotice(noteDto.getTitle(), noteDto.getContent());
        return "redirect:notice";
    }

    /**
     * 공지사항 삭제
     *
     * @param id 공지사항 ID
     * @return notice/index.html refresh
     */
    @DeleteMapping
    public String deleteNotice(@RequestParam Long id) {
        noticeService.deleteNotice(id);
        return "redirect:notice";
    }
}
