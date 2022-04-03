$(document).ready(function() {
	createPagenation(page, url);
	
	$('.acceptBtn').on('click', function() {
		let moneyHistorySeq = $(this).data('seq');
		let top = 100;
        let left = 100;
        let width = screen.availWidth * 4 / 6;
        let height = 400;
		let detailWindow = window.open(CONTEXT_ROOT + 'log/charge/agree?idx=' + moneyHistorySeq, 
			'Recharge application processing', 
			'width='+ width + ',height=' + height + ',top=' + top + ',left=' + left);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});

	$('.memberId').on('click', function (e) {
	    e.preventDefault();
	    let memberSeq = $(this).data('key');

	    let detailWindow = window.open(CONTEXT_ROOT + 'member/popup_detail?idx=' + memberSeq, 'Member Detail', features);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
	
	$(document).on('click', '.progress-bet', function (e) {
        e.preventDefault();

        let memberSeq = $(this).data('memberseq');
        let betWindow = window.open(CONTEXT_ROOT + 'member/popup_bet?mem_sn='+memberSeq, 'Betting Summary', 'width='+(screen.availWidth - 100)+', height=600');
        betWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });
	
	$("#depositTabBtn").on("click", function() {
		window.location.href=CONTEXT_ROOT + "/log/depositloglist";
	});
	$("#withdrawTabBtn").on("click", function() {
		window.location.href=CONTEXT_ROOT + "/log/withdrawloglist";
	});
	$("#pdepositTabBtn").on("click", function() {
		window.location.href=CONTEXT_ROOT + "/log/partner/depositloglist";
	});
	$("#pwithdrawTabBtn").on("click", function() {
		window.location.href=CONTEXT_ROOT + "/log/partner/withdrawloglist";
	});
	$("#moneyHistoryTabBtn").on("click", function() {
		window.location.href=CONTEXT_ROOT + "/log/moneyloglist";
	});
	$("#mileageHistoryTabBtn").on("click", function() {
		window.location.href=CONTEXT_ROOT + "/log/mileageloglist";
	});
	
});