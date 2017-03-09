
function ArticleChart(chartCanvasSelector) {
    const MAX_DATA_POINTS_SHOWN = 15;

    var ctx = $(chartCanvasSelector);

    var data = {
        labels: [],
        datasets: [
            {
                label: "Article/Sec",
                fill: true,
                backgroundColor: "rgba(151,187,205,0.2)",
                borderColor: "rgba(151,187,205,1)",
                pointBorderColor: "rgba(151,187,205,1)",
                pointBackgroundColor: "rgba(151,187,205,1)",
                data: []
            },
            {
                label: "Avg Word Count",
                fill: true,
                backgroundColor: "rgba(205,151,187,0.2)",
                borderColor: "rgba(205,151,187,1)",
                pointBorderColor: "rgba(205,151,187,1)",
                pointBackgroundColor: "rgba(205,151,187,1)",
                data: []
            },
            {
                label: "Avg Rating",
                fill: true,
                backgroundColor: "rgba(205,187,151,0.2)",
                borderColor: "rgba(205,187,151,1)",
                pointBorderColor: "rgba(205,187,151,1)",
                pointBackgroundColor: "rgba(205,187,151,1)",
                data: []
            },
            {
                label: "Avg Article fetch time in ms",
                fill: true,
                backgroundColor: "rgba(187,151,205,0.2)",
                borderColor: "rgba(187,151,205,1)",
                pointBorderColor: "rgba(187,151,205,1)",
                pointBackgroundColor: "rgba(187,151,205,1)",
                data: []
            }
        ]
    };

    var options = {
        animation : false
    };

    var chart = new Chart(ctx, {
        type: 'line',
        data: data,
        options: options
    });

    var updateChart = function (count, words, rating, fetchTimeInMillis) {
        var labels = chart.data.labels;
        if(labels.length == 0) {
            labels.push(1);
        } else {
            labels.push(labels[labels.length-1] + 1);
        }
        if (labels.length > MAX_DATA_POINTS_SHOWN) {
            labels.shift();
        }

        pushValue(chart.data.datasets[0].data, count);
        pushValue(chart.data.datasets[1].data, words/1000);
        pushValue(chart.data.datasets[2].data, rating);
        pushValue(chart.data.datasets[3].data, fetchTimeInMillis);

        chart.update(250, false);
    };

    function pushValue(dataArray, value) {
        dataArray.push(value);
        if (dataArray.length > MAX_DATA_POINTS_SHOWN) {
            dataArray.shift();
        }
    }

    return {
        update: updateChart
    };
}
