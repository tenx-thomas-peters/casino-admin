$(document).ready(function(){
	if(levelSeq != null) {
		$('#writeForm select[name="writer"]').append($('<option data-key="'+levelSeq+'" value="' + levelName + '">' + levelName + '</option>'));
	}
	
	$('#levelList').change(function(){
		console.log("asdfasdf");
		var levelSeq = $('#levelList').val();
		console.log(levelSeq);
		$.ajax({
			url: "getNickNameList",
			type: "GET",
			data: {"levelSeq": levelSeq },
			success: function(response){
				$('#writeForm select[name="writer"]').html($('<option value=""></option>'));
				response.map(function(item) {
					$('#writeForm select[name="writer"]').append($('<option data-key="'+item.seq+'" value="' + item.nickname + '">' + item.nickname + '</option>'));
				})
			}
		});
	});
});

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

$('#writer').change(function(e){
    let memberSeq = $(e.currentTarget.selectedOptions).attr('data-key');
	var sender = $('#writer').val();
	$('#writeForm').find('input[name=nickname]').val(sender);
	$('#writeForm').find('input[name=sender]').val(memberSeq);
});

$('#classification').change(function(){
	if($('#classification').val()==0 ){
		$.ajax({
			url:"getSiteList",
			type:"GET",
			data:{},
			success:function(response){
				$('#writeForm select[name="site"]').html($('<option value=""></option>'));
				response.map(function(item){
					$('#writeForm select[name="site"]').append($('<option value="' + item.siteDomain + '">' + item.siteDomain + '</option>'));
				})
			}
		});
	}else{
		$('#writeForm select[name="site"]').html('');
	}
})

$(document).on('click', '.answer', function (e) {
	console.log('answer loading...');
    e.preventDefault();
    if (!$("#answerForm").valid())
        return;
	$.ajax({
    	url: "answer",
        type: 'POST',
        data: $('#answerForm').serialize(),
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
});

$(document).on('click', '.update-memo', function (e) {
    e.preventDefault();
    let seq = $("#commentSeq").val();

    let top = 70;
    let left = 70;
    var width = screen.availWidth * 1 / 2;
    var height = 500;
    
    let detailWindow = window.open(CONTEXT_ROOT + 'board/getCommentById?seq=' + seq, 'Comment Detail',  'width='+ width + ',height=' + height + ',top=' + top + ',left=' + left);
    detailWindow.onbeforeunload = function () {
        window.location.reload();
    }
});


$(document).on('click', '.uploadFile', function() {
	let file = $('#uploadPath')[0].files[0];
	var formData = new FormData();
	formData.append("uploadPath", file);
	$.ajax({
        type: "POST",
        url: "uploadFile",
        async: true,
        data: formData,
        contentType: false,
        processData: false,
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
});

$(document).on('click', '.enroll', function (e) {
    e.preventDefault();
    if (!$("#writeForm").valid())
        return;
   	  
    let content = $('#writeForm textarea[name="content"]').code();
	let form = new FormData($('#writeForm')[0]);
	form.append('content', content);
	
	$.ajax({
    	url: "enroll",
        type: 'POST',
        data: form,
        dataType: 'json',
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

function updateCommentById(){
	$.ajax({
		url:"updateCommentById",
		type:"POST",
		data: $('#commentForm').serialize(),
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