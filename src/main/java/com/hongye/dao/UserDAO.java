package com.hongye.dao;

import com.hongye.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface UserDAO {

    User queryById(@Param("userId") int userId);

    List<User> queryByIdList(@Param("userIdList") List<Integer> userIdList);

    int updateUserStatus(@Param("userId") int userId, @Param("status") int status);
}
