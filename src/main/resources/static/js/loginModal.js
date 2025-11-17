let currentUser = null;

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

function setCurrentUser(user) {
    currentUser = user || null
    if (currentUser) {
        $(".logged-in-username").text(currentUser.displayName)
        showLoggedIn()
    } else {
        $(".logged-in-username").text("")
        showLoggedOut()
    }
}

function fetchCurrentUser() {
    $.ajax({
        url: window.location.protocol + "//" + window.location.host + "/api/user/current",
        type: "GET",
        dataType: "json",
        success: (data) => {
            setCurrentUser(data)
        },
        error: () => {
            setCurrentUser(null)
        }
    })
}

/**
 * Attempts to log in to a user account using the username from the #login-user input field
 */
function attemptLogin() {
    inputUsername = $("#login-user").val();
    $.ajax({
        url: window.location.protocol + "//" + window.location.host + "/api/user/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({username: inputUsername}),
        dataType: "json",
        success: () => {
            location.reload()
        },
        error: handleError
    });
}

function logout() {
    $.ajax({
        url: window.location.protocol + "//" + window.location.host + "/api/user/logout",
        type: "POST",
        complete: () => {
            location.reload()
        }
    })
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

    // Check the current session
    fetchCurrentUser()

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
    $(".logout-button").click(logout)

})
