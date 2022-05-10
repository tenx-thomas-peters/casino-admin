package com.casino.modules.admin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.admin.common.entity.MoneyHistory;
import com.casino.modules.admin.common.entity.SiteStatus;
import com.casino.modules.admin.common.form.DepositWithdrawStatisticForm;

public interface MoneyHistoryMapper extends BaseMapper<MoneyHistory> {

    IPage<MoneyHistory> findList(Page<MoneyHistory> page,
                                 @Param("entity") MoneyHistory moneyHistory,
                                 @Param("column") String column,
                                 @Param("order") Integer order,

                                 @Param("monStatusAdminEdit") Integer monStatusAdminEdit,
                                 @Param("userTypeDistributor") Integer userTypeDistributor,
                                 @Param("userTypeMember") Integer userTypeMember
                                 );

    IPage<MoneyHistory> findPartenerMoneyLogList(
            Page<MoneyHistory> page,
            @Param("entity") MoneyHistory moneyHistory,
            @Param("column") String column,
            @Param("order") Integer order,
            @Param("userTypeAdmin") Integer userTypeAdmin,
            @Param("userTypeNormal") Integer userTypeNormal
            );

    List<DepositWithdrawStatisticForm> depositWithdrawStatistic(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("dayDiff") Long dayDiff,

            @Param("monStatusAdminEdit") Integer monStatusAdminEdit,
            @Param("depositType") Integer depositType,
            @Param("withdrawType") Integer withdrawType,
            @Param("slotType") Integer slotType,
            @Param("baccaratType") Integer baccaratType,
            @Param("userTypeDistributor") Integer userTypeDistributor

            );

    List<SiteStatus> getSiteStatusList(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("dayDiff") Long dayDiff,

            @Param("depositType") Integer depositType,
            @Param("withdrawType") Integer withdrawType,
            @Param("slotType") Integer slotType,
            @Param("baccaratType") Integer baccaratType
            );

    List<MoneyHistory> getList(@Param("entity") MoneyHistory moneyHistory,
                               @Param("monStatusAdminEdit") Integer monStatusAdminEdit,
                               @Param("userTypeDistributor") Integer userTypeDistributor,
                               @Param("userTypeMember") Integer userTypeMember);

	Float getTotalAmountByDateRange(@Param("entity") MoneyHistory moneyHistory,
                                    @Param("userTypeMember") Integer userTypeMember, 
                                    @Param("monStatusAdminEdit") Integer monStatusAdminEdit,
                                    @Param("userTypeDistributor") Integer userTypeDistributor
                                    );

	List<MoneyHistory> getTodayMoneyHistory(
	        @Param("status") Integer status,
            @Param("operationType") Integer operationType,
            @Param("moneyOrPoint") Integer moneyOrPoint);

    Map<String, Number> getTodayFirstMoneyHistory(
            @Param("userSeq") String userSeq);

	IPage<MoneyHistory> getMonthMoneyLogByMemberSeq(Page<MoneyHistory> page, 
			@Param("memberSeq") String memberSeq, @Param("operationType") Integer operationType);

    boolean changeAdminReadStatusAll(@Param("operationType") Integer operationType, @Param("userType") Integer userType);
}
