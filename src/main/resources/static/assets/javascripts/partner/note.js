$(document).on('click', '.write-member-note', function (e) {
    e.preventDefault();
    if (!$('#member-note-form').valid())
        return;

    $.ajax({
        url: CONTEXT_ROOT + '/partner2/confirmNote',
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

function addChar(note_num){

    console.log(note_num);
    let content_string = ""
    switch (note_num){
        case 0:
            content_string = basicSetting.ccRepaymentInformation;break;
        case 1:
            content_string = basicSetting.crequestProcessingCompleted;break;
        case 2:
            content_string = basicSetting.ccRecommendationFromAcquaintances;break;
        case 3:
            content_string = basicSetting.ccReLoginRatingUp;break;
        case 4:
            content_string = basicSetting.ccLevel45UpAccount;break;
        case 5:
            content_string = basicSetting.ccLevel3UpAccount;break;
        case 6:
            content_string = basicSetting.ccLevel2UpAccount;break;
        case 7:
            content_string = basicSetting.ccLevel1DownAccount;break;
        case 8:
            content_string = basicSetting.ccLadderBettingX;break;
        case 9:
            content_string = basicSetting.ccGameErrorReport;break;
    }

    $("#content_string").text(content_string);
}