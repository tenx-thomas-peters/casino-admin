package com.casino.modules.admin.mapper;


import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.admin.common.entity.PopupSetting;

public interface PopupSettingMapper extends BaseMapper<PopupSetting> {
	
	IPage<PopupSetting> getList(Page<PopupSetting> page, 
			@Param("column") String column, 
			@Param("order") Integer order);	
}
