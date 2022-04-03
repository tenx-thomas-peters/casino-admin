$(document).ready(function () {
    $('select[name=selSeq]').change(function () {
        let store = '';
        let distributor = '';
        let subHeadquarter = '';

        $('.storeSeq').text('');
        $('.distributorSeq').text('');
        $('.subHeadquarterSeq').text('');

        let item = partnerList.filter(function (it) {
            return it.seq === $('select[name=selSeq]').val();
        });

        switch(item[0].userType) {
            case 3:
                subHeadquarter = partnerList.filter(function (it) {
                    return it.seq === $('select[name=selSeq]').val();
                });
                break;
            case 2:
                distributor = partnerList.filter(function (it) {
                    return it.seq === $('select[name=selSeq]').val();
                });
                subHeadquarter = partnerList.filter(function (it) {
                    return it.seq === distributor[0].subHeadquarterSeq;
                });
                break;
            case 1:
                store = partnerList.filter(function (it) {
                    return it.seq === $('select[name=selSeq]').val();
                });
                distributor = partnerList.filter(function (it) {
                    return it.seq === store[0].distributorSeq;
                });
                subHeadquarter = partnerList.filter(function (it) {
                    return it.seq === distributor[0].subHeadquarterSeq;
                });
                break;
            default:
                break;
        }

        if (store !== '' && store.length !== 0) {
            $('.storeSeq').text(store[0].id);
            $('.storeSeq').attr('data-key', store[0].seq);
        } else {
            $('.storeSeq').text('');
            $('.storeSeq').attr('data-key', '');
        }

        if (distributor !== '' && distributor.length !== 0) {
            $('.distributorSeq').text(distributor[0].id);
            $('.distributorSeq').attr('data-key', distributor[0].seq);
        } else {
            $('.distributorSeq').text('');
            $('.distributorSeq').attr('data-key', '');
        }

        if (subHeadquarter !== '' && subHeadquarter.length !== 0) {
            $('.subHeadquarterSeq').text(subHeadquarter[0].id);
            $('.subHeadquarterSeq').attr('data-key', subHeadquarter[0].seq);
        } else {
            $('.subHeadquarterSeq').text('');
            $('.subHeadquarterSeq').attr('data-key', '');
        }
    });

    $('select[name=selSeq]').trigger('change');

    $('.add-memo').click(function() {
        let inner = '<tr class="json-data">';
        inner += '<td class="text-center">';
        inner += '<p class="date" style="margin: 0">' + moment().format('YYYY-MM-DD') + '</p>';
        inner += '<p class="time" style="margin: 0">' + moment().format('HH:mm:ss') + '</p>';
        inner += '</td>';
        inner += '<td><textarea name="content" style="width: 100%;"></textarea></td>';
        inner += '<td class="text-center"><a class="update-memo">[' + txtEdit + ']</a><a class="delete-memo">[' + txtDelete + ']</a></td>';
        inner += '</tr>';

        $('#memberDetailModal').find('#memo-list').find('tbody').append(inner);

        $('#memberDetailModal .delete-memo').click(function() {
            $(this).parents('tr').remove();
        });
    });

    $('.delete-memo').click(function() {
        $(this).parents('tr').remove();
    });

    $('.erase-color').click(function () {
        $('#memberDetailModal').find('.input-group.color i').css('background', '');
        $('#memberDetailModal').find('input[name=color]').val('');
    });

    $('.write-note').click(function () {
        let memberSeq = $('input[name=seq]').val();
        let writeNoteWindow = window.open(CONTEXT_ROOT + 'member/popup_write?userid='+memberSeq, 'Write a Note', 'width=' + screen.availWidth / 2 + ', height=600');
        writeNoteWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });

    $('.money-change').click(function () {
        let memberSeq = $('input[name=seq]').val();
        let moneyChangeWindow = window.open(CONTEXT_ROOT + 'member/popup_moneychange?idx=' + memberSeq + '&act=money', 'Money Change', 'width='+screen.availWidth / 2+',height=600');
        moneyChangeWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });

    $('.bet-history').click(function () {
        let memberSeq = $('input[name=seq]').val();
        let betWindow = window.open(CONTEXT_ROOT + 'member/popup_bet?mem_sn='+memberSeq, 'Betting Summary', 'width='+(screen.availWidth - 100)+', height=600');
        betWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });

    $('.access-log').click(function () {
        let memberId = $('.member-id').text();
        let accessLogWindow = window.open(CONTEXT_ROOT + 'member/popup_loginlist?searchField=0&searchValue=' + memberId, 'Access Log', 'width='+screen.availWidth * 4 / 5 + ', height=600');
        accessLogWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });

    $('.update-member').click(function () {
        let form = $('#member-update-form');
        let formData = new FormData(form[0]);
        formData.append('storeSeq', $('.storeSeq').data('key') !== undefined ? $('.storeSeq').data('key') : '');
        formData.append('distributorSeq', $('.distributorSeq').data('key') !== undefined ? $('.distributorSeq').data('key') : '');
        formData.append('subHeadquarterSeq', $('.subHeadquarterSeq').data('key') !== undefined ? $('.subHeadquarterSeq').data('key') : '');

        let memo = [];
        $('#memo-list').find('.json-data').each(function() {
            let date = $(this).find('.date').text();
            let time = $(this).find('.time').text();
            let contents = $(this).find('textarea[name=content]').val();

            memo.push({
                hour: date + ' ' + time,
                contents: contents
            });
        });

        formData.append('memo', JSON.stringify(memo));

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
                        window.location.reload();
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

    $('.money-history').click(function () {
        let memberSeq = $('input[name=seq]').val();
        let top = 100;
        let left = 100;
        var width = screen.availWidth * 5 / 6;
        var height = 800;
        let detailWindow = window.open(CONTEXT_ROOT + 'log/member/money/historydata?memberSeq=' + memberSeq,
            'Member money history',
            'width='+ width + ',height=' + height + ',top=' + top + ',left=' + left);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });

    $('.mileage-history').click(function () {
        let memberSeq = $('input[name=seq]').val();
        let top = 100;
        let left = 100;
        var width = screen.availWidth * 5 / 6;
        var height = 800;
        let detailWindow = window.open(CONTEXT_ROOT + 'log/member/mileage/historydata?memberSeq=' + memberSeq,
            'Member money history',
            'width='+ width + ',height=' + height + ',top=' + top + ',left=' + left);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });

    $('.member-deposit').click(function () {
        let memberId = $('.member-id').text();
        let top = 100;
        let left = 100;
        var width = screen.availWidth * 5 / 6;
        var height = 800;
        let detailWindow = window.open(CONTEXT_ROOT + 'member/popup_charge?IDOrNickname=0&IDOrNickNameValue=' + memberId,
            'Member Deposit history',
            'width='+ width + ',height=' + height + ',top=' + top + ',left=' + left);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });

    $('.member-withdrawal').click(function () {
        let memberId = $('.member-id').text();
        let top = 100;
        let left = 100;
        var width = screen.availWidth * 5 / 6;
        var height = 800;
        let detailWindow = window.open(CONTEXT_ROOT + 'member/popup_exchange?IDOrNickname=0&IDOrNickNameValue=' + memberId,
            'Member Withdrawal history',
            'width='+ width + ',height=' + height + ',top=' + top + ',left=' + left);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });

    $('.member-money').click(function () {
        let memberSeq = $('input[name=seq]').val();
        let moneyChangeWindow = window.open(CONTEXT_ROOT + 'member/popup_moneychange?idx=' + memberSeq + '&act=money', 'Money Change', 'width='+screen.availWidth / 2+',height=600');
        moneyChangeWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });

    $('.recommended-person').click(function () {
        let memberSeq = $('input[name=seq]').val();
        let joinerWindow = window.open(CONTEXT_ROOT + 'member/popup_joiners?sn=' + memberSeq, 'Joiner', 'width='+screen.availWidth / 2+',height=600');
        joinerWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });

    $('.member-inbox').click(function () {
        let memberId = $('.member-id').text();
        let top = 100;
        let left = 100;
        var width = screen.availWidth * 5 / 6;
        var height = 800;
        let detailWindow = window.open(CONTEXT_ROOT + 'memo/popup_memo?selectType=2&keyword=' + memberId,
            'Member Deposit history',
            'width='+ width + ',height=' + height + ',top=' + top + ',left=' + left);
        detailWindow.onbeforeunload = function () {
            window.location.reload();
        }
    });
});