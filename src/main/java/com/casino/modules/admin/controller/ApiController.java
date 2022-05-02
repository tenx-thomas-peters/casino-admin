package com.casino.modules.admin.controller;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.casino.common.utils.HttpUtils;
import com.casino.modules.admin.common.entity.*;
import com.casino.modules.admin.common.form.APIUserForm;
import com.casino.modules.admin.service.*;
import com.casino.modules.shiro.authc.util.JwtUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.common.vo.Result;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api")
@Slf4j
public class ApiController {
    @Value(value = "${gameServer.url}")
    private String gameServerUrl;

    @Value(value = "${gameServer.apiKey}")
    private String apiKey;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IBasicSettingService basicSettingService;

    @Autowired
    private INoteService noteService;

    @Autowired
    private IMoneyHistoryService moneyHistoryService;

    @Autowired
    private IAccessLogService accessLogService;

    @Autowired
    private IPopupSettingService popupSettingService;

    @PostMapping(value = "auth/register")
    public Result<JSONObject> register(@RequestBody Member member, HttpServletRequest request) {
        Result<JSONObject> result = new Result<>();
        JSONObject obj = new JSONObject();
        try {
            String domain = request.getServerName();
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }

            QueryWrapper<Member> qw = new QueryWrapper<>();
            qw.eq("name", member.getName());
            List<Member> memberList = memberService.list(qw);
            if (!memberList.isEmpty()) {
                result.error505("Username already exists!");
            } else {
                member.setSeq(UUIDGenerator.generate());
                member.setUserType(CommonConstant.USER_TYPE_NORMAL);
//                member.setName(member.getAccountHolder());

                member.setSignupIp(ipAddress);
                member.setSiteDomain(domain);
                member.setSiteName(domain);

                if (memberService.save(member)) {
                    String token = JwtUtil.sign(member.getName(), member.getPassword());

                    obj.put("token", token);
                    obj.put("userInfo", member);
                    result.setResult(obj);

                    result.success("SignUp Success");
                } else {
                    result.error505("failed");
                }
            }
        } catch (Exception e) {
            log.error("url: /api/auth/register --- method: register --- message: " + e.toString());
            result.error500("Internal Server Error");
        }
        return result;
    }

    @GetMapping(value = "auth/signin")
    public Result<JSONObject> signIn(
            HttpServletRequest request,
            @RequestParam("loginID") String loginID,
            @RequestParam("password") String password) {
        Result<JSONObject> result = new Result<>();
        JSONObject obj = new JSONObject();
        try {
            String domain = request.getServerName();
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }

            QueryWrapper<Member> qw = new QueryWrapper<>();
            qw.eq("user_type", CommonConstant.USER_TYPE_NORMAL);
            qw.eq("id", loginID);

            Member member = memberService.getOne(qw);
            AccessLog accessLog = new AccessLog();

            if (member != null) {
                accessLog.setSeq(UUIDGenerator.generate());
                accessLog.setMemberSeq(member.getSeq());
                accessLog.setSite(domain);
                accessLog.setId(member.getId());
                accessLog.setNickname(member.getNickname());
                accessLog.setAccountHolder(member.getAccountHolder());
                accessLog.setLevel(member.getLevel());
                accessLog.setMoneyAmount(member.getMoneyAmount());
                accessLog.setDistributor(member.getDistributorSeq());
                accessLog.setConnectionIp(ipAddress);
                accessLog.setConnectionDomain(domain);

                qw.eq("password", password);
                member = memberService.getOne(qw);

                if (member != null && StringUtils.equals(member.getPassword(), password)) {
                    // set session for /user/refresh-token api
                    String token = JwtUtil.sign(loginID, password);

                    accessLog.setStatus(CommonConstant.ACCESS_LOG_SUCCESS);
                    obj = memberInfo(member.getSeq());
                    obj.put("token", token);
                    obj.put("userInfo", member);
                    result.setResult(obj);

                    result.success("Login Success!");
                } else {
                    accessLog.setStatus(CommonConstant.ACCESS_LOG_SUCCESS);
                    result.error505("Password is Incorrect!");
                }
            } else {
                accessLog.setStatus(CommonConstant.ACCESS_LOG_USER_ID_ERROR);
                result.error505("User Id does not exist!");
            }

            accessLogService.save(accessLog);
        } catch (Exception e) {
            log.error("url: /api/auth/signin --- method: signIn --- message: " + e.toString());
            result.error500("Internal Server Error");
        }
        return result;
    }

    @GetMapping(value = "auth/me")
    public Result<JSONObject> me(
            @RequestParam("token") String token,
            @RequestParam("name") String name,
            @RequestParam("password") String password) {
        Result<JSONObject> result = new Result<>();
        JSONObject obj = new JSONObject();
        try {
            if (JwtUtil.verify(token, name, password)) {
                QueryWrapper<Member> qw = new QueryWrapper<>();
                qw.eq("user_type", CommonConstant.USER_TYPE_NORMAL);
                qw.eq("name", name);
                Member member = memberService.getOne(qw);

                obj = memberInfo(member.getSeq());
                obj.put("userInfo", member);

                result.success("Verified!");
                result.setResult(obj);
            } else {
                result.error505("Expired!");
            }
        } catch (Exception e) {
            log.error("url: /api/auth/me --- method: me --- message: " + e.toString());
            result.error500("Internal Server Error");
        }
        return result;
    }

    @GetMapping(value = "/memberInfo")
    public Result<JSONObject> getMemberInfo(@RequestParam("memberSeq") String memberSeq) {
        Result<JSONObject> result = new Result<>();
        JSONObject jsonObject = new JSONObject();
        Integer noteCounts = 0;
        Float houseMoney = 0.0f;
        Float jackpotAmount = 0.0f;
        String inlineNotice = "";
        List<PopupSetting> popupSettingList = new ArrayList<>();

        Map<String, Object> topRanking = new HashMap<>();

        try {
            Member member = memberService.getById(memberSeq);
            String url = gameServerUrl + "/user?username=" + member.getId();
            ResponseEntity<String> res = HttpUtils.getUserInfo(url, apiKey);

            if (res.getStatusCode().value() == 200) {
                APIUserForm memberForms = JSON.parseObject(res.getBody().toString(), APIUserForm.class);
                member.setCasinoMoney(memberForms.getBalance());
                if (!memberService.updateById(member))
                    result.error505("===  update casino money failed");
            }
            else {result.error505("/user api failed");}

            Session session = SecurityUtils.getSubject().getSession();
            String token = (String) session.getAttribute("user_token");

            if (member != null) {
                // get note counts
                QueryWrapper<Note> noteQw = new QueryWrapper<>();
                noteQw.eq("receiver", memberSeq);
                noteQw.eq("read_status", CommonConstant.STATUS_UN_READ);
                noteCounts = noteService.count(noteQw);

                jsonObject.put("noteCounts", noteCounts);
                jsonObject.put("moneyAmount", member.getMoneyAmount());
                jsonObject.put("casinoMoney", member.getCasinoMoney());
                jsonObject.put("mileageAmount", member.getMileageAmount());
            }

            // get house money
            BasicSetting basicSetting = new BasicSetting();
            List<BasicSetting> list = basicSettingService.list();
            if (CollectionUtils.isNotEmpty(list)) {
                basicSetting = list.get(0);
                houseMoney = basicSetting.getHouseMoney();
                jackpotAmount = basicSetting.getJackpotAmount();
                inlineNotice = basicSetting.getMemberLineAdvertisement();

                topRanking.put("topMember", basicSetting.getWeeklyWithdrawalRankingTop1Id());
                topRanking.put("moneyAmount", basicSetting.getWeeklyWithdrawalRankingTop1Money());
            }

            QueryWrapper<PopupSetting> popQw = new QueryWrapper<>();
            popQw.ne("expiration_start", new Date());
            popQw.ge("expiration_end", new Date());
            popupSettingList = popupSettingService.list(popQw);

            jsonObject.put("houseMoney", houseMoney);
            jsonObject.put("jackpotAmount", jackpotAmount);
            jsonObject.put("topRanking", topRanking);
            jsonObject.put("inlineNotice", inlineNotice);
            jsonObject.put("popupNotice", popupSettingList);

            result.success("Success");
            result.setResult(jsonObject);
        } catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /auth/memberInfo --- method: getMemberInfo() --- message: " + e.toString());
        }
        return result;
    }

    public Result<Member> getCaisnoMoeny(String member_id){

        Result<Member> result = new Result<>();
        try {
            float casino_money = 0.0F;

            String url = gameServerUrl + "/user?username=" + member_id;
            ResponseEntity<String> res = HttpUtils.getUserInfo(url, apiKey);

            if (res.getStatusCode().value() == 200) {
                JSONObject json = JSON.parseObject(res.getBody().toString());

                APIUserForm memberForms = json.getJSONArray("data").toJavaObject(APIUserForm.class);

                QueryWrapper<Member> qw = new QueryWrapper<>();
                qw.eq("id", memberForms.getId());

                Member member = memberService.getOne(qw);

                member.setCasinoMoney(memberForms.getBalance());

                if (memberService.updateById(member)) log.info("=======================  update casino money successful  =======================");
                 else log.error("=======================  update casino money failed  =======================");
            } else {
                result.error505("/user api failed");
            }
        }
        catch (Exception e){
            result.error500("Internal Server Error");
            log.error("url: /user --- method: syncCasinoMoney --- message: " + e.toString());
        }
        return result;
    }

    public JSONObject memberInfo(String memberSeq) {
        JSONObject jsonObject = new JSONObject();
        Integer noteCounts = 0;
        Float houseMoney = 0.0f;
        Float jackpotAmount = 0.0f;
        Map<String, Object> topRanking = new HashMap<>();

        try {
            Member member = memberService.getById(memberSeq);

            // get note counts
            QueryWrapper<Note> noteQw = new QueryWrapper<>();
            noteQw.eq("receiver", memberSeq);
            noteQw.eq("read_status", CommonConstant.STATUS_UN_READ);
            noteCounts = noteService.count(noteQw);

            // get house money
            BasicSetting basicSetting = new BasicSetting();
            List<BasicSetting> list = basicSettingService.list();
            if (CollectionUtils.isNotEmpty(list)) {
                basicSetting = list.get(0);
                houseMoney = basicSetting.getHouseMoney();
                jackpotAmount = basicSetting.getJackpotAmount();
                topRanking.put("topMember", basicSetting.getWeeklyWithdrawalRankingTop1Id());
                topRanking.put("moneyAmount", basicSetting.getWeeklyWithdrawalRankingTop1Money());
            }

            jsonObject.put("noteCounts", noteCounts);
            jsonObject.put("moneyAmount", member.getMoneyAmount());
            jsonObject.put("mileageAmount", member.getMileageAmount());
            jsonObject.put("houseMoney", houseMoney);
            jsonObject.put("jackpotAmount", jackpotAmount);
            jsonObject.put("topRanking", topRanking);
        } catch (Exception e) {
            log.error("url: /auth/memberInfo --- method: memberInfo() --- message: " + e.toString());
        }
        return jsonObject;
    }

    @GetMapping(value = "getHomeInfo")
    public Result<JSONObject> getHomeInfo() {
        Result<JSONObject> result = new Result<>();
        JSONObject obj = new JSONObject();
        try {
            QueryWrapper<Note> qw = new QueryWrapper<>();

            // get recent notice
            qw.eq("type", CommonConstant.TYPE_POST);
            qw.eq("classification", CommonConstant.CLASSIFICATION_NOTICE);
            qw.orderByDesc("send_time");
            List<Note> noticeList = noteService.list(qw);
            Note notice = new Note();
            if (CollectionUtils.isNotEmpty(noticeList)) {
                notice = noticeList.get(0);
            }

            // get recent event
            qw = new QueryWrapper<>();
            qw.eq("type", CommonConstant.TYPE_POST);
            qw.eq("classification", CommonConstant.CLASSIFICATION_EVENT);
            qw.orderByDesc("send_time");
            List<Note> eventList = noteService.list(qw);
            Note event = new Note();
            if (CollectionUtils.isNotEmpty(eventList)) {
                event = eventList.get(0);
            }

            // get withdraw history - recent 5
            List<MoneyHistory> historyList = moneyHistoryService.getTodayMoneyHistory(
                    CommonConstant.MONEY_HISTORY_STATUS_COMPLETE,
                    CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW,
                    CommonConstant.MONEY_OR_POINT_MONEY);

            obj.put("notice", notice);
            obj.put("event", event);
            obj.put("withdraw", historyList);

            result.success("Success");
            result.setResult(obj);
        } catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /api/getHomeInfo --- method: getHomeInfo --- message: " + e.toString());
        }
        return result;
    }

    @GetMapping(value = "getNoteList")
    public Result<IPage<Note>> getNoteList(
            @RequestParam(name = "type", defaultValue = "0") Integer type,
            @RequestParam(name = "classification", defaultValue = "0") Integer classification,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Result<IPage<Note>> result = new Result<>();
        try {
            QueryWrapper<Note> qw = new QueryWrapper<>();
            qw.eq("type", type);
            qw.eq("classification", classification);
            Page<Note> page = new Page<Note>(pageNo, pageSize);
            IPage<Note> pageList = noteService.page(page, qw);

            result.success("Success");
            result.setResult(pageList);
        } catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /api/getNoteList --- method: getNoteList --- message: " + e.toString());
        }
        return result;
    }

    @GetMapping(value = "getNoticeDetail")
    public Result<Note> getNoticeDetail(
            @RequestParam("seq") String seq) {
        Result<Note> result = new Result<>();
        try {
            QueryWrapper<Note> qw = new QueryWrapper<>();
            qw.eq("seq", seq);
            Note note = noteService.getOne(qw);

            result.success("Success");
            result.setResult(note);
        } catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /api/getNoticeDetail --- method: getNoticeDetail --- message: " + e.toString());
        }
        return result;
    }

    @GetMapping(value = "getInboxNoteList")
    public Result<IPage<Note>> getInboxNoteList(
            @RequestParam(name = "type", defaultValue = "0") Integer type,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Result<IPage<Note>> result = new Result<>();
        try {
            QueryWrapper<Note> qw = new QueryWrapper<>();
            qw.eq("type", type);
            Page<Note> page = new Page<Note>(pageNo, pageSize);
            IPage<Note> pageList = noteService.page(page, qw);

            result.success("Success");
            result.setResult(pageList);
        } catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /api/getInboxNoteList --- method: getInboxNoteList --- message: " + e.toString());
        }
        return result;
    }

    @DeleteMapping(value = "deleteNote")
    public Result<Note> deleteNote(@RequestParam("noteSeq") String seq) {
        Result<Note> result = new Result<>();
        if (noteService.removeById(seq)) {
            result.success("delete success");
        } else {
            result.error505("delete failed");
        }
        return result;
    }

    @PostMapping(value = "changeReadStatus")
    public Result<Note> changeReadStatus(@RequestBody Note noteParam) {
        Result<Note> result = new Result<>();
        try {
            QueryWrapper<Note> qw = new QueryWrapper<>();
            qw.eq("seq", noteParam.getSeq());

            Note note = noteService.getOne(qw);
            note.setReadStatus(CommonConstant.READ_STATUS);
            if (noteService.saveOrUpdate(note)) {
                result.success("change success");
            }

        } catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /api/changeReadStatus --- method: changeReadStatus --- message: " + e.toString());
        }

        return result;
    }

    @GetMapping(value = "changeReadStatusAll")
    public Result<Note> changeReadStatusAll() {
        Result<Note> result = new Result<>();
        try {
            if (noteService.changeReadStatusAll()) {
                result.success("changed success");
            }

        } catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /api/changeReadStatusAll --- method: changeReadStatusAll --- message: " + e.toString());
        }

        return result;
    }

    @GetMapping(value = "removeAll")
    public Result<Note> removeAll() {
        Result<Note> result = new Result<>();
        try {
            if (noteService.removeAll()) {
                result.success("remove success");
            }

        } catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /api/removeAll --- method: removeAll --- message: " + e.toString());
        }

        return result;
    }

    /**
     * @return
     * @author SH
     * @since 2022.2.25.
     */
    @PostMapping(value = "sendRequestAccount")
    public Result<Note> sendRequestAccount(@RequestBody Note note) {
        Result<Note> result = new Result<>();
        note.setSeq(UUIDGenerator.generate());

        if (noteService.save(note)) {
            result.success("success");
        } else {
            result.error505("failed");
        }
        return result;
    }

    /**
     * @param memberSeq
     * @param pageNo
     * @param pageSize
     * @return
     * @author SH
     */
    @RequestMapping(value = "getMonthMoneyHistory")
    public Result<IPage<MoneyHistory>> getMonthMoneyHistory(@RequestParam("memberSeq") String memberSeq,
                                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                            @RequestParam(name = "operationType", defaultValue = "0") Integer operationType) {
        Result<IPage<MoneyHistory>> result = new Result<>();
        try {
            Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
            IPage<MoneyHistory> pageList = moneyHistoryService.getMonthMoneyLogByMemberSeq(page, memberSeq, operationType);
            result.success("success");
            result.setResult(pageList);
        } catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /api/getMonthMoneyHistory --- method: getMonthMoneyHistory --- message: " + e.toString());
        }
        return result;
    }

    /**
     * @param Seq
     * @return
     * @author SH
     */
    @PostMapping(value = "moneyHistoryDeleteBySeq")
    public Result<MoneyHistory> moneyHistoryDelteBySeq(@RequestBody String seq) {
        Result<MoneyHistory> result = new Result<>();
        if (moneyHistoryService.removeById(seq)) {
            result.success("delete success");
        } else {
            result.error505("delete failed");
        }
        return result;
    }

    /**
     * apply charge
     *
     * @param memberSeq
     * @param requestAmount
     * @param depositer
     * @return
     * @author SH
     */
    @PostMapping(value = "applyCharge")
    public Result<MoneyHistory> applyCharge(@RequestBody MoneyHistory moneyHistory) {
        System.out.println("moneyHistory");
        System.out.println(moneyHistory);
        Result<MoneyHistory> result = new Result<>();
        try {
            Member receiver = memberService.getById(moneyHistory.getReceiver());
            MoneyHistory receiverMoneyHistory = new MoneyHistory();

            receiverMoneyHistory.setSeq(UUIDGenerator.generate());
            receiverMoneyHistory.setReceiver(moneyHistory.getReceiver());
            receiverMoneyHistory.setApplicationTime(new Date());
            receiverMoneyHistory.setPrevAmount(receiver.getMoneyAmount());
            receiverMoneyHistory.setVariableAmount(Float.valueOf(moneyHistory.getActualAmount()));
            receiverMoneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_IN_PROGRESS);
            receiverMoneyHistory.setOperationType(CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT);
            receiverMoneyHistory.setMoneyOrPoint(CommonConstant.MONEY_OR_POINT_MONEY);
            receiverMoneyHistory.setNote(moneyHistory.getNote());

            if (moneyHistoryService.save(receiverMoneyHistory)) {
                result.success("apply charge success");
            } else {
                result.error505("apply charge failed");
            }
        } catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /api/applyCharge --- method: applyCharge --- message: " + e.toString());
        }
        return result;
    }

    @PostMapping(value = "postSupportForm")
    public Result<Note> postSupportForm(@RequestBody Note note) {
        Result<Note> result = new Result<>();
        try {
            note.setSeq(UUIDGenerator.generate());

            if (noteService.save(note)) {
                result.success("Success!");
            } else {
                result.error505("Failed!");
            }
        } catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /api/postSupportForm --- method: postSupportForm --- message: " + e.toString());
        }
        return result;
    }

    /**
     * apply charge
     *
     * @param memberSeq
     * @param requestAmount
     * @param depositer
     * @return
     * @author SH
     */
    @PostMapping(value = "addWithdrawal")
    public Result<MoneyHistory> addWithdrawal(@RequestBody MoneyHistory moneyHistoryParams) {
        Result<MoneyHistory> result = new Result<>();
        try {
            if (moneyHistoryParams != null) {
                MoneyHistory moneyHistory = new MoneyHistory();

                Member member = memberService.getById(moneyHistoryParams.getReceiver());
                float totalMoney = member.getCasinoMoney() + member.getMoneyAmount();
                if (moneyHistoryParams.getVariableAmount() > totalMoney ) {
                    result.error505("머니가 부족합니다. 머니를 체크해주세요");
                } else {
                    moneyHistory.setSeq(UUIDGenerator.generate());
                    moneyHistory.setPrevAmount(member.getMoneyAmount());
                    moneyHistory.setReceiver(moneyHistoryParams.getReceiver());
                    moneyHistory.setVariableAmount(moneyHistoryParams.getVariableAmount());
                    moneyHistory.setNote(moneyHistoryParams.getNote());
                    moneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_IN_PROGRESS);
                    moneyHistory.setApplicationTime(new Date());
                    moneyHistory.setOperationType(CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL);

                    if (moneyHistoryService.saveOrUpdate(moneyHistory)) {
                        result.success("success");
                    } else {
                        result.error505("failed");
                    }
                }
            }
        } catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /api/addWithdrawal --- method: addWithdrawal --- message: " + e.toString());
        }
        return result;
    }

    /**
     * sync local casino money and money for game service when user access game service
     * @param userSeq
     * @return
     */
    @GetMapping(value = "syncCasinoMoney")
    public Result<Member> syncCasinoMoney(
            @RequestParam("userSeq") String userSeq) {
        Result<Member> result = new Result<>();
        try {
            Member member = memberService.getById(userSeq);

            // Thomas 2022.04.11
            // set sync casino money with game service and local money
            // when user access to game service, casino money added, local money becomes 0

            if(member.getMoneyAmount() > 0){

                ResponseEntity<String> ret = HttpUtils.userAddBalance(gameServerUrl + "/user/add-balance", member.getName(), member.getMoneyAmount(), apiKey);

                if (ret.getStatusCode().value() == 200) {

                    float casino_money = 0;
                    if(member.getCasinoMoney() != null){
                        casino_money = member.getCasinoMoney();
                    }
                    float update_casino_money = casino_money + member.getMoneyAmount();
                    member.setCasinoMoney(update_casino_money);
                    member.setMoneyAmount(0.0F);

                    memberService.updateById(member);
                }
                else {
                    result.error505("sub -balance api failed");
                }
            }

            result.success("success");
            result.setResult(member);
        }
        catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /api/syncCasinoMoney --- method: syncCasinoMoney --- message: " + e.toString());
        }
        return result;
    }
}