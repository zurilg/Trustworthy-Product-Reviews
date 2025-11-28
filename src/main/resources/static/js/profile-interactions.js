(function () {

    function followUser(url, followeeId, button) {
        if (!followeeId) {
            alert("Error: Missing user identifier.");
            return;
        }

        const originalText = button.textContent;
        const isDynamicListAction = button.closest(".following-list");
        button.disabled = true;

        if (!button.closest(".recommended-friends-section") && !button.closest("#profile-owner-follow-toggle")) {
            button.textContent = "Updating...";
        }

        const params = new URLSearchParams();
        params.append("followeeId", followeeId);

        fetch(url, {method: "POST", headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: params.toString()
        })
            .then(function (response) {
                if (response.status === 401) {
                    alert("You must be logged in to perform this action.");
                    return { status: "error" };
                }
                return response.json().catch(function () {
                    return { status: "error", message: "Unexpected server response." };
                });
            })
            .then(function (data) {
                if (data && data.status === "ok") {
                    if (button.closest(".recommended-friends-section") || button.closest("#profile-owner-follow-toggle")) {
                        window.location.reload();
                        return;
                    }
                    if (isDynamicListAction && data.following === false) {
                        handleUnfollowSuccess(button, data.followeeId);
                        return;
                    }
                    button.disabled = false;
                    button.textContent = originalText;
                }
                else {
                    const message = (data && data.message) ? data.message : "Unable to update follow status.";
                    alert(message);
                    button.textContent = originalText;
                    button.disabled = false;
                }
            })
            .catch(function () {
                alert("Network error. Please try again.");
                button.textContent = originalText;
                button.disabled = false;
            });
    }

    function handleUnfollowSuccess(button, followeeId) {
        const followingListContainer = document.querySelector(".following-list");
        const followingList = followingListContainer ? followingListContainer.querySelector('.user-list') : null;
        const followingCountEl = document.getElementById("following-count");
        const listItem = button.closest('.list-item');

        if (listItem) {
            listItem.remove();
            if (followingCountEl) followingCountEl.textContent = parseInt(followingCountEl.textContent) - 1;
            if (followingList && followingList.children.length === 0) {
                const displayName = document.querySelector('.main-header').textContent.split(' Profile')[0];
                const noRelationshipsHtml = `<div id="following-no-relationships" class="no-relationships">
                        <p>${displayName} is not following anyone.</p></div>`;

                followingList.insertAdjacentHTML('beforebegin', noRelationshipsHtml);
            }
        }
    }

    function attachHandlers() {
        const profileContent = document.querySelector(".profile-content");
        if (!profileContent) return;
        profileContent.addEventListener("click", function (event) {
            const target = event.target;
            const button = target.closest(".follow-button, .unfollow-button");
            if (button) {
                event.preventDefault();
                const form = button.closest("form");
                const followeeId = form.querySelector('input[name="followeeId"]').value;
                const url = form.action;
                followUser(url, followeeId, button);
            }
        });
    }

    if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", attachHandlers);
    else attachHandlers();

})()
