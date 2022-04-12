package com.casino.modules.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.ibatis.ognl.MemberAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.modules.admin.common.entity.Dict;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.common.entity.MoneyHistory;
import com.casino.modules.admin.mapper.MemberMapper;
import com.casino.modules.admin.mapper.MoneyHistoryMapper;
import com.casino.modules.admin.service.IDictService;
import com.casino.modules.admin.service.IMockService;

@Service
public class MockServiceImpl  implements IMockService {
	
	@Autowired
    private IDictService dictService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private MoneyHistoryMapper moneyHistorymapper;
	
	@Autowired
	private MemberMapper memberMapper;

	@Override
	@Transactional(readOnly = false)
	public void updateMemberMoney(Member member, String moneyAmount, Integer operationType, Integer reasonType, String note) {
		QueryWrapper<Dict> qw = new QueryWrapper<>();
		qw.eq("dict_key", CommonConstant.DICT_KEY_MONEY_REASON);
		qw.eq("dict_value", reasonType);
		List<Dict> reasonList = dictService.list(qw);
		String reasonStrKey = reasonList.get(0).getStrValue();
		List<String> params = new ArrayList<String>();
		params.add(moneyAmount);
		String reason = messageSource.getMessage(reasonStrKey, params.toArray(), Locale.ENGLISH);
		
		MoneyHistory entity = new MoneyHistory();
		entity.setSeq(UUIDGenerator.generate());
		entity.setReceiver(member.getSeq());
		entity.setApplicationTime(new Date());
		entity.setPrevAmount(member.getMoneyAmount());
		entity.setVariableAmount(Float.valueOf(moneyAmount));
		entity.setStatus(CommonConstant.MONEY_HISTORY_STATUS_IN_PROGRESS);
		entity.setReasonType(reasonType);
		entity.setReason(reason);
		entity.setOperationType(operationType);
		entity.setMoneyOrPoint(CommonConstant.MONEY_OR_POINT_MONEY);
		entity.setNote(note);
		moneyHistorymapper.insert(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void moneyTransfer(String payerSeq, String receiverSeq, String moneyAmount, String note) {
		QueryWrapper<Dict> qw = new QueryWrapper<>();
		qw.eq("dict_key", CommonConstant.DICT_KEY_MONEY_REASON);
		qw.eq("dict_value", CommonConstant.MONEY_REASON_TRANSFER);
		List<Dict> reasonList = dictService.list(qw);
		String reasonStrKey = reasonList.get(0).getStrValue();
		List<String> params = new ArrayList<String>();
		params.add(moneyAmount);
		String reason = messageSource.getMessage(reasonStrKey, params.toArray(), Locale.ENGLISH);
		
		
		Member payer = memberMapper.selectById(payerSeq);
		Float payerFinalAmount = payer.getMoneyAmount() - Float.valueOf(moneyAmount);
		MoneyHistory payerMoneyHistory = new MoneyHistory();
		payerMoneyHistory.setSeq(UUIDGenerator.generate());
		payerMoneyHistory.setPayer(payerSeq);
		payerMoneyHistory.setReceiver(payerSeq);
		payerMoneyHistory.setApplicationTime(new Date());
		payerMoneyHistory.setPrevAmount(payer.getMoneyAmount());
		payerMoneyHistory.setVariableAmount(Float.valueOf(moneyAmount));
		payerMoneyHistory.setActualAmount(Float.valueOf(moneyAmount));
		payerMoneyHistory.setFinalAmount(payerFinalAmount);
		payerMoneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_COMPLETE);
		payerMoneyHistory.setReasonType(CommonConstant.MONEY_REASON_TRANSFER);
		payerMoneyHistory.setReason(reason);
		payerMoneyHistory.setOperationType(CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_OUT);
		payerMoneyHistory.setMoneyOrPoint(CommonConstant.MONEY_OR_POINT_MONEY);
		payerMoneyHistory.setNote(note);
		moneyHistorymapper.insert(payerMoneyHistory);
		payer.setMoneyAmount(payerFinalAmount);
		memberMapper.updateById(payer);
		
		Member receiver = memberMapper.selectById(receiverSeq);
		Float receiverFinalAmount = (receiver.getMoneyAmount() == null ? 0 : receiver.getMoneyAmount()) + Float.valueOf(moneyAmount);
		MoneyHistory receiverMoneyHistory = new MoneyHistory();
		receiverMoneyHistory.setSeq(UUIDGenerator.generate());
		receiverMoneyHistory.setPayer(payerSeq);
		receiverMoneyHistory.setReceiver(receiverSeq);
		receiverMoneyHistory.setApplicationTime(new Date());
		receiverMoneyHistory.setPrevAmount(receiver.getMoneyAmount());
		receiverMoneyHistory.setVariableAmount(Float.valueOf(moneyAmount));
		receiverMoneyHistory.setActualAmount(Float.valueOf(moneyAmount));
		receiverMoneyHistory.setFinalAmount(receiverFinalAmount);
		receiverMoneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_COMPLETE);
		receiverMoneyHistory.setReasonType(CommonConstant.MONEY_REASON_TRANSFER);
		receiverMoneyHistory.setReason(reason);
		receiverMoneyHistory.setOperationType(CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN);
		receiverMoneyHistory.setMoneyOrPoint(CommonConstant.MONEY_OR_POINT_MONEY);
		receiverMoneyHistory.setNote(note);
		moneyHistorymapper.insert(receiverMoneyHistory);
		receiver.setMoneyAmount(receiverFinalAmount);
		memberMapper.updateById(receiver);
	}
	
}
