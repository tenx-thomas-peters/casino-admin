package com.casino.modules.admin.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.modules.admin.common.entity.Dict;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.common.entity.MoneyHistory;
import com.casino.modules.admin.common.entity.SiteStatus;
import com.casino.modules.admin.common.form.DepositWithdrawStatisticForm;
import com.casino.modules.admin.mapper.MoneyHistoryMapper;
import com.casino.modules.admin.service.IDictService;
import com.casino.modules.admin.service.IMemberService;
import com.casino.modules.admin.service.IMoneyHistoryService;

@Service
public class MoneyHistoryServiceImpl extends ServiceImpl<MoneyHistoryMapper, MoneyHistory> implements IMoneyHistoryService {

    @Autowired
    private MoneyHistoryMapper moneyHistoryMapper;
    
    @Autowired
    private IMemberService memberService;
    
    @Autowired
    private IDictService dictService;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    public IPage<MoneyHistory> findList(
            Page<MoneyHistory> page,
            MoneyHistory moneyHistory,
            String column,
            Integer order) {
        return moneyHistoryMapper.findList(
        		page, 
        		moneyHistory, 
        		column, 
        		order, 
        		CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT, 
        		CommonConstant.USER_TYPE_DISTRIBUTOR, 
        		CommonConstant.USER_TYPE_NORMAL);
    }

    @Override
    public IPage<MoneyHistory> findPartenerMoneyLogList(
            Page<MoneyHistory> page,
            MoneyHistory moneyHistory,
            String column,
            Integer order) {
        return moneyHistoryMapper.findPartenerMoneyLogList(
        		page, 
        		moneyHistory, 
        		column, 
        		order, 
        		CommonConstant.USER_TYPE_ADMIN, 
        		CommonConstant.USER_TYPE_NORMAL);
    }

    @Override
    public List<DepositWithdrawStatisticForm> depositWithdrawStatistic(DepositWithdrawStatisticForm searchForm) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long timeDiff = Math.abs(sdf.parse(searchForm.getStartDate()).getTime() - sdf.parse(searchForm.getEndDate()).getTime());
        long dayDiff = timeDiff / (24 * 60 * 60 * 1000);

        return moneyHistoryMapper.depositWithdrawStatistic(searchForm.getStartDate(), searchForm.getEndDate(), dayDiff,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
				CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW,
				CommonConstant.BETTING_TYPE_SLOT,
				CommonConstant.BETTING_TYPE_BACCARAT,
				CommonConstant.USER_TYPE_DISTRIBUTOR
				);
    }

    @Override
    public List<SiteStatus> getSiteStatusList(SiteStatus siteStatus) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long timeDiff = Math.abs(sdf.parse(siteStatus.getStartDate()).getTime() - sdf.parse(siteStatus.getEndDate()).getTime());
        long dayDiff = timeDiff / (24 * 60 * 60 * 1000);

        return moneyHistoryMapper.getSiteStatusList(siteStatus.getStartDate(), siteStatus.getEndDate(), dayDiff,
				CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW,
				CommonConstant.BETTING_TYPE_SLOT,
				CommonConstant.BETTING_TYPE_BACCARAT
				);
    }

	@Override
	public List<MoneyHistory> getList(MoneyHistory moneyHistory) {
		return moneyHistoryMapper.getList(moneyHistory, 
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT, 
				CommonConstant.USER_TYPE_DISTRIBUTOR, 
				CommonConstant.USER_TYPE_NORMAL);
	}

	@Override
	public Float getTotalAmountByDateRange(MoneyHistory moneyHistory) {
		return moneyHistoryMapper.getTotalAmountByDateRange(
				moneyHistory, 
				CommonConstant.USER_TYPE_NORMAL, 
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT, 
				CommonConstant.USER_TYPE_DISTRIBUTOR);
	}

	@Override
	@Transactional(readOnly = false)
	public Boolean acceptMoneyHistory(String seq, Float amount, Float bonus, Integer operationType, Integer firstCharge, String reason) {
		MoneyHistory moneyHistory = moneyHistoryMapper.selectById(seq);
		Member member = memberService.getById(moneyHistory.getReceiver());
		moneyHistory.setPrevAmount(member.getMoneyAmount() == null  ? 0f : member.getMoneyAmount());
		moneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_COMPLETE);
		moneyHistory.setActualAmount(amount);
		moneyHistory.setBonus(bonus);
		Integer reasonType = 0;
		if(operationType.equals(CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT)) {
			moneyHistory.setFinalAmount(moneyHistory.getPrevAmount() + moneyHistory.getActualAmount());
			if(member.getUserType() == CommonConstant.USER_TYPE_NORMAL) {
				reasonType = CommonConstant.MONEY_REASON_CHARGE;
			}else {
				reasonType = CommonConstant.MONEY_REASON_PARTNER_RECOVERY;
			}
		}
		if(operationType == CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL) {
			moneyHistory.setFinalAmount(moneyHistory.getPrevAmount() - moneyHistory.getActualAmount());
			if(member.getUserType() == CommonConstant.USER_TYPE_NORMAL) {
				reasonType = CommonConstant.MONEY_REASON_EXCHANGE;
			}else {
				reasonType = CommonConstant.MONEY_REASON_PARTNER_PAYMENT;
			}
		}
	// Thomas 2022.05.19
