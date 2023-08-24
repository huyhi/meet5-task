package com.hongye.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAction {

    private Integer fromId;

    private Integer toId;

    private Integer type;

}
