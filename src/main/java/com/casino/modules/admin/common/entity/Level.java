package com.casino.modules.admin.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@TableName("level")
@Data
public class Level implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "seq")
	private String seq;
	
	@TableField(value = "level")
	private Integer levelNo;

	@TableField(value = "level_name")
	private String levelName;

	@JsonFormat(pattern = "yyyy-MM-dd hh:MM:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
	@TableField(value = "access_time")
	private Date accessTime;
	
	@TableField(value = "first_insect")
	private Float firstInsect;
	
	@TableField(value = "caterpillar")
	private Float caterpillar;
	
	@TableField(value = "correction")
	private Integer correction;
}
