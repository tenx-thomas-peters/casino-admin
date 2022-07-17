package com.casino.modules.admin.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("betting_summary")
public class BettingSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "seq")
    private String seq;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "check_time")
    private String checkTime;

    @TableField(value = "check_time_duration")
    private String checkTimeDuration;

    @TableField(value = "member_seq")
    private String memberSeq;

    @TableField(value = "playing_game")
    private String playingGame;

    @TableField(value = "type")
    private Integer type;

    @TableField(value = "slot_betting_amount")
    private Float slotBettingAmount;

    @TableField(value = "baccarat_betting_amount")
    private Float baccaratBettingAmount;

    @TableField(value = "baccarat_virtual_betting_amount")
    private Float baccaratVirtualBettingAmount;

    @TableField(value = "slot_winning_amount")
    private Float slotWinningAmount;

    @TableField(value = "baccarat_winning_amount")
    private Float baccaratWinningAmount;

    @TableField(value = "slot_lost_amount")
    private Float slotLostAmount;

    @TableField(value = "baccarat_lost_amount")
    private Float baccaratLostAmount;

    @TableField(value = "slot_rolling_amount")
    private Float slotRollingAmount;

    @TableField(value = "baccarat_rolling_amount")
    private Float baccaratRollingAmount;

    @TableField(value = "slot_bet_count")
    private Integer slotBetCount;

    @TableField(value = "baccarat_bet_count")
    private Integer baccaratBetCount;

    @TableField(value = "point_rate")
    private Float pointRate;

    @TableField(value = "store_seq")
    private String storeSeq;

    @TableField(value = "distributor_seq")
    private String distributorSeq;

    @TableField(value = "headquarter_seq")
    private String headquarterSeq;

    @TableField(value = "slot_store_rolling_amount")
    private Float slotStoreRollingAmount;

    @TableField(value = "baccarat_store_rolling_amount")
    private Float baccaratStoreRollingAmount;

    @TableField(value = "slot_distributor_rolling_amount")
    private Float slotDistributorRollingAmount;

    @TableField(value = "baccarat_distributor_rolling_amount")
    private Float baccaratDistributorRollingAmount;

    @TableField(value = "slot_headquarter_rolling_amount")
    private Float slotHeadquarterRollingAmount;

    @TableField(value = "baccarat_headquarter_rolling_amount")
    private Float baccaratHeadquarterRollingAmount;

}
