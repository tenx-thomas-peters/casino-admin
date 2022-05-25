$(document).on('click', '.member-id', function (e) {
    e.preventDefault();
    let memberSeq = $(this).data('key');

    let detailWindow = window.open(CONTEXT_ROOT + 'member/popup_detail?idx=' + memberSeq, 'Member Detail', features);
    detailWindow.onbeforeunload = function () {
        window.location.reload();
    }
});

$('.force-logout').on('click', function() {
    let memberSeq = $(this).data('memberseq');
    $.ajax({
        url: CONTEXT_ROOT + '/member/forceLogout',
        type: 'GET',
        data: {userSeq: memberSeq},
        dataType: 'json',
        success: function (res) {
            if (res.success) {
                new PNotify({
                    title: 'Success!',
                    text: "회원이 로그아웃되였습니다",
                    type: 'success',
                    buttons: {
                        closer: true,
                        sticker: false
                    }
                });
                setTimeout(function() {
                    window.location.reload();
                }, 1000);
            }
            else{
                new PNotify({
                    title: res.code + ' Error!',
                    text: res.message,
                    type: 'error',
                    buttons: {
                        closer: true,
                        sticker: false
                    }
                });
            }
        }
    });
})