(function ($) {
    $(document).ready(function () {
        var hashes = window.location.hash.split("/");
        if (hashes[0] && hashes[0].startsWith("#q")){
            $('#exerciseComment').toggle();
        }
        if (hashes[0] && hashes[0].startsWith("#s")) {
            var id = '#solution' + hashes[0].substring(2);
            $(id).toggle();
        }
        if (hashes[1] && hashes[1].startsWith("c")) {
            $('#' + hashes[1]).addClass("highlight");
        }
    });
})($);
