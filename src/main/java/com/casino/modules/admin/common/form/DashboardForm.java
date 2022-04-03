package com.casino.modules.admin.common.form;

import lombok.Data;

@Data
public class DashboardForm {
	
	private String seq;
	
	private String id;
	
	private String nickname;
	
	private String ranking;
	
	private Float totalBet;
	
	private String phoneNumber;
	
	private Float moneyAmount;
	
	private Float mileageAmount;
	
	private String levelSeq;
	
	private String levelName;
	
	private String distributorSeq;
	
	private String distributor;
	
	private Float winningAmount;
	
	private Float losingAmount;
	
	private Float rechargeAmount;
	
	private Float withdrawalAmount;
	
	private Float managerCorrection;
	
	private Float rechargeBonusAmount;
	
	private Float lotteryBonusPayment;
	
}
