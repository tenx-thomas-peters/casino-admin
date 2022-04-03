package com.casino.modules.admin.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.constant.CommonConstant;
import com.casino.common.vo.Result;
import com.casino.modules.admin.common.entity.AccessLog;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.service.IAccessLogService;
import com.casino.modules.admin.service.IMemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/member")
@Slf4j
public class AccessLogController {

	@Autowired
	private IAccessLogService accessLogService;
	
	@Autowired
	private IMemberService memberService;
	
	@RequestMapping(value = "/loginlist")
	public String getList(Model model, 
			@ModelAttribute("accessLog") AccessLog accessLog,
    		@RequestParam(name = "column", defaultValue = "connection_date") String column,
    		@RequestParam(name = "order", defaultValue = "1") Integer order,
    		@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
    		@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
    		@RequestParam(name = "hour", required = false) Integer hour,
    		@RequestParam(name = "checkStatus", required = false) Integer checkStatus,
			HttpServletRequest httpServletRequest) {
		
		try {
			Page<AccessLog> page = new Page<AccessLog>(pageNo, pageSize);
			IPage<AccessLog> pageList = accessLogService.getAccessLogList(page, accessLog, checkStatus, hour, column, order);
			
			List<Map<String, String>> siteList = memberService.getSiteList();
			
			QueryWrapper<Member> memberQuery = new QueryWrapper<Member>();
			memberQuery.eq("user_type", CommonConstant.USER_TYPE_DISTRIBUTOR);
			List<Member> partnerList = memberService.list(memberQuery);
						
			model.addAttribute("page", pageList);
			model.addAttribute("partnerList", partnerList);
			model.addAttribute("siteList", siteList);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("checkStatus", checkStatus);
			model.addAttribute("url", "/member/loginlist");
			
		} catch (Exception e) {
			log.error("url: /member/loginlist --- method: list --- error: " + e.toString());
		}
	
		return "views/admin/member/loginlist";
		
	}
	
	@PostMapping(value = "/accessLogDelete")
	@ResponseBody
	public Result<AccessLog> accessLogDelete(@RequestParam(name = "deleteSeq") String deleteSeq, HttpServletRequest request) {
		Result<AccessLog> result = new Result<>();
		try {
			if(accessLogService.deleteBySeq(deleteSeq)) {
				result.success("delete success");
			} else {
				result.error500("delete failed");
			}
			
		} catch(Exception e) {
			log.error("url: /member/logDelete --- method: accessLogDelete --- error: " + e.toString());
		}
		return result;
	}
	
    @PostMapping(value = "/batchDelete")
    @ResponseBody
	public Result<AccessLog> batchDelete(@RequestParam(name = "ids") String ids, HttpServletRequest request) {
		Result<AccessLog> result = new Result<>();
		List<String> idList = Arrays.asList(ids.split(","));
		try {
			if (accessLogService.batchDelete(idList)) {
				result.success("batch delete success");
			} else {
				result.error500("batch delete failed");
			}
		} catch (Exception e) {
			log.error("url: /member/accessLogDelete --- method: delete--- " + e.toString());
			result.error500("batch delete failed");
		}
		return result;
	}
	
}
