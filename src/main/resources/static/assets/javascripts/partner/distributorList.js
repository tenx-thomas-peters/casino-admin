 $(document).ready(function() {
	$('.headquarter-change-status-form').on('click', function() {
		$('#headquarterChangeStatusForm #headquarterStatusChange').attr('data-seq', $(this).data('seq'));
		$('#headquarterChangeStatusForm').modal();
	});
        	
	$('#headquarterChangeStatusForm #headquarterStatusChange').on('click', function() {
		changeStatus($(this).data('seq'));
		$('#headquarterChangeStatusForm').hide();
	});
        	
	$('.headquarter-delete-form').on('click', function() {
		$('#headquarterDeleteForm #headquarterDelete').attr('data-seq', $(this).data('seq'));
		$('#headquarterDeleteForm').modal();
		
	});
        	
	$('#headquarterDeleteForm #headquarterDelete').on('click', function() {
		deleteHeadquarter($(this).data('seq'));
		$('#headquarterDeleteForm').hide();
	});
	
	$('.headquarter-note-form').on('click', function() {
		let seq = $(this).parents('tr').find('.distributor-seq').data('key');
	    let noteWindow = window.open('./getMemo?seq=' + seq, 'Note', 'width='+screen.availWidth *2 / 3+',height=500');
	    noteWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
        	
	$('.distributor-add-form').click(function(){
		let regWindow = window.open('./distributorAddForm', 'Distributor Registration', 'width='+screen.availWidth / 2+',height=600');
		regWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
        	
});
 		
$(document).on('click', '.deputy-headquarter-detail', function (e) {
	let headquarterSeq = $(this).data('key');
	let detailWindow = window.open('./memberDetailsTop?idx=' + headquarterSeq, 'Deputy Headquarter Details', 'width='+screen.availWidth *2 / 3+',height=600');
	detailWindow.onbeforeunload = function () {
        window.location.reload();
    }
});

$(document).on('click', '.partner-distributor-detail', function (e) {
    e.preventDefault();
    let storeSeq = $(this).data('key');
    let detailWindow = window.open('./memberDetails?idx=' + storeSeq, 'Partner Distributor Detail', 'width='+screen.availWidth *2 / 3+',height=600');
    detailWindow.onbeforeunload = function () {
        window.location.reload();
    }
});

$(document).on('click', '.progress-bet', function (e) {
    e.preventDefault();
    let memberSeq = $(this).parents('tr').find('.distributor-seq').data('key');
    let betWindow = window.open(CONTEXT_ROOT + 'member/popup_bet?mem_sn='+memberSeq, 'Betting Summary', 'width='+(screen.availWidth - 100)+', height=600');
    betWindow.onbeforeunload = function () {
        window.location.reload();
    }
});
	
let today = moment().format('YYYY-MM-DD');

$(document).on('click', '.headquarter-storeCount', function (e) {
    e.preventDefault();
    let headquarterSeq = $(this).parents('tr').find('.distributor-seq').data('key');
    window.open('./shopMember?seq=' + headquarterSeq + '&fromApplicationTime=' + today + '&toApplicationTime=' + today + '&userType=' + 2, 'Sub-Store List', 'width='+screen.availWidth+',height=900');
});

$(document).on('click', '.headquarter-memberCount', function (e) {
    e.preventDefault();
    let headquarterSeq = $(this).parents('tr').find('.distributor-seq').data('key');
    window.open('./Member?seq=' + headquarterSeq + '&fromApplicationTime=' + today + '&toApplicationTime=' + today + '&userType=' + 2, 'Sub-Store List', features);
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
        
function deleteHeadquarter(seq) {
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
