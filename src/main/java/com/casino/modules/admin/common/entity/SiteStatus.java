package com.casino.modules.admin.common.entity;

import java.util.Date;

import lombok.Data;

@Data
public class SiteStatus {

	private Date eachDate;
	
	private Integer newMember;
	
	private Integer connectedMember;
	
	private Integer depositMember;
	
	private Integer bettingMember;
	
	private Float depositAmount;
	
	private Integer depositNumber;
	
	private Float withDrawalAmount;
	
	private Integer withDrawalNumber;
	
	private Float slotBettingAmount;
	
	private Integer slotBettingNumber;
	
	private Float baccaratBettingAmount;
	
	private Integer baccaratBettingNumber;
	
	private String startDate;
	
	private String endDate;
	
}
