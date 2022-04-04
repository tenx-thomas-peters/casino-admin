package com.casino.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.common.utils.UUIDGenerator;
import com.casino.modules.admin.common.entity.BettingSummary;
import com.casino.modules.admin.common.entity.Member;
import com.casino.modules.admin.common.form.BettingLogForm;
import com.casino.modules.admin.mapper.ScheduleMapper;
import com.casino.modules.admin.service.IBettingSummaryService;
import com.casino.modules.admin.service.IMemberService;
import com.casino.modules.admin.service.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, BettingSummary> implements IScheduleService {
    @Autowired
    private IMemberService memberService;

    @Autowired
    private IBettingSummaryService bettingSummaryService;

    @Override
    public boolean saveBettingSummary(List<BettingLogForm> bettingLogList) {
        boolean result = false;
        List<BettingSummary> bettingSummaryList = new ArrayList<>();
        for (BettingLogForm item : bettingLogList) {
            if (item.getType().equals("bet")) {
                BettingSummary bettingSummary = new BettingSummary();
                bettingSummary.setSeq(UUIDGenerator.generate());


                /* Thomas Peters 2022.04.03
                ----------------------------------------------------------------------------- <
                 * Convert date form from  2022-04-03 10:08:000000 to 2022-04-03T10:08:000000Z
                 */
                SimpleDateFormat destination_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat first_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
                Date first_date = null;
                try {
                    first_date = first_format.parse(item.getProcessedAt());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String str_first_date = destination_format.format(first_date);

                bettingSummary.setCheckTime(str_first_date);
                //----------------------------------------------------------------------------- >

                QueryWrapper<Member> qw = new QueryWrapper<>();
                qw.eq("id", item.getUser().getUsername());
                Member member = memberService.getOne(qw);

                bettingSummary.setMemberSeq(member != null ? member.getSeq() : "");
                bettingSummary.setStoreSeq(member != null ? member.getStoreSeq() : "");
                bettingSummary.setDistributorSeq(member != null ? member.getDistributorSeq() : "");
                bettingSummary.setHeadquarterSeq(member != null ? member.getSubHeadquarterSeq() : "");

                if (item.getDetails() != null && item.getDetails().getGame() != null) {
                    bettingSummary.setPlayingGame(item.getDetails().getGame().getTitle());
                    Integer type = item.getDetails().getGame().getType() != null && item.getDetails().getGame().getType().equals("slot") ? 0 : 1;
                    bettingSummary.setType(type);
                }

                bettingSummary.setBettingAmount(Math.abs(item.getAmount()));

                if (item.getType().equals("bet")) {
                    // get winning amount
                    BettingLogForm bettingLogForm = getBetWinRelation(bettingLogList, item.getId());
                    bettingSummary.setWinningAmount(bettingLogForm.getAmount());
                    if (bettingLogForm.getAmount() == 0) {
                        bettingSummary.setLostAmount(Math.abs(item.getAmount()));
                    }
                }

                bettingSummary.setBetCount(1);
//                bettingSummary.setBatRolling();
//                bettingSummary.setPointRate();

                System.out.println("bettingSummary");
                System.out.println(bettingSummary);
                bettingSummaryList.add(bettingSummary);
            }
        }

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
