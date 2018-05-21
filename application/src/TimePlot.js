import React from 'react';
import { AbstractUnmountablePlot } from './AbstractUnmountablePlot.js';
import { Line } from 'react-chartjs-2';
import *  as common from './common.js';

export class TimePlot extends AbstractUnmountablePlot {

    constructor(props) {
        super(props);

        this.state = {
            timeData: [],
        }
    }

    _handleDataset(data) {
        this.setState({
            timeData: common.getTimeDataBy(data, "temperature"),
        });
    }

    render() {
        return (
            <Line 
                key="temperatureTimePlot"
                options={{
                    title: {
                        display: true,
                        text: this.props.title,
                    },
                    scales: {
                        xAxes: [{
                            type: 'time',
                            time: {
                                displayFormats: {
                                    quarter: 'MMM YYYY'
                                }
                            },
                            scaleLabel: {
                                display: true,
                                labelString: this.props.xLabel,
                            },
                        }],
                        yAxes: [{
                            scaleLabel: {
                                display: true,
                                labelString: this.props.yLabel,
                            },
                        }],
                    },
                }}
            
                data={{
                    datasets: [
                        {
                            label: this.props.datasetLabel,
                            data: this.state.timeData,
                            fill: false,
                        },
                    ],
                }} />
        );
    }
}