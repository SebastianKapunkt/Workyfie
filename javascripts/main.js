function hideall() {
    $('#idee').hide();
    $('#user').hide();
    $('#technicals').hide();
    $('#buisnessmodel').hide();
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
    $('#buisnessmodel-link').click(function () {
       hideall();
        $('#buisnessmodel').show();
    });

    hideall();
    $('#idee').show();

    $("#userstoriesTable").tablesorter();
    $("#risikoanalyseTable").tablesorter();

    $("#risikoanalyseTable").stickyTableHeaders();
    $("#userstoriesTable").stickyTableHeaders();

    //$("#sticky-header").sticky({topSpacing:0});
});





