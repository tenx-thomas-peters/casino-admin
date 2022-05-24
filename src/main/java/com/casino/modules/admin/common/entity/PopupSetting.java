package com.casino.modules.admin.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@TableName("popup_setting")
@Data
public class PopupSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(value = "seq")
	private String seq;

	@TableField(value = "site_seq")
	private String siteSeq;

	@TableField(value = "type")
	private String type;

	@TableField(value = "title")
	private String title;

	@TableField(value = "content")
	private String content;

	@TableField(value = "location")
	private String location;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@TableField(value = "additional_date")
	private Date additionalDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@TableField(value = "expiration_start")
	private Date expirationStart;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@TableField(value = "expiration_end")
	private Date expirationEnd;

	@TableField(exist=false)
	private String xaxios;

	@TableField(exist=false)
	private String yaxios;

	@TableField(exist=false)
	private String width;

	@TableField(exist=false)
	private String height;

}
