$(() => {
    // Add a listener to the form submission
    $(".register-form").submit((event) => {
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
                console.log("registered")
            },
            error: () => {
                console.log("error")
            }
        })
    })
})