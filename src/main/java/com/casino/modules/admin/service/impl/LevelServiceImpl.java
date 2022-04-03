package com.casino.modules.admin.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.modules.admin.common.entity.Level;
import com.casino.modules.admin.mapper.LevelMapper;
import com.casino.modules.admin.service.ILevelService;

@Service
public class LevelServiceImpl extends ServiceImpl<LevelMapper, Level> implements ILevelService {

}
