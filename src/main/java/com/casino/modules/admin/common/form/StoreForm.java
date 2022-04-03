package com.casino.modules.admin.common.form;

import java.util.Date;

import lombok.Data;

@Data
public class StoreForm {

	private static final Integer isMoney = 0;
	
    private static final Integer isPoint = 1;
    
	private String seq;
	
	private String headquarter;
	
	private String headquarterSeq;
	
	private String distributor;
	
	private String distributorSeq;
	
	private String id;
	
	private String nickname;
	
	private Integer userType;
	
	private Float moneyAmount;
	
	private Integer memberCount;
	
	private Integer depositMemberCount;
	
	private Float depositMemberAmount;
	
	private Float depositPartnerAmount;
	
	private Float depositPayment;
	
	private Float withdrawalPayment;
	
	private Float withdrawalMemberAmount;
	
	private Float withdrawalPartnerAmount;
	
	private Float betAmount;
	
	private Float winningAmount;
	
	private Float lostAmount;
	
	private Float batRolling;
	
	private Float slotRate;
	
	private Float baccaratRate;
	
	private Integer status;
	
	private Date applicationTime;
	
	private String fromApplicationTime;
	
	private String toApplicationTime;
	
	private Date registrationDate;
	
	private Integer orderByType;
	
	private Integer selectType;
	
	private String keyword;
	
	private Integer gameType;
}
