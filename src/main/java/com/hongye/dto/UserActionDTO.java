package com.hongye.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserActionDTO {

    @NotNull
    private Integer fromUserId;

    @NotNull
    private Integer toUserId;

}
