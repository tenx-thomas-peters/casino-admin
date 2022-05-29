package com.casino.modules.admin.controller;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.admin.service.IAccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.common.vo.Result;
import com.casino.modules.admin.common.entity.AccessLog;
import com.casino.modules.admin.common.entity.BettingSummary;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.service.IBettingSummaryService;
import com.casino.modules.admin.service.IMemberService;
import com.casino.modules.admin.service.IMockService;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/mock")
@Slf4j
public class MockController {

	@Autowired
	IMockService mockService;

	@Autowired
	IMemberService memberService;

	@Autowired
	private IBettingSummaryService bettingSummaryService;

	@Autowired
	private IAccessLogService accessLogService;

	@Value(value = "${mock.baccarat}")
	private String mockBaccarat;

	@Value(value = "${mock.slot}")
	private String mockSlot;

	@Value(value = "${mock.roulette}")
	private String mockRoulette;

	@RequestMapping(value = "/member/money", method = { RequestMethod.GET, RequestMethod.POST })
	public String toMemberMoneyPage(Model model) {
		try {
			QueryWrapper<Member> qw = new QueryWrapper<>();
			qw.eq("user_type", CommonConstant.USER_TYPE_NORMAL);
			List<Member> memberList = memberService.list(qw);
			model.addAttribute("memberList", memberList);
		} catch (Exception e) {
			log.error("url: /mock/member/money --- method: toMemberMoneyPage --- error: " + e.toString());
		}
		return "views/mock/member-money";
	}

	@RequestMapping(value = "/partner/money", method = { RequestMethod.GET, RequestMethod.POST })
	public String toPartnerMoneyPage(Model model) {
		try {
			QueryWrapper<Member> qw1 = new QueryWrapper<>();
			qw1.gt("user_type", CommonConstant.USER_TYPE_NORMAL);
			List<Member> partnerList = memberService.list(qw1);
			model.addAttribute("partnerList", partnerList);
			
			QueryWrapper<Member> qw2 = new QueryWrapper<>();
			List<Member> memberList = memberService.list(qw2);
			model.addAttribute("memberList", memberList);
		} catch (Exception e) {
			log.error("url: /mock/partner/money --- method: toPartnerMoneyPage --- error: " + e.toString());
		}
		return "views/mock/partner-money";
	}

