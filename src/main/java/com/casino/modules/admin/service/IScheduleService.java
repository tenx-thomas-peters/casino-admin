package com.casino.modules.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.admin.common.entity.BettingSummary;
import com.casino.modules.admin.common.form.BettingLogForm;

import java.util.List;

public interface IScheduleService extends IService<BettingSummary> {
    public boolean saveBettingSummary(List<BettingLogForm> bettingLogFormList, long before_30_min);
}
