function allCheck(){
	var allCheck = $("#allCheck")[0].checked;
	var checkArray = document.getElementsByClassName("checkboxes");
	for(var i = 0; i < checkArray.length; i++){
		checkArray[i].checked = allCheck;
	}
};
$(document).on('click', '.pInboxTitle', function(e){
	let closed = $(this).attr("data-closed");
	if (closed == 0) {
		$(this).attr("data-closed", 1);
		let title = $(this).text();
		let content = $(this).data("content").replace(',<p>', '').replace('<p>', '');

		let detailTr = getDetailTr(title, content);
		$(this).parent().after(detailTr);
	}
	else{
		$(this).attr("data-closed", 0);
		$(this).parent().next().remove();
	}
})

function getDetailTr(title, content){
	return $('<tr class="detailTd"></tr>')
		.append($("<td colspan='20'></td>")
			.append(
				$("<div class='note-inbox-detail'></div>")
					.append(
						$("<div class='row note-content'></div>")
							.append("<div class='col-md-1'>" + titleStr + " : </div>")
							.append("<div class='col-md-11 note-content-body'>" + title +"</div class='row'></div>")
					)
					.append(
						$("<div class='row note-content'></div>")
							.append("<div class='col-md-1'>" + contentStr + " : </div>")
							.append("<div class='col-md-11 note-content-body'>" + content + "</div>")
					)
			)
		);
}

$(document).on('click', '.noteTitle', function(e){
	let closed = $(this).attr("data-closed");
	if (closed == 0) {
		$(this).attr("data-closed", 1);
		let title = $(this).data("title");
		let content = $(this).data("content").replace(',<p>', '').replace('<p>', '');
		 
		let detailTr = getDetailTr(title, content);
		$(this).after(detailTr);
	} else {
		$(this).attr("data-closed", 0);
		$(this).next().remove();
	}
});

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
            title: 'Faild!',
            text: 'Please Check it.',
            type: 'error',
            buttons: {
                closer: true,
                sticker: false
            }
		});
	} else {
		$.ajax({
			url: CONTEXT_ROOT + "memo/batchDelete",
			type: "POST",
			data: { ids: ids },
			success: function(response) {
				for(var i = 0; i < checkArray.length; i++){
					if(checkArray[i].checked) {
						checkArray[i].checked = false;
					}
				}
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
			}
		});
	}
}

$('.sender-nickname').on('click', function() {
	let memberSeq = $(this).data('mseq');
	let userType = $(this).data('utype');
	let url = "";
	if(userType == 1){
		url = CONTEXT_ROOT + 'partner2/memberDetailsStore?idx='
	}
	else if(userType == 2){
		url = CONTEXT_ROOT + 'partner2/memberDetails?idx='
	}
	else if(userType == 3){
		url = CONTEXT_ROOT + 'partner2/memberDetailsTop?idx='
	}

	let detailWindow = window.open(url + memberSeq, 'Member Detail', features);
	detailWindow.onbeforeunload = function () {
		window.location.reload();
	}
});

$('.answerbtn').on('click', function() {
	let memberSeq = $(this).data('mseq');
	let noteSeq = $(this).data('noteseq');

	let detailWindow = window.open(CONTEXT_ROOT + 'partner2/getMemo?seq=' + memberSeq + '&note_seq=' + noteSeq, 'Member Detail', features);
	detailWindow.onbeforeunload = function () {
		window.location.reload();
	}
});