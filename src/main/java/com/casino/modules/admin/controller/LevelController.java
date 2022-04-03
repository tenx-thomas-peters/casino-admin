package com.casino.modules.admin.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.common.vo.Result;
import com.casino.modules.admin.common.entity.Level;
import com.casino.modules.admin.common.entity.Note;
import com.casino.modules.admin.service.ILevelService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/config")
@Slf4j
public class LevelController {

	@Autowired
	private ILevelService levelService;

	@GetMapping(value = "/level")
	public String levelList(Model model, HttpServletRequest request,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		try {
			Page<Level> page = new Page<Level>(pageNo, pageSize);
			QueryWrapper<Level> qw = new QueryWrapper<>();
			IPage<Level> pageList = levelService.page(page, qw);

			model.addAttribute("pageList", pageList);
			model.addAttribute("url", "config/level");
		} catch (Exception e) {
			log.error("url: /config/level --- method: level --- error: " + e.toString());
		}

		return "views/admin/level/list";
	}

	
	 @GetMapping(value = "add_detail")
	 public String addDetail(Model model,HttpServletRequest request) { 
		 try {
			 model.addAttribute("url", "level/add_detail");
		 } catch (Exception e) {
			 log.error("url: /config/add_detail --- method: level --- error: " + e.toString());
		 }
		 return "views/admin/level/add_detail";
	 }
	 
	 
	@PostMapping(value = "/addLevel")
	@ResponseBody
	public Result<Level> addLevel(@ModelAttribute("level") Level level, HttpServletRequest request) {
		Result<Level> result = new Result<>();
		try {
			level.setSeq(UUIDGenerator.generate());
			if (levelService.save(level)) {
                result.success("success");
                result.setResult(level);
            } else {
                result.error505("failed");
            }
		} catch (Exception e) {
			log.error("url: /config/addLevel --- method: addLevel --- error: " + e.toString());
			result.error500("Internal Server Error");
		}

		return result;
	}

	@PostMapping(value = "/applyLevel")
	public String applyLevel(Level level, HttpServletRequest request) {
		try {
			level.setCorrection(CommonConstant.CORRECTION_APPLY);
			levelService.updateById(level);
		} catch (Exception e) {
			log.error("url: /config/applyLevel --- method: applyLevel --- error: " + e.toString());
		}

		return "redirect:/config/level";
	}

	@PostMapping(value = "deleteLevels")
	@ResponseBody
	public Result<Note> deleteLevels(@RequestParam(name = "ids") String ids, HttpServletRequest request) {
		Result<Note> result = new Result<>();
		List<String> idList = Arrays.asList(ids.split(","));
		try {
			if (levelService.removeByIds(idList)) {
				result.success("operation success");
			} else {
				result.success("operation failed");
			}
		} catch (Exception e) {
			log.error("url: /config/deleteLevels --- method: deleteLevels--- " + e.toString());
			result.error500("please confirm");
		}
		return result;
	}
}