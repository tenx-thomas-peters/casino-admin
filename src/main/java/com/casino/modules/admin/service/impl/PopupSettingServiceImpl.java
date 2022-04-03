package com.casino.modules.admin.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.modules.admin.common.entity.PopupSetting;
import com.casino.modules.admin.mapper.PopupSettingMapper;
import com.casino.modules.admin.service.IPopupSettingService;

@Service
public class PopupSettingServiceImpl extends ServiceImpl<PopupSettingMapper, PopupSetting> implements IPopupSettingService {
	
	@Autowired
	private PopupSettingMapper popupSettingMapper;
	
	@Override
	public IPage<PopupSetting> getList(
			Page<PopupSetting> page, 
			String column,
			Integer order) {
		return popupSettingMapper.getList(page, column, order);
	}
}
