function allCheck(){
	var allCheck = $("#allCheck")[0].checked;
	var checkArray = document.getElementsByClassName("checkboxes");
	for(var i = 0; i < checkArray.length; i++){
		checkArray[i].checked = allCheck;
	}
};

$(document).on('click', '.noteTitle', function(e){
	let closed = $(this).attr("data-closed");
	if (closed == 0) {
		$(this).attr("data-closed", 1);
		let title = $(this).text();
		let content = $(this).data("content").replace(',<p>', '').replace('<p>', '');
		 
		let detailTr = $('<tr class="detailTd"></tr>')
						.append($("<td colspan='20'></td>").append($("<div></div>").append("<div class=row></div>")
						.append("<div class='col-md-1' style='float:left!important'>Title : </div>")
						.append("<div class='col-md-10' style='text-align: left'>" + title +"</div class='row'></div><br/><br/>")
						.append("<div class='col-md-1'>Content : </div>").append("<div class='col-md-10' style='text-align: left'>" + content + "</div>")));
		$(this).parent().parent().after(detailTr);
	} else {
		$(this).attr("data-closed", 0);
		$(this).parent().parent().next().remove();
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