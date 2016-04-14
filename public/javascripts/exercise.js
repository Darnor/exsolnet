/**
 * Created by Claudia on 11.04.2016.
 */

(function ($) {

  

  /*  $(document).ready(function () {
        $('body').on('keyup', 'input', 'change', 'click', function () {
            if ($("#title").val() !== "" && $("#content").val() !== "" && $("form >  ul:first > li > p").length > 0) {
                if ($("#save").hasAttribute('disabled'))
                    $("#save").removeAttr('disabled');
            }
            else {
                $("#save").attr('disabled', 'disabled');
            }
        });
    });*/

    function createTagFilter(id) {
        var result = [];
        var tagString = $(id).attr('rel');
        if (tagString.length > 0) {
            var tagArray = tagString.split(",");
            for (var i in tagArray) {
                result.push({name: tagArray[i]});
            }
        }
        return result;
    }

    $(document).ready(function () {
        var route = $("#maintag-filter-list").attr('data-route');
        $("#maintag-filter-list").tokenInput(route, {
            prePopulate: createTagFilter("#maintag-filter-list"),
            theme: "facebook"
        });
    });

    $(document).ready(function () {
        var route = $("#othertag-filter-list").attr('data-route');
        $("#othertag-filter-list").tokenInput(route, {
            prePopulate: createTagFilter("#othertag-filter-list"),
            theme: "facebook"
        });
    });
})($);
