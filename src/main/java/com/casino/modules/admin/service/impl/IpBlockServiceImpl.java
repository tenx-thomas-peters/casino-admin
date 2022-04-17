package com.casino.modules.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.modules.admin.common.entity.IpBlock;
import com.casino.modules.admin.mapper.IpBlockMapper;
import com.casino.modules.admin.service.IIpBlockService;

@Service
public class IpBlockServiceImpl extends ServiceImpl<IpBlockMapper, IpBlock> implements IIpBlockService {
	
	@Autowired
	IpBlockMapper ipBlockMapper;

	@Override
	public IPage<IpBlock> getBlockList(Page<IpBlock> page, Integer order, String column) {
		return ipBlockMapper.getBlockList(page, order, column);
	}


}
