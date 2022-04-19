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
    private Float slotBettingAmount;
    private Float baccaratBettingAmount;
    private Float slotWinningAmount;
    private Float baccaratWinningAmount;
    private Integer slotBetCount;
    private Integer baccaratBetCount;
    private Float pointRate;

    private String storeSeq;
    private String storeID;
    private Float storeSlotRate;
    private Float storeBaccaratRate;
    private Float slotStoreRollingAmount;
    private Float baccaratStoreRollingAmount;

    private String distributorSeq;
    private String distributorID;
    private Float distributorSlotRate;
    private Float distributorBaccaratRate;
    private Float slotDistributorRollingAmount;
    private Float baccaratDistributorRollingAmount;

    private String subHeadquarterSeq;
    private String subHeadquarterID;
    private Float subHeadquarterSlotRate;
    private Float subHeadquarterBaccaratRate;
    private Float slotHeadquarterRollingAmount;
    private Float baccaratHeadquarterRollingAmount;

    private String searchField;
    private String searchValue;
}
