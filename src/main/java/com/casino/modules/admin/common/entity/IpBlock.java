package com.casino.modules.admin.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@TableName("ip_block")
@Data
public class IpBlock implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@TableField(value = "seq")
	private String seq;
	
	@TableField(value = "ip_address")
	private String ipAddress;
	
	@TableField(value = "cause")
	private String cause;
	
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	@TableField(value = "hour")
	private Date hour;
	
	@TableField(value = "state")
	private String state;
	
}
