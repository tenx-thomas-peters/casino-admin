package com.casino.modules.admin.common.form;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class MemberForm {
    private static final Integer operationTypeDeposit = 0;
    private static final Integer operationTypeWithdrawal = 1;
    private static final Integer operationTypeTransferIn = 2;
    private static final Integer operationTypeTransferOut = 3;
    private static final Integer isMoney = 0;
    private static final Integer isPoint = 1;
    private static final Integer userTypeMember = 0;
    private static final Integer userTypeStore = 1;
    private static final Integer userTypeDistributor = 2;
    private static final Integer userTypeSubHeadquarter = 3;

    private String seq;
    private String id;
    private String password;
    private String nickname;
    private String color;
    private String name;
    private Integer userType;
    private String storeSeq;
    private String storeID;
    private String storeNickname;
    private String storeLevel;
    private String distributorSeq;
    private String distributorID;
    private String distributorNickname;
    private String subHeadquarterSeq;
    private String subHeadquarterID;
    private String subHeadquarterNickname;
    private Float moneyAmount;
    private Float mileageAmount;
    private String bankName;
    private String accountHolder;
    private String bankNumber;
    private String exchangePassword;
    private Date registerDate;
    private String signupIP;
    private String siteDomain;
    private String siteName;
    private String levelSeq;
    private List<String> levels;
    private String level;
    private String levelName;
    private List<String> state;
    private String status;
    private String emailAddress;
    private String phoneNumber;
    private Float memberRevenue;
    private String recommender;
    private String recommendedReason;
    private Integer bettingPossible;
    private Integer recommendable;
    private String memo;
    private List<Map<String, String>> memoList;
    private Float slotRate;
    private Float baccaratRate;

    private String site;
    private Float betAmount;
    private Float winningAmount;
    private Float lostAmount;
    private Float batRolling;
    private Float deposit;
    private Integer depositCount;
    private Float adminDeposit;
    private Float withdrawal;
    private Integer withdrawalCount;
    private Float progressBet;
    private Date recentDate;
    private String recentDomain;
    private String connectionIP;
    private String connectionDomain;
    private Integer gameType;

    private String searchField;
    private String searchValue;

    private Integer distributorCount;
    private Integer storeCount;
    private Integer memberCount;
    private Integer depositMemberCount;
    private Float depositMemberAmount;
    
    private String fromApplicationTime;
    private String toApplicationTime;
    
    private String partnerId;
    private String partnerNickname;
    private String partnerLevel;
}
