package com.casino.modules.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.admin.common.entity.PopupSetting;

public interface IPopupSettingService extends IService<PopupSetting> {
	
	
	IPage<PopupSetting> getList(
			Page<PopupSetting> page,
			String column,
			Integer order);	
}
