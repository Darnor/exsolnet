/**
 * Created by Frank on 06.04.2016.
 */

(function ($) {
    function getURLParameter(name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [, ""])[1].replace(/\+/g, '%20')) || null;
    }

    function createTagFilterFromUrl(){
        var result = [];
        var tagString = getURLParameter("tags")
        if(tagString==null)
            return result;
        var tagArray = tagString.split(",");
        for(var i in tagArray){
            result.push({name: tagArray[i]});
        }
        return result;
    }

    $(document).ready(function () {
        var currentTagFilter = createTagFilterFromUrl()
        $("#tag-filter-list").tokenInput("/tags/query", {
            prePopulate: currentTagFilter,
            theme: "facebook"
        });
    });
})($);
