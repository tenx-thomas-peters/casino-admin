$(document).ready(function(){
	var siteSeq = $("input[name='site']").val();
	$("option[class='siteSeqOption'][value='" + siteSeq + "']").attr("selected", "selected");
});	