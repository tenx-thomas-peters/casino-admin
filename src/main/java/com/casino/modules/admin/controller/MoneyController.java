package com.casino.modules.admin.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.casino.common.utils.HttpUtils;
import com.casino.modules.admin.common.entity.Level;
import com.casino.modules.admin.service.ILevelService;
import com.casino.modules.admin.service.impl.MoneyHistoryServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.constant.CommonConstant;
import com.casino.common.vo.Result;
import com.casino.modules.admin.common.entity.Dict;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.common.entity.MoneyHistory;
import com.casino.modules.admin.service.IDictService;
import com.casino.modules.admin.service.IMemberService;
import com.casino.modules.admin.service.IMoneyHistoryService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/log")
@Slf4j
public class MoneyController {
    @Value(value = "${gameServer.apiKey}")
    private String apiKey;

    @Value(value = "${gameServer.url}")
    private String gameServerUrl;

    @Autowired
    private IMoneyHistoryService moneyHistoryService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private ILevelService levelService;

    @RequestMapping(value = "/moneyloglist", method = {RequestMethod.GET, RequestMethod.POST})
    public String moneyLoglist(Model model,
                               @ModelAttribute("moneyHistory") MoneyHistory moneyHistory,
                               @RequestParam(name = "column", defaultValue = "mon.application_time") String column,
                               @RequestParam(name = "order", defaultValue = "1") Integer order,
                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "100") Integer pageSize,
                               HttpServletRequest request) {
        try {
            Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
            moneyHistory.setCheckTimeType(moneyHistory.getCheckTimeTypeApplication());
            moneyHistory.setStatus(1);
            moneyHistory.setPartnerOrMember(0);
            IPage<MoneyHistory> pageList = moneyHistoryService.findList(page, moneyHistory, column, order);

            QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
            dictQueryWrapper.eq("dict_key", CommonConstant.DICT_KEY_MONEY_REASON);
            List<Dict> reasonTypeList = dictService.list(dictQueryWrapper);
            model.addAttribute("reasonTypeList", reasonTypeList);

            QueryWrapper<Member> qw = new QueryWrapper<>();
            qw.eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> storeList = memberService.list(qw);
            model.addAttribute("storeList", storeList);

            model.addAttribute("page", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("column", column);
            model.addAttribute("order", order);
            model.addAttribute("url", "/log/moneyloglist");
        } catch (Exception e) {
            log.error("url: /log/moneyloglist --- method: moneyLoglist --- error: " + e.toString());
        }
        return "views/admin/money/moneyLog";
    }

    @RequestMapping(value = "/partner_moneyloglist")
    public String getPartenerMoneyLogList(Model model,
                                          @ModelAttribute("moneyHistory") MoneyHistory moneyHistory,
                                          @RequestParam(name = "column", defaultValue = "mh.application_time") String column,
                                          @RequestParam(name = "order", defaultValue = "1") Integer order,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "100") Integer pageSize,
                                          HttpServletRequest request) {
        try {
            if (moneyHistory.getFromProcessTime() == null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                moneyHistory.setFromProcessTime(sdf.format(new Date()));
            }
            if (moneyHistory.getToProcessTime() == null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                moneyHistory.setToProcessTime(sdf.format(new Date()));
            }
            Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
            IPage<MoneyHistory> pageList = moneyHistoryService.findPartenerMoneyLogList(page, moneyHistory, column, order);

            moneyHistoryService.changeAdminReadStatusAll(CommonConstant.MONEY_OPERATION_TYPE_TRANSFER, CommonConstant.PARTNER_OR_MEMBER_PARTNER);

            QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
            dictQueryWrapper.eq("dict_key", CommonConstant.DICT_KEY_MONEY_REASON);
            List<Dict> reasonTypeList = dictService.list(dictQueryWrapper);

            model.addAttribute("moneyForReasonList", reasonTypeList);
            model.addAttribute("page", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("column", column);
            model.addAttribute("order", order);
            model.addAttribute("url", "/log/partner_moneyloglist");
        } catch (Exception e) {
            log.error("url: /log/partnerMoneyLogLlist --- method: getPartenerMoneyLogList --- error: " + e.toString());
        }
        return "views/admin/money/partnerMoneyLogList";
    }

    @RequestMapping(value = "/depositloglist", method = {RequestMethod.GET, RequestMethod.POST})
    public String depositLoglist(Model model,
                                 @ModelAttribute("moneyHistory") MoneyHistory moneyHistory,
                                 @RequestParam(name = "column", defaultValue = "mon.application_time") String column,
                                 @RequestParam(name = "order", defaultValue = "1") Integer order,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "100") Integer pageSize,
                                 HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(moneyHistory.getFromProcessTime())) {
                moneyHistory.setFromProcessTime(null);
            }
            if (StringUtils.isBlank(moneyHistory.getToProcessTime())) {
                moneyHistory.setToProcessTime(null);
            }
            Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
            moneyHistory.setPartnerOrMember(CommonConstant.PARTNER_OR_MEMBER_MEMBER);
            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT);
            moneyHistory.setReasonType(CommonConstant.MONEY_REASON_DEPOSIT);
            moneyHistory.setSearchType(moneyHistory.getSearchTypeMember());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isBlank(moneyHistory.getFromProcessTime()) && StringUtils.isBlank(moneyHistory.getToProcessTime())) {
                moneyHistory.setFromProcessTime(sdf.format(new Date()));
                moneyHistory.setToProcessTime(sdf.format(new Date()));
                moneyHistory.setCheckTimeType(moneyHistory.getCheckTimeTypeApplication());
            }

            moneyHistoryService.changeAdminReadStatusAll(CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT, CommonConstant.PARTNER_OR_MEMBER_MEMBER);
            IPage<MoneyHistory> pageList = moneyHistoryService.findList(page, moneyHistory, column, order);

            QueryWrapper<Member> qw = new QueryWrapper<>();
            qw.eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> storeList = memberService.list(qw);
            model.addAttribute("storeList", storeList);

            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT);
            moneyHistory.setReasonType(CommonConstant.MONEY_REASON_DEPOSIT);
            Float totalDeposit = moneyHistoryService.getTotalAmountByDateRange(moneyHistory);
            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW);
            moneyHistory.setReasonType(CommonConstant.MONEY_REASON_WITHDRAW);
            Float totalWithdraw = moneyHistoryService.getTotalAmountByDateRange(moneyHistory);

            model.addAttribute("totalDeposit", totalDeposit);
            model.addAttribute("totalWithdraw", totalWithdraw);

            model.addAttribute("page", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("column", column);
            model.addAttribute("order", order);
            model.addAttribute("url", "/log/depositloglist");
        } catch (Exception e) {
            log.error("url: /log/depositloglist --- method: depositLoglist --- error: " + e.toString());
        }
        return "views/admin/money/depositLog";
    }

    @RequestMapping(value = "/withdrawloglist", method = {RequestMethod.GET, RequestMethod.POST})
    public String withdrawLoglist(Model model,
                                  @ModelAttribute("moneyHistory") MoneyHistory moneyHistory,
                                  @RequestParam(name = "column", defaultValue = "mon.application_time") String column,
                                  @RequestParam(name = "order", defaultValue = "1") Integer order,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "100") Integer pageSize,
                                  HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(moneyHistory.getFromProcessTime())) {
                moneyHistory.setFromProcessTime(null);
            }
            if (StringUtils.isBlank(moneyHistory.getToProcessTime())) {
                moneyHistory.setToProcessTime(null);
            }
            Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
            moneyHistory.setPartnerOrMember(CommonConstant.PARTNER_OR_MEMBER_MEMBER);
            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW);
            moneyHistory.setReasonType(CommonConstant.MONEY_REASON_WITHDRAW);
            moneyHistory.setSearchType(moneyHistory.getSearchTypeMember());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isBlank(moneyHistory.getFromProcessTime()) && StringUtils.isBlank(moneyHistory.getToProcessTime())) {
                moneyHistory.setFromProcessTime(sdf.format(new Date()));
                moneyHistory.setToProcessTime(sdf.format(new Date()));
                moneyHistory.setCheckTimeType(moneyHistory.getCheckTimeTypeApplication());
            }
            moneyHistoryService.changeAdminReadStatusAll(CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW, CommonConstant.PARTNER_OR_MEMBER_MEMBER);
            IPage<MoneyHistory> pageList = moneyHistoryService.findList(page, moneyHistory, column, order);

            QueryWrapper<Member> qw = new QueryWrapper<>();
            qw.eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> storeList = memberService.list(qw);
            model.addAttribute("storeList", storeList);

            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT);
            moneyHistory.setReasonType(CommonConstant.MONEY_REASON_DEPOSIT);
            Float totalDeposit = moneyHistoryService.getTotalAmountByDateRange(moneyHistory);
            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW);
            moneyHistory.setReasonType(CommonConstant.MONEY_REASON_WITHDRAW);
            Float totalWithdraw = moneyHistoryService.getTotalAmountByDateRange(moneyHistory);

            model.addAttribute("totalDeposit", totalDeposit);
            model.addAttribute("totalWithdraw", totalWithdraw);

            List<Map<String, String>> siteList = memberService.getSiteList();
            model.addAttribute("siteList", siteList);

            model.addAttribute("page", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("column", column);
            model.addAttribute("order", order);
            model.addAttribute("url", "/log/withdrawloglist");
        } catch (Exception e) {
            log.error("url: /log/withdrawloglist --- method: withdrawLoglist --- error: " + e.toString());
        }
        return "views/admin/money/withdrawLog";
    }

    @RequestMapping(value = "/partner/depositloglist", method = {RequestMethod.GET, RequestMethod.POST})
    public String pDepositLoglist(Model model,
                                  @ModelAttribute("moneyHistory") MoneyHistory moneyHistory,
                                  @RequestParam(name = "column", defaultValue = "mon.application_time") String column,
                                  @RequestParam(name = "order", defaultValue = "1") Integer order,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "100") Integer pageSize,
                                  HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(moneyHistory.getFromProcessTime())) {
                moneyHistory.setFromProcessTime(null);
            }
            if (StringUtils.isBlank(moneyHistory.getToProcessTime())) {
                moneyHistory.setToProcessTime(null);
            }
            Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
            moneyHistory.setPartnerOrMember(CommonConstant.PARTNER_OR_MEMBER_PARTNER);
            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT);
            moneyHistory.setReasonType(CommonConstant.MONEY_REASON_DEPOSIT);
            moneyHistory.setSearchType(moneyHistory.getSearchTypeMember());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isBlank(moneyHistory.getFromProcessTime()) && StringUtils.isBlank(moneyHistory.getToProcessTime())) {
                moneyHistory.setFromProcessTime(sdf.format(new Date()));
                moneyHistory.setToProcessTime(sdf.format(new Date()));
                moneyHistory.setCheckTimeType(moneyHistory.getCheckTimeTypeApplication());
            }

            moneyHistoryService.changeAdminReadStatusAll(CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT, CommonConstant.PARTNER_OR_MEMBER_PARTNER);
            IPage<MoneyHistory> pageList = moneyHistoryService.findList(page, moneyHistory, column, order);

            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT);
            moneyHistory.setReasonType(CommonConstant.MONEY_REASON_DEPOSIT);
            Float totalDeposit = moneyHistoryService.getTotalAmountByDateRange(moneyHistory);
            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW);
            moneyHistory.setReasonType(CommonConstant.MONEY_REASON_WITHDRAW);
            Float totalWithdraw = moneyHistoryService.getTotalAmountByDateRange(moneyHistory);

            model.addAttribute("totalDeposit", totalDeposit);
            model.addAttribute("totalWithdraw", totalWithdraw);

            model.addAttribute("page", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("column", column);
            model.addAttribute("order", order);
            model.addAttribute("url", "/log/partner/depositloglist");
        } catch (Exception e) {
            log.error("url: /log/partner/depositloglist --- method: pDepositLoglist --- error: " + e.toString());
        }
        return "views/admin/money/partner/depositLog";
    }

    @RequestMapping(value = "/partner/withdrawloglist", method = {RequestMethod.GET, RequestMethod.POST})
    public String pWithdrawLoglist(Model model,
                                   @ModelAttribute("moneyHistory") MoneyHistory moneyHistory,
                                   @RequestParam(name = "column", defaultValue = "mon.application_time") String column,
                                   @RequestParam(name = "order", defaultValue = "1") Integer order,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "100") Integer pageSize,
                                   HttpServletRequest request) {
        try {
            Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
            moneyHistory.setPartnerOrMember(CommonConstant.PARTNER_OR_MEMBER_PARTNER);
            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW);
            moneyHistory.setReasonType(CommonConstant.MONEY_REASON_WITHDRAW);
            moneyHistory.setSearchType(moneyHistory.getSearchTypeMember());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isBlank(moneyHistory.getFromProcessTime()) && StringUtils.isBlank(moneyHistory.getToProcessTime())) {
                moneyHistory.setFromProcessTime(sdf.format(new Date()));
                moneyHistory.setToProcessTime(sdf.format(new Date()));
                moneyHistory.setCheckTimeType(moneyHistory.getCheckTimeTypeApplication());
            }

            moneyHistoryService.changeAdminReadStatusAll(CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW, CommonConstant.PARTNER_OR_MEMBER_PARTNER);
            IPage<MoneyHistory> pageList = moneyHistoryService.findList(page, moneyHistory, column, order);

            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT);
            moneyHistory.setReasonType(CommonConstant.MONEY_REASON_DEPOSIT);
            Float totalDeposit = moneyHistoryService.getTotalAmountByDateRange(moneyHistory);
            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW);
            moneyHistory.setReasonType(CommonConstant.MONEY_REASON_WITHDRAW);
            Float totalWithdraw = moneyHistoryService.getTotalAmountByDateRange(moneyHistory);

            model.addAttribute("totalDeposit", totalDeposit);
            model.addAttribute("totalWithdraw", totalWithdraw);

            model.addAttribute("page", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("column", column);
            model.addAttribute("order", order);
            model.addAttribute("url", "/log/partner/withdrawloglist");
        } catch (Exception e) {
            log.error("url: /log/partner/withdrawloglist --- method: pWithdrawLoglist --- error: " + e.toString());
        }
        return "views/admin/money/partner/withdrawLog";
    }

    @GetMapping(value = "/money/detaillist")
    public String moneyDetailList(
            Model model,
            @RequestParam("date") String date,
            @RequestParam("type") Integer type,
            @RequestParam("operationType") Integer operationType,
            HttpServletRequest request) {
        try {
            MoneyHistory moneyHistory = new MoneyHistory();
            moneyHistory.setCheckDay(date);
            moneyHistory.setSearchType(type);
            moneyHistory.setOperationType(operationType);
            List<MoneyHistory> list = moneyHistoryService.getList(moneyHistory);
            model.addAttribute("list", list);
        } catch (Exception e) {
            log.error("url: /log/money/detaillist --- method: moneyDetailList --- " + e.getMessage());
        }
        return "views/admin/money/detailPopUp";
    }

    @GetMapping(value = "/moneyrequest/decline")
    @ResponseBody
    public Result<MoneyHistory> moneyRequestDecline(
            @RequestParam("seq") String seq,
            HttpServletRequest request) {
        Result<MoneyHistory> result = new Result<>();
        try {
            MoneyHistory moneyHistory = moneyHistoryService.getById(seq);
            moneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_CANCEL);
            if (moneyHistoryService.updateById(moneyHistory)) {
                result.success("success!");
            } else {
                result.error500("failed");
            }
        } catch (Exception e) {
            log.error("url: /log/moneyrequest/decline --- method: moneyRequestDecline --- " + e.getMessage());
            result.error500("failed");
        }
        return result;
    }

    @GetMapping(value = "/moneyrequest/detail")
    @ResponseBody
    public Result<MoneyHistory> moneyRequestDetail(
            @RequestParam("seq") String seq,
            HttpServletRequest request) {
        Result<MoneyHistory> result = new Result<>();
        try {
            MoneyHistory moneyHistory = moneyHistoryService.getById(seq);
            result.success("success!");
            result.setResult(moneyHistory);
        } catch (Exception e) {
            log.error("url: /log/moneyrequest/detail --- method: moneyRequestDetail --- " + e.getMessage());
            result.error500("failed");
        }
        return result;
    }

    @RequestMapping(value = "/moneydeposit/accept")
    @ResponseBody
    public Result<MoneyHistory> moneyDepositAccept(
            @RequestParam("seq") String seq,
            @RequestParam(name = "depositAmount", defaultValue = "0") String strDepositAmount,
            @RequestParam(name = "bonus", defaultValue = "9687450") Float bonus,
            HttpServletRequest request) {
        Result<MoneyHistory> result = new Result<>();
        try {
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            float depositAmount = format.parse(strDepositAmount).floatValue();

            // member user is first charge ============================================= <
            Member member = memberService.getById(moneyHistoryService.getById(seq).getReceiver());
            Map<String, Number> firstdepositCount = moneyHistoryService.getTodayFirstMoneyHistory(member.getSeq());

            int firstChargeFlag = 0;
            float firstChargeAmount = 0f;
            int total_first_charge = firstdepositCount.get("total_first_charge").intValue();
            if(total_first_charge == 0){
                firstChargeFlag = 1;
                Level level = levelService.getById(member.getLevelSeq());
                if(bonus == 9687450){
                    bonus = level.getFirstInsect();
                }

                if(bonus > 0){
                    firstChargeAmount = bonus * depositAmount / 100;

                    System.out.println("MoneyController==moneyDepositAccept==");
                    System.out.println("\tfirst charge deposit *******************************************");
                    System.out.println("\t*** first charge rate: " + level.getFirstInsect());
                    System.out.println("\t*** deposit Amount: " + depositAmount);
                    System.out.println("\t*** first charge mileage Amount: " + firstChargeAmount);
                    System.out.println("\t*******************************************");

                    String mileageReason =
                            "첫충 입금 -> 레벨 " + level.getLevelName();

                    // set mileage
                    memberService.updateMemberHoldingMoney(
                            member.getSeq(),
                            0f,
                            member.getMileageAmount(),
                            firstChargeAmount,
                            Math.abs(firstChargeAmount),
                            member.getMileageAmount() + firstChargeAmount,
                            1,
                            0,
                            CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
                            CommonConstant.MONEY_REASON_DEPOSIT,
                            mileageReason,
                            0,
                            ""
                    );
                }
            }
            // member user is first charge ============================================= />

            String reason = "회원 입금 [" + depositAmount + "]";
            if (moneyHistoryService.acceptMoneyHistory(
                    seq,
                    depositAmount,
                    firstChargeAmount,
                    CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
                    firstChargeFlag,
                    reason,
                    CommonConstant.MONEY_REASON_DEPOSIT
            )) {
                result.success("success!");
            } else {
                result.error505("failed");
            }
        } catch (Exception e) {
            log.error("url: /log/moneydeposit/accept --- method: moneyDepositAccept --- " + e.getMessage());
            result.error500("failed");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/moneywithdraw/accept")
    @ResponseBody
    public Result<MoneyHistory> moneyWithdrawAccept(
            @RequestParam("seq") String seq,
            @RequestParam("withdrawAmount") Float withdrawAmount,
            HttpServletRequest request) {
        Result<MoneyHistory> result = new Result<>();
        try {

            MoneyHistory history = moneyHistoryService.getById(seq);
            Member member = memberService.getById(history.getReceiver());
            String reason = "회원 출금 [-" + withdrawAmount + "]";
            if (moneyHistoryService.acceptMoneyHistory(
                    seq,
                    withdrawAmount,
                    null,
                    CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL,
                    0,
                    reason,
                    CommonConstant.MONEY_REASON_WITHDRAW
            )) {
                result.success("success!");
            } else {
                result.error505("failed");
            }
        } catch (Exception e) {
            log.error("url: /log/moneywithdraw/accept --- method: moneyWithdrawAccept --- " + e.getMessage());
            result.error500("failed");
        }
        return result;
    }

    @RequestMapping(value = "/partnerMoneydeposit/accept")
    @ResponseBody
    public Result<MoneyHistory> PartnerMoneyDepositAccept(
            @RequestParam("seq") String seq,
            @RequestParam(name = "depositAmount", defaultValue = "0") Float depositAmount,
            @RequestParam(name = "bonus", defaultValue = "0") Float bonus,
            HttpServletRequest request) {
        Result<MoneyHistory> result = new Result<>();
        try {
            //TODO
            // game api - /user/add-balance start
            MoneyHistory history = moneyHistoryService.getById(seq);
            Member member = memberService.getById(history.getReceiver());
            Float amount = depositAmount + depositAmount * bonus;

            String reason = "파트너 입금 [" + depositAmount + "]";
            if (moneyHistoryService.acceptMoneyHistory(
                    seq,
                    depositAmount,
                    bonus,
                    CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
                    0,
                    reason,
                    CommonConstant.MONEY_REASON_DEPOSIT
            )) {
                result.success("success!");
            } else {
                result.error505("failed");
            }

        } catch (Exception e) {
            log.error("url: /log/partnerMoneydeposit/accept --- method: moneyDepositAccept --- " + e.getMessage());
            result.error500("failed");
        }
        return result;
    }

    @RequestMapping(value = "/partnerMoneywithdraw/accept")
    @ResponseBody
    public Result<MoneyHistory> PartnerMoneyWithdrawAccept(
            @RequestParam("seq") String seq,
            @RequestParam("withdrawAmount") Float withdrawAmount,
            HttpServletRequest request) {
        Result<MoneyHistory> result = new Result<>();
        try {
            //TODO
            // game api - /user/sub-balance start
            MoneyHistory history = moneyHistoryService.getById(seq);
            Member member = memberService.getById(history.getReceiver());
            String reason = "파트너 출금 [-" + withdrawAmount + "]";
            if (moneyHistoryService.acceptMoneyHistory(
                    seq,
                    withdrawAmount,
                    null,
                    CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL,
                    0,
                    reason,
                    CommonConstant.MONEY_REASON_WITHDRAW
            )) {
                result.success("success!");
            } else {
                result.error505("failed");
            }
        } catch (Exception e) {
            log.error("url: /log/partnerMoneywithdraw/accept --- method: moneyWithdrawAccept --- " + e.getMessage());
            result.error500("failed");
        }
        return result;
    }

    @RequestMapping(value = "/member/money/historydata", method = {RequestMethod.GET, RequestMethod.POST})
    public String memberMoneyHistoryData(
            Model model,
            @RequestParam("memberSeq") String memberSeq,
            HttpServletRequest request) {
        try {
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String reasonType = request.getParameter("reasonType");
            MoneyHistory moneyHistory = new MoneyHistory();
            Member member = new Member();
            member.setSeq(memberSeq);
            moneyHistory.setMember(member);
            moneyHistory.setCheckTimeType(moneyHistory.getCheckTimeTypeApplication());
            moneyHistory.setFromProcessTime(startDate);
            moneyHistory.setToProcessTime(endDate);
            moneyHistory.setReasonType(StringUtils.isBlank(reasonType) ? null : Integer.valueOf(reasonType));
            QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
            dictQueryWrapper.eq("dict_key", CommonConstant.DICT_KEY_MONEY_REASON);
            List<Dict> reasonTypeList = dictService.list(dictQueryWrapper);
            model.addAttribute("reasonTypeList", reasonTypeList);

            List<MoneyHistory> list = moneyHistoryService.getList(moneyHistory);
            model.addAttribute("list", list);
            model.addAttribute("memberSeq", memberSeq);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("reasonType", reasonType);
            model.addAttribute("url", "log/member/money/historydata");
        } catch (Exception e) {
            log.error("url: /log/member/money/historydata --- method: memberMoneyHistoryData --- " + e.getMessage());
        }
        return "views/admin/member/moneyHistory";
    }

    @GetMapping(value = "charge/agree")
    public String chargePopUp(@RequestParam("idx") String moneyHistorySeq, @RequestParam("member_type") String memberType, Model model, HttpServletRequest request) {
        String returnUrl = "";
        try {
            MoneyHistory moneyHistory = moneyHistoryService.getById(moneyHistorySeq);
            Member member = memberService.getById(moneyHistory.getReceiver());
            Level level = levelService.getById(member.getLevelSeq());
            moneyHistory.setMember(member);

            if(memberType.equals("member")){
                if (moneyHistory.getOperationType() == CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT) {
                    returnUrl = "views/admin/money/chargePopUp";
                } else {
                    returnUrl = "views/admin/money/withdrawPopUp";
                }
            }
            else{
                if (moneyHistory.getOperationType() == CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT) {
                    returnUrl = "views/admin/money/partnerChargePopUp";
                } else {
                    returnUrl = "views/admin/money/partnerWithdrawPopUp";
                }
            }

            Map<String, Number> firstdepositCount = moneyHistoryService.getTodayFirstMoneyHistory(member.getSeq());

            int total_first_charge = firstdepositCount.get("total_first_charge").intValue();

            model.addAttribute("moneyHistory", moneyHistory);
            model.addAttribute("level", level);
            model.addAttribute("firstCharge", total_first_charge);
            model.addAttribute("url", "/log/charge/agree");
        } catch (Exception e) {
            log.error("url: /log/charge/agree --- method: chargePopUp --- message: " + e.toString());
        }
        return returnUrl;
    }

    @GetMapping(value = "/new_popup_moneyloglist")
    public String moneyLogListModal(Model model,
                                    @ModelAttribute("moneyHistory") MoneyHistory moneyHistory,
                                    @RequestParam(name = "column", defaultValue = "mon.application_time") String column,
                                    @RequestParam(name = "order", defaultValue = "1") Integer order,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request) {
        try {
            Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
            moneyHistory.setCheckTimeType(moneyHistory.getCheckTimeTypeApplication());
            IPage<MoneyHistory> pageList = moneyHistoryService.findList(page, moneyHistory, column, order);

            QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
            dictQueryWrapper.eq("dict_key", CommonConstant.DICT_KEY_MONEY_REASON);
            List<Dict> reasonTypeList = dictService.list(dictQueryWrapper);
            model.addAttribute("reasonTypeList", reasonTypeList);

            QueryWrapper<Member> qw = new QueryWrapper<>();
            qw.eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> storeList = memberService.list(qw);
            model.addAttribute("storeList", storeList);

            model.addAttribute("page", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("column", column);
            model.addAttribute("order", order);
            model.addAttribute("url", "/log/new_popup_moneyloglist");
        } catch (Exception e) {
            log.error("url: /log/new_popup_moneyloglist --- method: moneyLogListModal --- error: " + e.toString());
        }
        return "views/admin/money/moneyLogListModal";
    }
}
