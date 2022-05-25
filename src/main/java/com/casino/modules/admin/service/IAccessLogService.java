package com.casino.modules.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.admin.common.entity.AccessLog;

public interface IAccessLogService extends IService<AccessLog> {

	IPage<AccessLog> getAccessLogList(Page<AccessLog> page, AccessLog accessLog, Integer checkStatus, Integer hour, String column, Integer order);
	
	boolean deleteBySeq(String deleteSeq);
	
	boolean batchDelete(List<String> ids);

	boolean changeAdminReadStatusAll();
}