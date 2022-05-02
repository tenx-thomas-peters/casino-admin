package com.casino.modules.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.admin.common.entity.BettingSummary;
import com.casino.modules.admin.common.form.BettingSummaryForm;

import java.util.List;

public interface IBettingSummaryService extends IService<BettingSummary> {
    IPage<BettingSummaryForm> getBettingSummaryList(
            Page<BettingSummaryForm> page,
            BettingSummaryForm bettingSummaryForm);

    List<BettingSummaryForm> getBettingSummaryList(BettingSummaryForm bettingSummaryForm);

    boolean changeAdminReadStatusAll();
}
