$('.add-memo').click(function() {
    let inner = '<tr class="json-data">';
    inner += '<td class="text-center">';
    inner += '<p class="date" style="margin: 0">' + moment().format('YYYY-MM-DD') + '</p>';
    inner += '<p class="time" style="margin: 0">' + moment().format('HH:mm:ss') + '</p>';
    inner += '</td>';
    inner += '<td><textarea name="content" style="width: 100%;"></textarea></td>';
    inner += '<td class="text-center"><a class="update-memo">[' + txtEdit + ']</a><a class="delete-memo">[' + txtDelete + ']</a></td>';
    inner += '</tr>';

    $('#note-form').find('#memo-list').find('tbody').append(inner);

    $('#note-form .delete-memo').click(function() {
        $(this).parents('tr').remove();
    });
});

$('.delete-memo').click(function() {
    $(this).parents('tr').remove();
});

$('#note-form #confirmNote').on('click', function() {
	let seq = $("#seq").val();
	confirmNote(seq);
});

function confirmNote(seq){
	
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
	
	$.ajax({
		url: CONTEXT_ROOT + "/partner2/confirmNote",
		type: "GET",
		data: {"seq": seq,
			"memo": JSON.stringify(memo)
			},
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
                    window.close();
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
	})
}