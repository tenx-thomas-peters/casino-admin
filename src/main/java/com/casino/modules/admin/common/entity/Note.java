package com.casino.modules.admin.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@TableName("note")
@Data
public class Note implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(value = "seq")
	private String seq;

	@TableField(exist=false)
	private String storeSeq;

	@TableField(exist=false)
	private String levelSeq;

	@TableField(value = "site")
	private String site;

	@TableField(value = "sender")
	private String sender;

	@TableField(exist=false)
	private String sender1;

	@TableField(value = "receiver")
	private String receiver;

	@TableField(value = "title")
	private String title;

	@TableField(value = "content")
	private String content;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@TableField(value = "send_time")
	private Date sendTime;
	
	@TableField(value = "lookup")
	private Integer lookUp;

	@TableField(value = "read_status")
	private Integer readStatus;
	
	@TableField(value = "answer_status")
	private Integer answerStatus;
	
	@TableField(value = "answer")
	private String answer;

	@TableField(value = "recommend_status")
	private Integer recommendStatus;

	@TableField(value = "type")
	private Integer type;

	@TableField(value = "send_type")
	private Integer sendType;

	@TableField(value = "classification")
	private Integer classification;
	
	
	@TableField(value = "upload_path")
    private String uploadPath;
	
	@TableField(exist=false)
	private String nickname;
	
	@TableField(exist=false)
	private String accountHolder;
	
	@TableField(exist=false)
	private String bankName;
	
	@JsonFormat(pattern = "yyyy-MM-dd hh:MM:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
	@TableField(exist=false)
	private Date registerDate;
	
	@TableField(exist=false)
	private String mSeq;
	
	@TableField(exist=false)
	private String mId;
	
	@TableField(exist=false)
	private String name;
	
	@TableField(exist=false)
	private String levelName;
	
	@TableField(exist=false)
	private Integer userType;
	
	@TableField(exist=false)
	private String color;
}
