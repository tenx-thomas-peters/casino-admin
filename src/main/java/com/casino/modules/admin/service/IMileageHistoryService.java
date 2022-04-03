package com.casino.modules.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.admin.common.entity.MileageHistory;

public interface IMileageHistoryService extends IService<MileageHistory> {
	
	IPage<MileageHistory> findList(
			Page<MileageHistory> page, 
			MileageHistory mileageHistory, 
			String column, 
			Integer order);

	List<MileageHistory> getList(MileageHistory mileageHistory);
}
