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

        System.out.println("IScheduleService==saveBettingSummary==");
        System.out.println("*************** param info ***************");
        if(bettingLogList!=null){
            System.out.println("*** count : "+bettingLogList.size());
            for(BettingLogForm item:bettingLogList){
                System.out.println("\t********** batting log info **********");
                System.out.println("\t*** id : "+item.getId());
                System.out.println("\t*** Type : "+item.getType());
                System.out.println("\t*** Amount : "+item.getAmount());
                System.out.println("\t*** Before : "+item.getBefore());
                System.out.println("\t*** UserId : "+item.getUserId());
                System.out.println("\t*** Status : "+item.getStatus());
                System.out.println("\t*** Details : "+item.getDetails());
                System.out.println("\t*** ProcessedAt : "+item.getProcessedAt());
                System.out.println("\t*** User : "+item.getUser());
                System.out.println("\t*******************************************");
            }
        }else{
            System.out.println("*** result data is null !");
        }
        System.out.println("*******************************************************");

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

                BettingSummary bettingSummary = new BettingSummary();
                TotalBettingAmount totalBettingAmount = getTotalBettingAmount(bettingLogList, member_username, game_detail_id);

                if(totalBettingAmount.status_play){

                    totalBettingAmount.setLostAmount();

                    float slot_store_rolling_amount = 0;
                    float baccarat_store_rolling_amount = 0;
                    float slot_distributor_rolling_amount = 0;
                    float baccarat_distributor_rolling_amount = 0;
                    float slot_headquarter_rolling_amount = 0;
                    float baccarat_headquarter_rolling_amount = 0;

                    float slot_distributor_rate_amount = 0;
                    float baccarat_distributor_rate_amount = 0;

                    //-------- get store
                    if(member.getStoreSeq() !=null && !member.getStoreSeq().equals("")){

                        Member store_member = memberService.getById(member.getStoreSeq());
                        System.out.println("IScheduleService==saveBettingSummary========store_member:" + store_member);

                        slot_store_rolling_amount = this.calulateRate(totalBettingAmount.slotBettingAmount, store_member.getSlotRate());
                        baccarat_store_rolling_amount = this.calulateRate(totalBettingAmount.baccaratBettingAmount, store_member.getBaccaratRate());

                        float store_variableAmount = slot_store_rolling_amount + baccarat_store_rolling_amount;

                        System.out.println("IScheduleService==saveBettingSummary========store rolling data" +
                                "seq:"+store_member.getSeq() +
                                "prevMoneyAmount:" + store_member.getMoneyAmount() +
                                "prevMoneyAmount:" + store_member.getMoneyAmount() +
                                "variableAmount:" + store_variableAmount +
                                "actualAmount:" + Math.abs(store_variableAmount) +
                                "finalAmount:" + (store_member.getMoneyAmount() + store_variableAmount));

                        if(memberService.updatePartnerMemberHoldingMoney(
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
                                "store",
                                0
                        )){
                            System.out.println("\tIScheduleService==saveBettingSummary======== store rolling data save success");
                        }else{
                            System.out.println("\tIScheduleService==saveBettingSummary======== store rolling data save fail ");
                        }
                    }

                    //-------- get distributor
                    if(member.getDistributorSeq() !=null && !member.getDistributorSeq().equals("")){
                        Member distributor_member = memberService.getById(member.getDistributorSeq());
                        System.out.println("IScheduleService==saveBettingSummary========distributor_member:" + distributor_member);

                        slot_distributor_rate_amount = this.calulateRate(totalBettingAmount.slotBettingAmount, distributor_member.getSlotRate());
                        baccarat_distributor_rate_amount =  this.calulateRate(totalBettingAmount.baccaratBettingAmount, distributor_member.getBaccaratRate());

                        slot_distributor_rolling_amount = slot_distributor_rate_amount - slot_store_rolling_amount;
                        baccarat_distributor_rolling_amount = baccarat_distributor_rate_amount - baccarat_store_rolling_amount;

                        float distributor_variableAmount = slot_distributor_rolling_amount + baccarat_distributor_rolling_amount;

                        System.out.println("IScheduleService==saveBettingSummary========distributor rolling data\t" +
                                "seq:"+distributor_member.getSeq() +
                                "prevMoneyAmount:" + distributor_member.getMoneyAmount() +
                                "prevMoneyAmount:" + distributor_member.getMoneyAmount() +
                                "variableAmount:" + distributor_variableAmount +
                                "actualAmount:" + Math.abs(distributor_variableAmount) +
                                "finalAmount:" + (distributor_member.getMoneyAmount() + distributor_variableAmount) );

                        if(memberService.updatePartnerMemberHoldingMoney(
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
                            )){
                                System.out.println("\tIScheduleService==saveBettingSummary======== distributor rolling data save success");
                            }else{
                                System.out.println("\tIScheduleService==saveBettingSummary======== distributor rolling data save fail ");
                            }
                    }

                    //-------- get headquarter
                    if(member.getSubHeadquarterSeq() !=null && !member.getSubHeadquarterSeq().equals("")){
                        Member headquarter_member = memberService.getById(member.getSubHeadquarterSeq());
                        System.out.println("IScheduleService==saveBettingSummary========headquarter_member:" + headquarter_member);

                        slot_headquarter_rolling_amount =
                                this.calulateRate(totalBettingAmount.slotBettingAmount, headquarter_member.getSlotRate()) - slot_distributor_rate_amount;
                        baccarat_headquarter_rolling_amount =
                                this.calulateRate(totalBettingAmount.baccaratBettingAmount, headquarter_member.getBaccaratRate()) - baccarat_distributor_rate_amount;

                        float headquarter_variableAmount = slot_headquarter_rolling_amount + baccarat_headquarter_rolling_amount;

                        System.out.println("IScheduleService==saveBettingSummary========distributor rolling data\t" +
                                "seq:"+headquarter_member.getSeq() +
                                "prevMoneyAmount:" + headquarter_member.getMoneyAmount() +
                                "prevMoneyAmount:" + headquarter_member.getMoneyAmount() +
                                "variableAmount:" + headquarter_variableAmount +
                                "actualAmount:" + Math.abs(headquarter_variableAmount) +
                                "finalAmount:" + (headquarter_member.getMoneyAmount() + headquarter_variableAmount) );

                        if(memberService.updatePartnerMemberHoldingMoney(
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
                        )){
                            System.out.println("\tIScheduleService==saveBettingSummary======== headquarter rolling data save success");
                        }else{
                            System.out.println("\tIScheduleService==saveBettingSummary======== headquarter rolling data save fail ");
                        }
                    }

                    //-------- get member
                    float variableAmount = totalBettingAmount.getVariableAmount();

                    // set Reason of money transfer--------------------------------------- <
                    Integer reasonType = 0;
                    QueryWrapper<Dict> qwe = new QueryWrapper<>();
                    qwe.eq("dict_key", CommonConstant.DICT_KEY_MONEY_REASON);

                    //  When the result of game is winning status
                    if(totalBettingAmount.isTotalWinning()){
                        qwe.eq("dict_value", CommonConstant.MONEY_REASON_TRANSFER_WINNING);
                        reasonType = CommonConstant.MONEY_REASON_TRANSFER_WINNING;
                    }
                    else{
                        qwe.eq("dict_value", CommonConstant.MONEY_REASON_TRANSFER);
                        reasonType = CommonConstant.MONEY_REASON_TRANSFER;
                    }

                    List<Dict> reasonList = dictService.list(qwe);
                    List<String> params = new ArrayList<String>();
                    params.add(String.valueOf(variableAmount));
                    String reason = messageSource.getMessage(reasonList.get(0).getStrValue(), params.toArray(), Locale.ENGLISH);
                    // set Reason of money transfer--------------------------------------- />


                    System.out.println("IScheduleService==saveBettingSummary========distributor rolling data\t" +
                            "seq:"+member.getSeq() +
                            "prevMoneyAmount:" + member.getCasinoMoney() +
                            "prevMileage: 0" +
                            "variableAmount:" + variableAmount +
                            "actualAmount:" + Math.abs(variableAmount) +
                            "finalAmount:" + (member.getCasinoMoney() + variableAmount) +
                            "reason:" + reason);

                    member.setCasinoMoney(member.getCasinoMoney() + variableAmount);

//                    Thomas 2022.04.27 add log money history of betting result
//                    if(memberService.updateMemberHoldingMoney(
//                            member.getSeq(),
//                            member.getCasinoMoney(),
//                            0f,
//                            variableAmount,
//                            Math.abs(variableAmount),
//                            member.getCasinoMoney() + variableAmount,
//                            0,
//                            0,
//                            CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
//                            reasonType,
//                            reason,
//                            0
//                    )){

                    if(memberService.updateById(member)){
                        System.out.println("\tIScheduleService==saveBettingSummary======== member rolling data save success");
                    }else{
                        System.out.println("\tIScheduleService==saveBettingSummary======== member rolling data save fail ");
                    }

                    // save betting summary----------------------------------------------------------------------------- <
                    bettingSummary.setSeq(UUIDGenerator.generate());
                    bettingSummary.setSlotBettingAmount(totalBettingAmount.slotBettingAmount);
                    bettingSummary.setBaccaratBettingAmount(totalBettingAmount.baccaratBettingAmount);
                    bettingSummary.setSlotWinningAmount(totalBettingAmount.slotWinningAmount);
                    bettingSummary.setBaccaratWinningAmount(totalBettingAmount.baccaratWinningAmount);
                    bettingSummary.setSlotLostAmount(totalBettingAmount.slotLostAmount);
                    bettingSummary.setBaccaratLostAmount(totalBettingAmount.baccaratLostAmount);
                    bettingSummary.setSlotBetCount(totalBettingAmount.slotBetCount);
                    bettingSummary.setBaccaratBetCount(totalBettingAmount.baccaratBetCount);
                    bettingSummary.setPlayingGame(totalBettingAmount.playing_game);
                    bettingSummary.setCheckTime(checktime);
                    bettingSummary.setType(0);
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
                    // save betting summary----------------------------------------------------------------------------- />

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
            System.out.println("IScheduleService==saveBettingSummary======== batting list data save success");
        }
        else {
            System.out.println("IScheduleService==saveBettingSummary======== batting list data save fail");
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

    /**
     *
     * @param bettingLogList
     * @param member_username
     * @param game_detail_id
     * @return
     */
    public TotalBettingAmount getTotalBettingAmount(List<BettingLogForm> bettingLogList, String member_username, String game_detail_id){

        System.out.println("IScheduleService==getTotalBettingAmount====> calculating...........");

        TotalBettingAmount totalBettingAmount = new TotalBettingAmount();
        for (BettingLogForm item : bettingLogList) {

            if(member_username.equals(item.getUser().getUsername())){ // filter by username
                if(game_detail_id.equals(item.getDetails().getGame().getId())){ // filter by game id
                    totalBettingAmount.status_play = true;

                    if(item.getDetails().getGame().getType().equals("slots")){ // separate game type
                        if(item.getType().equals("bet")){
                            totalBettingAmount.slotBettingAmount += Math.abs( item.getAmount() );
                            totalBettingAmount.slotBetCount++;
                        }
                        else{
                            totalBettingAmount.slotWinningAmount += item.getAmount();
                        }
                    }
                    else{
                        if(item.getType().equals("bet")){
                            totalBettingAmount.baccaratBettingAmount += Math.abs( item.getAmount() );
                            totalBettingAmount.baccaratBetCount++;
                        }
                        else{
                            totalBettingAmount.baccaratWinningAmount += item.getAmount();
                        }
                    }
                    totalBettingAmount.playing_game = item.getDetails().getGame().getTitle();
                }
            }
            System.out.println(
                    "slotBettingAmount :" + totalBettingAmount.slotBettingAmount +
                    " slotBetCount :" + totalBettingAmount.slotBetCount +
                    " slotWinningAmount :" + totalBettingAmount.slotWinningAmount +
                    " baccaratBettingAmount :" + totalBettingAmount.baccaratBettingAmount +
                    " baccaratBetCount :" + totalBettingAmount.baccaratBetCount +
                    " baccaratWinningAmount :" + totalBettingAmount.baccaratWinningAmount);

        }

        System.out.println("*************** Total Betting Amount calculating Result ***************");
        System.out.println(
                                "slotBettingAmount :" + totalBettingAmount.slotBettingAmount +
                                " slotBetCount :" + totalBettingAmount.slotBetCount +
                                " slotWinningAmount :" + totalBettingAmount.slotWinningAmount +
                                " baccaratBettingAmount :" + totalBettingAmount.baccaratBettingAmount +
                                " baccaratBetCount :" + totalBettingAmount.baccaratBetCount +
                                " baccaratWinningAmount :" + totalBettingAmount.baccaratWinningAmount);
        System.out.println("************************************************************************");

        return totalBettingAmount;
    }
}

