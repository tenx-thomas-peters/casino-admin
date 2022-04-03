(function( $ ) {
	'use strict';

	var memberListInit = function() {
	    var $table = $('#member-list');
	    var checkedMembers = [];

	    $("#member-add-form").validate({
	        rules: {
	            password: { minlength: 8 }
            },
            messages: {
                password: { minlength: "Min is 8" }
            }
        });

        $table.find('.group-checkable').change(function () {
            var set = jQuery(this).attr("data-set");
            var checked = jQuery(this).is(":checked");
            jQuery(set).each(function () {
                if (checked) {
                    $(this).prop("checked", true);
                    $(this).parents('tr').addClass("active");
                } else {
                    $(this).prop("checked", false);
                    $(this).parents('tr').removeClass("active");
                }
            });
        });

        $table.on('change', 'tbody tr .checkboxes', function () {
            $(this).parents('tr').toggleClass("active");
        });

        $(document).on('click', '.modal-member-add', function () {
            let regWindow = window.open(CONTEXT_ROOT + 'member/add', 'Member Registration', 'width='+screen.availWidth / 2+',height=600');
            regWindow.onbeforeunload = function () {
                window.location.reload();
            }
        });

        $(document).on('click', '.member-stop', function (e) {
            e.preventDefault();

            $('#memberStopModal').modal('hide');

            var ids = '';
            $('.checkboxes').each(function(index, el) {
                if ($(el).is(':checked'))
                    ids += $(el).data('key') + ',';
            });

            if (ids === '') {
                new PNotify({
                    title: 'Error',
                    text: 'Please select items!',
                    type: 'error',
                    buttons: {
                        closer: true,
                        sticker: false
                    }
                });
            } else {
                $.ajax({
                    url: CONTEXT_ROOT + 'member/stop_member_ajax',
                    type: 'POST',
                    data: {ids: ids},
                    dataType: 'json',
                    success: function (res) {
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
                    error: function (err) {
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

        $(document).on('click', '.member-delete', function (e) {
            e.preventDefault();
            $('#memberDeleteModal').modal('hide');

            var ids = '';
            $('.checkboxes').each(function(index, el) {
                if ($(el).is(':checked'))
                    ids += $(el).data('key') + ',';
            });

            if (ids === '') {
                new PNotify({
                    title: 'Error',
                    text: 'Please select items!',
                    type: 'error',
                    buttons: {
                        closer: true,
                        sticker: false
                    }
                });
            } else {
                $.ajax({
                    url: CONTEXT_ROOT + '/member/delete_ajax',
                    type: 'POST',
                    data: {ids: ids},
                    dataType: 'json',
                    success: function (res) {
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
                    error: function (err) {
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

        $(document).on('click', '.member-update', function (e) {
            e.preventDefault();

            let formData = new FormData();
            formData.append("seq", $(this).parents('tr').find('.checkboxes').data('key'));
            formData.append("levelSeq", $(this).parents('tr').find('select[name=levelSeq]').val());
            formData.append("status", $(this).parents('tr').find('select[name=status]').val());
            formData.append("siteDomain", $(this).parents('tr').find('select[name=siteDomain]').val());

            $.ajax({
                url: CONTEXT_ROOT + '/member/update_ajax',
                type: 'POST',
                dataType: 'json',
                cache : false,
                contentType : false,
                processData : false,
                data: formData,
                success: function (res) {
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
                error: function (err) {
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

        $(document).on('click', '.member-id', function (e) {
            e.preventDefault();
            let memberSeq = $(this).parents('tr').find('.checkboxes').data('key');

            let detailWindow = window.open(CONTEXT_ROOT + 'member/popup_detail?idx=' + memberSeq, 'Member Detail', features);
            detailWindow.onbeforeunload = function () {
                window.location.reload();
            }
        });

        $(document).on('click', '.member-nickname', function(e) {
            e.preventDefault();
            let memberSeq = $(this).parents('tr').find('.checkboxes').data('key');
            let memberID = $(this).parents('tr').find('.member-id').text();

            let memberNote = window.open(CONTEXT_ROOT + 'member/popup_write?userid=' + memberSeq, 'Member Note', 'width=' + screen.availWidth / 2 + ', height=600');
            memberNote.onbeforeunload = function () {
                window.location.reload();
            }
        });

        $(document).on('click', '.member-deposit', function (e) {
            e.preventDefault();
            let memberSeq = $(this).parents('tr').find('.checkboxes').data('key');

            let moneyChangeWindow = window.open(CONTEXT_ROOT + 'member/popup_moneychange?idx=' + memberSeq + '&act=money', 'Member Detail', 'width='+screen.availWidth / 2+',height=600');
            moneyChangeWindow.onbeforeunload = function () {
                window.location.reload();
            }
        });

        $(document).on('click', '.progress-bet', function (e) {
            e.preventDefault();

            let memberSeq = $(this).parents('tr').find('.checkboxes').data('key');
            let betWindow = window.open(CONTEXT_ROOT + 'member/popup_bet?mem_sn='+memberSeq, 'Betting Summary', 'width='+(screen.availWidth - 100)+', height=600');
            betWindow.onbeforeunload = function () {
                window.location.reload();
            }
        });

        $(document).on('click', '.connection-ip', function (e) {
            e.preventDefault();

            let memberId = $(this).parents('tr').find('.member-id').text();

            let accessLogWindow = window.open(CONTEXT_ROOT + 'member/popup_loginlist?searchField=0&searchValue=' + memberId, 'Access Log', 'width='+screen.availWidth * 4 / 5 + ', height=600');
            accessLogWindow.onbeforeunload = function () {
                window.location.reload();
            }
        });
	};

	$(function() {
		memberListInit();
	});
	
	$('.money-history-link').on('click', function() {
		let memberSeq = $(this).data('seq');
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
	
	$('.mileage-history-link').on('click', function() {
		let memberSeq = $(this).data('seq');
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
}).apply( this, [ jQuery ]);