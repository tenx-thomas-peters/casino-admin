package com.casino.modules.admin.common.entity;

import lombok.Data;

@Data
public class Dashboard {
	
	private String memberSeq;
	
	private String id;
	
	private String nickname;
	
	private String topDistributor;
	
	private String domain;
	
	private String topDistributorId;
	
	private Integer connections;
	
	private Float chargedAmount;
	
	private Float withdrawalAmount;
	
	private Float wonAmount;
}
