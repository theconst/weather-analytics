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
                    scales: {
                        xAxes: [{
                            type: 'time',
                            time: {
                                displayFormats: {
                                    quarter: 'MMM YYYY'
                                }
                            }
                        }]
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