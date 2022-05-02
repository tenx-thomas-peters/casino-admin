package com.casino.modules.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.constant.CommonConstant;
import com.casino.common.vo.Result;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.common.form.BettingSummaryForm;
import com.casino.modules.admin.service.IBettingSummaryService;
import com.casino.modules.admin.service.IMemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/log")
@Slf4j
public class BettingSummaryController {
    @Autowired
    private IBettingSummaryService bettingSummaryService;

    @Autowired
    private IMemberService memberService;

    @RequestMapping(value = "slotloglist")
    public String slotLogList(@ModelAttribute("bettingSummaryForm") BettingSummaryForm bettingSummaryForm,
                              @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                              Model model, HttpServletRequest request) {
        try {
            bettingSummaryForm.setUserType(CommonConstant.USER_TYPE_NORMAL);
            Page<BettingSummaryForm> page = new Page<>(pageNo, pageSize);
            IPage<BettingSummaryForm> pageList = bettingSummaryService.getBettingSummaryList(page, bettingSummaryForm);
            bettingSummaryService.changeAdminReadStatusAll();

            QueryWrapper<Member> memberQw = new QueryWrapper<>();
            memberQw.eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> partnerList = memberService.list(memberQw);

            model.addAttribute("pageList", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("partnerList", partnerList);
            model.addAttribute("url", "log/slotloglist");
        } catch (Exception e) {
            log.error("url: /log/slotloglist --- method: slotLogList --- message: " + e.toString());
        }
        return "views/admin/bettingSummary/logList";
    }

    @RequestMapping(value = "slotloglist_ajax")
    @ResponseBody
    public Result<List<BettingSummaryForm>> slotLogListAjax(
            @ModelAttribute("bettingSummaryForm") BettingSummaryForm bettingSummaryForm,
            HttpServletRequest request) {
        Result<List<BettingSummaryForm>> result = new Result<>();
        try {
            List<BettingSummaryForm> list = bettingSummaryService.getBettingSummaryList(bettingSummaryForm);

            result.setResult(list);
            result.success("success");
        } catch (Exception e) {
            log.error("url: /log/slotloglist_ajax --- method: slotLogListAjax --- message: " + e.toString());
            result.error500("Internal Server Error");
        }

        return result;
    }
}
