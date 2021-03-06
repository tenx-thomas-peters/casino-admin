package com.casino.modules.admin.controller;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.casino.common.utils.HttpUtils;
import com.casino.modules.admin.common.entity.*;
import com.casino.modules.admin.common.form.APIUserForm;
import com.casino.modules.admin.common.form.BettingLogForm;
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
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;

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

            // set partner id from referralCode
            QueryWrapper<Member> partner_qw = new QueryWrapper<>();
            partner_qw.eq("id", member.getReferralCode());
            partner_qw.ne("user_type", CommonConstant.USER_TYPE_NORMAL);
            Member partnerMember = memberService.getOne(partner_qw);

            if (!memberList.isEmpty()) {
                result.error505("???????????? ?????? ????????? ???????????????");
            } 
            else {
                member.setSeq(UUIDGenerator.generate());
                member.setUserType(CommonConstant.USER_TYPE_NORMAL);
                member.setName(member.getAccountHolder());

                member.setSignupIp(ipAddress);
                member.setSiteDomain(domain);
                member.setSiteName(domain);

                // referralCode is partner id
                // set partner id
                if(partnerMember != null){
                    if(partnerMember.getUserType().equals(CommonConstant.USER_TYPE_STORE)){
                        member.setStoreSeq(partnerMember.getSeq());
                    }else if(partnerMember.getUserType().equals(CommonConstant.USER_TYPE_DISTRIBUTOR)){
                        member.setDistributorSeq(partnerMember.getSeq());
                    }else if(partnerMember.getUserType().equals(CommonConstant.USER_TYPE_SUB_HEADQUARTER)){
                        member.setSubHeadquarterSeq(partnerMember.getSeq());
                    }
                }

                //TODO
                // game api - /user/create start
                ResponseEntity<?> ret1 = HttpUtils.createUser(gameServerUrl + "/user/create", member.getId(), member.getNickname(), apiKey);

                if (ret1.getStatusCode().value() == 200) {

                    if (memberService.save(member)) {
                        String token = JwtUtil.sign(member.getId(), member.getPassword());

                        obj.put("token", token);
                        obj.put("userInfo", member);
                        result.setResult(obj);

                        result.success("SignUp Success");
                    } else {
                        result.error505("failed");
                    }
                }
                else{
                    result.error505("API request failed to create account in Game service");
                }
            }
        } catch (Exception e) {
            log.error("url: /api/auth/register --- method: register --- message: " + e.toString());
            result.error500("Internal Server Error");
        }
        return result;
    }

    @PostMapping(value = "auth/signout")
    public Result<String> signout(@RequestBody Member member, HttpServletRequest request ){

        Result<String> result = new Result<>();
        try{
            System.out.println("member");
            System.out.println(member);
            if(member.getSeq() != null){
                QueryWrapper<Member> qw = new QueryWrapper<>();
                qw.eq("token", member.getToken());

                Member member1 = memberService.getOne(qw);
                member1.setToken("000");
                member1.setLoginStatus(0);
                if(memberService.updateById(member1)){
                    result.success("Success");

                    QueryWrapper<AccessLog> access_qw = new QueryWrapper<>();
                    access_qw.eq("member_token", member.getToken());

                    AccessLog accessLog = accessLogService.getOne(access_qw);
                    accessLog.setCurrentLoginStatus(CommonConstant.CURRENT_LOGOUT);
                    accessLog.setMemberToken("");
                    accessLogService.updateById(accessLog);
                }
                else{
                    result.error500("Couldn't log out Database Server Error");
                }
            }
            else{
                result.success("Success");
            }
        } catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /auth/signout --- method: getNoticeDetail --- message: " + e.toString());
            e.printStackTrace();
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
                    accessLog.setCurrentLoginStatus(CommonConstant.CURRENT_LOGIN);
                    accessLog.setMemberToken(token);

                    obj = memberInfo(member.getSeq());
                    obj.put("token", token);
                    obj.put("userInfo", member);
                    result.setResult(obj);

                    member.setToken(token);
                    member.setLoginStatus(1);
                    memberService.updateById(member);

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
            @RequestParam("loginID") String loginID,
            @RequestParam("password") String password) {
        Result<JSONObject> result = new Result<>();
        JSONObject obj = new JSONObject();
        try {
            if (JwtUtil.verify(token, loginID, password)) {
                QueryWrapper<Member> qw = new QueryWrapper<>();
                qw.eq("user_type", CommonConstant.USER_TYPE_NORMAL);
                qw.eq("id", loginID);
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
    public Result<JSONObject> getMemberInfo(@RequestParam("memberSeq") String memberSeq, @RequestParam("count") String requestCount, HttpServletRequest request) {
        Result<JSONObject> result = new Result<>();
        JSONObject jsonObject = new JSONObject();
        Integer noteCounts = 0;

        int intRequestCount = Integer.parseInt(requestCount);
        try {
            Member member = memberService.getById(memberSeq);
            String url = gameServerUrl + "/user?username=" + member.getId();
            ResponseEntity<String> res = HttpUtils.getUserInfo(url, apiKey);

            if (res.getStatusCode().value() == 200) {
                APIUserForm memberForms = JSON.parseObject(res.getBody().toString(), APIUserForm.class);

                // update casino money and save history
                // duration time - 5 min
                if(intRequestCount%60==0 && !member.getCasinoMoney().equals(memberForms.getBalance())){
                    member.setCasinoMoney(memberForms.getBalance());
                    this.updateCasinoMoneyStatus(member);
                    if (!memberService.updateById(member))
                        result.error505("===  update casino money failed");
                }

                Session session = SecurityUtils.getSubject().getSession();
                String token = (String) session.getAttribute("user_token");

                // get note counts
                QueryWrapper<Note> noteQw = new QueryWrapper<>();
                noteQw.eq("receiver", memberSeq);
                noteQw.eq("read_status", CommonConstant.STATUS_UN_READ);
                noteCounts = noteService.count(noteQw);

                jsonObject.put("noteCounts", noteCounts);
                jsonObject.put("moneyAmount", member.getMoneyAmount());
                jsonObject.put("casinoMoney", memberForms.getBalance());
                jsonObject.put("mileageAmount", member.getMileageAmount());
                jsonObject.put("token", member.getToken());
                result.success("Success");
                result.setResult(jsonObject);
            }
            else {result.error505("/user api failed");}
        } catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /auth/memberInfo --- method: getMemberInfo() --- message: " + e.toString());
        }
        return result;
    }

    public boolean updateCasinoMoneyStatus(Member member){

        String reason = "??????????????????["+member.getCasinoMoney()+"]";
        if (memberService.updateMemberHoldingMoney(
                member.getSeq(),
                member.getMoneyAmount(),
                member.getMileageAmount(),
                0F,
                0F,
                member.getMoneyAmount(),
                CommonConstant.MONEY_OR_POINT_MONEY,
                CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT,
                CommonConstant.MONEY_HISTORY_STATUS_COMPLETE,
                CommonConstant.MONEY_REASON_CASINO_MONEY,
                reason,
                ""
        )) {
            System.out.println("MemberController==update Member Holding Money ==> success");
        } else {
            System.out.println("MemberController==update Member Holding Money ==> failed");
        }
        return true;
    }

    @GetMapping(value = "/popup_list")
    public Result<JSONObject> popupList() {
        Result<JSONObject> result = new Result<>();
        JSONObject jsonObject = new JSONObject();
        List<PopupSetting> popupSettingList = new ArrayList<>();
        List<PopupSetting> convertPopupSettingList = new ArrayList<>();

        try{
            QueryWrapper<PopupSetting> popQw = new QueryWrapper<>();
            popQw.ne("today_show", new Date());
//            popQw.ge("expiration_end", new Date());
            popupSettingList = popupSettingService.list();

            for (PopupSetting temp_item : popupSettingList) {

                String location = temp_item.getLocation();

                String[] splitString = location.split(",");

                String[] xAxios = splitString[0].split(":");
                String[] yAxios = splitString[1].split(":");
                String[] width = splitString[2].split(":");
                String[] height = splitString[3].split(":");
                temp_item.setXaxios(xAxios[1]);
                temp_item.setYaxios(yAxios[1]);
                temp_item.setWidth(width[1]);
                temp_item.setHeight(height[1]);

                convertPopupSettingList.add(temp_item);
            }

            jsonObject.put("popupNotice", convertPopupSettingList);
            result.success("Success");
            result.setResult(jsonObject);
        }
        catch (Exception e){
            result.error500("Internal Server Error");
            log.error("url: /popup_list --- method: getMemberInfo() --- message: " + e.toString());
        }
        return result;
    }

    @GetMapping(value = "/set_popup_today_show")
    public Result<JSONObject> setPopupTodayShow() {
        Result<JSONObject> result = new Result<>();
        JSONObject jsonObject = new JSONObject();
        List<PopupSetting> popupSettingList = new ArrayList<>();

        try{
            QueryWrapper<PopupSetting> popQw = new QueryWrapper<>();
            popQw.ne("today_show", new Date());
//            popQw.ge("expiration_end", new Date());
            popupSettingList = popupSettingService.list(popQw);
            jsonObject.put("popupNotice", popupSettingList);
            result.success("Success");
            result.setResult(jsonObject);
        }
        catch (Exception e){
            result.error500("Internal Server Error");
            log.error("url: /popup_list --- method: getMemberInfo() --- message: " + e.toString());
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

    @GetMapping(value = "getInitialData")
    public Result<JSONObject> getHomeInfo() {
        Result<JSONObject> result = new Result<>();
        JSONObject obj = new JSONObject();
        try {
            QueryWrapper<Note> qw = new QueryWrapper<>();

            Float houseMoney = 0.0f;
            Float jackpotAmount = 0.0f;
            String inlineNotice = "";
            String baccaratCheck = "";
            String slotCheck = "";

            Map<String, Object> topRanking = new HashMap<>();

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

                baccaratCheck = basicSetting.getBaccaratCheck();
                slotCheck = basicSetting.getSlotCheck();
            }

            obj.put("houseMoney", houseMoney);
            obj.put("jackpotAmount", jackpotAmount);
            obj.put("inlineNotice", inlineNotice);
            obj.put("topRanking", topRanking);
            obj.put("notice", notice);
            obj.put("event", event);
            obj.put("withdraw", historyList);
            obj.put("baccaratCheck", baccaratCheck);
            obj.put("slotCheck", slotCheck);

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
            @RequestParam(name = "sender", defaultValue = "0") String sender,
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

    @GetMapping(value = "getSupportList")
    public Result<IPage<Note>> getSupportList(
            @RequestParam(name = "sender", defaultValue = "0") String sender,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Result<IPage<Note>> result = new Result<>();
        try {
            QueryWrapper<Note> qw = new QueryWrapper<>();
            qw.eq("sender", sender);
            qw.eq("type", CommonConstant.TYPE_POST);
            qw.eq("classification", CommonConstant.CLASSIFICATION_CUSTOMER);
            qw.eq("send_type", CommonConstant.TYPE_RECEIVE_NOTE);
            qw.eq("remove_status", CommonConstant.STATUS_UNREMOVED);
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
            @RequestParam(name = "receiver") String receiver,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {

        Result<IPage<Note>> result = new Result<>();
        try {
            QueryWrapper<Note> qw = new QueryWrapper<>();
            qw.eq("type", type);
            qw.eq("receiver", receiver);
            qw.eq("send_type", 0);
            qw.eq("remove_status", 0);
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

        Note note = noteService.getById(seq);
        note.setRemoveStatus(1);
        if (noteService.updateById(note)) {
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
                                                            @RequestParam(name = "operationType", defaultValue = "0") Integer operationType,
                                                            @RequestParam(name = "reasonType", defaultValue = "0") Integer reasonType) {
        Result<IPage<MoneyHistory>> result = new Result<>();
        try {
            Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
            IPage<MoneyHistory> pageList = moneyHistoryService.getMonthMoneyLogByMemberSeq(page, memberSeq, operationType, reasonType);
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
            receiverMoneyHistory.setReasonType(CommonConstant.MONEY_REASON_DEPOSIT);
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
            note.setSendType(1);
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
                MoneyHistory moneyHistory1 = new MoneyHistory();

                Member member = memberService.getById(moneyHistoryParams.getReceiver());

                float reqAmount = moneyHistoryParams.getVariableAmount();
                float holdingTotalMoney = member.getCasinoMoney() + member.getMoneyAmount();

                if (reqAmount > holdingTotalMoney ) {
                    result.error505("????????? ???????????????. ????????? ??????????????????");
                } else {
                    if(member.getMoneyAmount() < reqAmount){
                        float restAmount = reqAmount - member.getMoneyAmount();
                        ResponseEntity<String> ret = HttpUtils.userSubBalance(gameServerUrl + "/user/sub-balance", member.getId(), restAmount, apiKey);
                        if (ret.getStatusCode().value() == 200) {

                            // save money history for trasfer money from casino to site money --------------- <
                            moneyHistory1.setSeq(UUIDGenerator.generate());
                            moneyHistory1.setReceiver(member.getSeq());

                            // previous Amount
                            moneyHistory1.setPrevAmount(member.getMoneyAmount());
                            // variable amount
                            moneyHistory1.setVariableAmount(restAmount);
                            // final amount
                            moneyHistory1.setFinalAmount(member.getMoneyAmount() + restAmount);

                            float casinoVariableAmount = member.getCasinoMoney() - restAmount;
                            moneyHistory1.setReason("??????(???????????? <- ???????????????["+ casinoVariableAmount  +"])");
                            moneyHistory1.setStatus(CommonConstant.MONEY_HISTORY_STATUS_COMPLETE);

                            // set after 1s
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Calendar date = Calendar.getInstance();
                            long before_sec = date.getTimeInMillis() - 1000;
                            Date before_sec_date = simpleDateFormat.parse(simpleDateFormat.format(new Date(before_sec)));

                            moneyHistory1.setApplicationTime(before_sec_date);
                            moneyHistory1.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT);
                            moneyHistory1.setReasonType(CommonConstant.MONEY_REASON_GAME_DEPOSIT);
                            moneyHistoryService.save(moneyHistory1);
                            // save money history for trasfer money from casino to site money --------------- />

                            // update money of member of casino and holding money -------------------------- <
                            member.setCasinoMoney(member.getCasinoMoney() - restAmount);
                            member.setMoneyAmount(member.getMoneyAmount() + restAmount);
                            memberService.updateById(member);
                            // update money of member of casino and holding money -------------------------- />
                        }
                        else{
                            result.error505("/user/sub-balance request failed");
                        }
                    }

                    moneyHistory.setSeq(UUIDGenerator.generate());
                    moneyHistory.setPrevAmount(member.getMoneyAmount());
                    moneyHistory.setReceiver(moneyHistoryParams.getReceiver());
                    moneyHistory.setVariableAmount(moneyHistoryParams.getVariableAmount());
                    moneyHistory.setNote(moneyHistoryParams.getNote());
                    moneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_IN_PROGRESS);
                    moneyHistory.setApplicationTime(new Date());
                    moneyHistory.setOperationType(CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL);
                    moneyHistory.setReasonType(CommonConstant.MONEY_REASON_WITHDRAW);

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
            MoneyHistory moneyHistory1 = new MoneyHistory();

            // Thomas 2022.04.11
            // set sync casino money with game service and local money
            // when user access to game service, casino money added, local money becomes 0

            if(member.getMoneyAmount() > 0){

                ResponseEntity<String> ret = HttpUtils.userAddBalance(gameServerUrl + "/user/add-balance", member.getId(), member.getMoneyAmount(), apiKey);

                if (ret.getStatusCode().value() == 200) {

                    float casino_money = 0;
                    if(member.getCasinoMoney() != null){
                        casino_money = member.getCasinoMoney();
                    }

                    // save money history for trasfer money from casino to site money --------------- <
                    moneyHistory1.setSeq(UUIDGenerator.generate());
                    moneyHistory1.setReceiver(member.getSeq());
                    // previous Amount
                    moneyHistory1.setPrevAmount(member.getMoneyAmount());
                    // variable amount
                    moneyHistory1.setVariableAmount(member.getMoneyAmount());
                    // final amount
                    moneyHistory1.setFinalAmount(0.0F);

                    moneyHistory1.setReason("???????????? -> ???????????????["+ member.getMoneyAmount()  +"]");
                    moneyHistory1.setStatus(CommonConstant.MONEY_HISTORY_STATUS_COMPLETE);
                    moneyHistory1.setApplicationTime(new Date());
                    moneyHistory1.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW);
                    moneyHistory1.setReasonType(CommonConstant.MONEY_REASON_GAME_WITHDRAW);
                    moneyHistoryService.save(moneyHistory1);
                    // save money history for trasfer money from casino to site money --------------- />

                    float update_casino_money = casino_money + member.getMoneyAmount();
                    member.setCasinoMoney(update_casino_money);
                    member.setMoneyAmount(0.0F);

                    memberService.updateById(member);
                    result.success("success");
                    result.setResult(member);
                }
                else {
                    String rawString = ret.getBody();
                    byte[] bytes = rawString.getBytes(StandardCharsets.UTF_8);

                    String utf8EncodedString = new String(bytes, StandardCharsets.UTF_8);

                    assertEquals(rawString, utf8EncodedString);
//                    result.errorMsg(ret.getBody(), ret.getStatusCode().value());
                    result.errorMsg("??????????????? ????????? ???????????????", ret.getStatusCode().value());
                }
            }
            else{
                String url = gameServerUrl + "/user?username=" + member.getId();
                ResponseEntity<String> res = HttpUtils.getUserInfo(url, apiKey);

                if (res.getStatusCode().value() == 200) {
                    APIUserForm memberForms = JSON.parseObject(res.getBody().toString(), APIUserForm.class);

                    member.setCasinoMoney(memberForms.getBalance());
                    memberService.updateById(member);
                    result.success("success");
                    result.setResult(member);
                }
                else {result.error505("/user api failed");}
            }
        } catch (HttpStatusCodeException e) {
            log.error("url: /api/syncCasinoMoney --- method: syncCasinoMoney --- message: " + e.toString());
        }
        return result;
    }

    @GetMapping(value = "exchangePoint")
    public Result<Member> exchangePoint(@RequestParam("userSeq") String userSeq) {
        Result<Member> result = new Result<>();
        try {
            Member member = memberService.getById(userSeq);

            System.out.println("ApiController==exchangePoint API==");
            System.out.println("*************** request exchange api ***************");
            System.out.println("\t*** member seq : "+member.getSeq());
            System.out.println("\t*** member MoneyAmount : "+member.getMoneyAmount());
            System.out.println("\t*** member MileageAmount : "+member.getMileageAmount());
            System.out.println("\t*******************************************");

            String reason = "?????????["+ member.getMileageAmount() +"]->????????????";
            if (memberService.updateMemberHoldingMoney(
                    member.getSeq(),
                    member.getMoneyAmount(),
                    member.getMileageAmount(),
                    member.getMileageAmount(),
                    member.getMileageAmount(),
                    0f,
                    CommonConstant.CLASSIFICATION_MILEAGE,
                    CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW,
                    CommonConstant.MONEY_HISTORY_STATUS_COMPLETE,
                    CommonConstant.MONEY_REASON_POINT,
                    reason,
                    ""
                ) &&
                    memberService.updateMemberHoldingMoney(
                    member.getSeq(),
                    member.getMoneyAmount(),
                    0f,
                    member.getMileageAmount(),
                    member.getMileageAmount(),
                    member.getMoneyAmount() + member.getMileageAmount(),
                    CommonConstant.CLASSIFICATION_MONEY,
                    CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT,
                    CommonConstant.MONEY_HISTORY_STATUS_COMPLETE,
                    CommonConstant.MONEY_REASON_POINT,
                    reason,
                    ""
                )
            ) {
                result.success("success");
                System.out.println("MemberController==update Member Holding Money ==> success");
            } else {
                result.error505("fail");
                System.out.println("MemberController==update Member Holding Money ==> failed");
            }
            result.success("success");
            result.setResult(member);
        }
        catch (Exception e) {
            result.error500("Internal Server Error");
            log.error("url: /api/exchangePoint --- method: syncCasinoMoney --- message: " + e.toString());
            e.printStackTrace();
        }
        return result;
    }
}