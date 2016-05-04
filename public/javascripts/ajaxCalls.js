function upVoteSolution(id) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (xhttp.readyState === 4 && xhttp.status === 200) {
            document.getElementById("solutionPoints" + id).innerHTML = xhttp.responseText;
        }
    };
    xhttp.open("POST", "/solutions/" + id + "/upvote", true);
    xhttp.send();
}

function downVoteSolution(id) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (xhttp.readyState === 4 && xhttp.status === 200) {
            document.getElementById("solutionPoints" + id).innerHTML = xhttp.responseText;
        }
    };
    xhttp.open("POST", "/solutions/" + id + "/downvote", true);
    xhttp.send();
}

function upVoteExercise(id) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (xhttp.readyState === 4 && xhttp.status === 200) {
            document.getElementById("exercisePoints").innerHTML = xhttp.responseText;
        }
    };
    xhttp.open("POST", "/exercises/" + id + "/upvote", true);
    xhttp.send();
}

function downVoteExercise(id) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (xhttp.readyState === 4 && xhttp.status === 200) {
            document.getElementById("exercisePoints").innerHTML = xhttp.responseText;
        }
    };
    xhttp.open("POST", "/exercises/" + id + "/downvote", true);
    xhttp.send();
}

function trackTag(tagId) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            if (document.getElementById("track_" + tagId).getAttribute("class") == "btn btn-success btn-block") {
                document.getElementById("track_" + tagId).setAttribute("class", "btn btn-primary btn-block");
                document.getElementById("track_" + tagId).setAttribute("value", "Folgen");
            } else {
                document.getElementById("track_" + tagId).setAttribute("class", "btn btn-success btn-block");
                document.getElementById("track_" + tagId).setAttribute("value", "Nicht mehr folgen");
            }
        }
    };
    xhttp.open("POST", "/tags/track?tagId=" + tagId, true);
    xhttp.send();
}

function deleteSolution(solutionId, exerciseId) {
    $.ajax({
        type: "DELETE",
        url: "/solutions/" + solutionId + "/delete",
        complete: function (xhttp) {
            top.location.href = "/exercises/" + exerciseId
        }
    });
}

function deleteExercise(exerciseId) {
    $.ajax({
        type: "DELETE",
        url: "/exercises/" + exerciseId + "/delete",
        complete: function (xhttp) {
            top.location.href = "/exercises"
        }
    });
}
