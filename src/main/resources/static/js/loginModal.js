function attemptLogin() {
    // Get the input
    inputUsername = $("#login-user").val();
    // Send API Request to get the UUID of the user
    $.ajax({
        url: window.location.protocol + "//" + window.location.host + "/api/user/login",
        type: "GET",
        data: {username: inputUsername},
        dataType: "json",
        success: (data) => {
            // Successfully got the user
            // Save the user as a cookie
            setCookie(data)
            // Refresh the page
            location.reload()
        },
        error: handleError
    });
}

function checkCookies() {
    // If we have no cookies no need to check
    if (getCookie() === undefined) {
        // Wipe the cookies
        deleteCookie()
        // Show logged out elements
        showLoggedOut()
        return
    }

    // Send request to see if the cookies stored are good
    $.ajax({
        url: window.location.protocol + "//" + window.location.host + "/api/user/login",
        type: "GET",
        data: {username: getCookie().userName},
        dataType: "json",
        success: (data) => {
            // Successfully got the user
            // Save the user as a cookie
            setCookie(data)
            // Show logged in elements
            showLoggedIn()
        },
        error: () => {
            // Failed to get the user
            // Wipe the cookies
            deleteCookie()
            // Show logged out elements
            showLoggedOut()
        }
    });
}

function getCookie() {
    if(Cookies.get("loggedin") === undefined) {
        return undefined
    }
    return JSON.parse(Cookies.get("loggedin"))
}

function setCookie(json) {
    Cookies.set("loggedin", JSON.stringify(json))
    Cookies.set("loggedin-uuid", json.id)
}

function deleteCookie() {
    // Wipe the cookie
    Cookies.remove("loggedin")
    Cookies.remove("loggedin-uuid")
}

function handleError(jqXHR, textStatus, errorThrown) {
    console.log(jqXHR)
    console.log(textStatus)
    console.log(errorThrown)
    let response = jqXHR.responseJSON
    // Check if we are receiving an error from the DTO
    if(response.errors) {
        // Put the error message into the error text box
        $(".error-text").text(response.errors[0].defaultMessage)
    } else {
        // Put the error message into the error text box
        $(".error-text").text(response.message)
    }
    // Show the error
    $(".error-text").addClass("error-message")
}

function showModal() {
    // Show the modal
    $(".login-modal").show()
    // Reset the text
    $(".login-user").val("")
}

function hideModal() {
    // Hide the modal
    $(".login-modal").hide()
}

function showLoggedIn() {
    // Hide the modal
    hideModal()
    // Swap the visibility of logged-in/logged-out elements
    $(".logged-in").show()
    $(".logged-out").hide()

    // Update the display name
    $(".logged-in-username").text(getCookie().displayName)
}

function showLoggedOut() {
    // Swap the visibility of logged-in/logged-out elements
    $(".logged-out").show()
    $(".logged-in").hide()
}

// On page load
$(() => {
    // Hide the modal
    hideModal()

    // Check if the cookies are good
    checkCookies()

    // Add the callback for the header's open button
    $(".login-open").click(showModal)
    // Add the callback for the modal's close button
    $(".login-close").click(hideModal)

    // Add the callback for the user pressing "login"
    $(".login-button").click(attemptLogin)
    // Add the callback for the user pressing enter on the login username
    $("#login-user").keypress((event) => {
        if(event.which === 13) attemptLogin()
    })
    // Add the callback for the user pressing "logout"
    $(".logout-button").click(() => {
        // Wipe the cookies
        deleteCookie()
        // Refresh the page
        location.reload()
    })

})