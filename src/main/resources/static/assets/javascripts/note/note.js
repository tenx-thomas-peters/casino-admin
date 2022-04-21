$(document).ready(function(){
	// $('#userType').change(function(){
	// 	var level = $("#levelType").val();
	// 	var userType = $("#userType").val();
    //     $('select[name="mSeq"]').html($('<option value=""></option>'));
	// 	getMemberList(level, userType);
	// });
	//
	// $('#levelType').change(function(){
	// 	var level = $("#levelType").val();
	// 	var userType = $("#userType").val();
	// 	getMemberList(level, userType);
	// });
	//
	$('select[name=levelSeq]').trigger('change');
	
	$(document).on('click', '.send-note', function (e) {
        e.preventDefault();
        if (!$('#noteForm').valid())
            return;

        $('#noteForm').submit();
    });
	
	$("#noteForm").validate({
        submitHandler: function(form) {
            $.ajax({
                url: "send",
                type: 'POST',
                data: $('#noteForm').serialize(),
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
                            window.location.reload();
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
        }
    });
	
	$(document).on('click', '.memo-add', function (e) {
        e.preventDefault();
        if (!$('#pNoteForm').valid())
            return;

        $('#pNoteForm').submit();
    });
	
	$("#pNoteForm").validate({
        submitHandler: function(form) {
            $.ajax({
                url: "memoadd",
                type: 'POST',
                data: $('#pNoteForm').serialize(),
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
                            window.location.reload();
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
        }
    });
});

// function getMemberList(level, userType) {
// 	$.ajax({
// 		url: "getMemberList",
// 		type: "GET",
// 		data: { "levelSeq": level, "userType": userType },
// 		success: function(response){
// 			$('select[name="mSeq"]').html($('<option value=""></option>'));
// 			response.map(function(item) {
// 				$('select[name="mSeq"]').append($('<option value="' + item.seq + '">' + item.nickname + '</option>'));
// 			})
// 		}
// 	});
// }