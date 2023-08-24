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
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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

    @Value("${app.user.bulkInsertPerBatch}")
    private int bulkInsertPerBatch;

    // Use the @Transactional annotation to start a db transaction
    @Transactional(rollbackFor = Throwable.class)
    public void record(UserActionDTO userAction, UserActionTypeEnum actionType) {
        List<User> userList = userDAO.queryByIdList(
                Arrays.asList(userAction.getFromUserId(), userAction.getToUserId())
        );
        Map<Integer, User> userIdMap = userList.stream().collect(
                Collectors.toMap(User::getId, item -> item)
        );

        // check if the user ID requested exist to ensure data consistency and integrity.
        // userList.size() < 2 indicate that at least one of the queried IDs does not exist.
        if (userList.size() < 2) {
            List<Integer> userIdNotExist = new ArrayList<>();
            if (! userIdMap.containsKey(userAction.getFromUserId())) {
                userIdNotExist.add(userAction.getFromUserId());
            }
            if (! userIdMap.containsKey(userAction.getToUserId())) {
                userIdNotExist.add(userAction.getToUserId());
            }
            throw new RuntimeException(String.format("userId not exist. userId: %s", userIdNotExist));
        }

        userActionDAO.singleInsert(UserAction.builder()
                .fromId(userAction.getFromUserId())
                .toId(userAction.getToUserId())
                .type(actionType.getCode())
                .build()
        );

        // check if the user is 'Fraud', if is, than mark user status as 'Fraud'
        checkAndMarkFraud(userIdMap.get(userAction.getFromUserId()));
    }


    public void checkAndMarkFraud(User user) {
        // if the user is already mark as 'Fraud' or it has been more than 10 minutes since user registration
        // then do not need to check if this user is 'Fraud'
        if (user.getStatus() == UserStatusEnum.FRAUD.getCode()) {
            return;
        }
        Date fraudCheckMinutesLater = addMinutes(user.getCreatedAt(), fraudCheckMinutes);
        if (new Date().after(fraudCheckMinutesLater)) {
            return;
        }

        int userInteractedNum = userActionDAO.distinctUserCount(user.getId(), user.getCreatedAt(), fraudCheckMinutesLater);
        if (userInteractedNum > fraudThreshold) {
            userDAO.updateUserStatus(user.getId(), UserStatusEnum.FRAUD.getCode());
        }
    }


    private Date addMinutes(Date time, int minutes) {
        long newTs = time.getTime() + minutes * 60 * 1000;  // minutes in milliseconds
        return new Date(newTs);
    }

    /**
     * Bulk insert userAction data implementation
     * @param userActionList The list of visit or like relationships to be inserted.
     * @return The total number of successfully inserted data records.
     */
    public int bulkInsert(List<UserAction> userActionList) {
        if (CollectionUtils.isEmpty(userActionList)) {
            return 0;
        }

        int rows = 0;
        for (int i = 0; i * bulkInsertPerBatch < userActionList.size(); i++) {
            rows += userActionDAO.batchInsert(
                    userActionList.subList(i * bulkInsertPerBatch, (i + 1) * bulkInsertPerBatch)
            );
        }
        return rows;
    }
}
