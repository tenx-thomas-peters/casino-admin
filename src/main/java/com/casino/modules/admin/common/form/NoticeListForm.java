package com.casino.modules.admin.common.form;

import java.util.Date;

import lombok.Data;

@Data
public class NoticeListForm {
	
	private Integer province;
	
	private Integer selectType;
	
	private String keyword;
	
	private Date sendTimeFrom;
	
	private Date sendTimeTo;
	
	private String site;

}
