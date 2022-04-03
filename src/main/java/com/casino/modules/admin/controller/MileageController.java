package com.casino.modules.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.constant.CommonConstant;
import com.casino.modules.admin.common.entity.Dict;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.common.entity.MileageHistory;
import com.casino.modules.admin.service.IDictService;
import com.casino.modules.admin.service.IMemberService;
import com.casino.modules.admin.service.IMileageHistoryService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/log")
@Slf4j
public class MileageController {

    @Autowired
    private IMileageHistoryService mileageHistoryService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private IMemberService memberService;

    @RequestMapping(value = "/mileageloglist", method = {RequestMethod.GET, RequestMethod.POST})
    public String mileageloglist(Model model,
                                 @ModelAttribute("mileageHistory") MileageHistory mileageHistory,
                                 @RequestParam(name = "column", defaultValue = "mil.process_time") String column,
                                 @RequestParam(name = "order", defaultValue = "1") Integer order,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 HttpServletRequest request) {
        try {
            Page<MileageHistory> page = new Page<MileageHistory>(pageNo, pageSize);
            IPage<MileageHistory> pageList = mileageHistoryService.findList(page, mileageHistory, column, order);

            QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
            dictQueryWrapper.eq("dict_key", CommonConstant.DICT_KEY_MILEAGE_REASON);
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
            model.addAttribute("url", "/log/mileageloglist");
        } catch (Exception e) {
            log.error("url: /log/mileageloglist --- method: mileageloglist --- error: " + e.toString());
        }
        return "views/admin/mileage/mileageLog";
    }

    @GetMapping(value = "/mileage/detaillist")
    public String mileageDetailList(
            Model model,
            @RequestParam("date") String date,
            @RequestParam("operationType") Integer operationType,
            HttpServletRequest request) {
        try {
            MileageHistory mileageHistory = new MileageHistory();
            mileageHistory.setCheckDay(date);
            mileageHistory.setOperationType(operationType);
            List<MileageHistory> list = mileageHistoryService.getList(mileageHistory);
            model.addAttribute("list", list);
        } catch (Exception e) {
            log.error("url: /log/mileage/detaillist --- method: mileageDetailList --- " + e.getMessage());
        }
        return "views/admin/mileage/detailPopUp";
    }

    @RequestMapping(value = "/member/mileage/historydata", method = {RequestMethod.GET, RequestMethod.POST})
    public String memberMileageHistoryData(
            Model model,
            @RequestParam("memberSeq") String memberSeq,
            HttpServletRequest request) {
        try {
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String reasonType = request.getParameter("reasonType");
            MileageHistory mileageHistory = new MileageHistory();
            Member member = new Member();
            member.setSeq(memberSeq);
            mileageHistory.setMember(member);
            mileageHistory.setFromProcessTime(startDate);
            mileageHistory.setToProcessTime(endDate);
            mileageHistory.setReasonType(StringUtils.isBlank(reasonType) ? null : Integer.valueOf(reasonType));

            QueryWrapper<Dict> dictQueryWrapper2 = new QueryWrapper<>();
            dictQueryWrapper2.eq("dict_key", CommonConstant.DICT_KEY_MILEAGE_REASON);
            List<Dict> reasonTypeList2 = dictService.list(dictQueryWrapper2);
            model.addAttribute("reasonTypeList", reasonTypeList2);

            List<MileageHistory> list = mileageHistoryService.getList(mileageHistory);
            model.addAttribute("list", list);
            model.addAttribute("memberSeq", memberSeq);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("reasonType", reasonType);
            model.addAttribute("url", "log/member/mileage/historydata");
        } catch (Exception e) {
            log.error("url: /log/member/mileage/historydata --- method: memberMileageHistoryData --- " + e.getMessage());
        }
        return "views/admin/member/mileageHistory";
    }

}
