package com.casino.modules.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.admin.common.entity.IpBlock;

public interface IIpBlockService extends IService<IpBlock> {

	IPage<IpBlock> getBlockList(Page<IpBlock> page, Integer order, String column);
}