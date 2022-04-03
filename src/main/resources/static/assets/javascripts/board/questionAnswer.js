$(document).ready(function(){
	$("#content").html(content.replace(',<p>', '<p>'));
});

function getConfig(keyword) {
	$.ajax({
		url: CONTEXT_ROOT + "config/getConfig",
		type: "GET",
		success: function(response) {
			var config = response;
			switch (keyword) {
				case "level1":
					$("#answer").val(config.ccLevel1DownAccount);
					break;
				case "level2":
					$("#answer").val(config.ccLevel2UpAccount);
					break;
				case "level3":
					$("#answer").val(config.ccLevel3UpAccount);
					break;
				case "level45":
					$("#answer").val(config.ccLevel45UpAccount);
					break;
				case "requestProcessingCompleted":
					$("#answer").val(config.crequestProcessingCompleted);
					break;
				case "reLoginRatingUp":
					$("#answer").val(config.ccReLoginRatingUp);
					break;
				case "friendRecommend":
					$("#answer").val(config.ccRecommendationFromAcquaintances);
					break;
				case "ladderBettingX":
					$("#answer").val(config.ccLadderBettingX);
					break;
				case "gameErrorReport":
					$("#answer").val(config.ccGameErrorReport);
					break;
				case "reDepositInformation":
					$("#answer").val(config.ccRepaymentInformation);
					break;
				default:
					break;
			}
		},
		error: function(err) {
		}
	});
}

$('.answer-btn').on('click', function() {
    var answer = $("#answer").val();
    if(answer != "") {
    	$.ajax({
    		url: CONTEXT_ROOT + "board/questionSendAnswer",
    		type: "POST",
    		data: $('#questionAnswerForm').serialize(),
            dataType: 'json',
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
    } else {
		new PNotify({
            title: 'Error!',
            text: "Please input Answer.",
            type: 'error',
            buttons: {
                closer: true,
                sticker: false
            }
		});
    }
});