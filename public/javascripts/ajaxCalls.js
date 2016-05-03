/**
 * Created by mario on 02.05.16.
 */

function upVoteSolution(id){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            document.getElementById("solutionPoints" + id).innerHTML = xhttp.responseText;
        }
    };
    xhttp.open("GET", "/solution/" + id + "/upvote", true);
    xhttp.send();
}

function downVoteSolution(id){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            document.getElementById("solutionPoints" + id).innerHTML = xhttp.responseText;
        }
    };
    xhttp.open("GET", "/solution/" + id + "/downvote", true);
    xhttp.send();
}

function upVoteExercise(id){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            document.getElementById("exercisePoints").innerHTML = xhttp.responseText;
        }
    };
    xhttp.open("GET", "/exercise/" + id + "/upvote", true);
    xhttp.send();
}

function downVoteExercise(id){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            document.getElementById("exercisePoints").innerHTML = xhttp.responseText;
        }
    };
    xhttp.open("GET", "/exercise/" + id + "/downvote", true);
    xhttp.send();
}
