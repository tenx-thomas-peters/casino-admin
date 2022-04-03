function allCheck(){
	var allCheck = $("#allCheck")[0].checked;
	var checkArray = document.getElementsByClassName("checkboxes");
	for(var i = 0; i < checkArray.length; i++){
		checkArray[i].checked = allCheck;
	}
};

function apply(seq, levelName, firstInsect, caterpillar) {
	console.log(seq);
}

function batchLevelDelete() {
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
			url: CONTEXT_ROOT + "config/deleteLevels",
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
				window.location.href = CONTEXT_ROOT + "config/level";
			},
			error: function(err) {
				console.log(err);
			}
		});
	}
}

(function( $ ) {
	'use strict';
	
/*	$(document).on('click', '.levelAdd', function (e) {
        e.preventDefault();
        if (!$('#addForm').valid())
            return;

        $('#addForm').submit();

    });*/
	$(document).on('click', '.levelAdd', function (e) {
	    e.preventDefault();
	    if (!$('#level-addForm').valid())
	        return;

	    $.ajax({
	        url: CONTEXT_ROOT + '/config/addLevel',
	        type: 'POST',
	        // data: $('#addForm').submit(),
	        data: $('#level-addForm').serialize(),
	        dataType: 'json',
	        success: function(res) {
	            if (res.success) {
	                new PNotify({
	                    title: 'Success!',
	                    text: res.message,
	                    type: 'success',
	                    buttons: {
	                        closer: true,
	                        sticker: false
	                    }
	                });

	                setTimeout(function() {
	                    window.close();
	                }, 1000);
	            } else {
	                new PNotify({
	                    title: 'Error!',
	                    text: res.message,
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
	        }
	    });
	});
	
	$(document).on('click', '.levelInput', function(e) {
		e.preventDefault();
		$(this).parent().parent().children().last().children().removeAttr('disabled');
	});
	
    $(document).on('click', '.add-level', function () {
        let regWindow = window.open('./add_detail', 'add_detail', 'width='+screen.availWidth / 2+',height=600');
        regWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });
	
}).apply( this, [ jQuery ]);
