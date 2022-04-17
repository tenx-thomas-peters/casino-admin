package com.casino.modules.admin.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.utils.UUIDGenerator;
import com.casino.common.vo.Result;
import com.casino.modules.admin.common.entity.IpBlock;
import com.casino.modules.admin.service.IIpBlockService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "ipBlock")
@Slf4j
public class IpBlockController {
	
	@Autowired
	IIpBlockService ipBlockService;
	
	@RequestMapping(value = "/list")
	public String ipBlockList(Model model, 
			@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "order", defaultValue = "1") Integer order,
			@RequestParam(value = "column", defaultValue = "hour") String column,
			@ModelAttribute("ipBlock") IpBlock ipBlock, HttpServletRequest request) {
		try {
			Page<IpBlock> page = new Page<IpBlock>(pageNo, pageSize);
			IPage<IpBlock> pageList = ipBlockService.getBlockList(page, order, column);
			
			model.addAttribute("pageList", pageList);
			model.addAttribute("url", "/ipBlock/list");
		} catch(Exception e) {
			log.error("url: /ipBlock/list --- method: ipBlockList --- error: " + e.toString());
		}
		return "views/admin/common/ipBlock";
	}
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public Result<Map<String, Object>> add(
			@RequestParam(value = "ipAddress") String ipAddress,
			@RequestParam(value = "cause") String cause) {
		Result<Map<String, Object>> result = new Result<>();
		try {
			IpBlock ipBlock = new IpBlock();
			if (ipAddress == null || ipAddress == "") {
				result.error505("failed");
			} else if (cause == null || cause == "") {
				result.error505("failed");
			} else {
				ipBlock.setSeq(UUIDGenerator.generate());
				ipBlock.setIpAddress(ipAddress);
				ipBlock.setCause(cause);
				ipBlock.setHour(new Date());
				ipBlock.setState("release");
				if (ipBlockService.save(ipBlock)) {
					result.success("success");
				} else {
					result.error505("failed");
				}
			}
		} catch(Exception e) {
			log.error("url: /ipBlock/list --- method: ipBlockList --- error: " + e.toString());
		}
		return result;
	}
}
