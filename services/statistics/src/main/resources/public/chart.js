
function UpdateChart(chartCanvasSelector, dataLabels) {

    const MAX_DATA_POINTS_SHOWN = 15;
    const colors = ["151,187,205", "205,151,187", "205,187,151", "161,212,144"];

    var options = {
        animation : false
    };

    var ctx = $(chartCanvasSelector);

    var data = {
        labels: [],
        datasets: []
    };

    for (var i = 0; i < dataLabels.length; i++) {
        data.datasets.push({
            label: dataLabels[i],
            fill: true,
            backgroundColor: getBackgroundColor(i),
            borderColor: getMainColor(i),
            pointBorderColor: getMainColor(i),
            pointBackgroundColor: getMainColor(i),
            data: []
        });
    }

    var chart = new Chart(ctx, {
        type: 'line',
        data: data,
        options: options
    });

    var updateChart = function (values) {
        var labels = chart.data.labels;
        if(labels.length == 0) {
            labels.push(1);
        } else {
            labels.push(labels[labels.length-1] + 1);
        }
        if (labels.length > MAX_DATA_POINTS_SHOWN) {
            labels.shift();
        }

        if (chart.data.datasets.length !== values.length) {
            throw new Error("Illegal state in chart - unexpected number of new values: " + values.length);
        }

        for (var i = 0; i < values.length; i++) {
            pushValue(chart.data.datasets[i].data, values[i]);
        }

        chart.update(250, false);
    };

    function pushValue(dataArray, value) {
        dataArray.push(value);
        if (dataArray.length > MAX_DATA_POINTS_SHOWN) {
            dataArray.shift();
        }
    }

    function getBackgroundColor(i) {
        return "rgba("+getColor(i)+",0.2)";
    }

    function getMainColor(i) {
        return "rgba("+getColor(i)+",1)";
    }

    function getColor(i) {
        return colors[i % colors.length];
    }

    return {
        update: updateChart
    };
}
