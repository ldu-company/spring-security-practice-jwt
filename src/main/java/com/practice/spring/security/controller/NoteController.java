package com.practice.spring.security.controller;

import com.practice.spring.security.model.dto.NoteRegisterDto;
import com.practice.spring.security.model.vo.NoteVO;
import com.practice.spring.security.model.vo.UserVO;
import com.practice.spring.security.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;

    /**
     * 노트(게시글) 조회
     *
     * @return 노트 view (note/index.html)
     */
    @GetMapping
    public String getNote(Authentication authentication, Model model) {
        System.out.println("!!! "+ authentication.getPrincipal());
        UserVO user = (UserVO) authentication.getPrincipal();
        List<NoteVO> notes = noteService.findByUser(user);
        // note/index.html 에서 notes 사용가능
        model.addAttribute("notes", notes);
        // note/index.html 제공
        return "note/index";
    }

    /**
     * 노트 저장
     */
    @PostMapping
    public String saveNote(Authentication authentication, @ModelAttribute NoteRegisterDto noteDto) {
        UserVO user = (UserVO) authentication.getPrincipal();
        noteService.saveNote(user, noteDto.getTitle(), noteDto.getContent());
        return "redirect:note";
    }

    /**
     * 노트 삭제
     */
    @DeleteMapping
    public String deleteNote(Authentication authentication, @RequestParam Long id) {
        UserVO user = (UserVO) authentication.getPrincipal();
        noteService.deleteNote(user, id);
        return "redirect:note";
    }
}

