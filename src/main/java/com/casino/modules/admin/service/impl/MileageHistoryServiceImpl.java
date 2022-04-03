package com.casino.modules.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.modules.admin.common.entity.MileageHistory;
import com.casino.modules.admin.mapper.MileageHistoryMapper;
import com.casino.modules.admin.service.IMileageHistoryService;

@Service
public class MileageHistoryServiceImpl extends ServiceImpl<MileageHistoryMapper, MileageHistory> implements IMileageHistoryService {
	
	@Autowired
	private MileageHistoryMapper mileageHistoryMapper;

	@Override
	public IPage<MileageHistory> findList(
			Page<MileageHistory> page, 
			MileageHistory mileageHistory, 
			String column, 
			Integer order) {
		return mileageHistoryMapper.findList(page, mileageHistory, column, order);
	}

	@Override
	public List<MileageHistory> getList(MileageHistory mileageHistory) {
		return mileageHistoryMapper.getList(mileageHistory);
	}
	
}
