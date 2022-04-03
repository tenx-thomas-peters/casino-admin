function backup() {
	$.ajax({
		url: CONTEXT_ROOT + "config/dbBackup",
		type: "POST",
		data: {},
		responseType: 'blob',
		success: function(response) {
			if(response != "") {
				new PNotify({
	                title: 'Success!',
	                text: "success",
	                type: 'success',
	                buttons: {
	                    closer: true,
	                    sticker: false
	                }
	            });
				var filename = moment(new Date()).format("YYYY-MM-DD(HH-mm-ss-SSS)") + ".sql";
				if (typeof window.navigator.msSaveBlob !== 'undefined') {
		            window.navigator.msSaveBlob(new Blob([response]), filename);
		        } else {
		            let url = window.URL.createObjectURL(new Blob([response]));
		            let link = document.createElement('a');

		            link.style.display = 'none';
		            link.href = url;
		            link.setAttribute('download', filename);
		            document.body.appendChild(link);
		            link.click();

		            document.body.removeChild(link);
		            window.URL.revokeObjectURL(url);
		        }
			} else {
				new PNotify({
	                title: 'Error!',
	                text: 'Please install Xampp, or Please create New Holder.',
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
                text: 'Please install Xampp, or Please create New Holder.',
                type: 'error',
                buttons: {
                    closer: true,
                    sticker: false
                }
			});
		}
	});
}