class TotalBettingAmount{
    boolean status_play;
    float slotBettingAmount;
    float baccaratBettingAmount;
    float slotWinningAmount;
    float baccaratWinningAmount;
    float slotLostAmount;
    float baccaratLostAmount;
    Integer slotBetCount;
    Integer baccaratBetCount;
    String playing_game;

    public TotalBettingAmount() {
        this.status_play = false;
        this.slotBettingAmount = 0;
        this.baccaratBettingAmount = 0;
        this.slotWinningAmount = 0;
        this.baccaratWinningAmount = 0;
        this.slotLostAmount = 0;
        this.baccaratLostAmount = 0;
        this.slotBetCount = 0;
        this.baccaratBetCount = 0;
        this.playing_game = "";
    }

    public void setLostAmount() {
        if(this.slotWinningAmount < this.slotBettingAmount)
            this.slotLostAmount = this.slotBettingAmount - this.slotWinningAmount;

        if(this.baccaratWinningAmount < this.baccaratBettingAmount)
            this.baccaratLostAmount = this.baccaratBettingAmount - this.baccaratWinningAmount;
    }

    public float getVariableAmount(){
        return (slotWinningAmount + baccaratWinningAmount) - (slotBettingAmount+baccaratBettingAmount);
    }

    public boolean isTotalWinning(){
        if((this.slotWinningAmount + this.baccaratWinningAmount) > (this.slotBettingAmount + this.baccaratBettingAmount))
            return true;
        else
            return false;
    }
}
