$('.erase-color').click(function () {
    $('#partnerDistributorDetail').find('.input-group.color i').css('background', '');
    $('#partnerDistributorDetail').find('input[name=color]').val('#000000');
});

$(document).on('click', '.update-partner-distributor-member', function () {
    let form = $('#partner-distributor-update-form');
    let formData = new FormData(form[0]);
    let today = moment().format('YYYY-MM-DD HH:mm:ss');
    let memo = [];
    $('#memo-list').find('.json-data').each(function() {
    	let date = today.split(' ')[0];
        let time = today.split(' ')[1];
        let contents = $(this).find('textarea[name=content]').val();

        memo.push({
            hour: date + ' ' + time,
            contents: contents
        });
    });

    formData.append('memo', JSON.stringify(memo));
    console.log(formData);

    if (!form.valid())
        return;

    $.ajax({
        url: CONTEXT_ROOT + '/member/update_ajax',
        type: 'post',
        data: formData,
        processData: false,
        contentType: false,
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
            console.log(err);
        }
    });
});