package com.casino.modules.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.common.entity.MileageHistory;
import com.casino.modules.admin.common.entity.MoneyHistory;
import com.casino.modules.admin.common.form.DistributorForm;
import com.casino.modules.admin.common.form.HeadquarterForm;
import com.casino.modules.admin.common.form.MemberForm;
import com.casino.modules.admin.common.form.StoreForm;
import com.casino.modules.admin.mapper.MemberMapper;
import com.casino.modules.admin.service.IMemberService;
import com.casino.modules.admin.service.IMileageHistoryService;
import com.casino.modules.admin.service.IMoneyHistoryService;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private IMoneyHistoryService moneyHistoryService;

	@Autowired
	private IMileageHistoryService mileageHistoryService;

	@Override
	public Member getRecipient(String levelSeq, String userType, String siteSeq) {
		return memberMapper.getRecipient(levelSeq, userType, siteSeq);
	}

	@Override
	public IPage<MemberForm> getMemberList(Page<MemberForm> page, MemberForm memberForm, String column, Integer order, Integer loginStatus) {
		return memberMapper.getMemberList(page, memberForm, column, order, loginStatus,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
				CommonConstant.MONEY_HISTORY_STATUS_COMPLETE);
	}

	@Override
	public MemberForm getMemberBySeq(MemberForm memberForm) {
		return memberMapper.getMemberBySeq(memberForm, CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
				CommonConstant.MONEY_HISTORY_STATUS_COMPLETE);
	}

	@Override
	public IPage<HeadquarterForm> findHeadquarterList(Page<HeadquarterForm> page, HeadquarterForm headquarter,
			String column, Integer order) {
		return memberMapper.findHeadquarterList(page, headquarter, column, order,
				CommonConstant.USER_TYPE_SUB_HEADQUARTER,
				CommonConstant.USER_TYPE_DISTRIBUTOR,
				CommonConstant.USER_TYPE_STORE,
				CommonConstant.USER_TYPE_NORMAL,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN,
				CommonConstant.MONEY_HISTORY_STATUS_COMPLETE,
				CommonConstant.SELECT_TYPE_0,
				CommonConstant.SELECT_TYPE_1, CommonConstant.SELECT_TYPE_2);
	}

	@Override
	public IPage<StoreForm> findStoreList(Page<StoreForm> page, StoreForm store, String column, Integer order) {
		return memberMapper.findStoreList(page, store, column, order,
				CommonConstant.USER_TYPE_SUB_HEADQUARTER,
				CommonConstant.USER_TYPE_DISTRIBUTOR,
				CommonConstant.USER_TYPE_STORE,
				CommonConstant.USER_TYPE_NORMAL,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN,
				CommonConstant.MONEY_HISTORY_STATUS_COMPLETE,
				CommonConstant.SELECT_TYPE_0,
				CommonConstant.SELECT_TYPE_1, CommonConstant.SELECT_TYPE_2);
	}

	@Override
	public IPage<DistributorForm> findDistributorList(Page<DistributorForm> page, DistributorForm distributor,
			String column, Integer order) {
		return memberMapper.findDistributorList(page, distributor, column, order,
				CommonConstant.USER_TYPE_SUB_HEADQUARTER,
				CommonConstant.USER_TYPE_DISTRIBUTOR,
				CommonConstant.USER_TYPE_STORE,
				CommonConstant.USER_TYPE_NORMAL,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN,
				CommonConstant.MONEY_HISTORY_STATUS_COMPLETE,
				CommonConstant.SELECT_TYPE_0,
				CommonConstant.SELECT_TYPE_1, CommonConstant.SELECT_TYPE_2);
	}

	@Override
	public List<Map<String, String>> getSiteList() {
		return memberMapper.getSiteList();
	}

	@Override
	public List<DistributorForm> getDistributorListModal(String seq, DistributorForm distributor) {
		return memberMapper.getDistributorListModal(seq,
				distributor,
				CommonConstant.USER_TYPE_SUB_HEADQUARTER,
				CommonConstant.USER_TYPE_DISTRIBUTOR,
				CommonConstant.USER_TYPE_STORE,
				CommonConstant.USER_TYPE_NORMAL,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL,
				CommonConstant.MONEY_HISTORY_STATUS_COMPLETE,
				CommonConstant.IS_MONEY
				);
	}

	@Override
	public List<StoreForm> getStoreListModal(StoreForm store) {
		return memberMapper.getStoreListModal(store,
				CommonConstant.USER_TYPE_SUB_HEADQUARTER,
				CommonConstant.USER_TYPE_DISTRIBUTOR,
				CommonConstant.USER_TYPE_STORE,
				CommonConstant.USER_TYPE_NORMAL,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL,
				CommonConstant.MONEY_HISTORY_STATUS_COMPLETE
				);
	}

	@Override
	public List<MemberForm> getMemberListModal(MemberForm member) {
		return memberMapper.getMemberListModal(member, CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT);
	}

	@Override
	@Transactional(readOnly = false)
	public boolean updateMemberHoldingMoney(
			String memberSeq,
			Float prevMoneyAmount,
			Float prevMileageAmount,
			Float variableAmount,
			Float actualAmount,
			Float finalAmount,
			Integer classification,
			Integer transactionClassification,
			Integer status,
			Integer reasonType,
			String reason,
			Integer chargeCount
	) {
		System.out.println("MemberServiceImpl==updateMemberHoldingMoney==");
		System.out.println("*************** save param info ***************");
		System.out.println("\t*** memberSeq : "+memberSeq);
		System.out.println("\t*** prevMoneyAmount : "+prevMoneyAmount);
		System.out.println("\t*** prevMileageAmount : "+prevMileageAmount);
		System.out.println("\t*** variableAmount : "+variableAmount);
		System.out.println("\t*** actualAmount : "+actualAmount);
		System.out.println("\t*** finalAmount : "+finalAmount);
		System.out.println("\t*** classification : "+classification);
		System.out.println("\t*** transactionClassification : "+transactionClassification);
		System.out.println("\t*** reason : "+reason);
		System.out.println("\t*******************************************");

		String seq = UUIDGenerator.generate();
		Member member = getById(memberSeq);
		boolean ret = false;

		if (classification.equals(CommonConstant.CLASSIFICATION_MONEY)) {
			MoneyHistory moneyHistory = new MoneyHistory();
			moneyHistory.setSeq(seq);
			moneyHistory.setReceiver(memberSeq);
			moneyHistory.setApplicationTime(new Date());
			moneyHistory.setProcessTime(new Date());
			moneyHistory.setPrevAmount(prevMoneyAmount);
			moneyHistory.setVariableAmount(variableAmount);
			moneyHistory.setActualAmount(actualAmount);
			moneyHistory.setFinalAmount(finalAmount);

			// classification = ku bun
			// 0: money, 1: point
			moneyHistory.setMoneyOrPoint(classification);

			//transactionClassification = ke rea ku bun
			// 0: increase, 1: decrease
			moneyHistory.setOperationType(transactionClassification);
			moneyHistory.setStatus(status);
			moneyHistory.setReasonType(reasonType);
			moneyHistory.setReason(reason);

			if(!chargeCount.equals(0)){
				moneyHistory.setChargeCount(chargeCount);
			}

			member.setMoneyAmount(finalAmount);

			System.out.println("charge money");
			if (moneyHistoryService.save(moneyHistory) && updateById(member)) {
				ret = true;
			}
		} else {
			MileageHistory mileageHistory = new MileageHistory();

			float finalMileageAmount = transactionClassification.equals(CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT)
					? prevMileageAmount + variableAmount
					: prevMileageAmount - variableAmount;
			mileageHistory.setSeq(seq);
			mileageHistory.setMemberSeq(memberSeq);
			mileageHistory.setProcessTime(new Date());
			mileageHistory.setPrevAmount(prevMileageAmount);
			mileageHistory.setVariableAmount(variableAmount);
			mileageHistory.setFinalAmount(finalMileageAmount);
			mileageHistory.setReasonType(reasonType);
			mileageHistory.setReason(reason);
			mileageHistory.setOperationType(transactionClassification);

			member.setMileageAmount(finalMileageAmount);
			System.out.println("charge mileage");
			System.out.println("mileage updated Member:" + member);
			System.out.println(finalMileageAmount);
			if (mileageHistoryService.save(mileageHistory) && updateById(member)) {
				ret = true;
			}
		}

		return ret;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean updatePartnerMemberHoldingMoney(
			String memberSeq,
			Float prevMoneyAmount,
			Float prevMileageAmount,
			Float variableAmount,
			Float actualAmount,
			Float finalAmount,
			Integer classification,
			Integer transactionClassification,
			Integer status,
			Integer reasonType,
			String reason,
			Integer chargeCount
	) {
		String seq = UUIDGenerator.generate();
		Member member = getById(memberSeq);
		boolean ret = false;

		if (classification.equals(CommonConstant.CLASSIFICATION_MONEY)) {
			MoneyHistory moneyHistory = new MoneyHistory();
			moneyHistory.setSeq(seq);
			moneyHistory.setReceiver(memberSeq);
			moneyHistory.setApplicationTime(new Date());
			moneyHistory.setProcessTime(new Date());
			moneyHistory.setPrevAmount(prevMoneyAmount);
			moneyHistory.setVariableAmount(variableAmount);
			moneyHistory.setActualAmount(actualAmount);
			moneyHistory.setFinalAmount(finalAmount);

			// classification = ku bun
			// 0: money, 1: point
			moneyHistory.setMoneyOrPoint(classification);

			//transactionClassification = ke rea ku bun
			// 0: increase, 1: decrease
			moneyHistory.setOperationType(transactionClassification);
			moneyHistory.setStatus(status);
			moneyHistory.setReasonType(reasonType);
			moneyHistory.setReason(reason);

			if(!chargeCount.equals(0)){
				moneyHistory.setChargeCount(chargeCount);
			}

			member.setMoneyAmount(finalAmount);

			if (moneyHistoryService.save(moneyHistory) && updateById(member)) {
				ret = true;
			}
		} else {
			MileageHistory mileageHistory = new MileageHistory();
			mileageHistory.setSeq(seq);
			mileageHistory.setMemberSeq(memberSeq);
			mileageHistory.setProcessTime(new Date());
			mileageHistory.setPrevAmount(prevMileageAmount);
			mileageHistory.setVariableAmount(variableAmount);
			mileageHistory.setFinalAmount(finalAmount);
			mileageHistory.setReasonType(reasonType);
			mileageHistory.setReason(reason);
			mileageHistory.setOperationType(transactionClassification);

			member.setMileageAmount(finalAmount);

			if (mileageHistoryService.save(mileageHistory) && updateById(member)) {
				ret = true;
			}
		}

		return ret;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean stopMember(List<String> memberSeqList) {
		boolean ret = true;

		QueryWrapper<Member> qw = new QueryWrapper<>();
		qw.in("seq", memberSeqList);
		List<Member> list = list(qw);

		for (Member item : list) {
			item.setStatus(CommonConstant.MEMBER_STATUS_STOP);

			if (!updateById(item)) {
				ret = false;
				break;
			}
		}
		return ret;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean updateMember(Member member) {
		boolean ret = false;
		QueryWrapper<Member> qw = new QueryWrapper<>();

		List<Member> memberList = new ArrayList<>();

		Integer userType = getById(member.getSeq()).getUserType();
		String subHeadquarterSeq = "";
		String distributorSeq = "";

		switch (userType) {
			case 1:
				subHeadquarterSeq = getById(member.getDistributorSeq()).getSubHeadquarterSeq();
				distributorSeq = member.getDistributorSeq();

				qw.eq("store_seq", member.getSeq());
				memberList = list(qw);

				member.setSubHeadquarterSeq(subHeadquarterSeq);
				break;
			case 2:
				subHeadquarterSeq = member.getSubHeadquarterSeq();
				distributorSeq = member.getSeq();

				qw.eq("distributor_seq", member.getSeq());
				memberList = list(qw);

				break;
			default:
				break;
		}

		for (Member item : memberList) {
			item.setDistributorSeq(distributorSeq);
			item.setSubHeadquarterSeq(subHeadquarterSeq);

			if (!updateById(item)) {
				break;
			}
		}

		if (updateById(member)) {
			ret = true;
		}

		return ret;
	}

}