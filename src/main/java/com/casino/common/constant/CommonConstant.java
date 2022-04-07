package com.casino.common.constant;

public interface CommonConstant {
	public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
	public static final Integer SC_NORMAL_ERROR_505 = 505;
	public static final Integer SC_OK_200 = 200;
	public static final Integer SC_LOGIC_ERROR = 600;

	public static String PREFIX_USER_ROLE = "PREFIX_USER_ROLE";
	public static String PREFIX_USER_PERMISSION = "PREFIX_USER_PERMISSION ";
	public static int TOKEN_EXPIRE_TIME = 3600;
	public static String PREFIX_USER_TOKEN = "PREFIX_USER_TOKEN ";

	// Note Classification
	public static final Integer CLASSIFICATION_NOTICE = 0;
	public static final Integer CLASSIFICATION_FAQ = 1;
	public static final Integer CLASSIFICATION_FREE_BOARD = 2;
	public static final Integer CLASSIFICATION_EVENT = 3;
	public static final Integer CLASSIFICATION_CUSTOMER = 4;
	public static final Integer CLASSIFICATION_M_POST = 5;

	// Note Correction
	public static final Integer CORRECTION_NOT_APPLY = 0;
	public static final Integer CORRECTION_APPLY = 1;

	// admin deposit
	public static final Integer CLASSIFICATION_MONEY = 0;
	public static final Integer CLASSIFICATION_MILEAGE = 1;

	// member user type
	public static final Integer USER_TYPE_NORMAL = 0;
	public static final Integer USER_TYPE_STORE = 1;
	public static final Integer USER_TYPE_DISTRIBUTOR = 2;
	public static final Integer USER_TYPE_SUB_HEADQUARTER = 3;
	public static final Integer USER_TYPE_ADMIN = 4;

	// member status
	public static final Integer USER_STATUS_NORMAL = 0;
	public static final Integer USER_STATUS_STOP = 1;

	// money history status
	public static final Integer IN_PROGRESS = 0;
	public static final Integer COMPLETE = 1;
	public static final Integer CANCEL = 2;
	public static final Integer ADMIN_EDIT = 3;

	// recommend status
	public static final Integer STATUS_UN_RECOMMEND = 0;
	public static final Integer STATUS_RECOMMEND = 1;

	// read status
	public static final Integer STATUS_UN_READ = 0;
	public static final Integer STATUS_READ = 1;

	// answer status
	public static final Integer STATUS_NO_ANSWER = 0;
	public static final Integer STATUS_ANSWER = 1;

	// note type
	public static final Integer TYPE_NOTE = 0;
	public static final Integer TYPE_P_NOTE = 1;
	public static final Integer TYPE_POST = 2;
	
	public static final Integer UNREAD_STATUS = 0;
	public static final Integer READ_STATUS = 1;

	public static final Integer MONEY_OPERATION_TYPE_DEPOSIT = 0;
	public static final Integer MONEY_OPERATION_TYPE_WITHDRAW = 1;

	// select type
	public static final Integer SELECT_TYPE_0 = 0;
	public static final Integer SELECT_TYPE_1 = 1;
	public static final Integer SELECT_TYPE_2 = 2;
	public static final Integer SELECT_TYPE_3 = 3;

	// money history
	public static final Integer MONEY_HISTORY_STATUS_IN_PROGRESS = 0;
	public static final Integer MONEY_HISTORY_STATUS_COMPLETE = 1;
	public static final Integer MONEY_HISTORY_STATUS_CANCEL = 2;
	public static final Integer MONEY_HISTORY_STATUS_PARTNER_PAYMENT = 3;

	public static final Integer MONEY_HISTORY_OPERATION_TYPE_DEPOSIT = 0;
	public static final Integer MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL = 1;
	public static final Integer MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN = 2;
	public static final Integer MONEY_HISTORY_OPERATION_TYPE_TRANSFER_OUT = 3;

	public static final Integer MONEY_REASON_CHARGE = 0;
	public static final Integer MONEY_REASON_EXCHANGE = 1;
	public static final Integer MONEY_REASON_PARTNER_RECOVERY = 2;
	public static final Integer MONEY_REASON_PARTNER_PAYMENT = 3;
	public static final Integer MONEY_REASON_TRANSFER = 4;
	public static final Integer MONEY_REASON_TRANSFER_WINNING = 6;
	public static final Integer MONEY_REASON_ADMINEDIT = 5;

	public static final Integer PARTNER_OR_MEMBER_MEMBER = 0;
	public static final Integer PARTNER_OR_MEMBER_PARTNER = 1;

	public static final String DICT_KEY_MONEY_REASON = "REASON_FOR_MONEY";
	public static final String DICT_KEY_MILEAGE_REASON = "REASON_FOR_MILEAGE";

	// member
	public static final Integer MEMBER_STATUS_STOP = 0;
	public static final Integer MEMBER_STATUS_NORMAL = 1;
	public static final Integer MEMBER_STATUS_TEST = 2;
	public static final Integer MEMBER_STATUS_JUDGE = 3;
	public static final Integer MEMBER_STATUS_WITHDRAWAL = 4;

	public static final Integer BETTING_TYPE_SLOT = 0;
	public static final Integer BETTING_TYPE_BACCARAT = 1;

	public static final Integer IS_MONEY = 0;
	
	public static final Integer MONEY_OR_POINT_MONEY = 0;
	public static final Integer MONEY_OR_POINT_POINT = 1;

	public static final Integer ACCESS_LOG_SUCCESS = 0;
	public static final Integer ACCESS_LOG_PASSWORD_ERROR = 1;
	public static final Integer ACCESS_LOG_USER_ID_ERROR = 2;

}
