package com.casino.modules.admin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.common.form.DistributorForm;
import com.casino.modules.admin.common.form.HeadquarterForm;
import com.casino.modules.admin.common.form.MemberForm;
import com.casino.modules.admin.common.form.StoreForm;

public interface IMemberService extends IService<Member> {

    IPage<MemberForm> getMemberList(Page<MemberForm> page, MemberForm memberForm, String column, Integer order);

    MemberForm getMemberBySeq(MemberForm memberForm);

    IPage<HeadquarterForm> findHeadquarterList(Page<HeadquarterForm> page, HeadquarterForm headquarter, String column, Integer order);

    IPage<DistributorForm> findDistributorList(Page<DistributorForm> page, DistributorForm distributor, String column, Integer order);

    Member getRecipient(String levelSeq, String userType, String siteSeq);

    IPage<StoreForm> findStoreList(Page<StoreForm> page, StoreForm store, String column, Integer order);

    List<Map<String, String>> getSiteList();

    List<DistributorForm> getDistributorListModal(String seq, DistributorForm distributor);

    List<StoreForm> getStoreListModal(StoreForm store);

    List<MemberForm> getMemberListModal(MemberForm member);

    boolean updateMemberHoldingMoney(
            String memberSeq,
            Float prevMoneyAmount,
            Float prevMileageAmount,
            Float variableAmount,
            Float actualAmount,
            Float finalAmount,
            Integer classification,
            Integer transactionClassification,
            Integer status,
            Integer reasonType,
            String reason,
            Integer chargeCount);

    boolean updatePartnerMemberHoldingMoney(
            String memberSeq,
            Float prevMoneyAmount,
            Float prevMileageAmount,
            Float variableAmount,
            Float actualAmount,
            Float finalAmount,
            Integer classification,
            Integer transactionClassification,
            Integer status,
            Integer reasonType,
            String reason,
            Integer chargeCount);

    boolean stopMember(List<String> memberSeqList);

    boolean updateMember(Member member);

}