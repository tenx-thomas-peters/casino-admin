$(document).on('click', '.updatePwd', function (e) {
    e.preventDefault();
    if (!$('#pwdForm').valid())
        return;

    $.ajax({
        url: CONTEXT_ROOT + '/member/update_password',
        type: 'POST',
        data: $('#pwdForm').serialize(),
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
                
                window.location.reload();
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
