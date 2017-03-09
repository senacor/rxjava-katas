(function() {

    const DEFAULT_BUFFER_INTERVAL_IN_MILLIS = 1000;

    var chart;
    var chartUpdateInterval = DEFAULT_BUFFER_INTERVAL_IN_MILLIS;
    var subscription;

    EventSourceObservable("#readEvents",
        function (observable) { // on event source connected
            $("#avgCalculationIntervalReadEvents input").prop("disabled", true);
            subscription = observable
                .bufferTime(chartUpdateInterval)
                .subscribe(
                    function (readEventss) { // onNext
                        // TODO make nice
                        var sum = 0;
                        readEventss.forEach(function (readEvents) {
                            sum = sum + readEvents.length
                        });

                        chart.update([sum]);
                    });

        },
        function () { // on event source disconnected
            subscription.dispose();
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
            chartUpdateInterval = +value;
        });
    };

    /**
     * Link ui events and draw initial chart.
     */
    $(function () {
        validateAvgCalculationIntervalOnFocusOut();
        chart = new UpdateChart("#chartReadEvents", ["Article Read Events"]);
    });

})();
