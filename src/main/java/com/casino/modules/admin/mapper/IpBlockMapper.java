package com.casino.modules.admin.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.admin.common.entity.IpBlock;

public interface IpBlockMapper extends BaseMapper<IpBlock> {

	IPage<IpBlock> getBlockList(
			Page<IpBlock> page,
			@Param("order") Integer order,
			@Param("column") String column);

}
