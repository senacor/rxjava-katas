(function() {
    const DEFAULT_UPDATE_INTERVAL_IN_MILLIS = 1000;

    EventSourceObservable("#topArticleControls",
        function (observable) { // on event source connected
            $("#topArticleUpdateInterval").prop("disabled", true);
            observable.subscribe(
                function (topArticle) { // onNext
                    var topArticleHtml = "";
                    var counter = 1;
                    topArticle.forEach(function (article) {
                        topArticleHtml = topArticleHtml.concat("<div>" + counter + ". <a href=\"" + article.url + "\">" + article.name + "</a> - " + article.reads + "</div>");
                        counter++;
                    });
                    $("#topArticle").html(topArticleHtml);
                });

        },
        function () { // on event source disconnected
            $("#topArticleUpdateInterval").prop("disabled", false);
        });


    var validateUpdateIntervalOnFocusOut = function () {
        var input = $("#topArticleUpdateInterval");
        input.val(DEFAULT_UPDATE_INTERVAL_IN_MILLIS);
        input.focusout(function () {
            var value = input.val();
            if (!value || !/^\d+$/.test(value)) {
                value = DEFAULT_UPDATE_INTERVAL_IN_MILLIS;
                input.val(value);
            }
            var url = $("#topArticleControls .url");
            var oldValue = url.val();
            url.val(oldValue.substring(0, oldValue.lastIndexOf("=") + 1) + value);
        });
    };

    /**
     * Link ui events and draw initial chart.
     */
    $(function () {
        validateUpdateIntervalOnFocusOut();
    });
})();
