(function( $ ) {
	'use strict';
	
	$(document).on('click', '.deleteModal', function () {
        var popupSettingSeq = $(this).parent().parent().children().first().val();
    	$("[name='modalSeq']").val(popupSettingSeq);
    });
	
	$(document).on('click', '.editPopupBut', function () {
        var popupSettingSeq = $(this).parent().parent().children().first().val();
        
        let editWindow = window.open('./editPopupPage?idx=' + popupSettingSeq, 'Edit Popup', 'width='+screen.availWidth / 2+',height=600');
        editWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });
	
	$(document).on('click', '.addPopupBut', function () {
        let regWindow = window.open('./addPopupPage', 'addPopupPage', 'width='+screen.availWidth / 2+',height=600');
        regWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });
	
	$(document).on('click', '.popupAddBut', function (e) {
        e.preventDefault();
        if (!$('#popupAddForm').valid())
            return;
        
	    $.ajax({
	        url: CONTEXT_ROOT + '/config/addPopup',
	        type: 'POST',
	        data: $('#popupAddForm').serialize(),
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
	
	
	$(document).on('click', '.popupEditButton', function (e) {
        e.preventDefault();
        if (!$('#popupEditForm').valid())
            return;

        $.ajax({
	        url: CONTEXT_ROOT + '/config/editPopup',
	        type: 'POST',
	        data: $('#popupEditForm').serialize(),
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
}).apply( this, [ jQuery ]);

function popupDelete() {
	var seq = $("[name='modalSeq']").val();
		$.ajax({
			url: CONTEXT_ROOT + "config/delete",
			type: "POST",
			data: { "seq": seq },
			success: function(response) {
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
					window.location.href = CONTEXT_ROOT + "config/popuplist";
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

