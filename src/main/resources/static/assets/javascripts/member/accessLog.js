function checkAll(){
	var checkAll = $("#checkAll")[0].checked;
	var checkArray = document.getElementsByClassName("checkboxes");
	for(var i = 0; i < checkArray.length; i++){
		checkArray[i].checked = checkAll;
	}
};

function searchSubmit() {
	if($("input[name=checkStatus]").prop("checked") == true) {
		$("input[name=checkStatus]").val(1);
	}
	$('#formSearch').submit();
}

function hourSearch(hour) {
	$('input[name=hour]').val(hour);
	$('#formSearch').submit();
}

$(document).on('click', '.member-seq', function (e) {
    e.preventDefault();
    var memberSeq = $(this).parents('tr').find('input[name=memberSeq]').data('key');
    window.open('./popup_detail?idx=' + memberSeq, 'Member Detail', 'height=auto, width=auto');
    /*memberDetailPopup(memberSeq);*/
});

function logDelete() {
	var logSeq = $('input[name=seq]').val();
		$.ajax({
		url: CONTEXT_ROOT + "member/accessLogDelete",
		type: "POST",
		data: { "deleteSeq": logSeq },
		success: function(response) {
			window.location.reload();
		},
		error: function(err) {
			console.log(err);
		}
	})
}

function batchDelete() {
	var checkArray = document.getElementsByClassName("checkboxes");
	var ids = "";
	for(var i = 0; i < checkArray.length; i++){
		if(checkArray[i].checked) {
		    ids += checkArray[i].name + ",";
		}
	}
	if(ids == "") {
		new PNotify({
			title: 'Faild',
			text: 'Please Check it.',
			type: 'custom',
			addclass: 'notification-danger'
		});
	} else {
		$.ajax({
			url: CONTEXT_ROOT + "member/batchDelete",
			type: "POST",
			data: { ids: ids },
			success: function(response) {
				for(var i = 0; i < checkArray.length; i++){
					if(checkArray[i].checked) {
						checkArray[i].checked = false;
					}
				}
				new PNotify({
					title: 'Success',
					text: 'Success',
					type: 'success',
					shadow: true
				});

				window.location.reload();
			},
			error: function(err) {
				console.log(err);
			}
		});
	}
}

function resetSearch() {
	window.location.href = CONTEXT_ROOT + "member/loginlist";
}