function memberDetailPopup(memberSeq) {
    $.ajax({
        url: CONTEXT_ROOT + '/member/getBySeq',
        type: 'get',
        data: {seq: memberSeq},
        dataType: 'json',
        success: function (res) {
            if (res.success) {
                $('#memberDetailModal').modal();
                $.each(res.result.member, function(key, value) {
                    $('#memberDetailModal').find('p[name=' + key + ']').text(value);
                    $('#memberDetailModal').find('input[name=' + key + ']').val(value);
                });

                $('#memberDetailModal').find('.input-group.color i').css('background', res.result.member.color);

                let inner = '';
                $.each(res.result.levelList, function(key, value) {
                    inner += '<option value="' + value.seq + '"';
                    inner += res.result.member.levelSeq === value.seq ? ' selected>' : '>';
                    inner += value.levelName;
                    inner += '</option>';
                });
                $('#memberDetailModal').find('select[name=levelSeq]').html(inner);

                inner = '';
                $.each(res.result.statusList, function(key, value) {
                    inner += '<option value="' + value.dictValue + '"';
                    inner += res.result.member.status.toString() === value.dictValue.toString() ? ' selected>' : '>';
                    inner += value.strValue;
                    inner += '</option>';
                });
                $('#memberDetailModal').find('select[name=status]').html(inner);

                $('#memberDetailModal').find('#bettingPossibleOn').val(0);
                $('#memberDetailModal').find('#bettingPossibleOff').val(1);
                $('#memberDetailModal').find('#recommendableOn').val(0);
                $('#memberDetailModal').find('#recommendableOff').val(1);

                if (res.result.member.bettingPossible === 0) {
                    $('#memberDetailModal').find('#bettingPossibleOn').prop('checked', true);
                } else {
                    $('#memberDetailModal').find('#bettingPossibleOff').prop('checked', true);
                }

                if (res.result.member.recommendable === 0) {
                    $('#memberDetailModal').find('#recommendableOn').prop('checked', true);
                } else {
                    $('#memberDetailModal').find('#recommendableOff').prop('checked', true);
                }

                inner = '';
                $.each(res.result.storeList, function (key, item) {
                    let selected = res.result.member.storeSeq === item.seq ? 'selected' : '';
                    inner += '<option value="' + item.seq + '" ' + selected + '>' + item.id + '</option>';
                });
                $('#memberDetailModal').find('select[name=storeSeq]').html(inner);

                let memo = res.result.member.memo !== null ? JSON.parse(res.result.member.memo) : '';

                inner = '';
                inner += '<tr>';
                inner += '<td></td>';
                inner += '<td><textarea name="content" style="width: 100%;"></textarea></td>';
                inner += '<td class="text-center"><a class="add-memo">[' + txtAdd + ']</a></td>';
                inner += '</tr>';

                if (memo !== '') {
                    $.each(memo, function(key, value) {
                        inner += '<tr class="json-data">';
                        inner += '<td class="text-center">';
                        inner += '<p class="date" style="margin: 0">' + value.hour.split(" ")[0] + '</p>';
                        inner += '<p class="time" style="margin: 0">' + value.hour.split(" ")[1] + '</p>';
                        inner += '</td>';
                        inner += '<td><textarea name="content" style="width: 100%;">' + value.contents + '</textarea></td>';
                        inner += '<td class="text-center"><a class="update-memo">[' + txtEdit + ']</a><a class="delete-memo">[' + txtDelete + ']</a></td>';
                        inner += '</tr>';
                    });
                }

                $('#memberDetailModal').find('#memo-list').find('tbody').html(inner);

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
                    $('#memberDetailModal').find('input[name=color]').val(' ');
                });
            }
        },
        error: function (err) {
            new PNotify({
                title: 'Error!',
                text: err,
                type: 'error',
                buttons: {
                    closer: true,
                    sticker: false
                }
            });
        }
    });

    $('.update-member').on('click', function () {
        let form = $('#member-update-form');
        let formData = new FormData(form[0]);

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

    $(document).on('click', '.close-detail-modal', function (e) {
        e.preventDefault();
        $('#memberDetailModal').find('#member-update-form')[0].reset();
        $('#memberDetailModal').find('#memo-list').find('tbody').html('');
        $('#memberDetailModal').modal('hide');
    });
}

function modifyHoldingMoneyPopup(memberSeq) {
    let form = $('#member-holding-money-form');
    form.validate({
        errorElement: 'span', //default input error message container
        errorClass: 'help-block help-block-error', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        rules: {
            prevMoneyAmount: {
                required: true,
                number: true
            },
            prevMileageAmount: {
                required: true,
                number: true
            },
            variableMoney: {
                required: true,
                number: true
            }
        },
        highlight: function (element) { // hightlight error inputs
            $(element)
                .closest('.form-group').addClass('has-error'); // set error class to the control group
        },
        unhighlight: function (element) { // revert the change done by hightlight
            $(element)
                .closest('.form-group').removeClass('has-error'); // set error class to the control group
        },
        success: function (label) {
            label
                .closest('.form-group').removeClass('has-error'); // set success class to the control group
        },
        submitHandler: function (form) {
            $.ajax({
                url: CONTEXT_ROOT + '/member/updateHoldingMoney',
                type: 'POST',
                data: $(form).serialize(),
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
        }
    });

    $.ajax({
        url: CONTEXT_ROOT + '/member/getBySeq',
        type: 'get',
        data: {seq: memberSeq},
        dataType: 'json',
        success: function (res) {
            if (res.success) {
                $(form).find('input[name=memberSeq]').val(memberSeq);
                $(form).find('p[name=id]').text(res.result.member.id);
                $(form).find('input[name=prevMoneyAmount]').val(res.result.member.moneyAmount);
                $(form).find('input[name=prevMileageAmount]').val(res.result.member.mileageAmount);

                $('#modifyHoldingMoneyModal').modal();
            }
        },
        error: function (err) {
           new PNotify({
               title: 'Error!',
               text: err,
               type: 'error',
               buttons: {
                   closer: true,
                   sticker: false
               }
           });
        }
    });

    $('.update-holding-money').click(function (e) {
        e.preventDefault();

        if (!form.valid())
            return;

        form.submit();
    });

    $('.close-holding-money-modal').click(function(e) {
        form.find('.form-group').each(function() {
            $(this).removeClass('has-error');
            $(this).find('.help-block.help-block-error').text('');
        });

        $('#modifyHoldingMoneyModal').modal('hide');
    });
}

function bettingSummaryPopup(memberSeq) {
    $('#bettingSummaryModal').find('input[name=memberSeq]').val(memberSeq);

    $.ajax({
        url: CONTEXT_ROOT + '/log/slotloglist_ajax',
        type: 'post',
        dataType: 'json',
        data: $('#bettingSummaryForm').serialize(),
        success: function (res) {
            if (res.success) {
                let records = res.result;

                let inner = '';
                $.each(records, function (key, entity) {
                    inner += '<tr>';
                    inner += '<td class="text-center"><p>' + entity.checkTime + '</p></td>';
                    inner += '<td class="text-center"><p>' + entity.memberID + '</p></td>';
                    inner += '<td class="text-center"><p>' + entity.memberNickname + '</p></td>';
                    inner += '<td class="text-center">playing game</td>';
                    inner += '<td class="text-center"><p>slot</p><p>baccarat</p></td>';
                    inner += '<td class="text-center">';
                    let tmp = entity.type == 0 ? entity.bettingAmount : 0;
                    inner += '<p>' + tmp + '</p>';
                    inner += '<p>' + tmp + '</p>';
                    inner += '</td>';
                    inner += '<td class="text-center">';
                    tmp = entity.type == 0 ? entity.winningAmount : 0;
                    inner += '<p>' + tmp + '</p>';
                    inner += '<p>' + tmp + '</p>';
                    inner += '</td>';
                    inner += '<td class="text-center">';
                    tmp = entity.type == 0 ? entity.betCount : 0;
                    inner += '<p>' + tmp + '</p>';
                    inner += '<p>' + tmp + '</p>';
                    inner += '</td>';
                    inner += '<td class="text-center">';
                    inner += '<p>' + entity.memberSlotRate + '</p>';
                    inner += '<p>' + entity.memberBaccaratRate + '</p>';
                    inner += '</td>';
                    inner += '<td class="text-center">';
                    inner += '<p>' + entity.storeID + '</p>';
                    inner += '</td>';
                    inner += '<td class="text-center">';
                    inner += '<p>' + entity.storeSlotRate + '%</p>';
                    inner += '<p>' + entity.storeBaccaratRate + '%</p>';
                    inner += '</td>';
                    inner += '<td class="text-center">';
                    tmp = entity.type == 0 ? entity.storeRollingAmount : 0
                    inner += '<p>' + tmp + '</p>';
                    inner += '<p>' + tmp + '</p>';
                    inner += '</td>';
                    inner += '<td class="text-center">';
                    inner += '<p>' + entity.distributorID + '</p>';
                    inner += '</td>';
                    inner += '<td class="text-center">';
                    inner += '<p>' + entity.distributorSlotRate + '%</p>';
                    inner += '<p>' + entity.distributorBaccaratRate + '%</p>';
                    inner += '</td>';
                    inner += '<td class="text-center">';
                    tmp = entity.type == 0 ? entity.distributorRollingAmount : 0;
                    inner += '<p>' + tmp + '</p>';
                    inner += '<p>' + tmp + '</p>';
                    inner += '</td>';
                    inner += '<td class="text-center">';
                    inner += '<p>' + entity.subHeadquarterID + '</p>';
                    inner += '</td>';
                    inner += '<td class="text-center">';
                    inner += '<p>' + entity.subHeadquarterSlotRate + '%</p>';
                    inner += '<p>' + entity.subHeadquarterBaccaratRate + '%</p>';
                    inner += '</td>';
                    inner += '<td class="text-center">';
                    tmp = entity.type == 0 ? entity.subHeadquarterRollingAmount : 0
                    inner += '<p>' + tmp + '</p>';
                    inner += '<p>' + tmp + '</p>';
                    inner += '</td>';
                    inner += '</tr>';
                });

                if (res.result.length == 0) {
                    inner = '<tr><td class="text-center" colspan="18" style="padding: 20px;">No Data</td></tr>'
                }
                $('#betting-list').find('tbody').html(inner);

                $('#bettingSummaryModal').modal();
            }
        },
        error: function (err) {
            console.log(err);
        }
    });
}

$('.search-betting-summary').click(function(e) {
    e.preventDefault();
    bettingSummaryPopup($('#bettingSummaryModal').find('input[name=memberSeq]').val());
});

function memberNotePopup(memberSeq, memberID) {
    let form = $('#member-note-form');

    $(form).find('input[name=receiver]').val(memberSeq);
    $(form).find('input[name=recipient]').val(memberID);

    $('#memberNoteNodal').modal();

    $('.write-member-note').click(function(e) {
        e.preventDefault();

        if (!form.valid())
            return;

        form.submit();
    });

    $('.close-member-note-modal').click(function(e) {
        form.find('.form-group').each(function() {
            $(this).removeClass('has-error');
            $(this).find('.help-block.help-block-error').text('');
        });

        $(form)[0].reset();
        $(form).validate().resetForm();

        $('#memberNoteNodal').modal('hide');
    });
}

function moneyRequestDecline(seq) {
	$.ajax({
		url: CONTEXT_ROOT + "log/moneyrequest/decline",
			type: "GET",
			data: {"seq": seq },
		success: function(response) {
			if(response.success){
				new PNotify({
                    title: 'Success!',
                    text: response.message,
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
                    text: response.message,
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

            $.magnificPopup.close();
		}
	});
}

$('#moneylog-datatable .declineBtn').on('click', function() {
	$('#declineConfirmModal #confirmBtn').attr('data-seq', $(this).data('seq'));
	$('#declineConfirmModal').modal();
});

$('#declineConfirmModal #confirmBtn').on('click', function() {
	moneyRequestDecline($(this).data('seq'));
})

function yesterdaySubmit() {
	$('#fromProcessTime').val(moment().add(-1, 'days').format('YYYY-MM-DD'));
	$('#toProcessTime').val(moment().add(-1, 'days').format('YYYY-MM-DD'));
	$('#formSearch').submit();
};

function todaySubmit() {
	$('#fromProcessTime').val(moment().format('YYYY-MM-DD'));
	$('#toProcessTime').val(moment().format('YYYY-MM-DD'));
	$('#formSearch').submit();
}
