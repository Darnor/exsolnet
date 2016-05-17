function upVoteSolution(id) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (xhttp.readyState === 4 && xhttp.status === 200) {
            document.getElementById("solutionPoints" + id).innerHTML = xhttp.responseText;
        }
    };
    xhttp.open("POST", "/solutions/" + id + "/upvote", true);
    xhttp.send();
    changecolorSolution(1, id, "Solution");
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
    changecolorSolution(-1, id, "Solution");
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
    changecolorSolution(1, "", "Exercise");

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
    changecolorSolution(-1, "", "Exercise");
}
function changecolorSolution(value, id, kind) {

    if (value === 1) {
        if (document.getElementById("upVote" + kind + id).classList.contains("upcolor")) {
            document.getElementById("upVote" + kind + id).classList.add("defaultcolor");
            document.getElementById("upVote" + kind + id).classList.remove("upcolor");
        }
        else {
            if (document.getElementById("downVote" + kind + id).classList.contains("downcolor")) {
                document.getElementById("downVote" + kind + id).classList.remove("downcolor");
                document.getElementById("downVote" + kind + id).classList.add("defaultcolor");
            }
            document.getElementById("upVote" + kind + id).classList.remove("defaultcolor");
            document.getElementById("upVote" + kind + id).classList.add("upcolor");
        }
    }
    if (value === -1) {

        if (document.getElementById("downVote" + kind + id).classList.contains("downcolor")) {
            document.getElementById("downVote" + kind + id).classList.add("defaultcolor");
            document.getElementById("downVote" + kind + id).classList.remove("downcolor");
        }
        else {
            if (document.getElementById("upVote" + kind + id).classList.contains("upcolor")) {
                document.getElementById("upVote" + kind + id).classList.remove("upcolor");
                document.getElementById("upVote" + kind + id).classList.add("defaultcolor");
            }
            document.getElementById("downVote" + kind + id).classList.add("downcolor");
            document.getElementById("downVote" + kind + id).classList.remove("defaultcolor");
        }
    }

}

function trackTag(tagId) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (xhttp.readyState === 4 && xhttp.status === 200) {
            if (document.getElementById("track_" + tagId).getAttribute("class") === "btn btn-success btn-block") {
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
