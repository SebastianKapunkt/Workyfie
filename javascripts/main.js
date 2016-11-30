function hideall() {
    $('#idee').hide();
    $('#user').hide();
    $('#technicals').hide();
}

$( document ).ready(function() {
    $('#persona-link').click(function() {
        hideall();
        $('#user').show();
    });

    $('#system-link').click(function() {
        hideall();
        $('#technicals').show();
    });

    $('#idee-link').click(function() {
        hideall();
        $('#idee').show();
    });

    hideall();
    $('#idee').show();
});





