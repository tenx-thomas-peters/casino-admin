/* Add here all your JS customizations */
// main section width
(function( $ ) {

	'use strict';

//    if ($('body').width() > 796) {
//        $('.content-body').width($('.inner-wrapper').width() - $('.sidebar-left').width() - 80 + 'px');
//    }

    if ($('body').width() <= 1515 && $('body').width() >768 && $('.inner-wrapper').width() != null && $('.sidebar-left').width() != null) {
        $('.content-body').width($('.inner-wrapper').width() - $('.sidebar-left').width() - 80 + 'px');
    }

    if (url !== undefined && url !== '') {
        var atag = $("#menu a[href$='" + url + "']");
        atag.parent().addClass('nav-active');
        atag.parent().parent().parent().addClass('nav-expanded');
    }
}).apply( this, [ jQuery ]);