//		set reason manually

//		QueryWrapper<Dict> qw = new QueryWrapper<>();
//		qw.eq("dict_key", CommonConstant.DICT_KEY_MONEY_REASON);
//		qw.eq("dict_value", reasonType);
//		List<Dict> reasonList = dictService.list(qw);
//		String reasonStrKey = reasonList.get(0).getStrValue();
//		List<String> params = new ArrayList<String>();
//		params.add(String.valueOf(amount));
//		String reason = messageSource.getMessage(reasonStrKey, params.toArray(), Locale.ENGLISH);

		moneyHistory.setReason(reason);
		moneyHistory.setProcessTime(new Date());

		// set first charge flag
		moneyHistory.setFirstCharge(firstCharge);
		member.setMoneyAmount(moneyHistory.getFinalAmount());
		
		if(this.updateById(moneyHistory)) {
			if(memberService.updateById(member)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<MoneyHistory> getTodayMoneyHistory(Integer status, Integer operationType, Integer moneyOrPoint) {
		return moneyHistoryMapper.getTodayMoneyHistory(status, operationType, moneyOrPoint);
	}

	@Override
	public IPage<MoneyHistory> getMonthMoneyLogByMemberSeq(Page<MoneyHistory> page, String memberSeq, Integer operationType) {
		return moneyHistoryMapper.getMonthMoneyLogByMemberSeq(page, memberSeq, operationType);
	}

	@Override
	public boolean updateMoneyHistory(MoneyHistory moneyHistoryParams) {
		if (moneyHistoryParams != null) {
			MoneyHistory moneyHistory = new MoneyHistory();
			
			QueryWrapper<Member> qw = new QueryWrapper<>();
    		qw.eq("seq", moneyHistoryParams.getReceiver());
    		
    		Member member = memberService.getOne(qw);
    		
    		if(member.getMoneyAmount() == null || member.getMoneyAmount() == Float.valueOf(CommonConstant.IS_MONEY)) {
    			return false;
    		} else {
    			if (moneyHistoryParams.getVariableAmount() > member.getMoneyAmount()) {
    				return false;
    			} else {
    				Float finalAmount = Float.valueOf(member.getMoneyAmount()) - Float.valueOf(moneyHistoryParams.getVariableAmount());
        			moneyHistory.setFinalAmount(finalAmount);
        			moneyHistory.setSeq(UUIDGenerator.generate());
            		moneyHistory.setPrevAmount(Float.valueOf(member.getMoneyAmount()));
            		moneyHistory.setReceiver(moneyHistoryParams.getReceiver());
            		moneyHistory.setVariableAmount(Float.valueOf(moneyHistoryParams.getVariableAmount()));
            		moneyHistory.setActualAmount(Float.valueOf(moneyHistoryParams.getVariableAmount()));
            		moneyHistory.setNote(moneyHistoryParams.getNote());
            		moneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_IN_PROGRESS);
            		moneyHistory.setApplicationTime(new Date());
            		moneyHistory.setOperationType(CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL);

            		if(this.saveOrUpdate(moneyHistory)) {
            			return true;
            		} else {
            			return false;
            		}
    			}
    		}
		}
		return false;
	}

	@Override
	public boolean changeAdminReadStatusAll(Integer operationType, Integer userType) {
		return moneyHistoryMapper.changeAdminReadStatusAll(operationType, userType);
	}

	@Override
	public Map<String, Number> getTodayFirstMoneyHistory(String userSeq) {
		System.out.println("userSeq");
		System.out.println(userSeq);
		return moneyHistoryMapper.getTodayFirstMoneyHistory(userSeq);
	}
}
