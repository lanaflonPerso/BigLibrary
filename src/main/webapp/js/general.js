function validate(validationMessage) {
	return confirm(validationMessage);
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