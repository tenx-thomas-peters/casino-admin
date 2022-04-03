package com.casino.modules.admin.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.modules.admin.common.entity.Dict;
import com.casino.modules.admin.mapper.DictMapper;
import com.casino.modules.admin.service.IDictService;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {
}
