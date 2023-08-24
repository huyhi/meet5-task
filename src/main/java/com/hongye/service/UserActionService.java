package com.hongye.service;

import com.hongye.constant.UserActionTypeEnum;
import com.hongye.constant.UserStatusEnum;
import com.hongye.dao.UserActionDAO;
import com.hongye.dao.UserDAO;
import com.hongye.dto.UserActionDTO;
import com.hongye.model.User;
import com.hongye.model.UserAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class UserActionService {

    @Autowired
    private UserActionDAO userActionDAO;

    @Autowired
    private UserDAO userDAO;

    @Value("${app.user.fraudThreshold}")
    private int fraudThreshold;

    @Value("${app.user.fraudCheckMinutes}")
    private int fraudCheckMinutes;

    // Use the @Transactional annotation to start a db transaction
    @Transactional(rollbackFor = Throwable.class)
    public void record(UserActionDTO userAction, UserActionTypeEnum actionType) {
        userActionDAO.singleRecord(UserAction.builder()
                .fromId(userAction.getFromUserId())
                .toId(userAction.getToUserId())
                .type(actionType.getCode())
                .build()
        );

        // check if the user is 'Fraud', if is, than mark user status as 'Fraud'
        checkAndMarkFraud(userAction.getFromUserId());
    }


    public void checkAndMarkFraud(int userId) {
        User user = userDAO.queryById(userId);
        if (user == null) {
            throw new RuntimeException(String.format("user not found. userId: %d", userId));
        }
        // if the user is already mark as 'Fraud' or it has been more than 10 minutes since user registration
        // then do not need to check if this user is 'Fraud'
        if (user.getStatus() == UserStatusEnum.FRAUD.getCode()) {
            return;
        }
        Date fraudCheckMinutesLater = addMinutes(user.getCreatedAt(), fraudCheckMinutes);
        if (new Date().after(fraudCheckMinutesLater)) {
            return;
        }

        int userInteractedNum = userActionDAO.distinctUserCount(userId, user.getCreatedAt(), fraudCheckMinutesLater);
        if (userInteractedNum > fraudThreshold) {
            userDAO.updateUserStatus(userId, UserStatusEnum.FRAUD.getCode());
        }
    }


    private Date addMinutes(Date time, int minutes) {
        long newTs = time.getTime() + minutes * 60 * 1000;  // minutes in milliseconds
        return new Date(newTs);
    }
}
