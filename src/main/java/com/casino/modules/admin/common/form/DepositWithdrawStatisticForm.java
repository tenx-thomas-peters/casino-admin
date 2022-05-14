package com.casino.modules.admin.common.form;

import java.util.Date;

import lombok.Data;

@Data
public class DepositWithdrawStatisticForm {
	
	Date dates;
	Float slotBet;
	Float slotWin;
	Float slotLost;
	Float baccaratBet;
	Float baccaratWin;
	Float baccaratLost;

	Float deposit;
	Integer depositCount;
	Float withdraw;
	Integer withdrawCount;
	Float distributorDeposit;
	Float distributorWithdraw;
	Float managerDeposit;
	Float managerWithdraw;
	
	Float pointDeposit;
	Float pointWithdraw;
	
	String startDate;
	String endDate;
	
	Integer checkTimeType;
	
	Float totalAmount;
}
