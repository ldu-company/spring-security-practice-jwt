package com.practice.spring.security.service;

import com.practice.spring.security.mapper.UserMapper;
import com.practice.spring.security.model.vo.UserVO;
import com.practice.spring.security.exception.AlreadyRegisteredUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    /**
     * 유저 등록
     *
     * @param username username
     * @param password password
     * @return 유저 권한을 가지고 있는 유저
     */
    public UserVO signup(
            String username,
            String password
    ) {
        if (userMapper.selectUsername(username) != null) {
            throw new AlreadyRegisteredUserException();
        }
        userMapper.insertUser(new UserVO(username, passwordEncoder.encode(password), "ROLE_USER"));

        return userMapper.selectUsername(username);
    }

    /**
     * 관리자 등록
     *
     * @param username username
     * @param password password
     * @return 관리자 권한을 가지고 있는 유저
     */
    public void signupAdmin(
            String username,
            String password
    ) {
        if (userMapper.selectUsername(username) != null) {
            throw new AlreadyRegisteredUserException();
        }
        userMapper.insertUser(new UserVO(username, passwordEncoder.encode(password), "ROLE_ADMIN"));
    }

    public UserVO selectUsername(String username) {
        return userMapper.selectUsername(username);
    }
}