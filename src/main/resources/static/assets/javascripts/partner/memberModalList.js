$(document).on('click', '.progress-bet', function (e) {
    e.preventDefault();
    let seq = $(this).parents('tr').find('.member-seq').data('key');
    let betWindow = window.open(CONTEXT_ROOT + 'member/popup_bet?mem_sn='+seq, 'Betting Summary', 'width='+(screen.availWidth - 100)+', height=600');
    betWindow.onbeforeunload = function () {
        window.location.reload();
    }
});

$(document).on('click', '.member-id', function (e) {
    e.preventDefault();
    let memberSeq = $(this).parents('tr').find('.member-seq').data('key');

    let detailWindow = window.open(CONTEXT_ROOT + 'member/popup_detail?idx=' + memberSeq, '', features);
    detailWindow.onbeforeunload = function () {
        window.location.reload();
    }
});