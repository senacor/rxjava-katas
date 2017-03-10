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
                        var sumReadEvents = 0;
                        var sumLoadTime = 0;
                        var min = null;
                        var max = null;
                        var updateCalculation = function (fetchTime) {
                            sumReadEvents = sumReadEvents + 1;
                            sumLoadTime = sumLoadTime + fetchTime;
                            if (fetchTime) {
                                if (!min || min > fetchTime) {
                                    min = fetchTime;
                                }
                                if (!max || max < fetchTime) {
                                    max = fetchTime;
                                }
                            }
                        };
                        readEventss.forEach(function (readEvents) {
                            if (Array.isArray(readEvents)) {
                                readEvents.forEach(function (readEvent) {
                                    updateCalculation(readEvent.fetchTimeInMillis);
                                });
                            } else {
                                updateCalculation(readEvents.fetchTimeInMillis);
                            }
                        });
                        chart.update([sumReadEvents, sumLoadTime/sumReadEvents, max , min]);
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
        chart = new UpdateChart("#chartReadEvents", ["Read Events", "Avg fetch time in ms", "Max fetch time in ms", "Min fetch time in ms"]);
    });

})();
