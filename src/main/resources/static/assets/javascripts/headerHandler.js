$(document).ready(function() {
	var mDeposit = 0;
	var mWithdraw = 0;
	var mPosts = 0;
	var mCustomerService = 0;
	
	getHeaderInfo();
	
	window.setInterval(getHeaderInfo,5000);
	
	function getHeaderInfo(){
		$.ajax({
            url: CONTEXT_ROOT + 'dashboard/headerinfo',
            type: 'post',
            success: function(res) {
            	var audio = new Audio();
            	audio.src = CONTEXT_ROOT + "assets/audio/alarm.ogg";
            	
                if (res.success) {
                	let playflg = false;
                	if(res.data.member_deposit_cnt !=0 && mDeposit != res.data.member_deposit_cnt) {
                		mDeposit = res.data.member_deposit_cnt;
                		playflg = true;
                	}
                	if(res.data.member_withdraw_cnt !=0 && mWithdraw != res.data.member_withdraw_cnt) {
                		mWithdraw = res.data.member_withdraw_cnt
                		playflg = true;
                	}
                	if(res.data.m_post !=0 && mPosts != res.data.m_post){
                		mPosts = res.data.m_post
                		playflg = true;
                	}
                	if(res.data.m_customer_service !=0 && mCustomerService != res.data.m_customer_service) {
                		mCustomerService = res.data.m_customer_service
                		playflg = true;
                	}
                	if(playflg) {
                		audio.play();
                	}
                	
                	$('span.m-login-member').html(Number(parseFloat(res.data.member_login).toFixed(0)).toLocaleString('en'));
                	$('span.m-deposit').html(Number(parseFloat(res.data.member_deposit_cnt).toFixed(0)).toLocaleString('en'));
                	$('span.m-withdraw').html(Number(parseFloat(res.data.member_withdraw_cnt).toFixed(0)).toLocaleString('en'));
                	$('span.m-posts').html(Number(parseFloat(res.data.m_post).toFixed(0)).toLocaleString('en'));
                	$('span.m-customer-service').html(Number(parseFloat(res.data.m_customer_service).toFixed(0)).toLocaleString('en'));
                	$('span.m-betting-summary').html(Number(parseFloat(res.data.bs).toFixed(0)).toLocaleString('en'));
                	$('span.p-distributor').html(Number(parseFloat(res.data.distributor_login).toFixed(0)).toLocaleString('en'));
                	$('span.p-store').html(Number(parseFloat(res.data.store_login).toFixed(0)).toLocaleString('en'));
                	$('span.p-deposit').html(Number(parseFloat(res.data.partner_deposit_cnt).toFixed(0)).toLocaleString('en'));
                	$('span.p-withdraw').html(Number(parseFloat(res.data.partner_withdraw_cnt).toFixed(0)).toLocaleString('en'));
                	$('span.p-send-collect').html(Number(parseFloat(res.data.partner_send_collect_cnt).toFixed(0)).toLocaleString('en'));
                	$('span.p-note').html(Number(parseFloat(res.data.p_note).toFixed(0)).toLocaleString('en'));
                	$('span.m-today-deposit').html(Number(parseFloat(res.data.member_deposit_sum).toFixed(0)).toLocaleString('en'));
                	$('span.m-today-withdraw').html(Number(parseFloat(res.data.member_withdraw_sum).toFixed(0)).toLocaleString('en'));
                	$('span.m-total-money').html(Number(parseFloat(res.data.total_money).toFixed(0)).toLocaleString('en'));
                	$('span.m-total-point').html(Number(parseFloat(res.data.total_mileage).toFixed(0)).toLocaleString('en'));
                	$('span.login-member-link').html(Number(parseFloat(res.data.member_login).toFixed(0)).toLocaleString('en'));
                	$('span.p-today-deposit').html(Number(parseFloat(res.data.partner_deposit_sum).toFixed(0)).toLocaleString('en'));
                	$('span.p-today-withdraw').html(Number(parseFloat(res.data.partner_withdraw_sum).toFixed(0)).toLocaleString('en'));
                	$('span.p-total-money').html(Number(parseFloat(res.data.ptotal_money).toFixed(0)).toLocaleString('en'));
                }
            },
            error: function(err) {
            }
        });
	}
});


