package com.casino.modules.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.common.form.DistributorForm;
import com.casino.modules.admin.common.form.HeadquarterForm;

import com.casino.modules.admin.common.form.MemberForm;
import com.casino.modules.admin.common.form.StoreForm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface MemberMapper extends BaseMapper<Member> {
    IPage<MemberForm> getMemberList(Page<MemberForm> page,
                                    @Param("entity") MemberForm memberForm,
                                    @Param("column") String column,
                                    @Param("order") Integer order,
                                    @Param("MONEY_HISTORY_OPERATION_TYPE_DEPOSIT") Integer MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
                                    @Param("MONEY_HISTORY_STATUS_PARTNER_PAYMENT") Integer MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
                                    @Param("MONEY_HISTORY_STATUS_COMPLETE") Integer MONEY_HISTORY_STATUS_COMPLETE);

    MemberForm getMemberBySeq(@Param("entity") MemberForm memberForm,
                              @Param("MONEY_HISTORY_OPERATION_TYPE_DEPOSIT") Integer MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
                              @Param("MONEY_HISTORY_STATUS_PARTNER_PAYMENT") Integer MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
                              @Param("MONEY_HISTORY_STATUS_COMPLETE") Integer MONEY_HISTORY_STATUS_COMPLETE);

    IPage<HeadquarterForm> findHeadquarterList(Page<HeadquarterForm> page,
                                               @Param("headquarter") HeadquarterForm headquarter,
                                               @Param("column") String column,
                                               @Param("order") Integer order, 
                                               @Param("userTypeSubHeadquarter")Integer userTypeSubHeadquarter,
                                               @Param("userTypeDistributor")Integer userTypeDistributor, 
                                               @Param("userTypeStore")Integer userTypeStore, 
                                               @Param("userTypeNormal")Integer userTypeNormal, 
                                               @Param("moneyHistoryOperationTypeDeposit")Integer moneyHistoryOperationTypeDeposit, 
                                               @Param("moneyHistoryStatusPartnerPayment")Integer moneyHistoryStatusPartnerPayment,
                                               @Param("moneyHistoryOperationTypeWithdrawal")Integer moneyHistoryOperationTypeWithdrawal,
                                               @Param("moneyHistoryOperationTypeTransferIn")Integer moneyHistoryOperationTypeTransferIn,
                                               @Param("moneyHistoryStatusComplete")Integer moneyHistoryStatusComplete,
                                               @Param("selectType0") Integer selectType0,
                                               @Param("selectType1") Integer selectType1,
                                               @Param("selectType2") Integer selectType2
                                               );

    Member getRecipient(@Param("levelSeq") String levelSeq,
                        @Param("userType") String userType,
                        @Param("siteSeq") String siteSeq);

    IPage<StoreForm> findStoreList(Page<StoreForm> page,
                                   @Param("store") StoreForm store,
                                   @Param("column") String column,
                                   @Param("order") Integer order,
                                   @Param("userTypeSubHeadquarter")Integer userTypeSubHeadquarter,
                                   @Param("userTypeDistributor")Integer userTypeDistributor, 
                                   @Param("userTypeStore")Integer userTypeStore, 
                                   @Param("userTypeNormal")Integer userTypeNormal, 
                                   @Param("moneyHistoryOperationTypeDeposit")Integer moneyHistoryOperationTypeDeposit, 
                                   @Param("moneyHistoryStatusPartnerPayment")Integer moneyHistoryStatusPartnerPayment,
                                   @Param("moneyHistoryOperationTypeWithdrawal")Integer moneyHistoryOperationTypeWithdrawal,
                                   @Param("moneyHistoryOperationTypeTransferIn")Integer moneyHistoryOperationTypeTransferIn,
                                   @Param("moneyHistoryStatusComplete")Integer moneyHistoryStatusComplete,
                                   @Param("selectType0") Integer selectType0,
                                   @Param("selectType1") Integer selectType1,
                                   @Param("selectType2") Integer selectType2
                                   );

    IPage<DistributorForm> findDistributorList(Page<DistributorForm> page,
                                               @Param("distributor") DistributorForm headquarter,
                                               @Param("column") String column,
                                               @Param("order") Integer order, 
                                               @Param("userTypeSubHeadquarter")Integer userTypeSubHeadquarter,
                                               @Param("userTypeDistributor")Integer userTypeDistributor, 
                                               @Param("userTypeStore")Integer userTypeStore, 
                                               @Param("userTypeNormal")Integer userTypeNormal, 
                                               @Param("moneyHistoryOperationTypeDeposit")Integer moneyHistoryOperationTypeDeposit, 
                                               @Param("moneyHistoryStatusPartnerPayment")Integer moneyHistoryStatusPartnerPayment,
                                               @Param("moneyHistoryOperationTypeWithdrawal")Integer moneyHistoryOperationTypeWithdrawal,
                                               @Param("moneyHistoryOperationTypeTransferIn")Integer moneyHistoryOperationTypeTransferIn,
                                               @Param("moneyHistoryStatusComplete")Integer moneyHistoryStatusComplete,
                                               @Param("selectType0") Integer selectType0,
                                               @Param("selectType1") Integer selectType1,
                                               @Param("selectType2") Integer selectType2
                                               );

    List<Map<String, String>> getSiteList();
    
	List<DistributorForm> getDistributorListModal(
			@Param("seq")String seq,
            @Param("distributor")DistributorForm distributor,
			@Param("userTypeSubHeadquarter")Integer userTypeSubHeadquarter,
            @Param("userTypeDistributor")Integer userTypeDistributor, 
            @Param("userTypeStore")Integer userTypeStore, 
            @Param("userTypeNormal")Integer userTypeNormal, 
            @Param("moneyHistoryOperationTypeDeposit")Integer moneyHistoryOperationTypeDeposit, 
            @Param("moneyHistoryStatusPartnerPayment")Integer moneyHistoryStatusPartnerPayment,
            @Param("moneyHistoryOperationTypeTransferIn")Integer moneyHistoryOperationTypeTransferIn,
            @Param("moneyHistoryOperationTypeWithdrawal")Integer moneyHistoryOperationTypeWithdrawal,
            @Param("moneyHistoryStatusComplete")Integer moneyHistoryStatusComplete,
            @Param("isMoney")Integer isMoney);

	List<StoreForm> getStoreListModal(@Param("entity")StoreForm store,
			@Param("userTypeSubHeadquarter")Integer userTypeSubHeadquarter,
            @Param("userTypeDistributor")Integer userTypeDistributor, 
            @Param("userTypeStore")Integer userTypeStore, 
            @Param("userTypeNormal")Integer userTypeNormal, 
            @Param("moneyHistoryOperationTypeDeposit")Integer moneyHistoryOperationTypeDeposit, 
            @Param("moneyHistoryStatusPartnerPayment")Integer moneyHistoryStatusPartnerPayment,
            @Param("moneyHistoryOperationTypeTransferIn")Integer moneyHistoryOperationTypeTransferIn,
            @Param("moneyHistoryOperationTypeWithdrawal")Integer moneyHistoryOperationTypeWithdrawal,
            @Param("moneyHistoryStatusComplete")Integer moneyHistoryStatusComplete);

	List<MemberForm> getMemberListModal(@Param("entity")MemberForm member,
									    @Param("MONEY_HISTORY_OPERATION_TYPE_DEPOSIT") Integer MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
										@Param("MONEY_HISTORY_STATUS_PARTNER_PAYMENT") Integer MONEY_HISTORY_STATUS_PARTNER_PAYMENT);

}
