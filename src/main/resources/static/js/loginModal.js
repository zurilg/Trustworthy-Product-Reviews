// On page load
$(() => {
    // Hide the modal
    $(".login-modal").hide()

    // Add the callback for the header's open button
    $(".login-open").click(() => {
        // Reset the text
        $(".login-user").val("")
        // Show the modal
        $(".login-modal").show()
    })

    // Add the callback for the modal's close button
    $(".login-close").click(() => {
        // Hide the modal
        $(".login-modal").hide()
    })

    // Add the callback for the modal's login button
    $(".login-button").click(() => {
        // Get the input

        // Send API Request to get the UUID of the user

        // Save as cookie
    })
})