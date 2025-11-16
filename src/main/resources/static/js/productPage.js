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

function postReview() {
    $.post(
        "api/reviews",
        {
            
        }
    )
}

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
    $(".post-review").click(postReview())
})