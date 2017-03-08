(function() {

    const DEFAULT_BUFFER_INTERVAL_IN_MILLIS = 1000;
    const DEFULAT_NUMBER_OF_TOP_ARTICLE = 5;

    var connected = false;
    var subscriptions = [];
    var source;

    function subscribe() {
        var serverUrl = $("#topArticleUrl").val();
        LOG.append('Connecting to ' + serverUrl);
        setSubscribed(true);

        if (!!window.EventSource) {
            source = new EventSource(serverUrl);
        } else {
            LOG.append("Your browser doesn't support SSE");
            return;
        }

        source.addEventListener('message', function(e) {
            LOG.append("message: " + e.data);

            var topArticle = toJson(e);

            var topArticleHtml = "";
            var counter = 1;
            topArticle.forEach(function (article) {
                topArticleHtml = topArticleHtml.concat("<div>" + counter + ". <a href=\"" + article.url + "\">" + article.name + "</a> - " + article.reads + "</div>");
                counter++;
            });
            $("#topArticle").html(topArticleHtml);
        }, false);

        source.addEventListener('open', function(e) {
            // Connection was opened.
            LOG.append("Connected to " + serverUrl)
        }, false);

        source.addEventListener('error', function(e) {
            if (e.readyState == EventSource.CLOSED) {
                // Connection was closed.
                LOG.append('Connection terminated');
            } else {
                LOG.append('Error ' + e);
            }
        }, false);
    }

    function toJson(event) {
        try {
            return JSON.parse(event.data)
        } catch (event) {
            LOG.append("Could not parse data received data: " + event.data);
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

    function setSubscribed(subscribed) {
        if (subscribed) {
            $("#topArticleSubscribe").hide();
            $("#topArticleUnsubscribe").show();
            //$("#avgCalculationInterval input").prop("disabled", true);
        } else {
            connected = false;
            $("#topArticleSubscribe").show();
            $("#topArticleUnsubscribe").hide();
            //$("#avgCalculationInterval input").prop("disabled", false);
        }
    }

    /*var validateAvgCalculationIntervalOnFocusOut = function () {
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
    };*/

    /**
     * Link ui events and draw initial chart.
     */
    $(function () {
        // prevent form submit
        $("form").on('submit', function (e) {
            e.preventDefault();
        });
        $("#topArticleSubscribe").click(function () {
            subscribe();
        });
        $("#topArticleUnsubscribe").click(function () {
            unsubscribe();
        });
        setSubscribed(false);
        //validateAvgCalculationIntervalOnFocusOut();
    });

})();
