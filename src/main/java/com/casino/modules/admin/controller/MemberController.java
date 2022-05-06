package com.casino.modules.admin.controller;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.HttpUtils;
import com.casino.common.utils.UUIDGenerator;
import com.casino.common.vo.Result;
import com.casino.modules.admin.common.entity.AccessLog;
import com.casino.modules.admin.common.entity.Dict;
import com.casino.modules.admin.common.entity.Level;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.common.entity.MoneyHistory;
import com.casino.modules.admin.common.entity.Note;
import com.casino.modules.admin.common.entity.SysUser;
import com.casino.modules.admin.common.form.BettingSummaryForm;
import com.casino.modules.admin.common.form.MemberForm;
import com.casino.modules.admin.service.IAccessLogService;
import com.casino.modules.admin.service.IBettingSummaryService;
import com.casino.modules.admin.service.IDictService;
import com.casino.modules.admin.service.ILevelService;
import com.casino.modules.admin.service.IMemberService;
import com.casino.modules.admin.service.IMoneyHistoryService;
import com.casino.modules.admin.service.INoteService;
import com.casino.modules.admin.service.ISysUserService;
import org.apache.tomcat.util.codec.binary.Base64;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/member")
@Slf4j
public class MemberController {
    @Value(value = "${gameServer.url}")
    private String gameServerUrl;

    @Value(value = "${gameServer.apiKey}")
    private String apiKey;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private ILevelService levelService;

    @Autowired
    private IBettingSummaryService bettingSummaryService;

    @Autowired
    private INoteService noteService;

    @Autowired
    private IAccessLogService accessLogService;

    @Autowired
    private IMoneyHistoryService moneyHistoryService;

    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private ISysUserService sysUserService;

    @RequestMapping(value = "list")
    public String memberList(@ModelAttribute("memberForm") MemberForm memberForm,
                             @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                             @RequestParam(value = "pageSize", defaultValue = "150") Integer pageSize,
                             @RequestParam(value = "order", defaultValue = "1") Integer order,
                             @RequestParam(value = "column", defaultValue = "create_date") String column,
                             Model model, HttpServletRequest request) {
        try {
            Page<MemberForm> page = new Page<>(pageNo, pageSize);
            memberForm.setUserType(CommonConstant.USER_TYPE_NORMAL);
            IPage<MemberForm> pageList = memberService.getMemberList(page, memberForm, column, order);

            QueryWrapper<Dict> dictQw = new QueryWrapper<>();
            dictQw.eq("dict_key", "MEMBER_STATUS");
            List<Dict> statusList = dictService.list(dictQw);

            List<Level> levelList = levelService.list();
            List<Map<String, String>> siteList = memberService.getSiteList();

            QueryWrapper<Member> memberQw = new QueryWrapper<>();
            memberQw.eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> partnerList = memberService.list(memberQw);

            model.addAttribute("pageList", pageList);
            model.addAttribute("member", new Member());
            model.addAttribute("memberForm", memberForm);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("order", order);
            model.addAttribute("column", column);
            model.addAttribute("state", memberForm.getState() != null ? memberForm.getState().toString() : "");
            model.addAttribute("levels", memberForm.getLevels() != null ? memberForm.getLevels().toString() : "");
            model.addAttribute("levelList", levelList);
            model.addAttribute("statusList", statusList);
            model.addAttribute("siteList", siteList);
            model.addAttribute("partnerList", partnerList);
            String[] params = {String.valueOf(pageList.getTotal())};

            // Thomas 2022.04.25 -- remove
//            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
//            model.addAttribute("totalMsg", messageSource.getMessage("admin.member.peopleInTotal", params, localeResolver.resolveLocale(request)));
            
            model.addAttribute("totalMsg", pageList.getTotal());

            model.addAttribute("url", "member/list");
        } catch (Exception e) {
            log.error("url: /member/list --- method: getList --- message: " + e.toString());
        }

        return "views/admin/member/list";
    }

