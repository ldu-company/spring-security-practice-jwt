package com.practice.spring.security.mapper;

import com.practice.spring.security.model.vo.NoticeVO;
import com.practice.spring.security.model.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {

    public List<NoticeVO> selectNoticeAll();

    public int insertNotice(NoticeVO noticeVO);

    public int deleteNotice(Long id);

}
