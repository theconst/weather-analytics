import React from 'react';
import { Bar } from 'react-chartjs-2';
import { AbstractUnmountablePlot } from './AbstractUnmountablePlot.js';
import * as common from './common.js';

export class ModePlot extends AbstractUnmountablePlot {

    constructor(props) {
        super(props);

        this._isMounted = true;

        this.state = {
            labels: [],
            data: [],
        }
    }

    _handleDataset(data) {
        const totalHours = common.getTotalHoursBy(data, this.props.functionName);
        this.setState({
            labels: totalHours.labels,
            data: totalHours.data,
        });
    }

    render() {
        return (
            <Bar 
                data={{
                    labels: this.state.labels,
                    datasets: [
                        {
                            label: this.props.datasetLabel,
                            data: this.state.data,
                        },
                    ],
                }} 
                options={{
                    title: {
                        display: true,
                        text: this.props.title,
                    },
                    scales: {
                        yAxes: [{
                          scaleLabel: {
                            display: true,
                            labelString: this.props.yLabel,
                          }
                        }],
                        xAxes: [{
                            scaleLabel: {
                              display: true,
                              labelString: this.props.xLabel,
                            }
                          }]
                    },
                }}
                />
        );
    }
}