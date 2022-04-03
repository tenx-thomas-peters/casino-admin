$(document).ready(function() {
	$('.distributor-add-form').click(function(){
		window.open('./distributorAddForm', 'Distributor Registration', 'width='+screen.availWidth / 2+',height=600');
	});
	
	$(document).on('click', '.distributor-add', function (e) {
        e.preventDefault();
        if (!$('#distributor-add-form').valid())
            return;
        $('#distributor-add-form').submit();
    });
	
	$("#distributor-add-form").validate({
        rules: {
            password: { minlength: 8 }
        },
        messages: {
            password: { minlength: "Min is 8" }
        },
        submitHandler: function(form) {
            $.ajax({
                url: CONTEXT_ROOT + "/partner2/addDistributor_ajax",
                type: 'POST',
                data: $('#distributor-add-form').serialize(),
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