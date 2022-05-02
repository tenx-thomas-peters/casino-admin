package com.casino.modules.admin.service.impl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.modules.admin.common.entity.Dashboard;
import com.casino.modules.admin.common.form.DashboardForm;
import com.casino.modules.admin.mapper.DashboardMapper;
import com.casino.modules.admin.service.IDashboardService;

@Service
public class DashboardServiceImpl extends ServiceImpl<DashboardMapper, DashboardForm> implements IDashboardService {
	
	@Autowired
    private DashboardMapper dashboardMapper;
	
	@Override
	public DashboardForm searchDashboard(String name) {
		return dashboardMapper.searchDashboard(name);
	}
	
	@Override
	public IPage<Dashboard> getMembers(
			Page<Dashboard> page,
            String column,
            Integer order) {
		return dashboardMapper.getMembers(page, column, order);
	}

	@Override
	public Map<String, Number> getHeaderInfo() {
		Map<String, Number> headerInfo = dashboardMapper.getAccessData();
		Map<String, Number> getApplicationCountInfo = dashboardMapper.getApplicationCount();
		Map<String, Number> totalMoneyInfo = dashboardMapper.getTotalMoneyInfo();
		headerInfo.putAll(getApplicationCountInfo);
		headerInfo.putAll(totalMoneyInfo);
		return headerInfo;
	}
	
}
