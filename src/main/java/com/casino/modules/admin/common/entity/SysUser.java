package com.casino.modules.admin.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_user")
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "seq")
    private String seq;

    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "password")
    private String password;

    @TableField(value = "create_by")
    private String createBy;

    @TableField(value = "create_date")
    @JsonFormat(pattern = "yy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
    private Date createDate;

    @TableField(value = "update_by")
    private String updateBy;

    @TableField(value = "update_date")
    @JsonFormat(pattern = "yy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
    private Date updateDate;
}