    @GetMapping(value = "/add")
    public String addMember(Model model, HttpServletRequest request) {
        try {
            List<Map<String, String>> siteList = memberService.getSiteList();

            model.addAttribute("siteList", siteList);
            model.addAttribute("url", "member/add");
        } catch (Exception e) {
            log.error("url: /member/add --- method: addMember --- message: " + e.toString());
        }
        return "views/admin/member/add";
    }

    @PostMapping(value = "add_ajax")
    @ResponseBody
    public Result<Member> addMemberAjax(@ModelAttribute("member") Member member, HttpServletRequest request) {
        Result<Member> result = new Result<>();
        try {
            member.setSeq(UUIDGenerator.generate());
            member.setUserType(CommonConstant.USER_TYPE_NORMAL);
            member.setName(member.getAccountHolder());

            member.setSiteName("---");
            member.setSiteDomain("---");
            member.setSignupIp("---");

//            //TODO
//            // game api - /user/create start
            ResponseEntity<?> ret1 = HttpUtils.createUser(gameServerUrl + "/user/create", member.getId(), member.getNickname(), apiKey);

            if (ret1.getStatusCode().value() == 200) {
                String ret = ret1.getBody().toString();
                JSONObject json = JSONObject.parseObject(ret);

                if (json != null) {
                    member.setId(json.getString("username"));
                }

                if (memberService.save(member)) {
                    result.success("success");
                    result.setResult(member);
                } else {
                    result.error505("failed");
                }
            }
        } catch (Exception e) {
            log.error("url: /member/add_ajax --- method: addMemberAjax --- message: " + e.toString());
            result.error500("Internal Server Error");
        }
        return result;
    }

    @PostMapping(value = "update_ajax")
    @ResponseBody
    public Result<Member> updateMemberAjax(@ModelAttribute("member") Member member, HttpServletRequest request) {
        Result<Member> result = new Result<>();
        try {
            System.out.println("changed money");
            System.out.println(member.getMoneyAmount() + member.getChangeMoney());
            member.setMoneyAmount(member.getMoneyAmount() + member.getChangeMoney());
            if (memberService.updateById(member)) {
                result.success("success");
            } else {
                result.error505("fail");
            }
        } catch (Exception e) {
            log.error("url: /member/update_ajax --- method: updateMemberAjax --- message: " + e.toString());
            result.error500("Internal Server Error");
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping(value = "stop_member_ajax")
    @ResponseBody
    public Result<Member> stopMemberAjax(@RequestParam("ids") String ids, HttpServletRequest request) {
        Result<Member> result = new Result<>();
        try {
            List<String> idList = Arrays.asList(ids.split(","));

            if (memberService.stopMember(idList)) {
                result.success("success");
            } else {
                result.error505("fail");
            }
        } catch (Exception e) {
            log.error("url: /member/stop_member_ajax --- method: stopMemberAjax --- message: " + e.toString());
            result.error500("Internal Server Error");
        }
        return result;
    }

    @PostMapping(value = "delete_ajax")
    @ResponseBody
    public Result<Member> deleteMemberAjax(@RequestParam("ids") String ids, HttpServletRequest request) {
        Result<Member> result = new Result<>();
        try {
            List<String> idList = Arrays.asList(StringUtils.split(ids, ","));
            if (memberService.removeByIds(idList)) {
                result.success("success");
            } else {
                result.success("fail");
            }
        } catch (Exception e) {
            log.error("url: /member/delete_ajax --- method: deleteMemberAjax --- message: " + e.toString());
            result.error500("internal server error");
        }
        return result;
    }

    @GetMapping(value = "getBySeq")
    @ResponseBody
    public Result<Map<String, Object>> getMemberBySeq(@RequestParam("seq") String memberSeq, HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<>();
        try {
            MemberForm memberForm = new MemberForm();
            memberForm.setSeq(memberSeq);
            memberForm.setUserType(CommonConstant.USER_TYPE_NORMAL);
            memberForm = memberService.getMemberBySeq(memberForm);

            List<Level> levelList = levelService.list();

            QueryWrapper<Dict> dictQw = new QueryWrapper<>();
            dictQw.eq("dict_key", "MEMBER_STATUS");
            List<Dict> statusList = dictService.list(dictQw);

            QueryWrapper<Member> mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> storeList = memberService.list(mQw);

            mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_DISTRIBUTOR);
            List<Member> distributorList = memberService.list(mQw);

            mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_SUB_HEADQUARTER);
            List<Member> subHeadquarterList = memberService.list(mQw);

            Map<String, Object> map = new HashMap<>();
            map.put("member", memberForm);
            map.put("levelList", levelList);
            map.put("statusList", statusList);
            map.put("storeList", storeList);
            map.put("distributorList", distributorList);
            map.put("subHeadquarterList", subHeadquarterList);

            result.success("success");
            result.setResult(map);
        } catch (Exception e) {
            log.error("url: /member/getById --- method: getMember --- message: " + e.toString());
            result.error500("Internal Server Error");
        }
        return result;
    }

