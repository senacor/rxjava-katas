var LOG = (function(LOG) {

    var $log;

    /**
     * Link ui events and draw initial chart.
     */
    $(function () {
        $log = $("#log");

        $("#clearLog").click(function () {
            clear();
        });
    });

    var append = function(newEntry) {
        $log.append("<div>" + newEntry + "</div>");
    };

    var clear = function () {
        $log.html("");
    };

    // public interface
    LOG.append = append;
    LOG.clear = clear;
    return LOG;
})(LOG || {});