	@RequestMapping(value = "/money/deposit", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Result<String> moneyDeposit(Model model,
			@RequestParam("memberSeq") String memberSeq, 
			@RequestParam("moneyAmount") String moneyAmount, 
			@RequestParam(name = "note", defaultValue = "") String note) {
		Result<String> result = new Result<>();
		try {
			Member member = memberService.getById(memberSeq);
			Integer reasonType = 0;
			if(member.getUserType() == CommonConstant.USER_TYPE_NORMAL) {
				reasonType = CommonConstant.MONEY_REASON_DEPOSIT;
			}else {
				reasonType = CommonConstant.MONEY_REASON_PARTNER_DEPOSIT;
			}
			mockService.updateMemberMoney(member, moneyAmount, CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT, reasonType, note);
			result.success("success");
		} catch (Exception e) {
			log.error("url: /mock/money/deposit --- method: moneyDeposit --- error: " + e.toString());
		}
		return result;
	}

	@RequestMapping(value = "/money/withdraw", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Result<String> moneyWithdraw(Model model,
			@RequestParam("memberSeq") String memberSeq, 
			@RequestParam("moneyAmount") String moneyAmount, 
			@RequestParam(name = "note", defaultValue = "") String note) {
		Result<String> result = new Result<>();
		try {
			Member member = memberService.getById(memberSeq);
			Integer reasonType = 0;
			if(member.getUserType() == CommonConstant.USER_TYPE_NORMAL) {
				reasonType = CommonConstant.MONEY_REASON_WITHDRAW;
			}else {
				reasonType = CommonConstant.MONEY_REASON_PARTNER_WITHDRAW;
			}
			mockService.updateMemberMoney(member, moneyAmount, CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL, reasonType, note);
			result.success("success");
		} catch (Exception e) {
			log.error("url: /mock/money/withdraw --- method: moneyWithdraw --- error: " + e.toString());
		}
		return result;
	}

	@RequestMapping(value = "/money/transfer", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Result<String> moneyTransfer(Model model,
			@RequestParam("payerSeq") String payerSeq,
			@RequestParam("receiverSeq") String receiverSeq, 
			@RequestParam("moneyAmount") String moneyAmount, 
			@RequestParam(name = "note", defaultValue = "") String note) {
		Result<String> result = new Result<>();
		try {
			mockService.moneyTransfer(payerSeq, receiverSeq, moneyAmount, note);
			result.success("success");
		} catch (Exception e) {
			log.error("url: /mock/money/transfer --- method: moneyTransfer --- error: " + e.toString());
		}
		return result;
	}

	@RequestMapping(value = "/member/bettingSummary")
    public String bettingSummary(@ModelAttribute("bettingSummary") BettingSummary bettingSummary, Model model) {
		try {
			List<Member> memberList = memberService.list();

			model.addAttribute("memberList", memberList);
			model.addAttribute("save", false);
			model.addAttribute("url", "mock/member/bettingSummary");
		} catch (Exception e) {
			log.error("url: /mock/member/bettingSummary --- method: addBettingSummary --- message: " + e.toString());
		}

	    return "views/mock/betting-summary";
    }

    @RequestMapping(value = "/member/addBettingSummary")
	public String addBettingSummary(@ModelAttribute("bettingSummary") BettingSummary bettingSummary, Model model) {
		try {
			Member member = memberService.getById(bettingSummary.getMemberSeq());

			bettingSummary.setStoreSeq(member.getStoreSeq());
			bettingSummary.setDistributorSeq(member.getDistributorSeq());
			bettingSummary.setHeadquarterSeq(member.getSubHeadquarterSeq());
			bettingSummary.setPointRate(bettingSummary.getType().equals(0) ? member.getSlotRate() : member.getBaccaratRate());

			bettingSummary.setSeq(UUIDGenerator.generate());
			bettingSummary.setSlotBetCount(1);

			if (bettingSummaryService.save(bettingSummary)) {
				model.addAttribute("success", true);
			} else {
				model.addAttribute("success", false);
			}

			List<Member> memberList = memberService.list();

			model.addAttribute("memberList", memberList);
			model.addAttribute("save", true);
			model.addAttribute("url", "mock/member/bettingSummary");
		} catch (Exception e) {
			log.error("url: /mock/member/addBettingSummary --- method: addBettingSummary --- message: " + e.toString());
		}
		return "views/mock/betting-summary";
	}

    @RequestMapping(value = "/member/accessLog")
    public String accessLog(@ModelAttribute("accessLog") AccessLog accessLog, Model model) {
		try {
			List<Member> memberList = memberService.list();
			List<Map<String, String>> siteList = memberService.getSiteList();

			model.addAttribute("siteList", siteList);
			model.addAttribute("memberList", memberList);
			model.addAttribute("url", "mock/member/accessLog");
		} catch (Exception e) {
			log.error("url: /mock/member/accessLog --- method: accessLog --- message: " + e.toString());
		}
        return "views/mock/access-log";
    }

	@RequestMapping(value = "/member/addAccessLog")
	public String addAccessLog(@ModelAttribute("accessLog") AccessLog accessLog, Model model) {
		try {
			accessLog.setSeq(UUIDGenerator.generate());
			Member member = memberService.getById(accessLog.getMemberSeq());

			accessLog.setId(member.getId());
			accessLog.setNickname(member.getNickname());
			accessLog.setAccountHolder(member.getAccountHolder());
			accessLog.setLevel(member.getLevelSeq());
			accessLog.setMoneyAmount(member.getMoneyAmount());
			accessLog.setDistributor(member.getDistributorSeq());

			if (accessLogService.save(accessLog)) {
				model.addAttribute("success", true);
			} else {
				model.addAttribute("success", false);
			}

			List<Member> memberList = memberService.list();
			List<Map<String, String>> siteList = memberService.getSiteList();

			model.addAttribute("siteList", siteList);
			model.addAttribute("save", true);
			model.addAttribute("memberList", memberList);
			model.addAttribute("url", "mock/member/accessLog");
		} catch (Exception e) {
			log.error("url: /mock/member/accessLog --- method: accessLog --- message: " + e.toString());
		}
		return "views/mock/access-log";
	}

	@RequestMapping(value = "/member/popupEditMember")
	public String popupEditMember(@ModelAttribute("member") Member member, Model model) {
		try {
			List<Member> memberList = memberService.list();
			List<Map<String, String>> siteList = memberService.getSiteList();

			model.addAttribute("memberList", memberList);
			model.addAttribute("url", "mock/member/popupEditMember");
		} catch (Exception e) {
			log.error("url: /mock/member/popupEditMember --- method: popupEditMember --- message: " + e.toString());
		}
		return "views/mock/member-edit";
	}

	@RequestMapping(value = "/member/editMember")
	@ResponseBody
	public Result<Member> editMember(@ModelAttribute("member") Member member, Model model) {
		Result<Member> result = new Result<>();
		try {
			if (memberService.updateById(member)) {
				result.success("success");
				result.setResult(member);
			} else {
				result.error505("fail");
			}
			model.addAttribute("member", member);
		} catch (Exception e) {
			log.error("url: /mock/member/popupEditMember --- method: popupEditMember --- message: " + e.toString());
			result.error500("internal error");
		}

		return result;
	}

	@RequestMapping(value = "/transaction-list-simple")
	@ResponseBody
	public JSONObject getTransactionListSimple() {
		JSONObject data = new JSONObject();
		try {
			Resource resource = new ClassPathResource(mockBaccarat);
			File file = resource.getFile();

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}

			data = JSONObject.parseObject(builder.toString());
		} catch (Exception e) {
			log.error("url: /mock/transition-list-simple --- method: getTransitionListSimple --- message: " + e.toString());
		}
		return data;
	}

	@RequestMapping(value = "/user/create")
	@ResponseBody
	public JSONObject createUser(HttpServletRequest request) {
		JSONObject data = new JSONObject();
		try {
			String username = request.getParameter("username");
			String nickname = request.getParameter("nickname");

			data.put("id", Math.floor(Math.random() * (100 -1) + 1));
			data.put("username", username);
			data.put("nickname", nickname);
			data.put("country", "KOR");
			data.put("currency_code", "KRW");
			data.put("token", null);
			data.put("balance", 0);
			data.put("point", 0);
			data.put("created_at", new Date());
			data.put("updated_at", new Date());
			data.put("last_access_at", null);
			data.put("agent_id", Math.floor(Math.random() * (100 -1) + 1));
		} catch (Exception e) {
			log.error("url: /mock/transition-list-simple --- method: getTransitionListSimple --- message: " + e.toString());
		}
		return data;
	}

	@RequestMapping(value = "/user/refresh-token")
	@ResponseBody
	public JSONObject refreshToken(HttpServletRequest request) {
		JSONObject data = new JSONObject();
		try {
			String username = request.getParameter("username");

			data.put("token", username + "===RbMFI9mRlwzX4hZIuzPm1OQkcNNM8tlXBPUzfwB8");
		} catch (Exception e) {
			log.error("url: /mock/transition-list-simple --- method: getTransitionListSimple --- message: " + e.toString());
		}
		return data;
	}

	@RequestMapping(value = "/test")
	@ResponseBody
	public Result<IPage<Member>> test() {
		Result<IPage<Member>> result = new Result<>();
		try {
			Page<Member> page = new Page<>(1,10);
			IPage<Member> pageList = memberService.page(page);

			result.setResult(pageList);
			result.success("success");
		} catch (Exception e) {
			log.error("url: /mock/transition-list-simple --- method: getTransitionListSimple --- message: " + e.toString());
			result.error500("Internal Server Error");
		}
		return result;
	}
}
