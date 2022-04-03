package com.casino.modules.admin.service;



import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.admin.common.entity.Dashboard;
import com.casino.modules.admin.common.form.DashboardForm;

public interface IDashboardService extends IService<DashboardForm> {
	
	DashboardForm searchDashboard(String name);
	
	IPage<Dashboard> getMembers(
			Page<Dashboard> page,
            String column,
            Integer order);
	
	Map<String, Number> getHeaderInfo();
}