    @GetMapping(value = "popup_detail")
    public String popupDetail(@RequestParam("idx") String memberSeq, Model model, HttpServletRequest request) {
        try {
            MemberForm memberForm = new MemberForm();
            memberForm.setSeq(memberSeq);
            memberForm.setUserType(CommonConstant.USER_TYPE_NORMAL);
            memberForm = memberService.getMemberBySeq(memberForm);

            List<Map<String, String>> memoList = new ArrayList<>();
            JSONArray memoArr = JSONObject.parseArray(memberForm.getMemo());
            if (memoArr != null) {
                for (int i = 0; i < memoArr.size(); i++) {
                    JSONObject obj = memoArr.getJSONObject(i);
                    Map<String, String> memo = new HashMap<>();
                    memo.put("hour", obj.getString("hour"));
                    memo.put("contents", obj.getString("contents"));

                    memoList.add(memo);
                }
                memberForm.setMemoList(memoList);
            }

            List<Level> levelList = levelService.list();

            QueryWrapper<Dict> dictQw = new QueryWrapper<>();
            dictQw.eq("dict_key", "MEMBER_STATUS");
            List<Dict> statusList = dictService.list(dictQw);

            QueryWrapper<Member> mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> storeList = memberService.list(mQw);

            mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_DISTRIBUTOR);
            List<Member> distributorList = memberService.list(mQw);

            mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_SUB_HEADQUARTER);
            List<Member> subHeadquarterList = memberService.list(mQw);

            mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_SUB_HEADQUARTER)
                    .or().eq("user_type", CommonConstant.USER_TYPE_DISTRIBUTOR)
                    .or().eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> partnerList = memberService.list(mQw);

            model.addAttribute("memberForm", memberForm);
            model.addAttribute("levelList", levelList);
            model.addAttribute("statusList", statusList);
            model.addAttribute("storeList", storeList);
            model.addAttribute("distributorList", distributorList);
            model.addAttribute("subHeadquarterList", subHeadquarterList);
            model.addAttribute("partnerList", partnerList);
            model.addAttribute("url", "/member/popup_detail");
        } catch (Exception e) {
            log.error("url: /member/popup_detail --- method: popupDetail --- message: " + e.toString());
        }
        return "views/admin/member/detail";
    }

    @GetMapping(value = "popup_moneychange")
    public String popupMoneyChange(@RequestParam("idx") String memberSeq, @RequestParam("act") String classification, Model model) {
        try {
            MemberForm memberForm = new MemberForm();
            memberForm.setSeq(memberSeq);
            memberForm.setUserType(CommonConstant.USER_TYPE_NORMAL);
            memberForm = memberService.getMemberBySeq(memberForm);

            model.addAttribute("memberForm", memberForm);
            model.addAttribute("url", "member/popup_moneychange");
        } catch (Exception e) {
            log.error("url: /member/popup_moneychange --- method: popupMoneyChange --- message: " + e.toString());
        }
        return "views/admin/member/moneyChange";
    }

    @PostMapping(value = "updateHoldingMoney")
    @ResponseBody
    public Result<JSONObject> updateMemberHoldingMoney(
            @RequestParam(value = "memberSeq") String memberSeq,
            @RequestParam(value = "prevMoneyAmount") Float prevMoneyAmount,
            @RequestParam(value = "prevMileageAmount") Float prevMileageAmount,
            @RequestParam(value = "variableAmount") Float variableAmount,
            @RequestParam(value = "classification", defaultValue = "0") Integer classification,
            @RequestParam(value = "transactionClassification", defaultValue = "0") Integer transactionClassification,
            @RequestParam(value = "reason", defaultValue = "") String reason) {
        Result<JSONObject> result = new Result<>();
        try {

            System.out.println("MemberController==updateMemberHoldingMoney==>");

            QueryWrapper<Dict> qw = new QueryWrapper<>();
            qw.eq("dict_key", CommonConstant.DICT_KEY_MONEY_REASON);
            qw.eq("dict_value", CommonConstant.MONEY_REASON_ADMINEDIT);
            List<Dict> reasonList = dictService.list(qw);
            String reasonStrKey = reasonList.get(0).getStrValue();
            reason = messageSource.getMessage(reasonStrKey, null, Locale.ENGLISH);

            float actualAmount = variableAmount;
            Float finalAmount = transactionClassification.equals(CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT)
                    ? prevMoneyAmount + variableAmount
                    : prevMoneyAmount - variableAmount;
            Integer status = CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT;
            Integer reasonType = CommonConstant.MONEY_REASON_ADMINEDIT;
            Integer chargeCount = 0;
            if (memberService.updateMemberHoldingMoney(
                    memberSeq,
                    prevMoneyAmount,
                    prevMileageAmount,
                    variableAmount,
                    actualAmount,
                    finalAmount,
                    classification,
                    transactionClassification,
                    status,
                    reasonType,
                    reason,
                    chargeCount
            )) {
                result.success("success");
                System.out.println("MemberController==update Member Holding Money ==> success");
            } else {
                result.error505("fail");
                System.out.println("MemberController==update Member Holding Money ==> failed");
            }
        } catch (Exception e) {
            log.error("url: /member/updateHoldingMoney --- method: updateMemberHoldingMoney --- message: " + e.toString());
            result.error500("Internal Server Error");
        }
        return result;
    }

    @GetMapping(value = "popup_bet02")
    public String popupBet(@RequestParam("mem_sn") String memberSeq,
                           @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                           @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, Model model) {
        try {
            BettingSummaryForm bettingSummaryForm = new BettingSummaryForm();
            bettingSummaryForm.setMemberSeq(memberSeq);

            Page<BettingSummaryForm> page = new Page<>(pageNo, pageSize);
            IPage<BettingSummaryForm> pageList = bettingSummaryService.getBettingSummaryList(page, bettingSummaryForm);

            model.addAttribute("pageList", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("url", "member/popup_bet02");
        } catch (Exception e) {
            log.error("url: /member/popup_bet02 --- method: popupBet --- message: " + e.toString());
        }
        return "views/admin/member/bettingSummary";
    }

    @GetMapping(value = "popup_write")
    public String popupWrite(@RequestParam("userid") String memberSeq, Model model) {
        try {
            Member member = memberService.getById(memberSeq);

            model.addAttribute("member", member);
            model.addAttribute("url", "member/popup_write");
        } catch (Exception e) {
            log.error("url: /member/popup_write --- method: writeNote --- message: " + e.toString());
        }
        return "views/admin/member/writeNote";
    }

    @PostMapping(value = "write_note")
    @ResponseBody
    public Result<Note> writeNote(@ModelAttribute("note") Note note) {
        Result<Note> result = new Result<>();
        try {
            if (note != null) {
                note.setSeq(UUIDGenerator.generate());
                note.setReadStatus(CommonConstant.STATUS_UN_READ);
                note.setRecommendStatus(CommonConstant.STATUS_UN_RECOMMEND);
                note.setLookUp(0);
                note.setType(CommonConstant.TYPE_NOTE);
                note.setSendType(CommonConstant.TYPE_SEND_NOTE);
                note.setTitle(note.getTitle());
                note.setContent(note.getContent());
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
            log.error("url: /member/write_note --- method: writeNote --- message: " + e.toString());
            result.error500("Internal Server Error");
        }
        return result;
    }

    @GetMapping(value = "popup_loginlist")
    public String popupLoginList(@ModelAttribute("accessLog") AccessLog accessLog,
                                 @RequestParam(name = "column", defaultValue = "connection_date") String column,
                                 @RequestParam(name = "order", defaultValue = "1") Integer order,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 @RequestParam(name = "hour", required = false) Integer hour,
                                 @RequestParam(name = "checkStatus", required = false) Integer checkStatus,
                                 HttpServletRequest httpServletRequest, Model model) {
        try {
            Page<AccessLog> page = new Page<AccessLog>(pageNo, pageSize);
            IPage<AccessLog> pageList = accessLogService.getAccessLogList(page, accessLog, checkStatus, hour, column, order);

            model.addAttribute("page", pageList);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("url", "/member/popup_loginlist");
        } catch (Exception e) {
            log.error("url: /member/loginlist --- method: list --- error: " + e.toString());
        }

        return "views/admin/member/popupLoginList";
    }

    @RequestMapping(value = "popup_bet")
    public String popupBet(@ModelAttribute("mem_sn") String memberSeq,
                           @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                           @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                           Model model, HttpServletRequest request) {
        try {
            BettingSummaryForm bettingSummaryForm = new BettingSummaryForm();
            bettingSummaryForm.setMemberSeq(memberSeq);

            Page<BettingSummaryForm> page = new Page<>(pageNo, pageSize);
            IPage<BettingSummaryForm> pageList = bettingSummaryService.getBettingSummaryList(page, bettingSummaryForm);

            model.addAttribute("pageList", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("memberSeq", memberSeq);
            model.addAttribute("url", "/member/popup_bet");
        } catch (Exception e) {
            log.error("url: /member/popup_bet --- method: popupBet --- message: " + e.toString());
        }
        return "views/admin/member/bettingSummary";
    }

    @RequestMapping(value = "/popup_charge")
    public String popupCharge(Model model,
                              @ModelAttribute("moneyHistory") MoneyHistory moneyHistory,
                              @RequestParam(name = "column", defaultValue = "mon.application_time") String column,
                              @RequestParam(name = "order", defaultValue = "1") Integer order,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
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
            IPage<MoneyHistory> pageList = moneyHistoryService.findList(page, moneyHistory, column, order);

            QueryWrapper<Member> qw = new QueryWrapper<>();
            qw.eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> storeList = memberService.list(qw);
            model.addAttribute("storeList", storeList);

            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT);
            Float totalDeposit = moneyHistoryService.getTotalAmountByDateRange(moneyHistory);
            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW);
            Float totalWithdraw = moneyHistoryService.getTotalAmountByDateRange(moneyHistory);

            model.addAttribute("totalDeposit", totalDeposit);
            model.addAttribute("totalWithdraw", totalWithdraw);

            model.addAttribute("page", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("column", column);
            model.addAttribute("order", order);
            model.addAttribute("url", "/member/popup_charge");
        } catch (Exception e) {
            log.error("url: /member/popup_charge --- method: popupDeposit --- error: " + e.toString());
        }
        return "views/admin/member/popupMemberDeposit";
    }

    @RequestMapping(value = "/popup_exchange")
    public String popupExchange(Model model,
                                @ModelAttribute("moneyHistory") MoneyHistory moneyHistory,
                                @RequestParam(name = "column", defaultValue = "mon.application_time") String column,
                                @RequestParam(name = "order", defaultValue = "1") Integer order,
                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
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
            IPage<MoneyHistory> pageList = moneyHistoryService.findList(page, moneyHistory, column, order);

            QueryWrapper<Member> qw = new QueryWrapper<>();
            qw.eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> storeList = memberService.list(qw);
            model.addAttribute("storeList", storeList);

            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT);
            Float totalDeposit = moneyHistoryService.getTotalAmountByDateRange(moneyHistory);
            moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW);
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
            model.addAttribute("url", "/member/popup_exchange");
        } catch (Exception e) {
            log.error("url: /member/popup_exchange --- method: popupExchange --- error: " + e.toString());
        }
        return "views/admin/member/popupMemberWithdrawal";
    }

    @RequestMapping(value = "popup_joiners")
    public String popupJoiners() {
        return "views/admin/member/popupRecommendedPerson";
    }

    @GetMapping(value = "popup_simul_list")
    public String popup_simul_list(@ModelAttribute("accessLog") AccessLog accessLog,
                                   @RequestParam(name = "column", defaultValue = "connection_date") String column,
                                   @RequestParam(name = "order", defaultValue = "1") Integer order,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam(name = "hour", required = false) Integer hour,
                                   @RequestParam(name = "checkStatus") Integer checkStatus,
                                   HttpServletRequest httpServletRequest, Model model) {
        try {

            Page<AccessLog> page = new Page<AccessLog>(pageNo, pageSize);
            IPage<AccessLog> pageList = accessLogService.getAccessLogList(page, accessLog, checkStatus, hour, column, order);


            model.addAttribute("pageList", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("url", "member/popup_simul_list");
        } catch (Exception e) {
            log.error("url: /member/popup_simul_list --- method: popupSimulList --- message: " + e.toString());
        }
        return "views/admin/member/loginMember";
    }
    
    
    @RequestMapping(value = "password_manage")
    public String passwordManage() {
    	return "views/admin/common/passwordManagement";
    }
    
    @RequestMapping(value = "update_password")
    @ResponseBody
    public Result<Map<String, Object>> updatePwd(
    		@RequestParam(value = "currentPwd") String currentPwd,
    		@RequestParam(value = "newPwd") String newPwd,
    		@RequestParam(value="confirmPwd") String confirmPwd,
    		HttpServletRequest request) {
    	Result<Map<String, Object>> result = new Result<>();
    	try {
    		SysUser userInfo = (SysUser) SecurityUtils.getSubject().getPrincipal();
    		
    		QueryWrapper<SysUser> qw = new QueryWrapper<>();
			qw.eq("seq", userInfo.getSeq());
			
			SysUser sysUser = sysUserService.getOne(qw);
			if (currentPwd == null || currentPwd == "" || newPwd == null || newPwd == "" || confirmPwd == null || confirmPwd == "") {
				result.error505("failed");
			} else if (sysUser == null) {
				result.error505("failed");
			} else {
				MessageDigest md = MessageDigest.getInstance("MD5");
				String md5Pwd = Base64.encodeBase64String(md.digest(currentPwd.getBytes()));
				
				if (StringUtils.equals(md5Pwd, sysUser.getPassword())) {
					if (StringUtils.equals(newPwd, confirmPwd)) {
						String pwd = Base64.encodeBase64String(md.digest(confirmPwd.getBytes()));
						sysUser.setPassword(pwd);
						if(sysUserService.updateById(sysUser)) {
							result.success("success");
						} else {
							result.error505("update failed");
						}
					} else {
						result.error505("Confirm password incorrect");
					}
				} else {
					result.error505("Current password incorrect");
				}
			}
    	} catch(Exception e) {
    		log.error("url: /member/password_management --- method: updatePwd --- message: " + e.toString());
    	}
    	return result;
    }
}
