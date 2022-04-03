$(document).on('click', '.write-member-note', function (e) {
    e.preventDefault();
    if (!$('#member-note-form').valid())
        return;

    $.ajax({
        url: CONTEXT_ROOT + '/member/write_note',
        type: 'POST',
        data: $('#member-note-form').serialize(),
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