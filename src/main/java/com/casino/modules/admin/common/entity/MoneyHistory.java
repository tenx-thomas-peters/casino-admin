package com.casino.modules.admin.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@TableName("money_history")
@Data
public class MoneyHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableField(exist = false)
	private Integer checkTimeTypeApplication = 0;
	@TableField(exist = false)
	private Integer checkTimeTypeProgress = 1;

	@TableField(exist = false)
	private Integer searchTypeMember = 0;
	@TableField(exist = false)
	private Integer searchTypeAdminEdit = 1;
	@TableField(exist = false)
	private Integer searchTypeDistributor = 2;

	@TableField(exist = false)
	private Integer idOrNickNameId = 0;
	@TableField(exist = false)
	private Integer idOrNickNameNickName = 1;
	@TableField(exist = false)
	private Integer idOrNickNameAccountHolder = 2;

	@TableField(exist = false)
	private Integer partnerOrMemberMember = 0;
	@TableField(exist = false)
	private Integer partnerOrMemberPartner = 1;

	@TableField(exist = false)
	private Integer orderTypeApplicationTime = 0;
	@TableField(exist = false)
	private Integer orderTypeLevelUp = 1;
	@TableField(exist = false)
	private Integer orderTypeLevelDown = 2;

	@TableId(value = "seq")
	private String seq;

	@TableField(value = "payer")
	private String payer;
	
	@TableField(value = "receiver")
	private String receiver;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(value = "application_time")
	private Date applicationTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(value = "process_time")
	private Date processTime;

	@TableField(value = "prev_amount")
	private Float prevAmount;
	
	@TableField(value = "variable_amount")
	private Float variableAmount;
	
	@TableField(value = "actual_amount")
	private Float actualAmount;
	
	@TableField(value = "final_amount")
	private Float finalAmount;
	
	@TableField(value = "bonus")
	private Float bonus;
	
	@TableField(value = "status")
	private Integer status;
	
	@TableField(value = "reason_type")
	private Integer reasonType;
	
	@TableField(value = "reason")
	private String reason;
	
	@TableField(value = "charge_count")
	private Integer chargeCount;
	
	@TableField(value = "operation_type")
	private Integer operationType;
	
	@TableField(value = "money_or_point")
	private Integer moneyOrPoint;
	
	@TableField(value = "note")
	private String note;

	@TableField(value = "create_by")
	private String createBy;

	@TableField(value = "first_charge")
	private Integer firstCharge;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@TableField(value = "create_time")
	private Date createTime;

	@TableField(value = "update_by")
	private String updateBy;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@TableField(value = "update_time")
	private Date updateTime;
	
	@TableField(exist = false)
	private Member member;
	
	@TableField(exist = false)
	private String partnerName;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@TableField(exist = false)
	private String fromProcessTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@TableField(exist = false)
	private String toProcessTime;
	
	@TableField(exist = false)
	private Integer IDOrNickname;
	
	@TableField(exist = false)
	private String IDOrNickNameValue;
	
	@TableField(exist = false)
	private String checkDay;
	
	@TableField(exist = false)
	private Integer searchType;
	
	@TableField(exist = false)
	private Integer checkTimeType;
	
	@TableField(exist = false)
	private Integer partnerOrMember;
	
	@TableField(exist = false)
	private Integer orderType;
	
	@TableField(exist = false)
	private String accountHolder;
	
	@TableField(exist = false)
	private String bankName;
}
