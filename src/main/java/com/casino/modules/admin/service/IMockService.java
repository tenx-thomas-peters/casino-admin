package com.casino.modules.admin.service;

import com.casino.modules.admin.common.entity.Member;

public interface IMockService {

	void updateMemberMoney(
			Member member, 
			String moneyAmount, 
			Integer operationType,
			Integer reasonType, 
			String note);

	void moneyTransfer(String payerSeq, String receiverSeq, String moneyAmount, String note);

}
