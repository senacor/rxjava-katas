(function() {

    const DEFAULT_BUFFER_INTERVAL_IN_MILLIS = 1000;

    var chart;
    var chartUpdateInterval = DEFAULT_BUFFER_INTERVAL_IN_MILLIS;

    EventSourceObservable("#readEvents",
        function (observable) { // on event source connected
            $("#avgCalculationIntervalReadEvents input").prop("disabled", true);
            observable
                .bufferTime(chartUpdateInterval)
                .subscribe(
                    function (readEvents) { // onNext
                        chart.update(readEvents.length, null, null);
                    });

        },
        function () { // on event source disconnected
            $("#avgCalculationIntervalReadEvents input").prop("disabled", false);
        });

    var validateAvgCalculationIntervalOnFocusOut = function () {
        var input = $("#avgCalculationIntervalReadEvents input");
        input.val(DEFAULT_BUFFER_INTERVAL_IN_MILLIS);
        input.focusout(function () {
            var value = input.val();
            if (!value || !/^\d+$/.test(value)) {
                value = DEFAULT_BUFFER_INTERVAL_IN_MILLIS;
                input.val(value);
            }
            chartUpdateInterval = value;
        });
    };

    /**
     * Link ui events and draw initial chart.
     */
    $(function () {
        validateAvgCalculationIntervalOnFocusOut();
        chart = new ArticleChart("#chartReadEvents");
    });

})();
