package com.casino.modules.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.admin.common.entity.BettingSummary;
import com.casino.modules.admin.common.form.BettingSummaryForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BettingSummaryMapper extends BaseMapper<BettingSummary> {
    IPage<BettingSummaryForm> getBettingSummaryList(
            Page<BettingSummaryForm> page,
            @Param("entity") BettingSummaryForm bettingSummaryForm);

    List<BettingSummaryForm> getBettingSummaryList(@Param("entity") BettingSummaryForm bettingSummaryForm);

    boolean changeAdminReadStatusAll();
}
