(function() {

    const DEFAULT_BUFFER_INTERVAL_IN_MILLIS = 1000;
    const MAX_ARTICLE_DISPLAYED = 5;

    var chart;
    var connected = false;
    var subscriptions = [];
    var source;

    function subscribe() {
        var serverUrl = $("#url").val();
        appendLog('Connecting to ' + serverUrl);
        setSubscribed(true);

        if (!!window.EventSource) {
            source = new EventSource(serverUrl);
        } else {
            appendLog("Your browser doesn't support SSE");
            return;
        }

        source.addEventListener('message', function(e) {
            appendLog("message: " + e.data);

            var stats = toJson(e);
            chart.update(stats.articleCount, stats.wordCountAvg, stats.ratingAvg);
        }, false);

        source.addEventListener('open', function(e) {
            // Connection was opened.
            appendLog("Connected to " + serverUrl)
        }, false);

        source.addEventListener('error', function(e) {
            if (e.readyState == EventSource.CLOSED) {
                // Connection was closed.
                appendLog('Connection terminated');
            } else {
                appendLog('Error ' + e);
            }
        }, false);
    }

    function toJson(event) {
        try {
            return JSON.parse(event.data)
        } catch (event) {
            appendLog("Could not parse data received data: " + event.data);
        }
    }

    function unsubscribe() {
        source.close();
        setSubscribed(false);
        subscriptions.forEach(function (subscription) {
            subscription.dispose()
        });
        subscriptions = [];
    }

    function appendLog(message) {
        $("#log").append("<tr><td>" + message + "</td></tr>");
    }

    function setSubscribed(subscribed) {
        if (subscribed) {
            $("#subscribe").hide();
            $("#unsubscribe").show();
            $("#avgCalculationInterval input").prop("disabled", true);
        } else {
            connected = false;
            $("#subscribe").show();
            $("#unsubscribe").hide();
            $("#avgCalculationInterval input").prop("disabled", false);
        }
    }

    var validateAvgCalculationIntervalOnFocusOut = function () {
        var input = $("#avgCalculationInterval input");
        input.val(DEFAULT_BUFFER_INTERVAL_IN_MILLIS);
        input.focusout(function () {
            var value = input.val();
            if (!value || !/^\d+$/.test(value)) {
                value = DEFAULT_BUFFER_INTERVAL_IN_MILLIS;
                input.val(value);
            }
            var url = $("#url");
            var oldValue = url.val();
            url.val(oldValue.substring(0, oldValue.indexOf("=") + 1) + value);
        });
    };

    /**
     * Link ui events and draw initial chart.
     */
    $(function () {
        // prevent form submit
        $("form").on('submit', function (e) {
            e.preventDefault();
        });
        $("#subscribe").click(function () {
            subscribe();
        });
        $("#unsubscribe").click(function () {
            unsubscribe();
        });
        setSubscribed(false);
        validateAvgCalculationIntervalOnFocusOut();
        chart = new ArticleChart("#chart");
    });

})();
