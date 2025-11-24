$(() => {
    // Hide the error messages
    $(".error-message").hide();

    // Add a listener to the form submission
    $(".register-form").submit((event) => {
        // Hide the error messages
        $(".error-message").hide();
        // Prevent default
        event.preventDefault()

        // Get the form data
        let displayName = $(".register-form #displayname").val()
        let username = $(".register-form #username").val()
        let email = $(".register-form #email").val()

        // Send to API
        $.ajax({
            url: window.location.protocol + "//" + window.location.host + "/api/user/register",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({displayName: displayName, username: username, email: email}),
            dataType: "json",
            success: (data) => {
                let welcome = $(".register-form .welcome-message")
                welcome.show()
                welcome.children().eq(0).text("Welcome, " + data.displayName + "!")
            },
            error: (jqXHR) => {
                if(jqXHR.responseJSON.errors === undefined) {
                let e = $(".register-form #email-error")
                    e.text(jqXHR.responseJSON.message)
                    e.show()
                    return
                }
                jqXHR.responseJSON.errors.forEach((error) => {
                    let e = null;
                    if (error.field === "displayName") e = $(".register-form #displayname-error")
                    else if (error.field === "username") e = $(".register-form #username-error")
                    else if (error.field === "email") e = $(".register-form #email-error")

                    if (e !== null) {
                        e.text(error.defaultMessage)
                        e.show()
                    }

                })
            }
        })
    })
})