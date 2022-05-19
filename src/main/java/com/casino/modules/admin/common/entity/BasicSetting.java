package com.casino.modules.admin.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("basic_setting")
@Data
public class BasicSetting implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "seq")
    private String seq;

    @TableField(value = "jackpot_amount")
    private Float jackpotAmount;

    @TableField(value = "house_money")
    private Float houseMoney;

    @TableField(value = "weekly_withdrawal_ranking_top_1_id")
    private String weeklyWithdrawalRankingTop1Id;

    @TableField(value = "weekly_withdrawal_ranking_top_1_money")
    private Float weeklyWithdrawalRankingTop1Money;

    @TableField(value = "baccarat_check")
    private String baccaratCheck;
    
    @TableField(value = "slot_check")
    private String slotCheck;

    @TableField(value = "service_inspection")
    private String serviceInspection;

    @TableField(value = "inspection_comment")
    private String inspectionComment;

    @TableField(value = "admin_accessible_ip")
    private String adminAccessibleIp;

    @TableField(value = "minimun_amount_of_currency_exchange")
    private Integer minimunAmountOfCurrencyExchange;

    @TableField(value = "maximum_amount_of_exchange_at_one_time")
    private Integer maximumAmountOfExchangeAtOneTime;

    @TableField(value = "maximum_exchange_rate_per_day")
    private Integer maximumExchangeRatePerDay;

    @TableField(value = "bank_account")
    private String bankAccount;

    @TableField(value = "recommender_at_the_time_of_member_registration")
    private String recommenderAtTheTimeOfMemberRegistration;

    @TableField(value = "member_line_advertisement")
    private String memberLineAdvertisement;

    @TableField(value = "single_line_notice")
    private String singleLineNotice;

    @TableField(value = "signup_message_title")
    private String signupMessageTitle;

    @TableField(value = "signup_message_content")
    private String signupMessageContent;

    @TableField(value = "cc_level_1_down_account")
    private String ccLevel1DownAccount;

    @TableField(value = "cc_level_2_up_account")
    private String ccLevel2UpAccount;

    @TableField(value = "cc_level_3_up_account")
    private String ccLevel3UpAccount;

    @TableField(value = "cc_level_4_5_up_account")
    private String ccLevel45UpAccount;

    @TableField(value = "c_request_processing_completed")
    private String cRequestProcessingCompleted;

    @TableField(value = "cc_re_login_rating_up")
    private String ccReLoginRatingUp;

    @TableField(value = "cc_recommendation_from_acquaintances")
    private String ccRecommendationFromAcquaintances;

    @TableField(value = "cc_ladder_betting_x")
    private String ccLadderBettingX;

    @TableField(value = "cc_game_error_report")
    private String ccGameErrorReport;

    @TableField(value = "cc_repayment_information")
    private String ccRepaymentInformation;

    @TableField(value = "admin_memo")
    private String adminMemo;

}
