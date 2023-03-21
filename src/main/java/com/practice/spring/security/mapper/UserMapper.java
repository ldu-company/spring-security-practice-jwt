package com.practice.spring.security.mapper;

import com.practice.spring.security.model.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    public UserVO selectUsername(String name);

    public int insertUser(UserVO userVO);

}
