package com.casino.modules.admin.common.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameInfo implements Serializable { // game server entity
    private String title;

    private String type;

    private String id;

    private String vendor;

    private String thumbnail;
}
