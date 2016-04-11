/**
 * Created by Claudia on 11.04.2016.
 */

(function ($) {

    function createTagFilter(id){
        var result = [];
        var tagString = $(id).attr('rel');
        var tagArray = tagString.split(",");
        for(var i in tagArray){
            result.push({name: tagArray[i]});
        }
        return result;
    }

    $(document).ready(function () {
        $("#maintag-filter-list").tokenInput("/tags/maintag", {
            prePopulate: createTagFilter("#maintag-filter-list"),
            theme: "facebook"
        });
    });

    $(document).ready(function () {
        $("#othertag-filter-list").tokenInput("/tags/othertag", {
            prePopulate: createTagFilter("#othertag-filter-list"),
            theme: "facebook"
        });
    });
})($);