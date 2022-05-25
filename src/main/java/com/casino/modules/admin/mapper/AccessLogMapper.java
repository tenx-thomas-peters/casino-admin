package com.casino.modules.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.admin.common.entity.AccessLog;

public interface AccessLogMapper extends BaseMapper<AccessLog> {

	IPage<AccessLog> getAccessLogList(Page<AccessLog> page, 
			@Param("accessLogEntity") AccessLog accessLog,
			@Param("checkStatus") Integer checkStatus,
			@Param("column") String column, 
			@Param("order") Integer order);
	
	Boolean deleteBySeq(@Param("deleteSeq") String deleteSeq);
	
	Boolean batchDelete(@Param("ids") List<String> ids);

	boolean changeAdminReadStatusAll();
}
