/**
 * On page load
 */
$(() => {
    $(".star-box").each((i, box) => {
        box = $(box)
        // Append 5 blank stars
        for(let i = 0; i < 5; i ++) box.append($("<span class='star out'></span>"))
        // Loop through those stars
        box.children().each( (i, star) => {
            // For each star in box check against the box rating value and change
            if(box.data("stars") >= i+1)        star.className = "star full"
            else if(box.data("stars") >= i+0.5) star.className = "star half"
            else                                star.className = "star out"
        })
    })
})