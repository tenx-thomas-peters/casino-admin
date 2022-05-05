package com.casino.modules.admin.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.casino.modules.admin.common.entity.BasicSetting;
import com.casino.modules.admin.service.IBasicSettingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.common.vo.Result;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.common.entity.Note;
import com.casino.modules.admin.common.form.DistributorForm;
import com.casino.modules.admin.common.form.HeadquarterForm;
import com.casino.modules.admin.common.form.MemberForm;
import com.casino.modules.admin.common.form.StoreForm;
import com.casino.modules.admin.service.IMemberService;
import com.casino.modules.admin.service.INoteService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/partner2")
@Slf4j
public class PartnerController {

	@Autowired
	private IMemberService memberService;

	@Autowired
	private IBasicSettingService basicSettingService;
	
	@Autowired
	private INoteService noteService;

	@GetMapping(value = "/list_top")
	public String headquarterList(Model model, @ModelAttribute("headquarter") HeadquarterForm headquarter,
			@RequestParam(name = "column", defaultValue = "create_date") String column,
			@RequestParam(name = "order", defaultValue = "1") Integer order,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

		try {
			if(StringUtils.isBlank(headquarter.getFromApplicationTime())) {
        		headquarter.setFromApplicationTime(null);
        	}
        	if(StringUtils.isBlank(headquarter.getToApplicationTime())) {
        		headquarter.setToApplicationTime(null);
        	}
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(StringUtils.isBlank(headquarter.getFromApplicationTime()) && StringUtils.isBlank(headquarter.getToApplicationTime())) {
            	headquarter.setFromApplicationTime(sdf.format(new Date()));
            	headquarter.setToApplicationTime(sdf.format(new Date()));
            }
			headquarter.setUserType(CommonConstant.USER_TYPE_SUB_HEADQUARTER);
			Page<HeadquarterForm> page = new Page<HeadquarterForm>(pageNo, pageSize);
			IPage<HeadquarterForm> pageList = memberService.findHeadquarterList(page, headquarter, column, order);
            

			Double moneyAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getMoneyAmount()).sum();
			Double distributorCountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDistributorCount()).sum();
			Double storeCountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getStoreCount()).sum();
			Double memberCountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getMemberCount()).sum();
			Double depositMemberCountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositMemberCount()).sum();
			Double depositMemberAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositMemberAmount()).sum();
			Double depositPartnerAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositPartnerAmount()).sum();
			Double depositPaymentSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositPayment()).sum();
			Double withdrawalMemberAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalMemberAmount()).sum();
			Double withdrawalPartnerAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalPartnerAmount()).sum();
			Double withdrawalPaymentSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalPayment()).sum();

			Float slotBetAmountSum = 0f;
			Float baccaratBetAmountSum = 0f;
			Float slotWinningAmountSum = 0f;
			Float baccaratWinningAmountSum = 0f;
			Float slotLostAmountSum = 0f;
			Float baccaratLostAmountSum = 0f;
			Float slotBatRollingSum = 0f;
			Float baccaratBatRollingSum = 0f;
			
			for (HeadquarterForm item : pageList.getRecords()) {
				if(item.getGameType() != null) {
						slotBetAmountSum += item.getSlotBettingAmount();
						slotWinningAmountSum += item.getSlotWinningAmount();
						slotLostAmountSum += item.getSlotLostAmount();
						slotBatRollingSum += item.getSlotHeadquarterRollingAmount();

						baccaratBetAmountSum += item.getBaccaratBettingAmount();
						baccaratWinningAmountSum += item.getBaccaratWinningAmount();
						baccaratLostAmountSum += item.getBaccaratLostAmount();
						baccaratBatRollingSum += item.getBaccaratHeadquarterRollingAmount();
				}
			}
			
			Float losingAmount = slotBetAmountSum + baccaratBetAmountSum - slotWinningAmountSum - baccaratWinningAmountSum - slotLostAmountSum - baccaratLostAmountSum - slotBatRollingSum - baccaratBatRollingSum;

			model.addAttribute("moneyAmountSum", moneyAmountSum);
			model.addAttribute("distributorCountSum", distributorCountSum);
			model.addAttribute("storeCountSum", storeCountSum);
			model.addAttribute("memberCountSum", memberCountSum);
			model.addAttribute("depositMemberCountSum", depositMemberCountSum);
			model.addAttribute("depositMemberAmountSum", depositMemberAmountSum);
			model.addAttribute("depositPartnerAmountSum", depositPartnerAmountSum);
			model.addAttribute("depositPaymentSum", depositPaymentSum);
			model.addAttribute("withdrawalMemberAmountSum", withdrawalMemberAmountSum);
			model.addAttribute("withdrawalPartnerAmountSum", withdrawalPartnerAmountSum);
			model.addAttribute("withdrawalPaymentSum", withdrawalPaymentSum);
			model.addAttribute("slotBetAmountSum", slotBetAmountSum);
			model.addAttribute("slotWinningAmountSum", slotWinningAmountSum);
			model.addAttribute("slotLostAmountSum", slotLostAmountSum);
			model.addAttribute("slotBatRollingSum", slotBatRollingSum);
			model.addAttribute("baccaratBetAmountSum", baccaratBetAmountSum);
			model.addAttribute("baccaratWinningAmountSum", baccaratWinningAmountSum);
			model.addAttribute("baccaratLostAmountSum", baccaratLostAmountSum);
			model.addAttribute("baccaratBatRollingSum", baccaratBatRollingSum);
			model.addAttribute("losingAmount", losingAmount);
			model.addAttribute("page", pageList);
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("column", column);
			model.addAttribute("order", order);
			model.addAttribute("url", "partner2/list_top");
		} catch (Exception e) {
			log.error("url: /partner2/list_top --- method: headquarterList --- error: " + e.toString());
			e.printStackTrace();
		}

		return "views/admin/partner/headquarterList";
	}
	
	@GetMapping(value = "headquarterAddForm")
	public String addForm(Model model) {
    	try {
    		model.addAttribute("url", "partner2/headquarterAddForm");
    	} catch (Exception e) {
    		log.error("url: /partner2/headquarterAddForm --- method: addForm --- error: " + e.toString());
    	}
    	return "views/admin/partner/headquarterAdd";
    }
	
	@PostMapping(value = "/addHeadquarter")
	@ResponseBody
	public String addHeadquarter(Member member, HttpServletRequest request) {
		try {
			member.setSeq(UUIDGenerator.generate());
			member.setUserType(CommonConstant.USER_TYPE_SUB_HEADQUARTER);
			member.setStatus(CommonConstant.USER_STATUS_NORMAL);
			member.setRegisterDate(new Date());
			memberService.save(member);
		} catch (Exception e) {
			log.error("url: /partner2/addHeadquarter --- method: addHeadquarter --- " + e.getMessage());
		}
		return "redirect:/partner/headquarterList";
	}
	
	@PostMapping(value = "/addHeadquarter_ajax")
	@ResponseBody
	public Result<Member> addHeadquarterAjax(Member member, HttpServletRequest request) {
		Result<Member> result = new Result<Member>();
		try {
			member.setSeq(UUIDGenerator.generate());
			member.setUserType(CommonConstant.USER_TYPE_SUB_HEADQUARTER);
			member.setStatus(CommonConstant.USER_STATUS_NORMAL);
			member.setRegisterDate(new Date());
			if(memberService.save(member)) {
				result.success("operation success!");
			}
			else {
				result.error500("operation failed");
			}
		} catch (Exception e) {
			log.error("url: /partner2/addHeadquarter_ajax --- method: addHeadquarterAjax --- " + e.getMessage());
		}
		return result;
	}
	
	@GetMapping(value = "/list")
	public String distributorList(Model model, @ModelAttribute("distributor") DistributorForm distributor,
			@RequestParam(name = "column", defaultValue = "id") String column,
			@RequestParam(name = "order", defaultValue = "1") Integer order,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "15") Integer pageSize, HttpServletRequest request) {

		try {
			
			if(StringUtils.isBlank(distributor.getFromApplicationTime())) {
				distributor.setFromApplicationTime(null);
        	}
        	if(StringUtils.isBlank(distributor.getToApplicationTime())) {
        		distributor.setToApplicationTime(null);
        	}
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(StringUtils.isBlank(distributor.getFromApplicationTime()) && StringUtils.isBlank(distributor.getToApplicationTime())) {
            	distributor.setFromApplicationTime(sdf.format(new Date()));
            	distributor.setToApplicationTime(sdf.format(new Date()));
            }
            
			distributor.setUserType(CommonConstant.USER_TYPE_DISTRIBUTOR);
			Page<DistributorForm> page = new Page<DistributorForm>(pageNo, pageSize);
			IPage<DistributorForm> pageList = memberService.findDistributorList(page, distributor, column, order);
			
			QueryWrapper<Member> headquarterQW = new QueryWrapper<>();
			headquarterQW.eq("user_type", CommonConstant.USER_TYPE_SUB_HEADQUARTER);
			List<Member> headquarterList = memberService.list(headquarterQW);
			
			Double moneyAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getMoneyAmount()).sum();
			Double memberCountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getMemberCount()).sum();
			Double storeCountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getStoreCount()).sum();
			Double depositMemberCountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositMemberCount()).sum();
			Double depositMemberAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositMemberAmount()).sum();
			Double depositPartnerAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositPartnerAmount()).sum();
			Double depositPaymentSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositPayment()).sum();
			Double withdrawalMemberAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalMemberAmount()).sum();
			Double withdrawalPartnerAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalPartnerAmount()).sum();
			Double withdrawalPaymentSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalPayment()).sum();
			
			Float slotBetAmountSum = 0f;
			Float baccaratBetAmountSum = 0f;
			Float slotWinningAmountSum = 0f;
			Float baccaratWinningAmountSum = 0f;
			Float slotLostAmountSum = 0f;
			Float baccaratLostAmountSum = 0f;
			Float slotBatRollingSum = 0f;
			Float baccaratBatRollingSum = 0f;

			for (DistributorForm item : pageList.getRecords()) {
				if(item.getGameType() != null) {

					slotBetAmountSum += item.getSlotBettingAmount();
					slotWinningAmountSum += item.getSlotWinningAmount();
					slotLostAmountSum += item.getSlotLostAmount();
					slotBatRollingSum += item.getSlotDistributorRollingAmount();

					baccaratBetAmountSum += item.getBaccaratBettingAmount();
					baccaratWinningAmountSum += item.getBaccaratWinningAmount();
					baccaratLostAmountSum += item.getBaccaratLostAmount();
					baccaratBatRollingSum += item.getBaccaratDistributorRollingAmount();
				}
			}
			
			Float losingAmount = slotBetAmountSum + baccaratBetAmountSum - slotWinningAmountSum - baccaratWinningAmountSum - slotLostAmountSum - baccaratLostAmountSum - slotBatRollingSum - baccaratBatRollingSum;
			

			model.addAttribute("headquarterList", headquarterList);
			model.addAttribute("moneyAmountSum", moneyAmountSum);
			model.addAttribute("memberCountSum", memberCountSum);
			model.addAttribute("storeCountSum", storeCountSum);
			model.addAttribute("depositMemberCountSum", depositMemberCountSum);
			model.addAttribute("depositMemberAmountSum", depositMemberAmountSum);
			model.addAttribute("depositPartnerAmountSum", depositPartnerAmountSum);
			model.addAttribute("depositPaymentSum", depositPaymentSum);
			model.addAttribute("withdrawalMemberAmountSum", withdrawalMemberAmountSum);
			model.addAttribute("withdrawalPartnerAmountSum", withdrawalPartnerAmountSum);
			model.addAttribute("withdrawalPaymentSum", withdrawalPaymentSum);
			model.addAttribute("slotBetAmountSum", slotBetAmountSum);
			model.addAttribute("slotWinningAmountSum", slotWinningAmountSum);
			model.addAttribute("slotLostAmountSum", slotLostAmountSum);
			model.addAttribute("slotBatRollingSum", slotBatRollingSum);
			model.addAttribute("baccaratBetAmountSum", baccaratBetAmountSum);
			model.addAttribute("baccaratWinningAmountSum", baccaratWinningAmountSum);
			model.addAttribute("baccaratLostAmountSum", baccaratLostAmountSum);
			model.addAttribute("baccaratBatRollingSum", baccaratBatRollingSum);
			model.addAttribute("losingAmount", losingAmount);
			model.addAttribute("headquarterList", headquarterList);
			model.addAttribute("page", pageList);
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("column", column);
			model.addAttribute("order", order);
			model.addAttribute("url", "partner2/list");
			
		} catch (Exception e) {
			log.error("url: /partner2/list --- method: distributorList --- error: " + e.toString());
			e.printStackTrace();
		}

		return "views/admin/partner/distributorList";
	}
	
	@GetMapping(value = "distributorAddForm")
	public String addDistributor(Model model) {
    	try {
    		QueryWrapper<Member> headquarterQW = new QueryWrapper<>();
			headquarterQW.eq("user_type", CommonConstant.USER_TYPE_SUB_HEADQUARTER);
			List<Member> headquarterList = memberService.list(headquarterQW);
			
			model.addAttribute("headquarterList", headquarterList);
    		model.addAttribute("url", "partner2/distributorAddForm");
    	} catch (Exception e) {
    		log.error("url: /partner2/distributorAddForm --- method: addDistributor --- error: " + e.toString());
    	}
    	return "views/admin/partner/distributorAdd";
    }

	
	
	@PostMapping(value = "/addDistributor_ajax")
	@ResponseBody
	public Result<Member> addDistributorAjax(Member member, HttpServletRequest request) {
		Result<Member> result = new Result<Member>();
		try {
			member.setSeq(UUIDGenerator.generate());
			member.setUserType(CommonConstant.USER_TYPE_DISTRIBUTOR);
			member.setStatus(CommonConstant.USER_STATUS_NORMAL);
			member.setRegisterDate(new Date());
			if(memberService.save(member)) {
				result.success("operation success!");
			}
			else {
				result.error500("operation failed");
			}
		} catch (Exception e) {
			log.error("url: /partner2/addDistributor --- method: addDistributor --- " + e.getMessage());
		}
		return result;
	}

	@GetMapping(value = "/list_store")
	public String storeList(Model model, @ModelAttribute("store") StoreForm store,
			@RequestParam(name = "column", defaultValue = "create_date") String column,
			@RequestParam(name = "order", defaultValue = "1") Integer order,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
		try {
			if(StringUtils.isBlank(store.getFromApplicationTime())) {
				store.setFromApplicationTime(null);
        	}
        	if(StringUtils.isBlank(store.getToApplicationTime())) {
        		store.setToApplicationTime(null);
        	}
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(StringUtils.isBlank(store.getFromApplicationTime()) && StringUtils.isBlank(store.getToApplicationTime())) {
            	store.setFromApplicationTime(sdf.format(new Date()));
            	store.setToApplicationTime(sdf.format(new Date()));
            }
			
			store.setUserType(CommonConstant.USER_TYPE_STORE);
			Page<StoreForm> page = new Page<StoreForm>(pageNo, pageSize);
			IPage<StoreForm> pageList = memberService.findStoreList(page, store, column, order);
			
			Double moneyAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getMoneyAmount()).sum();
			Double memberCountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getMemberCount()).sum();
			Double depositMemberCountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositMemberCount()).sum();
			Double depositMemberAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositMemberAmount()).sum();
			Double depositPartnerAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositPartnerAmount()).sum();
			Double depositPaymentSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositPayment()).sum();
			Double withdrawalMemberAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalMemberAmount()).sum();
			Double withdrawalPartnerAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalPartnerAmount()).sum();
			Double withdrawalPaymentSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalPayment()).sum();

			Float slotBetAmountSum = 0f;
			Float baccaratBetAmountSum = 0f;
			Float slotWinningAmountSum = 0f;
			Float baccaratWinningAmountSum = 0f;
			Float slotLostAmountSum = 0f;
			Float baccaratLostAmountSum = 0f;
			Float slotBatRollingSum = 0f;
			Float baccaratBatRollingSum = 0f;
			
			for (StoreForm item : pageList.getRecords()) {
				if(item.getGameType() != null) {
					slotBetAmountSum += item.getSlotBettingAmount();
					slotWinningAmountSum += item.getSlotWinningAmount();
					slotLostAmountSum += item.getSlotLostAmount();
					slotBatRollingSum += item.getSlotStoreRollingAmount();

					baccaratBetAmountSum += item.getBaccaratBettingAmount();
					baccaratWinningAmountSum += item.getBaccaratWinningAmount();
					baccaratLostAmountSum += item.getBaccaratLostAmount();
					baccaratBatRollingSum += item.getBaccaratStoreRollingAmount();
				}
			}
			
			Float losingAmount = slotBetAmountSum + baccaratBetAmountSum - slotWinningAmountSum - baccaratWinningAmountSum - slotLostAmountSum - baccaratLostAmountSum - slotBatRollingSum - baccaratBatRollingSum;
			
			QueryWrapper<Member> distributorQW = new QueryWrapper<>();
			distributorQW.eq("user_type", CommonConstant.USER_TYPE_DISTRIBUTOR);
			List<Member> distributorList = memberService.list(distributorQW);

			QueryWrapper<Member> headquarterQW = new QueryWrapper<>();
			headquarterQW.eq("user_type", CommonConstant.USER_TYPE_SUB_HEADQUARTER);
			List<Member> headquarterList = memberService.list(headquarterQW);

			model.addAttribute("moneyAmountSum", moneyAmountSum);
			model.addAttribute("memberCountSum", memberCountSum);
			model.addAttribute("depositMemberCountSum", depositMemberCountSum);
			model.addAttribute("depositMemberAmountSum", depositMemberAmountSum);
			model.addAttribute("depositPartnerAmountSum", depositPartnerAmountSum);
			model.addAttribute("depositPaymentSum", depositPaymentSum);
			model.addAttribute("withdrawalMemberAmountSum", withdrawalMemberAmountSum);
			model.addAttribute("withdrawalPartnerAmountSum", withdrawalPartnerAmountSum);
			model.addAttribute("withdrawalPaymentSum", withdrawalPaymentSum);
			model.addAttribute("slotBetAmountSum", slotBetAmountSum);
			model.addAttribute("slotWinningAmountSum", slotWinningAmountSum);
			model.addAttribute("slotLostAmountSum", slotLostAmountSum);
			model.addAttribute("slotBatRollingSum", slotBatRollingSum);
			model.addAttribute("baccaratBetAmountSum", baccaratBetAmountSum);
			model.addAttribute("baccaratWinningAmountSum", baccaratWinningAmountSum);
			model.addAttribute("baccaratLostAmountSum", baccaratLostAmountSum);
			model.addAttribute("baccaratBatRollingSum", baccaratBatRollingSum);
			model.addAttribute("losingAmount", losingAmount);
			model.addAttribute("distributorList", distributorList);
			model.addAttribute("headquarterList", headquarterList);
			model.addAttribute("page", pageList);
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("column", column);
			model.addAttribute("order", order);
			model.addAttribute("store", store);
			model.addAttribute("url", "partner2/list_store");
		} catch (Exception e) {
			log.error("url: /partner2/list_store --- method: storeList --- error: " + e.toString());
		}

		return "views/admin/partner/storeList";
	}
	
	@GetMapping(value = "storeAddForm")
	public String storeRegistration(Model model) {
    	try {
    		QueryWrapper<Member> distributorQW = new QueryWrapper<>();
    		distributorQW.eq("user_type", CommonConstant.USER_TYPE_DISTRIBUTOR);
			List<Member> distributorList = memberService.list(distributorQW);
			
			model.addAttribute("distributorList", distributorList);
    		model.addAttribute("url","partner2/storeAddForm");
    	} catch (Exception e) {
    		log.error("url: /partner2/storeAddForm --- method: addForm --- error: " + e.toString());
    	}
    	return "views/admin/partner/storeAdd";
    }

	@PostMapping(value = "/addStore")
	@ResponseBody
	public String addStore(Member member, HttpServletRequest request) {
		try {
			member.setSeq(UUIDGenerator.generate());
			member.setUserType(CommonConstant.USER_TYPE_STORE);
			member.setStatus(CommonConstant.USER_STATUS_NORMAL);
			member.setRegisterDate(new Date());
			memberService.save(member);
		} catch (Exception e) {
			log.error("url: /partner2/addStore --- method: addStore --- " + e.getMessage());
		}
		return "redirect:partner/storeList";
	}
	
	@PostMapping(value = "/addStore_ajax")
	@ResponseBody
	public Result<Member> addStoreAjax(Member member, HttpServletRequest request) {
		Result<Member> result = new Result<Member>();
		try {
			member.setSeq(UUIDGenerator.generate());
			member.setUserType(CommonConstant.USER_TYPE_STORE);
			member.setStatus(CommonConstant.USER_STATUS_NORMAL);
			member.setSubHeadquarterSeq(memberService.getById(member.getDistributorSeq()).getSubHeadquarterSeq());
			member.setRegisterDate(new Date());
			if(memberService.save(member)) {
				result.success("operation success!");
			}
			else {
				result.error500("operation failed");
			}
		} catch (Exception e) {
			log.error("url: /partner2/addStore_ajax --- method: addStoreAjax --- " + e.getMessage());
		}
		return result;
	}
	
	@GetMapping(value = "/changeStatus")
	@ResponseBody
	public Result<Member> operation(@RequestParam("seq") String seq, Member member, HttpServletRequest request) {
		Result<Member> result = new Result<Member>();
		try {
			member = memberService.getById(seq);
			if(member.getStatus().equals(CommonConstant.USER_STATUS_NORMAL)) {
				member.setStatus(CommonConstant.USER_STATUS_STOP);
			}
			else {
				member.setStatus(CommonConstant.USER_STATUS_NORMAL);
			}
			if(memberService.updateById(member)) {
				result.success("operation success!");
			}
			else {
				result.error500("operation failed");
			}
		} catch (Exception e) {
			log.error("url: /partner2/operation --- method: operation --- " + e.getMessage());
		}
		return result;
	}
	
	@GetMapping(value = "/delete")
	@ResponseBody
	public Result<Member> delete(@RequestParam("seq") String seq, Member member, HttpServletRequest request) {
		Result<Member> result = new Result<Member>();
		try {
			member = memberService.getById(seq);
			if(memberService.removeById(seq)) {
				result.success("operation success!");
			}
			else {
				result.error500("operation failed");
			}
		} catch (Exception e) {
			log.error("url: /partner2/delete --- method: delete --- " + e.getMessage());
		}
		return result;
	}
	
	@GetMapping(value = "/getMemo")
	public String getMemo(@RequestParam("seq") String seq, Model model) {
		try {
			Member member = memberService.getById(seq);

			BasicSetting basicSetting = basicSettingService.getById(123);
			model.addAttribute("member", member);
			model.addAttribute("basicSetting", basicSetting);
			model.addAttribute("url", "partner2/getMemo");
		}
		catch(Exception e) {
			log.error("url: /partner2/getMemo --- method: getMemo --- " + e.getMessage());
		}
		return "views/admin/partner/note";
	}
	
	@PostMapping(value = "/confirmNote")
	@ResponseBody
	public Result<Note> confirmNote(@ModelAttribute("note") Note note) {
		Result<Note> result = new Result<>();
		try {
			if (note != null) {
				note.setSeq(UUIDGenerator.generate());
				note.setReadStatus(CommonConstant.STATUS_UN_READ);
				note.setRecommendStatus(CommonConstant.STATUS_UN_RECOMMEND);
				note.setLookUp(0);
				note.setType(CommonConstant.TYPE_P_NOTE);
				note.setSendType(CommonConstant.TYPE_SEND_NOTE);
				note.setTitle(note.getTitle());
				note.setContent(note.getContent());
				note.setReceiver(note.getReceiver());
				note.setSender(note.getSender1());

				if (noteService.save(note)) {
					result.success("Operate Success");
				} else {
					result.error500("Operate Faild");
				}
			} else {
				result.error500("Operate Faild");
			}
		} catch (Exception e) {
			log.error("url: /partner2/confirmNote --- method: confirmNote --- " + e.getMessage());
		}
		return result;
	}
	
	
	@GetMapping(value = "/chongMember")
	public String getChongMember(
			Model model,
			@ModelAttribute("distributor") DistributorForm distributor,
			@RequestParam("seq") String seq,
			@RequestParam("fromApplicationTime") String fromApplicationTime,
			@RequestParam("toApplicationTime") String toApplicationTime) {
		try {
			List<DistributorForm> distributorList = memberService.getDistributorListModal(seq, fromApplicationTime, toApplicationTime);
			
			Double storeCountSum = distributorList.stream().mapToDouble(temp->temp.getStoreCount()).sum();
			Double memberCountSum = distributorList.stream().mapToDouble(temp->temp.getMemberCount()).sum();
			Double depositMemberCountSum = distributorList.stream().mapToDouble(temp->temp.getDepositMemberCount()).sum();
			Double depositMemberAmountSum = distributorList.stream().mapToDouble(temp->temp.getDepositMemberAmount()).sum();
			Double depositPartnerAmountSum = distributorList.stream().mapToDouble(temp->temp.getDepositPartnerAmount()).sum();
			Double depositPaymentSum = distributorList.stream().mapToDouble(temp->temp.getDepositPayment()).sum();
			Double withdrawalMemberAmountSum = distributorList.stream().mapToDouble(temp->temp.getWithdrawalMemberAmount()).sum();
			Double withdrawalPartnerAmountSum = distributorList.stream().mapToDouble(temp->temp.getWithdrawalPartnerAmount()).sum();
			Double withdrawalPaymentSum = distributorList.stream().mapToDouble(temp->temp.getWithdrawalPayment()).sum();
			
			Float slotBetAmountSum = 0f;
			Float baccaratBetAmountSum = 0f;
			Float slotWinningAmountSum = 0f;
			Float baccaratWinningAmountSum = 0f;
			Float slotLostAmountSum = 0f;
			Float baccaratLostAmountSum = 0f;
			Float slotBatRollingSum = 0f;
			Float baccaratBatRollingSum = 0f;
			
			for (DistributorForm item : distributorList) {
				if(item.getGameType() != null) {

					slotBetAmountSum += item.getSlotBettingAmount();
					slotWinningAmountSum += item.getSlotWinningAmount();
					slotLostAmountSum += item.getSlotLostAmount();
					slotBatRollingSum += item.getSlotDistributorRollingAmount();

					baccaratBetAmountSum += item.getBaccaratBettingAmount();
					baccaratWinningAmountSum += item.getBaccaratWinningAmount();
					baccaratLostAmountSum += item.getBaccaratLostAmount();
					baccaratBatRollingSum += item.getBaccaratDistributorRollingAmount();
				}
			}
			
			Float losingAmount = slotBetAmountSum + baccaratBetAmountSum - slotWinningAmountSum - baccaratWinningAmountSum - slotLostAmountSum - baccaratLostAmountSum - slotBatRollingSum - baccaratBatRollingSum;
			
			QueryWrapper<Member> memoQW = new QueryWrapper<>();
			memoQW.eq("seq", seq);
			Member member = memberService.getOne(memoQW);

			model.addAttribute("storeCountSum", storeCountSum);
			model.addAttribute("memberCountSum", memberCountSum);
			model.addAttribute("depositMemberCountSum", depositMemberCountSum);
			model.addAttribute("depositMemberAmountSum", depositMemberAmountSum);
			model.addAttribute("depositPartnerAmountSum", depositPartnerAmountSum);
			model.addAttribute("depositPaymentSum", depositPaymentSum);
			model.addAttribute("withdrawalMemberAmountSum", withdrawalMemberAmountSum);
			model.addAttribute("withdrawalPartnerAmountSum", withdrawalPartnerAmountSum);
			model.addAttribute("withdrawalPaymentSum", withdrawalPaymentSum);
			model.addAttribute("slotBetAmountSum", slotBetAmountSum);
			model.addAttribute("slotWinningAmountSum", slotWinningAmountSum);
			model.addAttribute("slotLostAmountSum", slotLostAmountSum);
			model.addAttribute("slotBatRollingSum", slotBatRollingSum);
			model.addAttribute("baccaratBetAmountSum", baccaratBetAmountSum);
			model.addAttribute("baccaratWinningAmountSum", baccaratWinningAmountSum);
			model.addAttribute("baccaratLostAmountSum", baccaratLostAmountSum);
			model.addAttribute("baccaratBatRollingSum", baccaratBatRollingSum);
			model.addAttribute("losingAmount", losingAmount);
			model.addAttribute("distributorList", distributorList);
			model.addAttribute("Id", member.getId());
			model.addAttribute("userType", member.getUserType());
			model.addAttribute("url", "partner2/chongMember");
		}
		catch(Exception e) {
			log.error("url: /partner2/chongMember --- method: getChongMember --- " + e.getMessage());
			e.printStackTrace();
		}
		return "views/admin/common/distributorModalList";
	}
	
	@GetMapping(value = "/shopMember")
	public String getShopMember(
			Model model,
			@ModelAttribute("store") StoreForm store,
			@RequestParam("seq") String seq,
			@RequestParam("userType") String userType,
			@RequestParam("fromApplicationTime") String fromApplicationTime,
			@RequestParam("toApplicationTime") String toApplicationTime) {
		try {
			QueryWrapper<Member> memoQW = new QueryWrapper<>();
			memoQW.eq("seq", seq);
			Member member = memberService.getOne(memoQW);
			
			Integer type = Integer.valueOf(userType);
			
			if(type == CommonConstant.USER_TYPE_SUB_HEADQUARTER) {
				store.setHeadquarterSeq(seq);
			}
				
			if(type == CommonConstant.USER_TYPE_DISTRIBUTOR) {
				store.setDistributorSeq(seq);
			}
			store.setFromApplicationTime(fromApplicationTime);
			store.setToApplicationTime(toApplicationTime);
			store.setUserType(CommonConstant.USER_TYPE_STORE);
			List<StoreForm> storeList = memberService.getStoreListModal(store);
			
			Double memberCountSum = storeList.stream().mapToDouble(temp->temp.getMemberCount()).sum();
			Double depositMemberCountSum = storeList.stream().mapToDouble(temp->temp.getDepositMemberCount()).sum();
			Double depositMemberAmountSum = storeList.stream().mapToDouble(temp->temp.getDepositMemberAmount()).sum();
			Double depositPartnerAmountSum = storeList.stream().mapToDouble(temp->temp.getDepositPartnerAmount()).sum();
			Double depositPaymentSum = storeList.stream().mapToDouble(temp->temp.getDepositPayment()).sum();
			Double withdrawalMemberAmountSum = storeList.stream().mapToDouble(temp->temp.getWithdrawalMemberAmount()).sum();
			Double withdrawalPartnerAmountSum = storeList.stream().mapToDouble(temp->temp.getWithdrawalPartnerAmount()).sum();
			Double withdrawalPaymentSum = storeList.stream().mapToDouble(temp->temp.getWithdrawalPayment()).sum();
			
			Float slotBetAmountSum = 0f;
			Float baccaratBetAmountSum = 0f;
			Float slotWinningAmountSum = 0f;
			Float baccaratWinningAmountSum = 0f;
			Float slotLostAmountSum = 0f;
			Float baccaratLostAmountSum = 0f;
			Float slotBatRollingSum = 0f;
			Float baccaratBatRollingSum = 0f;
			
			for (StoreForm item : storeList) {
				if(item.getGameType() != null) {
					slotBetAmountSum += item.getSlotBettingAmount();
					slotWinningAmountSum += item.getSlotWinningAmount();
					slotLostAmountSum += item.getSlotLostAmount();
					slotBatRollingSum += item.getSlotStoreRollingAmount();

					baccaratBetAmountSum += item.getBaccaratBettingAmount();
					baccaratWinningAmountSum += item.getBaccaratWinningAmount();
					baccaratLostAmountSum += item.getBaccaratLostAmount();
					baccaratBatRollingSum += item.getBaccaratStoreRollingAmount();
				}
			}
			
			Float losingAmount = slotBetAmountSum + baccaratBetAmountSum - slotWinningAmountSum - baccaratWinningAmountSum - slotLostAmountSum - baccaratLostAmountSum - slotBatRollingSum - baccaratBatRollingSum;
			
			model.addAttribute("memberCountSum", memberCountSum);
			model.addAttribute("depositMemberCountSum", depositMemberCountSum);
			model.addAttribute("depositMemberAmountSum", depositMemberAmountSum);
			model.addAttribute("depositPartnerAmountSum", depositPartnerAmountSum);
			model.addAttribute("depositPaymentSum", depositPaymentSum);
			model.addAttribute("withdrawalMemberAmountSum", withdrawalMemberAmountSum);
			model.addAttribute("withdrawalPartnerAmountSum", withdrawalPartnerAmountSum);
			model.addAttribute("withdrawalPaymentSum", withdrawalPaymentSum);
			model.addAttribute("slotBetAmountSum", slotBetAmountSum);
			model.addAttribute("slotWinningAmountSum", slotWinningAmountSum);
			model.addAttribute("slotLostAmountSum", slotLostAmountSum);
			model.addAttribute("slotBatRollingSum", slotBatRollingSum);
			model.addAttribute("baccaratBetAmountSum", baccaratBetAmountSum);
			model.addAttribute("baccaratWinningAmountSum", baccaratWinningAmountSum);
			model.addAttribute("baccaratLostAmountSum", baccaratLostAmountSum);
			model.addAttribute("baccaratBatRollingSum", baccaratBatRollingSum);
			model.addAttribute("losingAmount", losingAmount);
			model.addAttribute("Id", member.getId());
			model.addAttribute("userType", member.getUserType());
			model.addAttribute("storeList", storeList);
			model.addAttribute("url", "partner2/shopMember");
		}
		catch(Exception e) {
			log.error("url: /partner2/shopMember --- method: getShopMember --- " + e.getMessage());
		}
		return "views/admin/common/storeModalList";
	}
	
	@GetMapping(value = "/Member")
	public String getMember(
			Model model,
			@ModelAttribute("member") MemberForm member,
			@RequestParam("seq") String seq,
			@RequestParam("userType") String userType,
			@RequestParam("fromApplicationTime") String fromApplicationTime,
			@RequestParam("toApplicationTime") String toApplicationTime) {
		try {
			QueryWrapper<Member> memoQW = new QueryWrapper<>();
			memoQW.eq("seq", seq);
			Member newMember = memberService.getOne(memoQW);
			
			Integer type = Integer.valueOf(userType);
			
			if(type == CommonConstant.USER_TYPE_SUB_HEADQUARTER) {
				member.setSubHeadquarterSeq(seq);
			}
				
			if(type == CommonConstant.USER_TYPE_DISTRIBUTOR) {
				member.setDistributorSeq(seq);
			}
				
			if(type == CommonConstant.USER_TYPE_STORE) {
				member.setStoreSeq(seq);
			}
			member.setFromApplicationTime(fromApplicationTime);
			member.setToApplicationTime(toApplicationTime);
			member.setUserType(CommonConstant.USER_TYPE_NORMAL);
			List<MemberForm> memberList = memberService.getMemberListModal(member);
			
			for(int i = 0; i < memberList.size(); i++) {
				if(!StringUtils.isBlank(memberList.get(i).getStoreSeq())) {
					memberList.get(i).setPartnerId(memberList.get(i).getStoreID());
					memberList.get(i).setPartnerNickname(memberList.get(i).getStoreNickname());
					memberList.get(i).setPartnerLevel("Store");
				}
				else if(!StringUtils.isBlank(memberList.get(i).getDistributorSeq())) {
					memberList.get(i).setPartnerId(memberList.get(i).getDistributorID());
					memberList.get(i).setPartnerNickname(memberList.get(i).getDistributorNickname());
					memberList.get(i).setPartnerLevel("Distributor");
				}
				else if(!StringUtils.isBlank(memberList.get(i).getSubHeadquarterSeq())) {
					memberList.get(i).setPartnerId(memberList.get(i).getSubHeadquarterID());
					memberList.get(i).setPartnerNickname(memberList.get(i).getSubHeadquarterNickname());
					memberList.get(i).setPartnerLevel("Sub-Headquarter");
				}
			}
			
			Double moneyAmountSum = memberList.stream().mapToDouble(temp->temp.getMoneyAmount()).sum();
			Double depositSum = memberList.stream().mapToDouble(temp->temp.getDeposit()).sum();
			Double withdrawalSum = memberList.stream().mapToDouble(temp->temp.getWithdrawal()).sum();
			
			Float slotBetAmountSum = 0f;
			Float baccaratBetAmountSum = 0f;
			Float slotWinningAmountSum = 0f;
			Float baccaratWinningAmountSum = 0f;
			Float slotLostAmountSum = 0f;
			Float baccaratLostAmountSum = 0f;
			
			for (MemberForm item : memberList) {
				if(item.getGameType() != null) {
					slotBetAmountSum += item.getSlotBettingAmount();
					slotWinningAmountSum += item.getSlotWinningAmount();
					slotLostAmountSum += item.getSlotLostAmount();

					baccaratBetAmountSum += item.getBaccaratBettingAmount();
					baccaratWinningAmountSum += item.getBaccaratWinningAmount();
					baccaratLostAmountSum += item.getBaccaratLostAmount();
				}
			}
			
			model.addAttribute("moneyAmountSum", moneyAmountSum);
			model.addAttribute("depositSum", depositSum);
			model.addAttribute("withdrawalSum", withdrawalSum);
			model.addAttribute("slotBetAmountSum", slotBetAmountSum);
			model.addAttribute("slotWinningAmountSum", slotWinningAmountSum);
			model.addAttribute("slotLostAmountSum", slotLostAmountSum);
			model.addAttribute("baccaratBetAmountSum", baccaratBetAmountSum);
			model.addAttribute("baccaratWinningAmountSum", baccaratWinningAmountSum);
			model.addAttribute("baccaratLostAmountSum", baccaratLostAmountSum);
			model.addAttribute("Id", newMember.getId());
			model.addAttribute("userType", newMember.getUserType());
			model.addAttribute("memberList", memberList);
			model.addAttribute("url", "partner2/Member");
		}
		catch(Exception e) {
			log.error("url: /partner2/Member --- method: getMember --- " + e.getMessage());
			e.printStackTrace();
		}
		return "views/admin/common/memberModalList";
	}
	
	@GetMapping(value = "/popup_memoadd")
    public String popupMemoAdd(Model model) {
    	try {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		Date date = new Date();
    		String today = sdf.format(date);
    		model.addAttribute("url", "partner2/popup_memoadd");
    		model.addAttribute("today", today);
    	} catch (Exception e) {
    		log.error("url: /partner2/popup_memoadd --- method: popupMemoAdd --- error: " + e.toString());
    	}
    	return "views/admin/partner/memoAdd";
    }
	
	@PostMapping(value = "/memoadd")
	@ResponseBody
    public Result<Note> memoadd(@ModelAttribute("note") Note note) {
		Result<Note> result = new Result<>();
    	try {
    		if(note != null) {
    			QueryWrapper<Member> qw = new QueryWrapper<>();
    			qw.eq("user_type", note.getUserType());
    			List<Member> partnerList = memberService.list(qw);

    			if(partnerList.size() > 0) {
    				List<Note> noteSaveList = new ArrayList<>();
					for(Member partner : partnerList){
						Note noteSave = new Note();

						noteSave.setSeq(UUIDGenerator.generate());
						noteSave.setSender("운영팀");
						noteSave.setReceiver(partner.getSeq());
						noteSave.setReadStatus(CommonConstant.STATUS_UN_READ);
						noteSave.setRecommendStatus(CommonConstant.STATUS_UN_RECOMMEND);
						noteSave.setLookUp(0);
						noteSave.setType(CommonConstant.TYPE_P_NOTE);
						noteSave.setSendType(CommonConstant.TYPE_SEND_NOTE);
						noteSave.setContent(note.getContent());
						noteSave.setTitle(note.getTitle());

						noteSaveList.add(noteSave);
    				}
    				if(noteService.saveBatch(noteSaveList)) {
    					result.success("Operate Success");
    				} else {
    					result.error500("Operate Faild");
    				}
    			} else {
    				result.error500("No Members");
    			}
    		} else {
    			result.error500("Operate Faild");
    		}
    	} catch (Exception e) {
    		log.error("url: /partner2/memoadd --- method: memoadd --- error: " + e.toString());
    	}
    	return result;
    }
	
	@GetMapping(value = "memberDetailsTop")
    public String memberDetailsTop(@RequestParam("idx") String memberSeq, Model model, HttpServletRequest request) {
        try {
            MemberForm memberForm = new MemberForm();
            memberForm.setSeq(memberSeq);
            memberForm.setUserType(CommonConstant.USER_TYPE_NORMAL);
            memberForm = memberService.getMemberBySeq(memberForm);

            List<Map<String, String>> memoList = new ArrayList<>(); 
			JSONArray memoArr =JSONObject.parseArray(memberForm.getMemo());
			if (memoArr != null){
				for (int i = 0; i < memoArr.size(); i++) {
					JSONObject obj = memoArr.getJSONObject(i);
					Map<String, String> memo = new HashMap<>();
					memo.put("hour", obj.getString("hour")); 
					memo.put("contents", obj.getString("contents"));
					memoList.add(memo); 
				} 
				memberForm.setMemoList(memoList); 
			}

            QueryWrapper<Member> mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> storeList = memberService.list(mQw);

            mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_DISTRIBUTOR);
            List<Member> distributorList = memberService.list(mQw);

            mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_SUB_HEADQUARTER);
            List<Member> subHeadquarterList = memberService.list(mQw);

			System.out.println("memberForm");
			System.out.println(memberForm);

            model.addAttribute("memberForm", memberForm);
            model.addAttribute("storeList", storeList);
            model.addAttribute("distributorList", distributorList);
            model.addAttribute("subHeadquarterList", subHeadquarterList);
            model.addAttribute("url", "/partner2/memberDetailsTop");
        } catch (Exception e) {
            log.error("url: /member/memberDetailsTop --- method: memberDetailsTop --- message: " + e.toString());
        }
        return "views/admin/common/deputyHeadquarterDetail";
    }
	
	@GetMapping(value = "memberDetails")
    public String memberDetails(@RequestParam("idx") String memberSeq, Model model, HttpServletRequest request) {
        try {
            MemberForm memberForm = new MemberForm();
            memberForm.setSeq(memberSeq);
            memberForm.setUserType(CommonConstant.USER_TYPE_NORMAL);
            memberForm = memberService.getMemberBySeq(memberForm);

            List<Map<String, String>> memoList = new ArrayList<>(); 
			JSONArray memoArr =JSONObject.parseArray(memberForm.getMemo());
			if (memoArr != null){
				for (int i = 0; i < memoArr.size(); i++) {
					JSONObject obj = memoArr.getJSONObject(i);
					Map<String, String> memo = new HashMap<>(); memo.put("hour", obj.getString("hour")); 
					memo.put("contents", obj.getString("contents"));
					memoList.add(memo); 
				} 
				memberForm.setMemoList(memoList); 
			}

            QueryWrapper<Member> mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> storeList = memberService.list(mQw);

            mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_DISTRIBUTOR);
            List<Member> distributorList = memberService.list(mQw);

            mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_SUB_HEADQUARTER);
            List<Member> subHeadquarterList = memberService.list(mQw);

            model.addAttribute("memberForm", memberForm);
            model.addAttribute("storeList", storeList);
            model.addAttribute("distributorList", distributorList);
            model.addAttribute("subHeadquarterList", subHeadquarterList);
            model.addAttribute("url", "/partner2/memberDetails");
        } catch (Exception e) {
            log.error("url: /member/memberDetailsTop --- method: memberDetailsTop --- message: " + e.toString());
        }
        return "views/admin/common/partnerDistributorDetail";
    }
	
	@GetMapping(value = "memberDetailsStore")
    public String memberDetailsStore(@RequestParam("idx") String memberSeq, Model model, HttpServletRequest request) {
        try {
            MemberForm memberForm = new MemberForm();
            memberForm.setSeq(memberSeq);
            memberForm.setUserType(CommonConstant.USER_TYPE_NORMAL);
            memberForm = memberService.getMemberBySeq(memberForm);

            List<Map<String, String>> memoList = new ArrayList<>(); 
			JSONArray memoArr =JSONObject.parseArray(memberForm.getMemo());
			if (memoArr != null){
				for (int i = 0; i < memoArr.size(); i++) {
					JSONObject obj = memoArr.getJSONObject(i);
					Map<String, String> memo = new HashMap<>(); memo.put("hour", obj.getString("hour")); 
					memo.put("contents", obj.getString("contents"));
					memoList.add(memo); 
				} 
				memberForm.setMemoList(memoList); 
			}

            QueryWrapper<Member> mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> storeList = memberService.list(mQw);

            mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_DISTRIBUTOR);
            List<Member> distributorList = memberService.list(mQw);

            mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_SUB_HEADQUARTER);
            List<Member> subHeadquarterList = memberService.list(mQw);

            model.addAttribute("memberForm", memberForm);
            model.addAttribute("storeList", storeList);
            model.addAttribute("distributorList", distributorList);
            model.addAttribute("subHeadquarterList", subHeadquarterList);
            model.addAttribute("url", "/partner2/memberDetails");
        } catch (Exception e) {
            log.error("url: /member/memberDetailsStore --- method: memberDetailsStore --- message: " + e.toString());
        }
        return "views/admin/common/partnerStoreDetail";
    }
	
}