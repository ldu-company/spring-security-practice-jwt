package com.practice.spring.security.service;

import com.practice.spring.security.mapper.NoticeMapper;
import com.practice.spring.security.model.vo.NoticeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    /**
     * 모든 공지사항 조회
     *
     * @return 모든 공지사항 List
     */
    @Transactional(readOnly = true)
    public List<NoticeVO> findAll() {
        return noticeMapper.selectNoticeAll();
    }

    /**
     * 공지사항 저장
     *
     * @param title   제목
     * @param content 내용
     * @return 저장된 공지사항
     */
    public int saveNotice(String title, String content) {
        return noticeMapper.insertNotice(NoticeVO.builder()
                .title(title)
                .content(content)
                .build());
    }

    /**
     * 공지사항 삭제
     *
     * @param id ID
     */
    public void deleteNotice(Long id) {
        noticeMapper.deleteNotice(id);
    }
}
