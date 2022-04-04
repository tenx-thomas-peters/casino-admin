package com.casino.modules.admin.common.form;

import com.casino.modules.admin.common.entity.Member;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

public class BettingLogForm {
    private String id;
    private String type; // bet, win, cancel
    private Float amount;
    private Float before;
    private String userId;
    private String status;
    private BettingLogDetail details;
    private String refererId; // bet-win relation

//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
//    Date format from response data ( /transaction-sample-list  api)
    private String processedAt;
    private String createAt;
    private Member user; // {id: '', username: ''}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getBefore() {
        return before;
    }

    public void setBefore(Float before) {
        this.before = before;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BettingLogDetail getDetails() {
        return details;
    }

    public void setDetails(BettingLogDetail details) {
        this.details = details;
    }

    public String getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(String processedAt) {
        this.processedAt = processedAt;
    }

    public String getRefererId() {
        return refererId;
    }

    public void setRefererId(String refererId) {
        this.refererId = refererId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public Member getUser() {
        return user;
    }

    public void setUser(Member user) {
        this.user = user;
    }
}
