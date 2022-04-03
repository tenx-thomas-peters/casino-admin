package com.casino.modules.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.admin.common.entity.Level;

public interface LevelMapper extends BaseMapper<Level> {

	IPage<Level> findHeadQuaterList(Page<Level> page, Level level, String column, Integer order);
}
