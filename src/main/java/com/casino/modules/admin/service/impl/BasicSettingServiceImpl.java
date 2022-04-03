package com.casino.modules.admin.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.modules.admin.common.entity.BasicSetting;
import com.casino.modules.admin.mapper.BasicSettingMapper;
import com.casino.modules.admin.service.IBasicSettingService;

@Service
public class BasicSettingServiceImpl extends ServiceImpl<BasicSettingMapper, BasicSetting> implements IBasicSettingService {

}
