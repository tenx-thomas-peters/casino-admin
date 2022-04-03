package com.casino.modules.admin.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.casino.modules.admin.common.entity.SiteStatus;
import com.casino.modules.admin.common.form.DepositWithdrawStatisticForm;
import com.casino.modules.admin.service.IMoneyHistoryService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/stat")
@Slf4j
public class StatisticsController {
	
    @Autowired
    private IMoneyHistoryService moneyHistoryService;
    
    @GetMapping(value = "/money")
    public String depositWithdrawStatistic(Model model,
    		@ModelAttribute("searchForm") DepositWithdrawStatisticForm searchForm,
    		HttpServletRequest request) {
        try {
        	List<DepositWithdrawStatisticForm> list = new ArrayList<>();
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	if(StringUtils.isBlank(searchForm.getStartDate()) && StringUtils.isBlank(searchForm.getEndDate())) {
        		searchForm.setStartDate(sdf.format(new Date()));
        		searchForm.setEndDate(sdf.format(new Date()));
        	}
    		list = moneyHistoryService.depositWithdrawStatistic(searchForm);
            
            Double slotBetSum = list.stream().mapToDouble(temp->temp.getSlotBet()).sum();
            Double slotWinSum = list.stream().mapToDouble(temp->temp.getSlotWin()).sum();
            Double slotBetWinSum = list.stream().mapToDouble(temp->(temp.getSlotBet() - temp.getSlotWin())).sum();
            Double baccaratBetSum = list.stream().mapToDouble(temp->temp.getBaccaratBet()).sum();
            Double baccaratWinSum = list.stream().mapToDouble(temp->temp.getBaccaratWin()).sum();
            Double baccaratBetWinSum = list.stream().mapToDouble(temp->(temp.getBaccaratBet() - temp.getBaccaratWin())).sum();
            Double depositSum = list.stream().mapToDouble(temp->temp.getDeposit()).sum();
            Double withdrawSum = list.stream().mapToDouble(temp->temp.getWithdraw()).sum();
            Double distributorDepositSum = list.stream().mapToDouble(temp->temp.getDistributorDeposit()).sum();
            Double distributorWithdrawSum = list.stream().mapToDouble(temp->temp.getDistributorWithdraw()).sum();
            Double benefitSum = list.stream().mapToDouble(temp->(temp.getDeposit() - temp.getWithdraw())).sum();
            Double managerDepositSum = list.stream().mapToDouble(temp->temp.getManagerDeposit()).sum();
            Double managerWithdrawSum = list.stream().mapToDouble(temp->temp.getManagerWithdraw()).sum();
            Double pointDepositSum = list.stream().mapToDouble(temp->temp.getPointDeposit()).sum();
            Double pointWithdrawSum = list.stream().mapToDouble(temp->temp.getPointWithdraw()).sum();
            
            model.addAttribute("slotBetSum", slotBetSum);
            model.addAttribute("slotWinSum", slotWinSum);
            model.addAttribute("slotBetWinSum", slotBetWinSum);
            model.addAttribute("baccaratBetSum", baccaratBetSum);
            model.addAttribute("baccaratWinSum", baccaratWinSum);
            model.addAttribute("baccaratBetWinSum", baccaratBetWinSum);
            model.addAttribute("depositSum", depositSum);
            model.addAttribute("withdrawSum", withdrawSum);
            model.addAttribute("distributorDepositSum", distributorDepositSum);
            model.addAttribute("distributorWithdrawSum", distributorWithdrawSum);
            model.addAttribute("benefitSum", benefitSum);
            model.addAttribute("managerDepositSum", managerDepositSum);
            model.addAttribute("managerWithdrawSum", managerWithdrawSum);
            model.addAttribute("pointDepositSum", pointDepositSum);
            model.addAttribute("pointWithdrawSum", pointWithdrawSum);
        	model.addAttribute("list", list);
            model.addAttribute("url", "/stat/money");
        } catch (Exception e) {
            log.error("url: /stat/money --- method: depositWithdrawStatistic --- error: " + e.toString());
        }
        return "views/admin/statistic/depositWithdrawStatistic";
    }
    
    @GetMapping(value = "/site")
    public String getSiteStatusList(
    		Model model,
    		@ModelAttribute("siteStatus") SiteStatus siteStatus,
    		HttpServletRequest request) {
    	try {
    		List<SiteStatus> list = new ArrayList<>();
    		
    		if (siteStatus.getStartDate() == null || siteStatus.getStartDate() == "") {
    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    			siteStatus.setStartDate(sdf.format(new Date()));
    		}
    		if (siteStatus.getEndDate() == null || siteStatus.getEndDate() == "") {
    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    			siteStatus.setEndDate(sdf.format(new Date()));
    		}
			list = moneyHistoryService.getSiteStatusList(siteStatus);
    			
			Double newMemberSum = list.stream().mapToDouble(temp->temp.getNewMember()).sum();
			Double connetedMemberSum = list.stream().mapToDouble(temp->temp.getConnectedMember()).sum();
			Double depositMemberSum = list.stream().mapToDouble(temp->temp.getDepositMember()).sum();
			Double bettingMemberSum = list.stream().mapToDouble(temp->temp.getBettingMember()).sum();
			Double depositAmountSum = list.stream().mapToDouble(temp->temp.getDepositAmount()).sum();
			Double depositNumberSum = list.stream().mapToDouble(temp->temp.getDepositNumber()).sum();
			Double withdarwalAmountSum = list.stream().mapToDouble(temp->temp.getWithDrawalAmount()).sum();
			Double withdarwalNumberSum = list.stream().mapToDouble(temp->temp.getWithDrawalNumber()).sum();
			Double slotBettingNumberSum = list.stream().mapToDouble(temp->temp.getSlotBettingNumber()).sum();
			Double slotBettingAmountSum = list.stream().mapToDouble(temp->temp.getSlotBettingAmount()).sum();
			Double baccaratBettingNumberSum = list.stream().mapToDouble(temp->temp.getBaccaratBettingNumber()).sum();
			Double baccaratBettingAmountSum = list.stream().mapToDouble(temp->temp.getBaccaratBettingAmount()).sum();
			
			model.addAttribute("newMemberSum", newMemberSum);
			model.addAttribute("connetedMemberSum", connetedMemberSum);
			model.addAttribute("depositMemberSum", depositMemberSum);
			model.addAttribute("bettingMemberSum", bettingMemberSum);
			model.addAttribute("depositAmountSum", depositAmountSum);
			model.addAttribute("depositNumberSum", depositNumberSum);
			model.addAttribute("withdarwalAmountSum", withdarwalAmountSum);
			model.addAttribute("withdarwalNumberSum", withdarwalNumberSum);
			model.addAttribute("slotBettingNumberSum", slotBettingNumberSum);
			model.addAttribute("slotBettingAmountSum", slotBettingAmountSum);
			model.addAttribute("baccaratBettingNumberSum", baccaratBettingNumberSum);
			model.addAttribute("baccaratBettingAmountSum", baccaratBettingAmountSum);
    		
    		model.addAttribute("list", list);
            model.addAttribute("url", "/stat/site");
    	} catch (Exception e) {
            log.error("url: /stat/site --- method: getSiteStatusList --- error: " + e.toString());
        }
    	
    	return "views/admin/statistic/site";
    }
}
