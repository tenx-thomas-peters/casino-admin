package com.casino.modules.admin.controller;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.casino.modules.admin.common.entity.BasicSetting;
import com.casino.modules.admin.service.IBasicSettingService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/config")
@Slf4j
public class BasicSettingController {
	
	@Value(value = "${mysql-url}")
	private String mysqlUrl;
	
	@Value(value = "${mysql-dump}")
	private String mysqlDumpUrl;

	@Value(value = "${database}")
	private String database;
	
	@Value(value = "${host}")
	private String host;
	
	@Value(value = "${port}")
	private String port;
	
	@Value(value = "${db-username}")
	private String dbUsername;
	
	@Value(value = "${password}")
	private String password;
	
	@Value(value = "${backup-root}")
	private String backupRoot;
	
	@Autowired
	private IBasicSettingService basicSettingService;
	
	@RequestMapping(value = "/globalconfig")
    public String globalConfigList(Model model, HttpServletRequest request) {
		
        try {
			/*
			 * Page<BasicSetting> page = new Page<BasicSetting>(0, 10);
			 * QueryWrapper<BasicSetting> qw = new QueryWrapper<>();
			 */
             //IPage<BasicSetting> pageList = basicSettingService.page(page, qw);
			List<BasicSetting> basicSettingList = basicSettingService.list();
			BasicSetting basicSetting = new BasicSetting();
			if (!CollectionUtils.isEmpty(basicSettingList)) {
				basicSetting = basicSettingList.get(0);
			}

             model.addAttribute("basicSetting", basicSetting);
             model.addAttribute("url", "config/globalconfig");
        } catch (Exception e) {
            log.error("url: /config/globalconfig --- method: globalConfigList --- error: " + e.toString());
        }

        return "views/admin/basicSetting/list";
    }
	
	@RequestMapping(value = "/getConfig")
	@ResponseBody
	public BasicSetting getConfig() {
		List<BasicSetting> basicSettingList = basicSettingService.list();
		BasicSetting basicSetting = new BasicSetting();
		if (!CollectionUtils.isEmpty(basicSettingList)) {
			basicSetting = basicSettingList.get(0);
		}
		return basicSetting;
	}
	
	@RequestMapping(value = "/saveGlobalconfig")
    public String saveGlobalconfig(BasicSetting basicSetting, HttpServletRequest request) {
		
        try {
			basicSetting.setSeq("123");
			List<BasicSetting> basicSettingList = basicSettingService.list();
			if (!CollectionUtils.isEmpty(basicSettingList)) {
				basicSettingService.updateById(basicSetting);
			}else {
				basicSettingService.save(basicSetting);
			}
			
			//basicSettingService.insertBasicSetting(basicSetting);
        } catch (Exception e) {
            log.error("url: /config/saveGlobalconfig --- method: saveGlobalconfig --- error: " + e.toString());
        }

        return "redirect:/config/globalconfig";
    }
	
	@GetMapping(value = "dbOption")
	public String dbOption(Model model, HttpServletRequest request) {
		try {
			model.addAttribute("url", "config/dbOption");
		} catch (Exception e) {
			log.error("url: /config/globalconfig --- method: globalConfigList --- error: " + e.toString());
		}
		return "views/admin/basicSetting/dbOption";
	}
	
	@RequestMapping(value = "/dbBackup")
	@ResponseBody
	public void dbSave(HttpServletResponse response) {
		String path = new SimpleDateFormat("yyyy-MM-dd(HH-mm-ss-SSS)").format(new Date()) + ".sql";
		String cmd = mysqlDumpUrl + " -B " + database + " -h " + host + " -P " + port + " -u " + dbUsername + " --password=" + password;
		try {
			String output = execute_cmd(cmd);
			if(!output.contains("-- Dump completed on")) {
			} else {
				File file = new File(backupRoot + path);
				FileWriter writer;
				writer = new FileWriter(file);
				writer.write(output);
				writer.close();
				if (file.exists()) {
					response.setContentType("application/force-download;charset=UTF-8");
					response.addHeader("Content-Disposition", "attachment;fileName=" + file.getName() + ".sql");
					byte[] buffer = new byte[1024];
					FileInputStream fis = null;
					BufferedInputStream bis = null;
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream outputStream = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						outputStream.write(buffer, 0, i);
						i = bis.read(buffer);
					}
					if (bis != null) {
						bis.close();
					}
					if (fis != null) {
						fis.close();
					}
				}
			}
		} catch (IOException e) {
			log.error("url: /config/dbBackup --- method: dbSave --- error: " + e.toString());
		}
	}
	
	public static String execute_cmd(String cmd) throws IOException {
		String result = "";
		Process process = Runtime.getRuntime().exec(cmd);
		BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String str = null;
		while ((str = inStreamReader.readLine()) != null) {
			result += str + "\n";
		}
		return result;
	}

}