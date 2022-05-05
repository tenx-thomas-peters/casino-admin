$(document).ready(function(){
	
});

$('.question-title').on('click', function() {
	let seq = $(this).data('seq');
	console.log(seq);
	window.location.assign(CONTEXT_ROOT + 'board/getQuestionBySeq?seq=' + seq);
});

$('.question-nickname').on('click', function() {
	let memberSeq = $(this).data('mseq');
	console.log(memberSeq);

	let detailWindow = window.open(CONTEXT_ROOT + 'member/popup_detail?idx=' + memberSeq, 'Member Detail', features);
	detailWindow.onbeforeunload = function () {
		window.location.reload();
	}
});

$('.edit').on('click', function() {
	let seq = $(this).data('key');
	console.log(seq);
	window.location.assign(CONTEXT_ROOT + 'board/write?seq=' + seq);
});

function batchDelete() {
	var checkArray = document.getElementsByClassName("checkboxes");
	var ids = "";
	for(var i = 0; i < checkArray.length; i++){
		if(checkArray[i].checked) {
		    ids += checkArray[i].name + ",";
		}
	}
	if(ids == "") {
		new PNotify({
            title: 'Faild!',
            text: 'Please Check it.',
            type: 'error',
            buttons: {
                closer: true,
                sticker: false
            }
		});
	} else {
		$.ajax({
			url: CONTEXT_ROOT + "board/delete",
			type: "POST",
			data: { ids: ids },
			success: function(response) {
				for(var i = 0; i < checkArray.length; i++){
					if(checkArray[i].checked) {
						checkArray[i].checked = false;
					}
				}
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
}

function deleteById() {
	var id = $('input[name=seq]').val();
	$.ajax({
		url: CONTEXT_ROOT + "board/delete",
		type: "POST",
		data: { ids: id },
		success: function(response) {
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

function allCheck(){
	var allCheck = $("#allCheck")[0].checked;
	var checkArray = document.getElementsByClassName("checkboxes");
	for(var i = 0; i < checkArray.length; i++){
		checkArray[i].checked = allCheck;
	}
};

function batchNoteDelete() {
	var checkArray = document.getElementsByClassName("checkboxes");
	var ids = "";
	for(var i = 0; i < checkArray.length; i++){
		if(checkArray[i].checked) {
		    ids += checkArray[i].name + ",";
		}
	}
	if(ids == "") {
		new PNotify({
            title: 'Faild!',
            text: 'Please Check it.',
            type: 'error',
            buttons: {
                closer: true,
                sticker: false
            }
		});
	} else {
		$.ajax({
			url: CONTEXT_ROOT + "board/delete",
			type: "POST",
			data: { ids: ids },
			success: function(response) {
				for(var i = 0; i < checkArray.length; i++){
					if(checkArray[i].checked) {
						checkArray[i].checked = false;
					}
				}
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
	
}

function recommend() {
	var seq = $('input[name=seq]').val();
	$.ajax({
		url: CONTEXT_ROOT + "board/hits",
		type: "POST",
		data: { ids: seq },
		success: function(response) {
			if(response.success) {
				new PNotify({
                    title: 'Success!',
                    text: "Operate Success!",
                    type: 'success',
                    buttons: {
                        closer: true,
                        sticker: false
                    }
                });
				setTimeout(function() {
                    window.location.reload();
                }, 1000);
			}else {
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

function hits(){
	var checkArray = document.getElementsByClassName("checkboxes");
	var ids = "";
	for(var i = 0; i < checkArray.length; i++){
		if(checkArray[i].checked) {
		    ids += checkArray[i].name + ",";
		}
	}
	if(ids == "") {
		new PNotify({
            title: 'ERROR!',
            text: '실행할 내용을 선택하십시오',
            type: 'error',
            buttons: {
                closer: true,
                sticker: false
            }
		});
	} else {
		$.ajax({
			url: CONTEXT_ROOT + "board/hits",
			type: "POST",
			data: { ids: ids },
			success: function(response) {
				if(response.success) {
					new PNotify({
	                    title: 'Success!',
	                    text: "Operate Success!",
	                    type: 'success',
	                    buttons: {
	                        closer: true,
	                        sticker: false
	                    }
	                });
					setTimeout(function() {
	                    window.location.reload();
	                }, 1000);
				}else {
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
}