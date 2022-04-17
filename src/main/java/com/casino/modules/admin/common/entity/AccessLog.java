package com.casino.modules.admin.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@TableName("access_log")
@Data
public class AccessLog implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@TableField(value = "seq")
	private String seq;
	
	@TableField(value = "site")
	private String site;
	
	@TableField(value = "member_seq")
	private String memberSeq;
	
	@TableField(value = "id")
	private String id;
	
	@TableField(value = "nickname")
	private String nickname;
	
	@TableField(value = "account_holder")
	private String accountHolder;
	
	@TableField(value = "level")
	private String level;
	
	@TableField(value = "money_amount")
	private Float moneyAmount;
	
	@TableField(value = "connection_ip")
	private String connectionIp;
	
	@TableField(value = "connection_domain")
	private String connectionDomain;
	
	@TableField(value = "status")
	private Integer status;
	
	@TableField(value = "distributor")
	private String distributor;

	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	@TableField(value = "connection_date")
	private Date connectionDate;
	
	@TableField(exist = false)
	private String searchValue;
	
	@TableField(exist = false)
	private String searchField;
	
	@TableField(exist = false)
	private String subHeadquarterSeq;
	
	@TableField(exist = false)
	private String distributorName;
	
}