$(document).on('click', '.login-member-link', function() {
	let today = moment().format('YYYY-MM-DD');
	let regWindow = window.open(CONTEXT_ROOT + 'member/popup_simul_list?checkStatus=' + 0 + '&connectionDateString=' + today, 'Login Member', 'width='+screen.availWidth * 4 / 5+',height=600');
	regWindow.onbeforeunload = function () {
        window.location.reload();
    }
});


$(document).on('click', '.m-today-deposit', function (e) {
    e.preventDefault();
    let today = moment().format('YYYY-MM-DD');
    let moneyHistoryWindow = window.open(CONTEXT_ROOT + 'log/new_popup_moneyloglist?fromProcessTime=' + today + '&toProcessTime=' + today + '&operationType=' + 0, 'Money History', 'width='+screen.availWidth *4 / 5+',height=600');
    moneyHistoryWindow.onbeforeunload = function () {
        window.location.reload();
    }
});

$(document).on('click', '.m-today-withdraw', function (e) {
    e.preventDefault();
    let today = moment().format('YYYY-MM-DD');
    let moneyHistoryWindow = window.open(CONTEXT_ROOT + 'log/new_popup_moneyloglist?fromProcessTime=' + today + '&toProcessTime=' + today + '&operationType=' + 1, 'Money History', 'width='+screen.availWidth *4 / 5+',height=600');
    moneyHistoryWindow.onbeforeunload = function () {
        window.location.reload();
    }
});

$(document).on('click', '.m-login-member', function (e) {
    e.preventDefault();
    window.location.href = CONTEXT_ROOT + 'member/list';
});

$(document).on('click', '.m-deposit', function (e) {
    e.preventDefault();
    window.location.href = CONTEXT_ROOT + 'log/depositloglist';
});

$(document).on('click', '.m-withdraw', function (e) {
    e.preventDefault();
    window.location.href = CONTEXT_ROOT + 'log/withdrawloglist';
});

$(document).on('click', '.m-posts', function (e) {
    e.preventDefault();
    window.location.href = CONTEXT_ROOT + 'board/list?province=2&sendTimeTo=' + moment().format('YYYY-MM-DD');
});

$(document).on('click', '.m-customer-service', function (e) {
    e.preventDefault();
    window.location.href = CONTEXT_ROOT + 'board/questionlist';
});

$(document).on('click', '.m-betting-summary', function (e) {
    e.preventDefault();
    window.location.href = CONTEXT_ROOT + 'log/slotloglist';
});

$(document).on('click', '.p-distributor', function (e) {
    e.preventDefault();
    window.location.href = CONTEXT_ROOT + 'partner2/list';
});

$(document).on('click', '.p-store', function (e) {
    e.preventDefault();
    window.location.href = CONTEXT_ROOT + 'partner2/list_store';
});

$(document).on('click', '.p-deposit,.p-today-deposit', function (e) {
    e.preventDefault();
    window.location.href = CONTEXT_ROOT + 'log/partner/depositloglist?status=0';
});

$(document).on('click', '.p-withdraw,.p-today-withdraw', function (e) {
    e.preventDefault();
    window.location.href = CONTEXT_ROOT + 'log/partner/withdrawloglist?status=0';
});

$(document).on('click', '.p-send-collect', function (e) {
    e.preventDefault();
    window.location.href = CONTEXT_ROOT + 'log/partner_moneyloglist';
});

$(document).on('click', '.p-note', function (e) {
    e.preventDefault();
    window.location.href = CONTEXT_ROOT + 'memo/pInboxList';
});
