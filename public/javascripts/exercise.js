(function ($) {
    $(document).ready(function() {
        $(".tags-basic-single").select2();
        $(".tags-basic-multiple").select2();
        $(".tags-tokenizer").select2({
            tags: true,
            tokenSeparators: [',', ' ']
        });
    });


    function createTagFilter(id) {
        var result = [];
        var tagString = $(id).attr('rel');
        if (tagString && tagString.length > 0) {
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
            hintText: "Suche nach Haupt-Tags",
            theme: "facebook"
        });
    });

    $(document).ready(function () {
        var route = $("#othertag-filter-list").attr('data-route');
        $("#othertag-filter-list").tokenInput(route, {
            prePopulate: createTagFilter("#othertag-filter-list"),
            hintText: "Suche nach anderen Tags",
            theme: "facebook"
        });
    });
})($);
