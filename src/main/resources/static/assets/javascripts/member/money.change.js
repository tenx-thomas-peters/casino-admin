let form = $('#member-holding-money-form');
form.validate({
    errorElement: 'span', //default input error message container
    errorClass: 'help-block help-block-error', // default input error message class
    focusInvalid: false, // do not focus the last invalid input
    rules: {
        prevMoneyAmount: {
            required: true,
            number: true
        },
        prevMileageAmount: {
            required: true,
            number: true
        },
        variableMoney: {
            required: true,
            number: true
        }
    },
    highlight: function (element) { // hightlight error inputs
        $(element)
            .closest('.form-group').addClass('has-error'); // set error class to the control group
    },
    unhighlight: function (element) { // revert the change done by hightlight
        $(element)
            .closest('.form-group').removeClass('has-error'); // set error class to the control group
    },
    success: function (label) {
        label
            .closest('.form-group').removeClass('has-error'); // set success class to the control group
    },
    submitHandler: function (form) {
        $.ajax({
            url: CONTEXT_ROOT + '/member/updateHoldingMoney',
            type: 'POST',
            data: $(form).serialize(),
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

$('.update-holding-money').click(function (e) {
    e.preventDefault();

    if (!form.valid())
        return;

    form.submit();
});