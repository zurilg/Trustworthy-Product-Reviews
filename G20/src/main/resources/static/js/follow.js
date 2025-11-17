// follow.js
//handles follow / Unfollow actions on the product page without reloading.

(function () {
    "use strict";

    function sendFollowRequest(url, followerId, followeeId, onSuccess, onError) {
        if (!followerId || !followeeId) {
            if (typeof onError === "function") {
                onError("Missing user identifiers.");
            }
            return;
        }

        var params = new URLSearchParams();
        params.append("followerId", followerId);
        params.append("followeeId", followeeId);

        fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: params.toString()
        })
            .then(function (response) {
                return response.json().catch(function () {
                    return { status: "error", message: "Unexpected server response." };
                });
            })
            .then(function (data) {
                if (data && data.status === "ok") {
                    if (typeof onSuccess === "function") {
                        onSuccess();
                    }
                } else {
                    var message = (data && data.message) ? data.message : "Unable to update follow status.";
                    if (typeof onError === "function") {
                        onError(message);
                    } else {
                        alert(message);
                    }
                }
            })
            .catch(function () {
                if (typeof onError === "function") {
                    onError("Network error. Please try again.");
                } else {
                    alert("Network error. Please try again.");
                }
            });
    }

    function attachHandlers() {
        var reviewsContainer = document.querySelector(".all-reviews");
        if (!reviewsContainer) {
            return;
        }

        reviewsContainer.addEventListener("click", function (event) {
            var target = event.target;

            // Follow
            if (target.classList.contains("follow-btn")) {
                var followerId = target.getAttribute("data-follower-id");
                var followeeId = target.getAttribute("data-followee-id");

                // Find sibling unfollow button inside same review
                var reviewElement = target.closest(".review");
                var unfollowBtn = reviewElement
                    ? reviewElement.querySelector(".unfollow-btn")
                    : null;

                target.disabled = true;
                if (unfollowBtn) {
                    unfollowBtn.disabled = true;
                }

                sendFollowRequest(
                    "/api/follow",
                    followerId,
                    followeeId,
                    function () {
                        // Success: hide follow, show unfollow
                        target.style.display = "none";
                        if (unfollowBtn) {
                            unfollowBtn.style.display = "inline-block";
                        }
                    },
                    function (message) {
                        alert(message);
                    }
                );

                // Always re-enable after short delay to avoid stuck state
                setTimeout(function () {
                    target.disabled = false;
                    if (unfollowBtn) {
                        unfollowBtn.disabled = false;
                    }
                }, 500);

                return;
            }

            // Unfollow
            if (target.classList.contains("unfollow-btn")) {
                var followerIdU = target.getAttribute("data-follower-id");
                var followeeIdU = target.getAttribute("data-followee-id");

                var reviewElementU = target.closest(".review");
                var followBtn = reviewElementU
                    ? reviewElementU.querySelector(".follow-btn")
                    : null;

                target.disabled = true;
                if (followBtn) {
                    followBtn.disabled = true;
                }

                sendFollowRequest(
                    "/api/follow/unfollow",
                    followerIdU,
                    followeeIdU,
                    function () {
                        // Success: hide unfollow, show follow
                        target.style.display = "none";
                        if (followBtn) {
                            followBtn.style.display = "inline-block";
                        }
                    },
                    function (message) {
                        alert(message);
                    }
                );

                setTimeout(function () {
                    target.disabled = false;
                    if (followBtn) {
                        followBtn.disabled = false;
                    }
                }, 500);

                return;
            }
        });
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", attachHandlers);
    } else {
        attachHandlers();
    }

})();
