package com.practice.spring.security.service;

import com.practice.spring.security.mapper.NoteMapper;
import com.practice.spring.security.mapper.NoticeMapper;
import com.practice.spring.security.model.vo.NoteVO;
import com.practice.spring.security.model.vo.UserVO;
import lombok.RequiredArgsConstructor;
import com.practice.spring.security.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    /**
     * 노트 조회
     * 유저는 본인의 노트만 조회할 수 있다.
     * 어드민은 모든 노트를 조회할 수 있다.
     *
     * @param user 노트를 찾을 유저
     * @return 유저가 조회할 수 있는 모든 노트 List
     */
    @Transactional(readOnly = true)
    public List<NoteVO> findByUser(UserVO user) {
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (user.isAdmin()) {
            return noteMapper.selectNoteAll();
        }
        return noteMapper.selectNoteUserDesc(user.getUsername());
    }

    /**
     * 노트 저장
     *
     * @param user    노트 저장하는 유저
     * @param title   제목
     * @param content 내용
     * @return 저장된 노트
     */
    public void saveNote(UserVO user, String title, String content) {
        if (user == null) {
            throw new UserNotFoundException();
        }

        String username = user.getUsername();
        noteMapper.insertNote(new NoteVO(title, content, username));
    }

    /**
     * 노트 삭제
     *
     * @param user   삭제하려는 노트의 유저
     * @param noteId 노트 ID
     */
    public void deleteNote(UserVO user, Long noteId) {
        if (user == null) {
            throw new UserNotFoundException();
        }
        NoteVO note = noteMapper.selectNoteUserOne(noteId, user.getUsername());
        if (note != null) {
            noteMapper.deleteNote(noteId);
        }
    }
}
