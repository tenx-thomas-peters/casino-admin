$(document).on('click', '.progress-bet', function (e) {
    e.preventDefault();
    let seq = $(this).parents('tr').find('.store-seq').data('key');
    let betWindow = window.open(CONTEXT_ROOT + 'member/popup_bet?mem_sn='+seq, 'Betting Summary', 'width='+(screen.availWidth - 100)+', height=600');
    betWindow.onbeforeunload = function () {
        window.location.reload();
    }
});

let today = moment().format('YYYY-MM-DD');

$(document).on('click', '.store-memberCount', function (e) {
    e.preventDefault();
    let storeSeq = $(this).parents('tr').find('.store-seq').data('key');
    let detailWindow = window.open('./Member?seq=' + storeSeq + '&fromApplicationTime=' + today + '&toApplicationTime=' + today + '&userType=' + 1, '', partnerFeatures);
    detailWindow.onbeforeunload = function () {
        window.location.reload();
    }
});