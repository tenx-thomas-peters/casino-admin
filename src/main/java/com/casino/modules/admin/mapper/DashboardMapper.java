package com.casino.modules.admin.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.admin.common.entity.Dashboard;
import com.casino.modules.admin.common.form.DashboardForm;

public interface DashboardMapper extends BaseMapper<DashboardForm> {
	DashboardForm searchDashboard(@Param("name") String name);
	
	IPage<Dashboard> getMembers(
            Page<Dashboard> page,
            @Param("column") String column,
            @Param("order") Integer order);

	Map<String, Number> getAccessData();

	Map<String, Number> getTotalMoneyInfo();

	Map<String, Number> getApplicationCount();
}

	
