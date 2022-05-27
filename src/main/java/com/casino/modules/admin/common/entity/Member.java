package com.casino.modules.admin.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@TableName("member")
@Data
public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "seq")
    private String seq;

    @TableField(value = "id")
    private String id;

    @TableField(value = "password")
    private String password;

    @TableField(value = "name")
    private String name;

    @TableField(exist = false)
    private String username;

    @TableField(value = "nickname")
    private String nickname;

    @TableField(value = "color")
    private String color;

    @TableField(value = "user_type")
    private Integer userType;

    @TableField(value = "store_seq")
    private String storeSeq;

    @TableField(value = "distributor_seq")
    private String distributorSeq;

    @TableField(value = "sub_headquarter_seq")
    private String subHeadquarterSeq;

    @TableField(value = "money_amount")
    private Float moneyAmount;

    @TableField(value = "casino_money")
    private Float casinoMoney;

    @TableField(value = "mileage_amount")
    private Float mileageAmount;

    @TableField(value = "bank_name")
    private String bankName;

    @TableField(value = "account_holder")
    private String accountHolder;

    @TableField(value = "bank_number")
    private String bankNumber;

    @TableField(value = "exchange_password")
    private String exchangePassword;

    @TableField(value = "register_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date registerDate;

    @TableField(value = "signup_ip")
    private String signupIp;

    @TableField(value = "site_domain")
    private String siteDomain;
    
    @TableField(value = "site_name")
    private String siteName;

    @TableField(value = "level_seq")
    private String levelSeq;

    @TableField(value = "status")
    private Integer status;

    @TableField(value = "email_address")
    private String emailAddress;

    @TableField(value = "phone_number")
    private String phoneNumber;

    @TableField(value = "member_revenue")
    private Float memberRevenue;

    @TableField(value = "recommender")
    private String recommender;

    @TableField(value = "recommended_reason")
    private String recommendedReason;

    @TableField(value = "betting_possible")
    private Integer bettingPossible;

    @TableField(value = "recommendable")
    private Float recommendable;

    @TableField(value = "memo")
    private String memo;

    @TableField(value = "slot_rate")
    private Float slotRate;

    @TableField(value = "baccarat_rate")
    private Float baccaratRate;

    @TableField(value = "create_by")
    private String createBy;

    @TableField(value = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @TableField(value = "update_by")
    private String updateBy;

    @TableField(value = "token")
    private String token;

    @TableField(value = "login_status")
    private Integer loginStatus;

    @TableField(value = "update_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    @TableField(exist = false)
    private Member headQuarter;

    @TableField(exist = false)
    private String changeMoney;

    @TableField(exist = false)
    private String referralCode;

    @TableField(exist = false)
    private String withdrawPassword;

    @TableField(exist = false)
    private Member distributor;

    @TableField(exist = false)
    private Member store;
    
    @TableField(exist = false)
    private String level;

    // for game server api
    @TableField(exist = false)
    private Float amount;
}
