package com.casino.modules.admin.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@TableName("mileage_history")
@Data
public class MileageHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(value = "seq")
	private String seq;

	@TableField(value = "member_seq")
	private String memberSeq;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(value = "process_time")
	private Date processTime;

	@TableField(value = "prev_amount")
	private Float prevAmount;
	
	@TableField(value = "variable_amount")
	private Float variableAmount;
	
	@TableField(value = "final_amount")
	private Float finalAmount;
	
	@TableField(value = "reason_type")
	private Integer reasonType;
	
	@TableField(value = "reason")
	private String reason;

	@TableField(value = "operation_type")
	private Integer operationType;
	
	@TableField(value = "note")
	private String note;

	@TableField(value = "create_by")
	private String createBy;

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
	private String checkDay;
	
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
	private Integer orderType;
}
