package com.hongye.controller;

import com.hongye.constant.UserActionTypeEnum;
import com.hongye.dto.UserActionDTO;
import com.hongye.service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserActionController {

    @Autowired
    private UserActionService userActionService;


    @PostMapping("/user/visit")
    public void visit(@Validated @RequestBody UserActionDTO request) {
        userActionService.record(request, UserActionTypeEnum.VISIT);
    }

    @PostMapping("/user/like")
    public void like(@Validated @RequestBody UserActionDTO request) {
        userActionService.record(request, UserActionTypeEnum.LIKE);
    }
}
