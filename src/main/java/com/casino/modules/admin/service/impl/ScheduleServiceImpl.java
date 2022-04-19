package com.casino.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.modules.admin.common.entity.BettingSummary;
import com.casino.modules.admin.common.entity.Dict;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.common.entity.MoneyHistory;
import com.casino.modules.admin.common.form.BettingLogDetailGame;
import com.casino.modules.admin.common.form.BettingLogForm;
import com.casino.modules.admin.mapper.ScheduleMapper;
import com.casino.modules.admin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, BettingSummary> implements IScheduleService {
    @Autowired
    private IMemberService memberService;

    @Autowired
    private IBettingSummaryService bettingSummaryService;

    @Autowired
    private IMoneyHistoryService moneyHistoryService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean saveBettingSummary(List<BettingLogForm> bettingLogList) {
        boolean result = false;

        /* Thomas Peters 2022.04.03
        ----------------------------------------------------------------------------- <
         * sort out the logic for betting summary
         */

        //Convert date form from 2022-04-03T10:08:000000Z to 2022-04-03 10:08:000000 ----------------- <
        SimpleDateFormat destination_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat first_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
        Date origin_date = null;
        try {
            origin_date = first_format.parse(bettingLogList.get(0).getProcessedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String checktime = destination_format.format(origin_date);
        //----------------------------------------------------------------------------------------------/>


        List<BettingSummary> bettingSummaryList = new ArrayList<>();
        List<String> member_username_list = new ArrayList<>();
        List<String> game_detail_list = new ArrayList<>();


        //get username list in betting log -------------------------------
        for (BettingLogForm temp_item : bettingLogList) {
            if(!member_username_list.contains(temp_item.getUser().getUsername()))
                member_username_list.add(temp_item.getUser().getUsername());

        }

        //get game list in betting log -------------------------------
        for (BettingLogForm temp_item : bettingLogList) {
            if(!game_detail_list.contains(temp_item.getDetails().getGame().getId()))
                game_detail_list.add(temp_item.getDetails().getGame().getId());
        }


        for(String member_username : member_username_list){

            QueryWrapper<Member> qw = new QueryWrapper<>();
            qw.eq("id", member_username);
            Member member = memberService.getOne(qw);

            for(String game_detail_id : game_detail_list){

                boolean status_play = false;
                BettingSummary bettingSummary = new BettingSummary();
                bettingSummary.setSeq(UUIDGenerator.generate());
                float slotBettingAmount = 0;
                float baccaratBettingAmount = 0;
                float slotWinningAmount = 0;
                float baccaratWinningAmount = 0;
                float slotLostAmount = 0;
                float baccaratLostAmount = 0;
                Integer slotBetCount  = 0;
                Integer baccaratBetCount  = 0;


                String playing_game = "";
                Integer type = 0;

                for (BettingLogForm item : bettingLogList) {

                    if(member_username.equals(item.getUser().getUsername())){ // filter by username
                        if(game_detail_id.equals(item.getDetails().getGame().getId())){ // filter by game id
                            status_play = true;

                            if(item.getDetails().getGame().getType().equals("slots")){ // separate game type
                                if(item.getType().equals("bet")){
                                    slotBettingAmount += Math.abs( item.getAmount() );
                                    slotBetCount++;
                                }
                                else{
                                    slotWinningAmount += item.getAmount();
                                }
                            }
                            else{
                                if(item.getType().equals("bet")){
                                    baccaratBettingAmount += Math.abs( item.getAmount() );
                                    baccaratBetCount++;
                                }
                                else{
                                    baccaratWinningAmount += item.getAmount();
                                }
                            }
                            playing_game = item.getDetails().getGame().getTitle();
                        }
                    }
                }

                if(status_play){
                    if(slotWinningAmount < slotBettingAmount)
                        slotLostAmount = slotBettingAmount - slotWinningAmount;

                    if(baccaratWinningAmount < baccaratBettingAmount)
                        baccaratLostAmount = baccaratBettingAmount - baccaratWinningAmount;

                    float slot_store_rolling_amount = 0;
                    float baccarat_store_rolling_amount = 0;
                    float slot_distributor_rolling_amount = 0;
                    float baccarat_distributor_rolling_amount = 0;
                    float slot_headquarter_rolling_amount = 0;
                    float baccarat_headquarter_rolling_amount = 0;

                    float slot_distributor_rate_amount = 0;
                    float baccarat_distributor_rate_amount = 0;
                    float slot_headquarter_rate_amount = 0;
                    float baccarat_headquarter_rate_amount = 0;

                    if(member.getStoreSeq() !=null && member.getStoreSeq().equals("")){
                        Member store_member = memberService.getById(member.getStoreSeq());
                        slot_store_rolling_amount = this.calulateRate(slotBettingAmount, store_member.getSlotRate());
                        baccarat_store_rolling_amount = this.calulateRate(baccaratBettingAmount, store_member.getBaccaratRate());
                    }

                    if(member.getDistributorSeq() !=null && member.getDistributorSeq().equals("")){
                        Member distributor_member = memberService.getById(member.getDistributorSeq());
                        slot_distributor_rate_amount = this.calulateRate(slotBettingAmount, distributor_member.getSlotRate());
                        baccarat_distributor_rate_amount =  this.calulateRate(baccaratBettingAmount, distributor_member.getBaccaratRate());
                    }

                    if(member.getSubHeadquarterSeq() !=null && member.getSubHeadquarterSeq().equals("")){
                        Member headquarter_member = memberService.getById(member.getSubHeadquarterSeq());
                        slot_headquarter_rate_amount = this.calulateRate(slotBettingAmount, headquarter_member.getSlotRate());
                        baccarat_headquarter_rate_amount = this.calulateRate(baccaratBettingAmount, headquarter_member.getBaccaratRate());
                    }

                    if(slot_headquarter_rate_amount > 0){
                        slot_headquarter_rolling_amount = slot_headquarter_rate_amount - slot_distributor_rate_amount;
                        baccarat_headquarter_rolling_amount = baccarat_headquarter_rate_amount - baccarat_distributor_rate_amount;
                    }

                    if (slot_distributor_rate_amount > 0) {
                        slot_distributor_rolling_amount = slot_distributor_rate_amount - slot_store_rolling_amount;
                        baccarat_distributor_rolling_amount = baccarat_distributor_rate_amount - baccarat_store_rolling_amount;
                    }


                    bettingSummary.setSlotBettingAmount(slotBettingAmount);
                    bettingSummary.setBaccaratBettingAmount(baccaratBettingAmount);
                    bettingSummary.setSlotWinningAmount(slotWinningAmount);
                    bettingSummary.setBaccaratWinningAmount(baccaratWinningAmount);
                    bettingSummary.setSlotLostAmount(slotLostAmount);
                    bettingSummary.setBaccaratLostAmount(baccaratLostAmount);
                    bettingSummary.setSlotBetCount(slotBetCount);
                    bettingSummary.setBaccaratBetCount(baccaratBetCount);
                    bettingSummary.setPlayingGame(playing_game);
                    bettingSummary.setCheckTime(checktime);
                    bettingSummary.setType(type);
                    bettingSummary.setMemberSeq(member.getSeq());
                    bettingSummary.setStoreSeq(member.getStoreSeq() != null ? member.getStoreSeq() : "");
                    bettingSummary.setDistributorSeq(member.getDistributorSeq() != null ? member.getDistributorSeq() : "");
                    bettingSummary.setHeadquarterSeq(member.getSubHeadquarterSeq() != null ? member.getSubHeadquarterSeq() : "");

                    bettingSummary.setSlotStoreRollingAmount(slot_store_rolling_amount);
                    bettingSummary.setBaccaratStoreRollingAmount(baccarat_store_rolling_amount);
                    bettingSummary.setSlotDistributorRollingAmount(slot_distributor_rolling_amount);
                    bettingSummary.setBaccaratDistributorRollingAmount(baccarat_distributor_rolling_amount);
                    bettingSummary.setSlotHeadquarterRollingAmount(slot_headquarter_rolling_amount);
                    bettingSummary.setBaccaratHeadquarterRollingAmount(baccarat_headquarter_rolling_amount);
                    // total amount of member

//                    moneyHistory.setChargeCount(moneyHistory.getChargeCount() + 1);

                    String memberSeq = member.getSeq();
                    float prevMoneyAmount = member.getMoneyAmount();
                    float prevMileageAmount = 0;
                    float variableAmount = (slotWinningAmount + baccaratWinningAmount) - (slotBettingAmount+baccaratBettingAmount);
                    float actualAmount = Math.abs(variableAmount);
                    float finalAmount = member.getMoneyAmount() + variableAmount;
                    Integer classification = 0;
                    Integer transactionClassification = 0;
                    Integer status = CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT;

                    // set Reason of money transfer--------------------------------------- <
                    Integer reasonType = 0;
                    QueryWrapper<Dict> qwe = new QueryWrapper<>();
                    qwe.eq("dict_key", CommonConstant.DICT_KEY_MONEY_REASON);

                    if((slotWinningAmount + baccaratWinningAmount) < (slotBettingAmount+baccaratBettingAmount)){
                        qwe.eq("dict_value", CommonConstant.MONEY_REASON_TRANSFER);
                        reasonType = CommonConstant.MONEY_REASON_TRANSFER;
                    }
                    else{
                        qwe.eq("dict_value", CommonConstant.MONEY_REASON_TRANSFER_WINNING);
                        reasonType = CommonConstant.MONEY_REASON_TRANSFER_WINNING;
                    }

                    String reasonStrKey = "";
                    List<Dict> reasonList = dictService.list(qwe);
                    reasonStrKey = reasonList.get(0).getStrValue();
                    List<String> params = new ArrayList<String>();
                    params.add(String.valueOf(variableAmount));

                    String reason = messageSource.getMessage(reasonStrKey, params.toArray(), Locale.ENGLISH);
                    // set Reason of money transfer--------------------------------------- />

                    Integer chargeCount = 0;
                    if (memberService.updateMemberHoldingMoney(
                            memberSeq,
                            prevMoneyAmount,
                            prevMileageAmount,
                            variableAmount,
                            actualAmount,
                            finalAmount,
                            classification,
                            transactionClassification,
                            status,
                            reasonType,
                            reason,
                            chargeCount
                    )){
                        result = true;

                    } else {
                        result = false;
                    }

                    bettingSummaryList.add(bettingSummary);
                }
            }
        }
        //sort out the logic for betting summary ----------------------------------------------------------------------- />

        System.out.println("bettingSummaryList");
        System.out.println(bettingSummaryList);
        if (bettingSummaryService.saveBatch(bettingSummaryList)) {
            result = true;
        }

        return result;
    }

    public BettingLogForm getBetWinRelation(List<BettingLogForm> bettingLogList, String id) {
        BettingLogForm bettingLogForm = null;

        for (BettingLogForm item : bettingLogList) {
            if (item.getRefererId() != null && item.getRefererId().equals(id)) {
                bettingLogForm = item;
                break;
            }
        }
        return bettingLogForm;
    }

    public float calulateRate(float amount, float rate){
        return amount * rate /100;
    }
}
