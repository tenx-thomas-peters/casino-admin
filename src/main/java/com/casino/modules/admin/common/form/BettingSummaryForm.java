package com.casino.modules.admin.common.form;

import lombok.Data;

import java.util.Date;

@Data
public class BettingSummaryForm {
    private static final Integer userTypeMember = 0;
    private static final Integer userTypeStore = 1;
    private static final Integer userTypeDistributor = 2;
    private static final Integer userTypeSubHeadquarter = 3;

    private String seq;
    private Date checkTime;
    private Integer userType;
    private String fromProcessTime;
    private String toProcessTime;
    private String memberSeq;
    private String memberID;
    private String memberNickname;
    private Float memberSlotRate;
    private Float memberBaccaratRate;
    private String playingGame;
    private Integer type;
    private Float bettingAmount;
    private Float winningAmount;
    private Integer betCount;
    private Float pointRate;

    private String storeSeq;
    private String storeID;
    private Float storeSlotRate;
    private Float storeBaccaratRate;
    private Float storeRollingAmount;

    private String distributorSeq;
    private String distributorID;
    private Float distributorSlotRate;
    private Float distributorBaccaratRate;
    private Float distributorRollingAmount;

    private String subHeadquarterSeq;
    private String subHeadquarterID;
    private Float subHeadquarterSlotRate;
    private Float subHeadquarterBaccaratRate;
    private Float subHeadquarterRollingAmount;

    private String searchField;
    private String searchValue;
}
