package com.casino.modules.admin.common.form;

import java.util.Date;

import lombok.Data;

@Data
public class HeadquarterForm {
	
	private static final Integer isMoney = 0;
	
    private static final Integer isPoint = 1;
	
    private String seq;
	
	private String id;
	
	private Integer userType;
	
	private String nickname;
	
	private Float moneyAmount;
	
	private Integer distributorCount;
	
	private Integer storeCount;
	
	private Integer memberCount;
	
	private Integer depositMemberCount;
	
	private Float depositMemberAmount;
	
	private Float depositPartnerAmount;
	
	private Float depositPayment;
	
	private Float withdrawalPayment;
	
	private Float withdrawalMemberAmount;
	
	private Float withdrawalPartnerAmount;
	
	private Float slotBettingAmount;
	private Float baccaratBettingAmount;

	private Float slotWinningAmount;
	private Float baccaratWinningAmount;

	private Float slotLostAmount;
	private Float baccaratLostAmount;

	private Float slotHeadquarterRollingAmount;
	private Float baccaratHeadquarterRollingAmount;

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
	
	private Float processedRolling;

}
