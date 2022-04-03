$(document).on('click', '.member-id', function (e) {
    e.preventDefault();
    let memberSeq = $(this).data('key');

    let detailWindow = window.open(CONTEXT_ROOT + 'member/popup_detail?idx=' + memberSeq, 'Member Detail', features);
    detailWindow.onbeforeunload = function () {
        window.location.reload();
    }
});