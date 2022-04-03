$(document).ready(function() {
	
	$(document).on('click', '.store-add', function (e) {
        e.preventDefault();
        if (!$('#store-add-form').valid())
            return;

        $('#store-add-form').submit();
    });
	
	$("#store-add-form").validate({
        rules: {
            password: { minlength: 8 }
        },
        messages: {
            password: { minlength: "Min is 8" }
        },
        submitHandler: function(form) {
            $.ajax({
            	url: CONTEXT_ROOT + "/partner2/addStore_ajax",
                type: 'POST',
                data: $('#store-add-form').serialize(),
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
        }
    });
	
});