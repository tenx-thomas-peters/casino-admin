package com.casino.modules.admin.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "dict")
public class Dict implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField(value = "dict_key")
    private String dictKey;

    @TableField(value = "dict_value")
    private Integer dictValue;

    @TableField(value = "str_key")
    private String strKey;

    @TableField(value = "str_value")
    private String strValue;
}
