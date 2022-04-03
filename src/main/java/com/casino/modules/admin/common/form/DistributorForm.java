package com.casino.modules.admin.common.form;

import java.util.Date;

import lombok.Data;

@Data
public class DistributorForm {
	
	private static final Integer isMoney = 0;
	
    private static final Integer isPoint = 1;
	
	private String seq;
	
	private Integer userType;
	
	private String subHeadquarter;
	
	private String headquarterSeq;
	
	private String id;
	
	private String nickname;
	
	private Float moneyAmount;
	
	private Integer storeCount;
	
	private Integer memberCount;
	
	private Integer depositMemberCount;
	
	private Integer depositMemberAmount;
	
	private Float depositPartnerAmount;
	
	private Float depositPayment;
	
	private Float withdrawalPayment;
	
	private Float withdrawalPartnerAmount;
	
	private Float withdrawalMemberAmount;
	
	private Float betAmount;
	
	private Float winningAmount;
	
	private Float LostAmount;
	
	private Float batRolling;
	
	private Float settlementRate;
	
	private Float slotRate;
	
	private Float baccaratRate;
	
	private Float losingAmount;
	
	private Float rollingAmount;
	
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
