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
                float bettingAmount = 0;
                float winningAmount = 0;
                float lostAmount = 0;
                Integer betCount  = 0;
                String playing_game = "";
                Integer type = 0;

                for (BettingLogForm item : bettingLogList) {

                    if(member_username.equals(item.getUser().getUsername())){ // filter by username
                        if(game_detail_id.equals(item.getDetails().getGame().getId())){ // filter by game id
                            status_play = true;

                            type = item.getDetails().getGame().getType().equals("slots")?0:1;
                            playing_game = item.getDetails().getGame().getTitle();

                            if(item.getType().equals("bet")){
                                bettingAmount += Math.abs( item.getAmount() );
                                betCount++;
                            }
                            else{
                                winningAmount += item.getAmount();
                            }
                        }
                    }
                }

                if(status_play){
                    if(winningAmount < bettingAmount)
                        lostAmount = bettingAmount - winningAmount;

                    bettingSummary.setBettingAmount(bettingAmount);
                    bettingSummary.setWinningAmount(winningAmount);
                    bettingSummary.setLostAmount(lostAmount);
                    bettingSummary.setBetCount(betCount);
                    bettingSummary.setPlayingGame(playing_game);
                    bettingSummary.setCheckTime(checktime);
                    bettingSummary.setType(type);
                    bettingSummary.setMemberSeq(member != null ? member.getSeq() : "");
                    bettingSummary.setStoreSeq(member != null ? member.getStoreSeq() : "");
                    bettingSummary.setDistributorSeq(member != null ? member.getDistributorSeq() : "");
                    bettingSummary.setHeadquarterSeq(member != null ? member.getSubHeadquarterSeq() : "");
                    // total amount of member

//                    moneyHistory.setChargeCount(moneyHistory.getChargeCount() + 1);

                    String memberSeq = member.getSeq();
                    float prevMoneyAmount = member.getMoneyAmount();
                    float prevMileageAmount = 0;
                    float variableAmount = winningAmount - bettingAmount;
                    float actualAmount = Math.abs(bettingAmount - winningAmount);
                    float finalAmount = member.getMoneyAmount() - bettingAmount + winningAmount;
                    Integer classification = 0;
                    Integer transactionClassification = 0;
                    Integer status = CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT;

                    // set Reason of money transfer--------------------------------------- <
                    Integer reasonType = 0;
                    QueryWrapper<Dict> qwe = new QueryWrapper<>();
                    qwe.eq("dict_key", CommonConstant.DICT_KEY_MONEY_REASON);

                    if(winningAmount < bettingAmount){
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
                    params.add(String.valueOf(winningAmount - bettingAmount));

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
}
