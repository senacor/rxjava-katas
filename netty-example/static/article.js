(function() {

    const DEFAULT_BUFFER_INTERVAL_IN_MILLIS = 1000;
    const MAX_ARTICLE_DISPLAYED = 5;

    var chart;
    var connected = false;
    var subscriptions = [];

    function subscribe() {
        var webSocketUrl = $("#url").val();
        appendLog('Connecting to ' + webSocketUrl);
        setSubscribed(true);

        // subscribing creates the underlying socket and will emit a stream of incoming objects
        var socketSubject = connectToWebSocket(webSocketUrl);

        /**
         * Tasks:
         * 1. To display the date the incoming objects must be mapped to a json object. Use the function "toJson" to map the incoming objects.
         */
        var jsonObjectsObservable = socketSubject.map(toJson);

        /*
         * Tasks:
         * 2. Find out how share the observable providing the json objects.
         */
        var sharedObservable = jsonObjectsObservable.share();

        const subscription1 = sharedObservable.subscribe(
            function (article) { // next
                appendArticles(article);
                // DELETED chart.update(1, 0, 0);
            },
            function (e) { // error
                // errors and "unclean" closes land here
                appendLog("Error: " + e);
                setSubscribed(false);
            },
            function () { // complete
                appendLog("Complete");
                setSubscribed(false);
            }
        );
        subscriptions.push(subscription1);

        /*
         * Tasks:
         * 3. Subscribe a second observer to the sharedObservable and add the subscription to the subscriptions array "subscriptions.push(subscription2)
         * 4. Move the "chart.update(count, words, rating)" from the first observer into the new one.
         * 5. Use the method calculateAvg and the avgCalculationInterval to update the chart in the given interval with the avg. values.
         */
        var avgCalculationInterval = getAvgCalculationInterval();
        const subscription2 = sharedObservable
            .bufferTime(avgCalculationInterval)
            .map(calculateAvg)
            .subscribe(
                function (avg) { // next
                    chart.update(avg.count, avg.words, avg.rating);
                }
            );
        subscriptions.push(subscription2);
    }

    /**
     * @param articles Provide an array of articles.
     * @returns Returns the count of the articles, the avg. word count and the avg. rating. E.g. {count: 5, words: 756.45, rating: 3.5}
     */
    function calculateAvg(articles) {
        var wordSum = 0;
        var ratingSum = 0;
        if (articles && articles.constructor === Array && articles.length > 0) {
            var numOfItems = articles.length;
            articles.forEach(function (article) {
                wordSum += (article.wordCount) ? article.wordCount : 0;
                ratingSum += (article.rating) ? article.rating : 0;
            });
            return {
                count: numOfItems,
                words: (wordSum == 0) ? 0 : wordSum / numOfItems,
                rating: (ratingSum == 0) ? 0 : ratingSum / numOfItems
            };
        } else {
            return {count: 1, words: 0, rating: 0};
        }
    }

    function toJson(event) {
        try {
            return JSON.parse(event.data)
        } catch (event) {
            appendLog("Could not parse data received data: " + event.data);
        }
    }

    var connectToWebSocket = function (webSocketUrl) {
        var socketSubject;

        // an observer for when the socket is open
        var openObserver = Rx.Observer.create(function (e) {
            // Now it is safe to send a message
            socketSubject.onNext('#subscribe#articleBeingRead');
            connected = true;
            appendLog('Subscribed');
        });

        // an observer for when the socket is about to close
        var closingObserver = Rx.Observer.create(function () {
            appendLog('Connection terminated');
            setSubscribed(false);
        });

        // create a web socket subject
        socketSubject = Rx.DOM.fromWebSocket(
            webSocketUrl,
            null, // no protocol
            openObserver,
            closingObserver);

        return socketSubject;
    };

    function unsubscribe() {
        subscriptions.forEach(function (subscription) {
            subscription.dispose()
        });
        subscriptions = [];
    }

    function appendLog(message) {
        $("#log").append("<tr><td>" + message + "</td></tr>");
    }

    function appendArticles(article) {
        // add article
        var name = article;
        var wordCount = "-";
        var rating = "-";
        var content = "";

        if (article) {
            if (article.name) {
                name = article.name;
            }
            if (article.wordCount) {
                wordCount = article.wordCount;
            }
            if (article.rating) {
                rating = article.rating;
            }
            if (article.content) {
                content = article.content;
            }
        }

        $("#articleBeingRead").prepend("<div><b>" + name + "</b>" +
            "<span>Word Count:" + wordCount + " Rating: " + rating + "</span>" +
            "<br />" + content + "</div>");

        // show max 5 article

        if ($("#articleBeingRead div").length > MAX_ARTICLE_DISPLAYED) {
            $("#articleBeingRead div:last").remove();
        }
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
        $("#avgCalculationInterval input").val(DEFAULT_BUFFER_INTERVAL_IN_MILLIS);
        $("#avgCalculationInterval input").focusout(function () {
            const value = $("#avgCalculationInterval input").val();
            if (!value || !/^\d+$/.test(value)) {
                $("#avgCalculationInterval input").val(DEFAULT_BUFFER_INTERVAL_IN_MILLIS);
            }
        });
    };

    function getAvgCalculationInterval() {
        // + is required to convert to a number
        return +$("#avgCalculationInterval input").val();
    }

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
