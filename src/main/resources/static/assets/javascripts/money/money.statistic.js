$(document).ready(function() {
	var top = 100;
	var left = 100;
	var width = screen.availWidth * 5 / 6;
	var height = 800;
	var popupPos = 'width=' + width + ',height=' + height + ',top=' + top + ',left=' + left;
	
	$('.pointDeposit').on('click', function() {
		let checkDay = $(this).parent().data('checkday');
		let moneyHistorySeq = $(this).data('seq');
		let detailWindow = window.open(CONTEXT_ROOT + 'log/mileage/detaillist?date=' + checkDay + "&operationType=0", 
			'Member mileage details', 
			popupPos);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
	$('.pointWithdraw').on('click', function() {
		let checkDay = $(this).parent().data('checkday');
		let moneyHistorySeq = $(this).data('seq');
		let detailWindow = window.open(CONTEXT_ROOT + 'log/mileage/detaillist?date=' + checkDay + "&operationType=1", 
			'Member mileage details', 
			popupPos);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
	$('.managerDeposit').on('click', function() {
		let checkDay = $(this).parent().data('checkday');
		let moneyHistorySeq = $(this).data('seq');
		let detailWindow = window.open(CONTEXT_ROOT + 'log/money/detaillist?date=' + checkDay + "&type=1&operationType=0", 
			'Member money details', 
			popupPos);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
	$('.managerWithdraw').on('click', function() {
		let checkDay = $(this).parent().data('checkday');
		let moneyHistorySeq = $(this).data('seq');
		let detailWindow = window.open(CONTEXT_ROOT + 'log/money/detaillist?date=' + checkDay + "&type=1&operationType=1", 
			'Member money details', 
			popupPos);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
	$('.distributorDeposit').on('click', function() {
		let checkDay = $(this).parent().data('checkday');
		let moneyHistorySeq = $(this).data('seq');
		let detailWindow = window.open(CONTEXT_ROOT + 'log/money/detaillist?date=' + checkDay + "&type=2&operationType=0", 
			'Member money details', 
			popupPos);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
	$('.distributorWithdraw').on('click', function() {
		let checkDay = $(this).parent().data('checkday');
		let moneyHistorySeq = $(this).data('seq');
		let detailWindow = window.open(CONTEXT_ROOT + 'log/money/detaillist?date=' + checkDay + "&type=2&operationType=1", 
			'Member money details', 
			popupPos);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
	$('.memberDeposit').on('click', function() {
		let checkDay = $(this).parent().data('checkday');
		let moneyHistorySeq = $(this).data('seq');
		let detailWindow = window.open(CONTEXT_ROOT + 'log/money/detaillist?date=' + checkDay + "&type=0&operationType=0", 
			'Member money details', 
			popupPos);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
	$('.memberWithdraw').on('click', function() {
		let checkDay = $(this).parent().data('checkday');
		let moneyHistorySeq = $(this).data('seq');
		let detailWindow = window.open(CONTEXT_ROOT + 'log/money/detaillist?date=' + checkDay + "&type=0&operationType=1", 
			'Member money details', 
			popupPos);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
	});
})