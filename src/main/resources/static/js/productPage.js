/**
 * Updates a starbox's stars to a new value and changes the graphics
 * @param starbox The starbox to update
 * @param stars The new amount of stars to display
 */
function starboxRecalculate(starbox, stars) {
    starbox.children().each((i, star) => {
        star = $(star)
        star.removeClass()
        if(i < stars) {
            star.addClass("star full")
        } else {
            star.addClass("star out")
        }
    })
}

/**
 * Send the request to post the review from the editor
 */
function postReview() {
    // Get the info for the post request
    let productId = $(".review-data").data("productid")
    let authorId = Cookies.get("loggedin-uuid")
    let rating = $(".rating-controls > .star-box").attr("data-stars")
    let content = $("#rating-text").val()
    $.ajax({
        type: "POST",
        url: window.location.protocol + "//" + window.location.host + "/api/reviews",
        data: {
            productId: productId,
            authorId: authorId,
            rating: rating,
            content: content
        },
        success: () => {
            location.reload()
        },
        error: location.reload
    })
}

/**
 * On page load
 */
$(() => {
    // Add a callback to the star buttons
    $(".star-up").click(() => {
        let starbox = $(".rating-controls > .star-box")
        starbox.attr("data-stars", Math.min(parseInt(starbox.attr("data-stars")) + 1, 5))
        starboxRecalculate(starbox, parseInt(starbox.attr("data-stars")))

    })
    $(".star-down").click(() => {
        let starbox = $(".rating-controls > .star-box")
        starbox.attr("data-stars", Math.max(parseInt(starbox.attr("data-stars")) - 1, 1))
        starboxRecalculate(starbox, parseInt(starbox.attr("data-stars")))
    })

    // Add a callback to the review button
    $(".post-review").click(postReview)
})