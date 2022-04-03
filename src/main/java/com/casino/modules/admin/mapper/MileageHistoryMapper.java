package com.casino.modules.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.admin.common.entity.MileageHistory;

public interface MileageHistoryMapper extends BaseMapper<MileageHistory> {
	
	IPage<MileageHistory> findList(Page<MileageHistory> page, 
			@Param("entity") MileageHistory mileageHistory, 
			@Param("column") String column, 
			@Param("order") Integer order);

	List<MileageHistory> getList(@Param("entity") MileageHistory mileageHistory);
}
