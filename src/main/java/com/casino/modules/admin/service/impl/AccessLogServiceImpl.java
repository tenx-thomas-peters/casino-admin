package com.casino.modules.admin.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.modules.admin.common.entity.AccessLog;
import com.casino.modules.admin.mapper.AccessLogMapper;
import com.casino.modules.admin.service.IAccessLogService;

@Service
public class AccessLogServiceImpl extends ServiceImpl<AccessLogMapper, AccessLog> implements IAccessLogService {

	@Autowired
	private AccessLogMapper accessLogMapper;

	@Override
	public IPage<AccessLog> getAccessLogList(Page<AccessLog> page, AccessLog accessLog, Integer checkStatus, Integer hour, String column, Integer order) {		
		if (hour != null)
			accessLog.setConnectionDate(new Date());
		return accessLogMapper.getAccessLogList(page, accessLog, checkStatus, column, order);
	}

	@Override
	public boolean deleteBySeq(String deleteSeq) {
		return accessLogMapper.deleteBySeq(deleteSeq);
	}
	
	@Override
	public boolean batchDelete(List<String> ids) {
		return accessLogMapper.batchDelete(ids);
	}

	@Override
	public boolean changeAdminReadStatusAll() {
		return accessLogMapper.changeAdminReadStatusAll();
	}
}
