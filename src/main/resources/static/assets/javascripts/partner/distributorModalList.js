$(document).on('click', '.progress-bet', function (e) {
    e.preventDefault();
    let seq = $(this).parents('tr').find('.distributor-seq').data('key');
    let betWindow = window.open(CONTEXT_ROOT + 'member/popup_bet?mem_sn='+seq, 'Betting Summary', 'width='+(screen.availWidth - 100)+', height=600');
    betWindow.onbeforeunload = function () {
        window.location.reload();
    }
});

let today = moment().format('YYYY-MM-DD');

$(document).on('click', '.headquarter-storeCount', function (e) {
    e.preventDefault();
    let headquarterSeq = $(this).parents('tr').find('.distributor-seq').data('key');
    window.open('./shopMember?seq=' + headquarterSeq + '&fromApplicationTime=' + today + '&toApplicationTime=' + today + '&userType=' + 2, '', partnerFeatures);
});

$(document).on('click', '.headquarter-memberCount', function (e) {
    e.preventDefault();
    let headquarterSeq = $(this).parents('tr').find('.distributor-seq').data('key');
    window.open('./Member?seq=' + headquarterSeq + '&fromApplicationTime=' + today + '&toApplicationTime=' + today + '&userType=' + 2, '', partnerFeatures);
});
