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
            // trigger a "Login"
            login()
        },
        error: handleError
    });
}

function checkCookies() {
    // If we have no cookies no need to check
    if (getCookie() === undefined) {
        logout()
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
            // trigger a "Login"
            login()
        },
        error: logout
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
}

function handleError(jqXHR, textStatus, errorThrown) {
    // Put the error message into the error text box
    $(".error-text").text(textStatus)
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

function login() {
    // Hide the modal
    hideModal()
    // Swap the visibility of logged-in/logged-out elements
    $(".logged-in").show()
    $(".logged-out").hide()

    // Update the display name
    $(".logged-in-username").text(getCookie().displayName)
}

function logout() {
    // Wipe the cookie
    Cookies.remove("loggedin")
    // Swap the visibility of logged-in/logged-out elements
    $(".logged-in").hide()
    $(".logged-out").show()
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
    $(".logout-button").click(logout)

})