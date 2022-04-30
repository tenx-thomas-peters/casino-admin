package com.casino.modules.admin.common.form;

import lombok.Data;

@Data
public class NoteListForm {
	
	private Integer province;
	
	private Integer selectType;
	
	private String keyword;
	
	private String sendTimeFrom;
	
	private String sendTimeTo;
	
	private String site;
	
	private Integer type;

	private Integer sendType;

	private String levelType;

}
