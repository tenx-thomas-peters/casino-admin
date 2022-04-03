$(function(){
/*	$('<input type="hidden" name="pageNo">').appendTo($("#formSearch"));
	$('<input type="hidden" name="pageSize">').appendTo($("#formSearch"));*/
})
function createPagenation(page, url) {	
	/* custom pagenate */
	var curPage = page.current;
	var pageSize = page.size;
	var pageCnt = page.pages;
	var total = page.total;
	$('<input type="hidden" name="pageNo" class="myFont" value=' + curPage + '>').appendTo($("#formSearch"));
    $('<input type="hidden" name="pageSize" class="myFont" value='+ pageSize + '>').appendTo($("#formSearch"));
	var dropDownTag = $("<div id='dropdown'></div>");
	
	dropDownTag.append('<input class="input_select pageSize myFont" readonly="readonly" type="text" value="' + pageSize + '" />');
	var ulTag = $("<ul></ul>");
	var pgCnts = [10, 15, 20, 50, 100];
	
	pgCnts.forEach(function(item){
		ulTag.append($("<li></li>").append('<a class="pagination_btn myFont" style="cursor:pointer" data-key="' + curPage + '" data-val="' + item + '">' + item + '</a>'));
	});
	dropDownTag.append(ulTag);
	dropDownTag.append('<span>&nbsp;records per page</span>');
	
	
	var pagenateTag = $("<div></div>").addClass("paginate");
	var cContTag = $("<div></div>").addClass("c_cont");
	var ulTag = $("<ul></ul>");
	var firstTag = $("<li></li>").addClass('first').append('<i class="fa fa-backward"></i>');
	var prevTag = $('<li></li>').addClass('prev').append('<i class="fa fa-caret-left"></i>');
	if(curPage == 1){
		firstTag.addClass('disabled');
		prevTag.addClass('disabled');
	}else{
		firstTag.addClass('pagination_btn');
		prevTag.addClass('pagination_btn');
		
		prevTag.attr('data-key', curPage - 1);
		prevTag.attr('data-val', pageSize);
		
		firstTag.attr('data-key', 1);
		firstTag.attr('data-val', pageSize);
	}
	ulTag.append(firstTag).append(prevTag);
	var liPgNoTag = $("<li></li>").append('<input type="text" class="goto myFont" value="' + curPage + '" id="PageIndexTxt">').stop().append('page');
	ulTag.append('<li>all&nbsp;<span class="myFont" id="pageCnt" data-key="' + pageCnt + '">' + pageCnt + '</span>&nbsp;pages&nbsp;</li>');
	ulTag.append(liPgNoTag);
	
	var nextTag = $('<li></li>').addClass('next').append('<i class="fa fa-caret-right"></i>');
	var lastTag = $('<li></li>').addClass('last').append('<i class="fa fa-forward"></i>');
	if(curPage == pageCnt){
		nextTag.addClass('disabled');
		lastTag.addClass('disabled');
	}else{
		if(curPage + 1 > pageCnt){
			nextTag.attr('data-key', pageCnt);
			nextTag.attr('data-val', pageSize);
		}
		else{
			nextTag.attr('data-key', curPage + 1);
			nextTag.attr('data-val', pageSize);
		}
		lastTag.attr('data-key', pageCnt);
		lastTag.attr('data-val', pageSize);
		nextTag.addClass('pagination_btn');
		lastTag.addClass('pagination_btn');
	}
	ulTag.append(nextTag).append(lastTag);
	cContTag.append(ulTag).appendTo(pagenateTag);

	var rContTag = $('<div></div>').addClass('r_cont');
	rContTag.append('all <span class="red_num myFont">&nbsp;' + total + '&nbsp;</span>entries').appendTo(pagenateTag);
	
	$(".tab_cont.customPage").append(dropDownTag).append(pagenateTag);
}

$(document).on('click', '.pagination_btn', function(e){
	var pageNo = $(this).data('key');
	var pageSize = $(this).data('val');
	if(pageSize == 'Max')
		pageSize = 2147483647;
	$("input[name='pageNo']").val(pageNo);
	$("input[name='pageSize']").val(pageSize);
	$("#formSearch").submit();
});

$(document).on('change', '#PageIndexTxt', function(e){
	var pageNo = parseInt($(this).val());
	var pageCnt = $("#pageCnt").data('key');
	var pageSize = $(".input_select.pageSize").val();
	if(pageNo != NaN && pageNo > 0 && pageNo <= pageCnt){
		$("input[name='pageNo']").val(pageNo);
		$("input[name='pageSize']").val(pageSize);
		$("#formSearch").submit();
	}
})

$(document).on('click', '.input_select', function () {
    var ul = $("#dropdown ul");
    if (ul.css("display") == "none") {
        ul.slideDown("fast");
    } else {
        ul.slideUp("fast");
    }
});

$(document).on('click', '#dropdown ul li a', function () {
    var txt = $(this).text();
    $(".input_select").val(txt);
    var value = $(this).attr("rel");
    $("#dropdown ul").hide();
});