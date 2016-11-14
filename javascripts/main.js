function hideall() {
    $('#persona').css("display", "none");
    $('#system').css("display", "none");
}

$('#persona-link').click(function() {
    hideall();
    $('#persona').css("display", "flex");
});

$('#system-link').click(function() {
    hideall();
    $('#system').css("display", "flex");
});

