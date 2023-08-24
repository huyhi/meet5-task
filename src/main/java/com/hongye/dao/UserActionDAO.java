package com.hongye.dao;

import com.hongye.model.User;
import com.hongye.model.UserAction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * sql defined in path: src/main/resources/mapper/UserActionMapper.xml
 */
@Mapper
@Component
public interface UserActionDAO {

    List<User> allVisitors(@Param("userId") int userId);

    int singleInsert(UserAction action);

    int batchInsert(List<UserAction> list);

    int distinctUserCount(
            @Param("fromId") int fromId,
            @Param("timeLower") Date timeLower,
            @Param("timeUpper") Date timeUpper
    );
}
