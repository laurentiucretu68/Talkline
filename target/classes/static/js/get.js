$(document).ready(
    function() {

        // SUBMIT FORM
        $("#sendLike").click(function(event) {
            // Prevent the form from submitting via the browser.
            event.preventDefault();
            ajaxPost();
        });

        function ajaxPost() {
            // DO POST
            $.ajax({
                type : "POST",
                contentType : "application/json",
                url : "/update-likes",
                dataType : 'json',
                success : function(result) {
                    console.log(result);
                },
                error : function(e) {
                    alert(e);
                }
            });

        }

    })