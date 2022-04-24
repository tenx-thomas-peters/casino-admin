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

            System.out.println("saveBettingSummary temp_item-----------");
            System.out.println(temp_item.getUser());

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

                    System.out.println("member===========================");
                    System.out.println(member);
                    System.out.println(member.getStoreSeq());
                    //-------- get store
                    if(member.getStoreSeq() !=null && !member.getStoreSeq().equals("")){
                        System.out.println("store_member");

                        Member store_member = memberService.getById(member.getStoreSeq());
                        System.out.println(store_member);
                        slot_store_rolling_amount = this.calulateRate(slotBettingAmount, store_member.getSlotRate());
                        baccarat_store_rolling_amount = this.calulateRate(baccaratBettingAmount, store_member.getBaccaratRate());



                        System.out.println(store_member.getBaccaratRate());
                        System.out.println(baccaratBettingAmount);
                        System.out.println(baccarat_store_rolling_amount);

                        float store_variableAmount = slot_store_rolling_amount + baccarat_store_rolling_amount;
                        memberService.updatePartnerMemberHoldingMoney(
                                store_member.getSeq(),
                                store_member.getMoneyAmount(),
                                0f,
                                store_variableAmount,
                                Math.abs(store_variableAmount),
                                store_member.getMoneyAmount() + store_variableAmount,
                                0,
                                0,
                                CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
                                2,
                                "store ",
                                0
                        );
                    }

                    //-------- get distributor
                    if(member.getDistributorSeq() !=null && !member.getDistributorSeq().equals("")){
                        Member distributor_member = memberService.getById(member.getDistributorSeq());
                        slot_distributor_rate_amount = this.calulateRate(slotBettingAmount, distributor_member.getSlotRate());
                        baccarat_distributor_rate_amount =  this.calulateRate(baccaratBettingAmount, distributor_member.getBaccaratRate());

                        slot_distributor_rolling_amount = slot_distributor_rate_amount - slot_store_rolling_amount;
                        baccarat_distributor_rolling_amount = baccarat_distributor_rate_amount - baccarat_store_rolling_amount;

                        float distributor_variableAmount = slot_distributor_rolling_amount + baccarat_distributor_rolling_amount;
                        memberService.updatePartnerMemberHoldingMoney(
                                distributor_member.getSeq(),
                                distributor_member.getMoneyAmount(),
                                0f,
                                distributor_variableAmount,
                                Math.abs(distributor_variableAmount),
                                distributor_member.getMoneyAmount() + distributor_variableAmount,
                                0,
                                0,
                                CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
                                2,
                                "partner",
                                0
                        );
                    }

                    //-------- get headquarter
                    if(member.getSubHeadquarterSeq() !=null && !member.getSubHeadquarterSeq().equals("")){
                        Member headquarter_member = memberService.getById(member.getSubHeadquarterSeq());
                        slot_headquarter_rolling_amount = this.calulateRate(slotBettingAmount, headquarter_member.getSlotRate()) - slot_distributor_rate_amount;
                        baccarat_headquarter_rolling_amount = this.calulateRate(baccaratBettingAmount, headquarter_member.getBaccaratRate()) - baccarat_distributor_rate_amount;

                        float headquarter_variableAmount = slot_headquarter_rolling_amount + baccarat_headquarter_rolling_amount;
                        memberService.updatePartnerMemberHoldingMoney(
                                headquarter_member.getSeq(),
                                headquarter_member.getMoneyAmount(),
                                0f,
                                headquarter_variableAmount,
                                Math.abs(headquarter_variableAmount),
                                headquarter_member.getMoneyAmount() + headquarter_variableAmount,
                                0,
                                0,
                                CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
                                2,
                                "Headquarter",
                                0
                        );
                    }

                    //-------- get member
                    float variableAmount = (slotWinningAmount + baccaratWinningAmount) - (slotBettingAmount+baccaratBettingAmount);

                    // set Reason of money transfer--------------------------------------- <
                    Integer reasonType = 0;

                    QueryWrapper<Dict> qwe = new QueryWrapper<>();
                    qwe.eq("dict_key", CommonConstant.DICT_KEY_MONEY_REASON);

                    if((slotWinningAmount + baccaratWinningAmount) < (slotBettingAmount + baccaratBettingAmount)){
                        qwe.eq("dict_value", CommonConstant.MONEY_REASON_TRANSFER);
                        reasonType = CommonConstant.MONEY_REASON_TRANSFER;
                    }
                    else{
                        qwe.eq("dict_value", CommonConstant.MONEY_REASON_TRANSFER_WINNING);
                        reasonType = CommonConstant.MONEY_REASON_TRANSFER_WINNING;
                    }

                    List<Dict> reasonList = dictService.list(qwe);
                    List<String> params = new ArrayList<String>();
                    params.add(String.valueOf(variableAmount));
                    String reason = messageSource.getMessage(reasonList.get(0).getStrValue(), params.toArray(), Locale.ENGLISH);
                    // set Reason of money transfer--------------------------------------- />

                    memberService.updateMemberHoldingMoney(
                            member.getSeq(),
                            member.getCasinoMoney(),
                            0f,
                            variableAmount,
                            Math.abs(variableAmount),
                            member.getCasinoMoney() + variableAmount,
                            0,
                            0,
                            CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
                            reasonType,
                            reason,
                            0
                    );


                    // save betting summary-----------------------------------------------------------------------------
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
                    // save betting summary-----------------------------------------------------------------------------

                    // total amount of member

//                    moneyHistory.setChargeCount(moneyHistory.getChargeCount() + 1);

                    bettingSummaryList.add(bettingSummary);
                }
            }
        }
        //sort out the logic for betting summary ----------------------------------------------------------------------- />

        System.out.println("betting summary -----------------------------------------------------------------------");
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
