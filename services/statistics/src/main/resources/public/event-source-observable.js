/**
 * Created by abremm on 08.03.2017.
 */

function EventSourceObservable(htmlRootIdSelector, onSubscribe, onUnsubscribe) {

    var htmlRootIdSelector = htmlRootIdSelector;
    var onSubscribe = onSubscribe;
    var onUnsubscribe = onUnsubscribe;

    var source;
    var connected = false;
    var subscriptions = [];

    function subscribe() {
        connected = false;
        var observable = Rx.Observable.create(function (observer) {

            var serverUrl = $(htmlRootIdSelector + " .url").val();
            LOG.append('Connecting to ' + serverUrl);
            setSubscribed(true);

            if (!!window.EventSource) {
                source = new EventSource(serverUrl);
            } else {
                LOG.append("Your browser doesn't support SSE");
                return;
            }

            source.addEventListener('message', function(e) {
                var json = null;
                try {
                    json = JSON.parse(e.data);
                } catch (event) {
                    LOG.append("Could not parse data received data: " + event.data);
                }
                if (json) {
                    observer.onNext(json);
                }
            }, false);

            source.addEventListener('open', function(e) {
                // Connection was opened.
                connected = true;
                LOG.append("Connected to " + serverUrl)
            }, false);

            source.addEventListener('error', function(e) {
                if (e.readyState == EventSource.CLOSED) {
                    // Connection was closed.
                    LOG.append('Connection terminated');
                    observer.onComplete();
                    unsubscribe();
                } else {
                    LOG.append('Error ' + e);
                    observer.onError(e);
                    unsubscribe();
                }
            }, false);
        });

        onSubscribe(observable);
    }

    function unsubscribe() {
        source.close();
        setSubscribed(false);
        subscriptions.forEach(function (subscription) {
            subscription.dispose()
        });
        subscriptions = [];
        onUnsubscribe();
    }

    function setSubscribed(subscribed) {
        if (subscribed) {
            $(htmlRootIdSelector + " .subscribe").hide();
            $(htmlRootIdSelector + " .unsubscribe").show();
        } else {
            connected = false;
            $(htmlRootIdSelector + " .subscribe").show();
            $(htmlRootIdSelector + " .unsubscribe").hide();
        }
    }

    /**
     * Link ui events and draw initial chart.
     */
    $(function () {
        // prevent form submit
        $("form").on('submit', function (e) {
            e.preventDefault();
        });
        $(htmlRootIdSelector + " .subscribe").click(function () {
            subscribe();
        });
        $(htmlRootIdSelector + " .unsubscribe").click(function () {
            unsubscribe();
        });
        setSubscribed(false);
    });


}
