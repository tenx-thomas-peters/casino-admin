$(document).ready(function() {
	
	$('.store-change-status-form').on('click', function() {
		$('#storeChangeStatusForm #storeStatusChange').attr('data-seq', $(this).data('seq'));
		$('#storeChangeStatusForm').modal();
	});
	
	$('#storeChangeStatusForm #storeStatusChange').on('click', function() {
		changeStatus($(this).data('seq'));
		$('#storeChangeStatusForm').hide();
	});
	
	$('.store-delete-form').on('click', function() {
		$('#storeDeleteForm #storeDelete').attr('data-seq', $(this).data('seq'));
		$('#storeDeleteForm').modal();
		
	});
	
	$('#storeDeleteForm #storeDelete').on('click', function() {
		deleteStore($(this).data('seq'));
		$('#storeDeleteForm').hide();
	});
	
	$('.store-note-form').on('click', function() {
		let seq = $(this).parents('tr').find('.store-seq').data('key');
	    let noteWindow = window.open('./getMemo?seq=' + seq, 'Note', 'width='+screen.availWidth *2 / 3+',height=500');
	    noteWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
	
	$(document).on('click', '.deputy-headquarter-detail', function (e) {
        e.preventDefault();
        let headquarterSeq = $(this).data('key');
        detailWindow = window.open('./memberDetailsTop?idx=' + headquarterSeq, 'Deputy Headquarter Details', 'width='+screen.availWidth *2 / 3+',height=600');
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });
	
	$(document).on('click', '.partner-distributor-detail', function (e) {
        e.preventDefault();
        let distributorSeq = $(this).data('key');
        detailWindow = window.open('./memberDetails?idx=' + distributorSeq, 'Partner Distributor Detail', 'width='+screen.availWidth *2 / 3+',height=600');
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });
	
	$(document).on('click', '.partner-store-detail', function (e) {
        e.preventDefault();
        let storeSeq = $(this).data('key');
        detailWindow = window.open('./memberDetailsStore?idx=' + storeSeq, 'Partner Store Detail', 'width='+screen.availWidth *2 / 3+',height=600');
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });
	
	$(document).on('click', '.progress-bet', function (e) {
        e.preventDefault();
        let memberSeq = $(this).parents('tr').find('.store-seq').data('key');
        let betWindow = window.open(CONTEXT_ROOT + 'member/popup_bet?mem_sn='+memberSeq, 'Betting Summary', 'width='+(screen.availWidth - 100)+', height=600');
        betWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });
	
	let today = moment().format('YYYY-MM-DD');
	
	$(document).on('click', '.store-memberCount', function (e) {
	    e.preventDefault();
	    let storeSeq = $(this).parents('tr').find('.store-seq').data('key');
	    let detailWindow = window.open('./Member?seq=' + storeSeq + '&fromApplicationTime=' + today + '&toApplicationTime=' + today + '&userType=' + 1, '', partnerFeatures);
	    detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
	
	$('.store-add-form').click(function(){
		let regWindow = window.open('./storeAddForm', 'Store Registration', 'width='+screen.availWidth / 2+',height=600');
		regWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
	
});

$(document).ready(function(){
	$('#headquarterList').change(function(){
		var headquarterSeq = $('#headquarterList').val();
		$.ajax({
			url: CONTEXT_ROOT + "/partner2/getDistributorList",
			type: "GET",
			data: {"headquarterSeq": headquarterSeq },
			success: function(response){
				$('#store-add-form select[name="distributorSeq"]').html($('<option value=""></option>'));
				response.result.map(function(item) {
					$('#store-add-form select[name="distributorSeq"]').append($('<option value="' + item.seq + '">' + item.id + '</option>'));
				})
			}
		});
	});
});

function changeStatus(seq) {
	
	$.ajax({
		url: CONTEXT_ROOT + "/partner2/changeStatus",
		type: "GET",
		data: {"seq": seq },
		success: function(response) {
			if(response.success){
				new PNotify({
                    title: 'Success!',
                    text: response.message,
                    type: 'success',
                    buttons: {
                        closer: true,
                        sticker: false
                    }
                });
				
				setTimeout(function() {
                    window.location.reload();
                }, 1000);
			} else {
				new PNotify({
                    title: 'Error!',
                    text: response.message,
                    type: 'error',
                    buttons: {
                        closer: true,
                        sticker: false
                    }
                });
			}
		},
		error: function(err) {
			new PNotify({
                title: 'Error!',
                text: err.message,
                type: 'error',
                buttons: {
                    closer: true,
                    sticker: false
                }
            });

            $.magnificPopup.close();
		}
	});
}

function deleteStore(seq) {
	$.ajax({
		url: CONTEXT_ROOT + "/partner2/delete",
		type: "GET",
		data: {"seq": seq },
		success: function(response) {
			if(response.success){
				new PNotify({
                    title: 'Success!',
                    text: response.message,
                    type: 'success',
                    buttons: {
                        closer: true,
                        sticker: false
                    }
                });
				
				setTimeout(function() {
                    window.location.reload();
                }, 1000);
			} else {
				new PNotify({
                    title: 'Error!',
                    text: response.message,
                    type: 'error',
                    buttons: {
                        closer: true,
                        sticker: false
                    }
                });
			}
		},
		error: function(err) {
			new PNotify({
                title: 'Error!',
                text: err.message,
                type: 'error',
                buttons: {
                    closer: true,
                    sticker: false
                }
            });

            $.magnificPopup.close();
		}
	})
}       	

