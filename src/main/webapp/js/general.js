function validate(validationMessage) {
	return confirm(validationMessage);
}

function reloadCaptcha() {
	var d = new Date();
	$("#captcha_image")
			.attr("src", "/Library_SLYNKO/captchaImg?" + d.getTime());
}

$(document).ready(function() {
	$(function() {
		$("#datepicker").datepicker();
		$("#datepicker").datepicker({
			language : 'en',
			minDate : new Date()
		})
	});
	
	$("#cubeDisable").click(function() {
		$(".container").html("");
		$(".container").removeClass();
	});
});