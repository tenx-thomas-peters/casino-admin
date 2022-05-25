package com.casino.modules.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.casino.modules.admin.common.entity.BasicSetting;
import com.casino.modules.admin.service.IBasicSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.vo.Result;
import com.casino.modules.admin.common.entity.Dashboard;
import com.casino.modules.admin.common.form.DashboardForm;
import com.casino.modules.admin.service.IDashboardService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/dashboard")
@Slf4j
public class DashboardController {
	@Autowired
    private IDashboardService dashboardService;

	@Autowired
	private IBasicSettingService basicSettingService;
	
	@RequestMapping(value = "/index")
    public String index(Model model, 
    		@RequestParam(name = "column", defaultValue = "mon.application_time") String column,
    		@RequestParam(name = "order", defaultValue = "1") Integer order,
    		@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
    		@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
    		HttpServletRequest request) {
		try {
			Page<Dashboard> page = new Page<Dashboard>(pageNo, pageSize);
			IPage<Dashboard> pageList = dashboardService.getMembers(page, column, order);
			
			model.addAttribute("page", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("column", column);
            model.addAttribute("order", order);
            model.addAttribute("url", "/dashboard/index");
		} catch(Exception e) {
			log.error("url: /dashboard/index --- method: index --- error: " + e.toString());
		}
		return "views/admin/dashboard/index";
    }
	
	
	@PostMapping(value = "/search")
	@ResponseBody
	public Result<DashboardForm> searchDashboard(@RequestParam(name = "name") String name, HttpServletRequest request) {
		Result<DashboardForm> result = new Result<>();
		try {
			DashboardForm data = dashboardService.searchDashboard(name);
			result.setResult(data);
		} catch (Exception e) {
			log.error("url: /dashboard/search --- method: searchDashboard --- error: " + e.toString());
		}
		
		return result;
	}
	
	@PostMapping(value = "/headerinfo")
	@ResponseBody
	public Result<Map<String, Number>> headerInfo() {
		Result<Map<String, Number>> result = new Result<>();
		try {
			Map<String, Number> data = new HashMap<>();
			data = dashboardService.getHeaderInfo();
			result.setData(data);
			result.success("success");
		} catch (Exception e) {
			log.error("url: /dashboard/headerinfo --- method: headerInfo --- error: " + e.toString());
		}
		
		return result;
	}

	@GetMapping(value = "/adminmemo")
	@ResponseBody
	public Result<BasicSetting> adminmemo() {
		Result<BasicSetting> result = new Result<>();
		try {
			BasicSetting basicSetting = basicSettingService.getById(123);
			result.setData(basicSetting);
			result.success("success");
		} catch (Exception e) {
			log.error("url: /adminmemo --- method: headerInfo --- error: " + e.toString());
		}

		return result;
	}

	@PostMapping(value = "/savememo")
	@ResponseBody
	public Result<Map<String, Number>> savememo(@RequestParam(name = "memo") String adminMemo, HttpServletRequest request) {
		Result<Map<String, Number>> result = new Result<>();
		try {
			BasicSetting basicSetting = basicSettingService.getById(123);
			basicSetting.setAdminMemo(adminMemo);
			basicSettingService.updateById(basicSetting);
			result.success("success");
		} catch (Exception e) {
			log.error("url: /dashboard/savememo --- method: headerInfo --- error: " + e.toString());
		}

		return result;
	}
}


