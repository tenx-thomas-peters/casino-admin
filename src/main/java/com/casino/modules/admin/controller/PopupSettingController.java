package com.casino.modules.admin.controller;

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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.utils.UUIDGenerator;
import com.casino.common.vo.Result;
import com.casino.modules.admin.common.entity.Note;
import com.casino.modules.admin.common.entity.PopupSetting;
import com.casino.modules.admin.service.IPopupSettingService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/config")
@Slf4j
public class PopupSettingController {
	@Autowired
    private IPopupSettingService popupSettingService;

    @GetMapping(value = "/popuplist")
    public String popupList(Model model, 
    		@RequestParam(name = "column", defaultValue = "ps.additional_date") String column,
    		@RequestParam(name = "order", defaultValue = "1") Integer order,
    		@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
    		@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
    		HttpServletRequest request) {
        try {
        	Page<PopupSetting> page = new Page<PopupSetting>(pageNo, pageSize);
            IPage<PopupSetting> pageList = popupSettingService.getList(page, column, order);
            
            model.addAttribute("page", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("column", column);
            model.addAttribute("order", order);
            model.addAttribute("url", "/config/popuplist");
        } catch (Exception e) {
            log.error("url: /config/popuplist --- method: popupList --- error: " + e.toString());
        }

        return "views/admin/popup/popupSetting";
    }
    
    @PostMapping(value = "/delete")
    @ResponseBody
	public Result<Note> delete(@RequestParam(name = "seq") String seq, HttpServletRequest request) {
		Result<Note> result = new Result<>();
		try {
			if (popupSettingService.removeById(seq)) {
				result.success("operation success");
			} else {
				result.success("operation failed");
			}
		} catch (Exception e) {
			log.error("url: /config/delete --- method: delete--- " + e.toString());
			result.error500("please confirm");
		}
		return result;
	}
    
    @GetMapping(value = "/addPopupPage")
    public String addPopupPage(Model model, HttpServletRequest request) {
        try {
        	model.addAttribute("url", "config/addPopupPage");
        } catch (Exception e) {
            log.error("url: /config/addPopupPage --- method: addPopupPage --- error: " + e.toString());
        }

        return "views/admin/popup/addPopup";
    }
    
    @GetMapping(value = "/editPopupPage")
    public String editPopupPage(@RequestParam("idx") String popupSettingSeq, Model model, HttpServletRequest request) {
        try {
        	PopupSetting popupSetting = new PopupSetting();
        	popupSetting.setSeq(popupSettingSeq);
        	popupSetting = popupSettingService.getById(popupSettingSeq);
        	
        	model.addAttribute("popupSetting", popupSetting);
        } catch (Exception e) {
            log.error("url: /config/editPopupPage --- method: editPopupPage --- error: " + e.toString());
        }

        return "views/admin/popup/editPopup";
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
	
    @PostMapping(value = "/addPopup")
    @ResponseBody
    public Result<PopupSetting> addPopup(@ModelAttribute("popup") PopupSetting popup, HttpServletRequest request) {
    	Result<PopupSetting> result = new Result<>();
        try {
        	popup.setSeq(UUIDGenerator.generate());
        	if (popupSettingService.save(popup)) {
        		result.success("success");
        		result.setResult(popup);
        	} else {
                result.error505("failed");
            }
        } catch (Exception e) {
            log.error("url: /config/addPopup --- method: addPopup --- error: " + e.toString());
            result.error500("Internal Server Error");
        }

        return result;
    }
	 
    @PostMapping(value = "/loadPopup")
    @ResponseBody
    public Result<PopupSetting> loadPopup(@RequestParam(name = "seq") String seq, HttpServletRequest request) {
    	Result<PopupSetting> result = new Result<>();
        try {
        	PopupSetting data = popupSettingService.getById(seq);
        	result.setResult(data);
        } catch (Exception e) {
            log.error("url: /config/editPopup --- method: editPopup --- error: " + e.toString());
        }

        return result;
    }
    
    @PostMapping(value = "/editPopup")
    @ResponseBody
    public Result<PopupSetting> editPopup(@ModelAttribute("popup") PopupSetting popup, HttpServletRequest request) {
    	Result<PopupSetting> result = new Result<>();
        try {
        	if (popupSettingService.updateById(popup)) {
        		result.success("success");
        	} else {
                result.error505("failed");
            }
        	
        } catch (Exception e) {
            log.error("url: /config/editPopup --- method: editPopup --- error: " + e.toString());
            result.error505("Internal Server Error");
        }

        return result;
    }
}
