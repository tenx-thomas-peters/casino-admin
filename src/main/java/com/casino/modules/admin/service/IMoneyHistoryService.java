package com.casino.modules.admin.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.admin.common.entity.MoneyHistory;
import com.casino.modules.admin.common.entity.SiteStatus;
import com.casino.modules.admin.common.form.DepositWithdrawStatisticForm;

public interface IMoneyHistoryService extends IService<MoneyHistory> {

    IPage<MoneyHistory> findList(
            Page<MoneyHistory> page,
            MoneyHistory moneyHistory,
            String column,
            Integer order);

    IPage<MoneyHistory> findPartenerMoneyLogList(
            Page<MoneyHistory> page,
            MoneyHistory moneyHistory,
            String column,
            Integer order);

    List<DepositWithdrawStatisticForm> depositWithdrawStatistic(DepositWithdrawStatisticForm searchForm) throws ParseException;

    List<SiteStatus> getSiteStatusList(SiteStatus siteStatus) throws ParseException;

	List<MoneyHistory> getList(MoneyHistory moneyHistory);

	Float getTotalAmountByDateRange(MoneyHistory moneyHistory);
	
	Boolean acceptMoneyHistory(String seq, Float amount, Float bonus, Integer operationType, Integer firstCharge, String reason, Integer reasonType);

	// for api
	List<MoneyHistory> getTodayMoneyHistory(Integer status, Integer operationType, Integer moneyOrPoint);
	
	IPage<MoneyHistory> getMonthMoneyLogByMemberSeq(Page<MoneyHistory> page, String memberSeq, Integer operationType, Integer reasonType);

	boolean updateMoneyHistory(MoneyHistory moneyHistoryParams);

	boolean changeAdminReadStatusAll(Integer operationType, Integer userType);

	Map<String, Number> getTodayFirstMoneyHistory(String userSeq);
}
