/**
 * Attempts to log in to a user account using the username from the #login-user input field
 */
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

/**
 * Attempts to log in to a user account using a username stored in the cookies
 */
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

/**
 * Gets the User object from the cookies
 * @returns The logged-in user
 */
function getCookie() {
    if(Cookies.get("loggedin") === undefined) {
        return undefined
    }
    return JSON.parse(Cookies.get("loggedin"))
}

/**
 * Sets the logged-in user cookies saving the full object for front-end display and the
 * UUID to be passed to the backend in future requests.
 *
 * @param json the JSON representation of the logged-in User object
 */
function setCookie(json) {
    Cookies.set("loggedin", JSON.stringify(json))
    Cookies.set("loggedin-uuid", json.id)
}

/**
 * Deletes any cookies relating to the login functionality
 */
function deleteCookie() {
    // Wipe the cookie
    Cookies.remove("loggedin")
    Cookies.remove("loggedin-uuid")
}

/**
 * Handles an error from the backend by displaying it to the .error-text element
 * @param jqXHR the error response object
 */
function handleError(jqXHR) {
    let response = jqXHR.responseJSON
    // Check if we are receiving an error from the DTO
    let errorText = $(".error-text")
    if(response.errors) {
        // Put the error message into the error text box
        errorText.text(response.errors[0].defaultMessage)
    } else {
        // Put the error message into the error text box
        errorText.text(response.message)
    }
    // Show the error
    errorText.addClass("error-message")
}

/**
 * Shows the login modal
 */
function showModal() {
    // Show the modal
    $(".login-modal").show()
    // Reset the text
    $(".login-user").val("")
}

/**
 * Hides the login modal
 */
function hideModal() {
    // Hide the modal
    $(".login-modal").hide()
}

/**
 * Shows logged-in elements and displays the user's display name
 */
function showLoggedIn() {
    // Hide the modal
    hideModal()
    // Swap the visibility of logged-in/logged-out elements
    $(".logged-in").show()
    $(".logged-out").hide()

    // Update the display name
    $(".logged-in-username").text(getCookie().displayName)
}

/**
 * Hides the logged-in elements
 */
function showLoggedOut() {
    // Swap the visibility of logged-in/logged-out elements
    $(".logged-out").show()
    $(".logged-in").hide()
}

/**
 * On page load
 */
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