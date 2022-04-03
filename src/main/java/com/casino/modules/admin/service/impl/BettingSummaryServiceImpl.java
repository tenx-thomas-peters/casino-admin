package com.casino.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.modules.admin.common.entity.BettingSummary;
import com.casino.modules.admin.common.form.BettingSummaryForm;
import com.casino.modules.admin.mapper.BettingSummaryMapper;
import com.casino.modules.admin.service.IBettingSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BettingSummaryServiceImpl extends ServiceImpl<BettingSummaryMapper, BettingSummary> implements IBettingSummaryService {
    @Autowired
    private BettingSummaryMapper bettingSummaryMapper;

    @Override
    public IPage<BettingSummaryForm> getBettingSummaryList(
            Page<BettingSummaryForm> page,
            BettingSummaryForm bettingSummaryForm) {
        return bettingSummaryMapper.getBettingSummaryList(page, bettingSummaryForm);
    }

    @Override
    public List<BettingSummaryForm> getBettingSummaryList(BettingSummaryForm bettingSummaryForm) {
        return bettingSummaryMapper.getBettingSummaryList(bettingSummaryForm);
    }
}
