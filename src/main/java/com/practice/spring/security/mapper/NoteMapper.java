package com.practice.spring.security.mapper;

import com.practice.spring.security.model.vo.NoteVO;
import com.practice.spring.security.model.vo.NoticeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoteMapper {

    public List<NoteVO> selectNoteAll();

    public List<NoteVO> selectNoteUserDesc(String user);

    public NoteVO selectNoteUserOne(Long noteId, String user);

    public int insertNote(NoteVO noticeVO);

    public int deleteNote(Long id);


}
