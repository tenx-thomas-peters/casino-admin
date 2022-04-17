package com.casino.modules.admin.common.form;

import lombok.Data;

@Data
public class APIUserForm {

    private String id;
    private String username;
    private String nickname;
    private String country;
    private String currency_code;
    private Float balance;
    private Float point;
    private String token;
    private String created_at;
    private String updated_at;
    private String last_access_at;
    private String agent_id;
}
