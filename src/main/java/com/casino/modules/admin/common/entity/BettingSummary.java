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

    @TableField(value = "member_seq")
    private String memberSeq;

    @TableField(value = "playing_game")
    private String playingGame;

    @TableField(value = "type")
    private Integer type;

    @TableField(value = "betting_amount")
    private Float bettingAmount;

    @TableField(value = "winning_amount")
    private Float winningAmount;

    @TableField(value = "lost_amount")
    private Float lostAmount;

    @TableField(value = "bat_rolling")
    private Float batRolling;

    @TableField(value = "bet_count")
    private Integer betCount;

    @TableField(value = "point_rate")
    private Float pointRate;

    @TableField(value = "store_seq")
    private String storeSeq;

    @TableField(value = "distributor_seq")
    private String distributorSeq;

    @TableField(value = "headquarter_seq")
    private String headquarterSeq;
}
