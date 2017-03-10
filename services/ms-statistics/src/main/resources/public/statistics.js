(function() {

    const DEFAULT_BUFFER_INTERVAL_IN_MILLIS = 1000;

    var chart;
    EventSourceObservable("#statistics",
        function (observable) { // on event source connected
            $("#avgCalculationInterval input").prop("disabled", true);
            observable.subscribe(
                function (stats) { // onNext
                    chart.update([stats.articleCount, stats.wordCountAvg/1000, stats.ratingAvg, stats.fetchTimeInMillisAvg]);
                });

        },
        function () { // on event source disconnected
            $("#avgCalculationInterval input").prop("disabled", false);
        });

    var validateAvgCalculationIntervalOnFocusOut = function () {
        var input = $("#avgCalculationInterval input");
        input.val(DEFAULT_BUFFER_INTERVAL_IN_MILLIS);
        input.focusout(function () {
            var value = input.val();
            if (!value || !/^\d+$/.test(value)) {
                value = DEFAULT_BUFFER_INTERVAL_IN_MILLIS;
                input.val(value);
            }
            var url = $("#statistics .url");
            var oldValue = url.val();
            url.val(oldValue.substring(0, oldValue.indexOf("=") + 1) + value);
        });
    };

    /**
     * Link ui events and draw initial chart.
     */
    $(function () {
        validateAvgCalculationIntervalOnFocusOut();
        chart = new UpdateChart("#chart", ["Article/Sec", "Avg Word Count", "Avg Rating", "Avg Article fetch time in ms"]);
    });

})();
