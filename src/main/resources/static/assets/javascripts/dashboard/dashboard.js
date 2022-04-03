$(document).ready(function(){
	$('.dashboardSearch').on('click', function() {
		
		var name = $('#search-input').val();
		$.ajax({
            url: CONTEXT_ROOT + '/dashboard/search',
            type: 'POST',
            dataType: 'json',
            data: { "name": name },
            success: function (res) {
            	if (res.result.seq != null) {
            		$("[name='id']").val(res.result.id);
            		$("[name='nickname']").val(res.result.nickname);
	                $("[name='ranking']").val(res.result.ranking);
	                $("[name='totalBet']").val(res.result.totalBet);
	                $("[name='phoneNumber']").val(res.result.phoneNumber);
	                $("[name='distributor']").val(res.result.distributor);
	                $("[name='moneyAmount']").val(res.result.moneyAmount);
	                $("[name='mileageAmount']").val(res.result.mileageAmount);
	                $("[name='winningAmount']").val(res.result.winningAmount);
	                $("[name='losingAmount']").val(res.result.losingAmount);
	                $("[name='rechargeAmount']").val(res.result.rechargeAmount);
	                $("[name='withdrawalAmount']").val(res.result.withdrawalAmount);
	                $("[name='rechargeBonusAmount']").val(res.result.rechargeBonusAmount);
                } else {
                	$("[name='id']").val("");
            		$("[name='nickname']").val("");
	                $("[name='ranking']").val("");
	                $("[name='totalBet']").val("");
	                $("[name='phoneNumber']").val("");
	                $("[name='distributor']").val("");
	                $("[name='moneyAmount']").val("");
	                $("[name='mileageAmount']").val("");
	                $("[name='winningAmount']").val("");
	                $("[name='losingAmount']").val("");
	                $("[name='rechargeAmount']").val("");
	                $("[name='withdrawalAmount']").val("");
	                $("[name='rechargeBonusAmount']").val("");
                    new PNotify({
                        title: 'Result',
                        text: 'No Data',
                        type: 'error',
                        buttons: {
                            closer: true,
                            sticker: false
                        }
                    });
                }
            },
            error: function (err) {
            	new PNotify({
                    title: 'Error!',
                    text: err.message,
                    type: 'error',
                    buttons: {
                        closer: true,
                        sticker: false
                    }
                });
            	
            }
        });
	});
	
});