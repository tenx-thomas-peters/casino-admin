package com.casino.modules.admin.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.casino.common.utils.HttpUtils;
import com.casino.modules.admin.common.form.BettingLogForm;
import com.casino.modules.admin.service.IMemberService;
import com.casino.modules.admin.service.IScheduleService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduleController {
    @Value(value = "${gameServer.url}")
    private String gameServerUrl;

    @Value(value = "${gameServer.apiKey}")
    private String apiKey;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IScheduleService scheduleService;

    private Long lastRequestTime;

    @Scheduled(cron = "${casino.schedule.delay}")
    public void getTransactionListSimple() {
        try {

            long ONE_MINUTE_IN_MILLIS = 60000;
            Calendar date = Calendar.getInstance();
            long t, before_30_min;
            if (this.lastRequestTime == null) {
                t = date.getTimeInMillis();
                before_30_min = t - (5 * ONE_MINUTE_IN_MILLIS);
            } else {
                before_30_min = this.lastRequestTime;
                t = date.getTimeInMillis();
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC+0"));
            Date utc_date = simpleDateFormat.parse(simpleDateFormat.format(new Date(before_30_min)));
            Date current_utc_date = simpleDateFormat.parse(simpleDateFormat.format(new Date(t)));
            String date_30_ago = simpleDateFormat.format(utc_date);
            String current = simpleDateFormat.format(current_utc_date);

            Integer defaultPerPage = 100;
            Integer pageNo = 1;

            String transaction_list_simple_url = gameServerUrl + "/transaction-list-simple?" +
                    "start=" + date_30_ago +
                    "&end=" + current +
                    "&page=" + pageNo.toString() +
                    "&perPage=" + defaultPerPage.toString() ;

            ResponseEntity<String> result = HttpUtils.getTransactionListSimple(transaction_list_simple_url, apiKey);

            if (result.getStatusCode().value() == 200) {
                JSONObject json = JSON.parseObject(result.getBody().toString());

                if (json.size() > 0) {
                    List<BettingLogForm> bettingLogList = json.getJSONArray("data").toJavaList(BettingLogForm.class);

                    while (json.getString("next_page_url") != null) {
                        pageNo++;

                        String transaction_list_simple_url_replace = gameServerUrl + "/transaction-list-simple" +
                                "?start=" + date_30_ago +
                                "&end=" + current +
                                "&page=" + pageNo.toString() +
                                "&perPage=" + defaultPerPage.toString() ;

                        result = HttpUtils.getTransactionListSimple(transaction_list_simple_url_replace, apiKey);
                        json = JSONObject.parseObject(result.getBody().toString());
                        List<BettingLogForm> bettingLogList1 = json.getJSONArray("data").toJavaList(BettingLogForm.class);
                        bettingLogList.addAll(bettingLogList1);
                    }

                    if (scheduleService.saveBettingSummary(bettingLogList)) {
                        log.info("=======================  betting summary save success  =======================");
                        this.lastRequestTime = t;
                    } else {
                        log.error("=======================  betting summary save failed  =======================");
                    }
                }
            } else {
                log.error("=======================  betting summary save failed  =======================");
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
    }
}
