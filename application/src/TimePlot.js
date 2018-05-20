import React, { Component } from 'react';
import { Line } from 'react-chartjs-2';
import *  as common from './common.js';

export class TimePlot extends Component {

    constructor(props) {
        super(props);

        this._isMounted = true;

        this.state = {
            timeData: [],
        }
    }

    // TODO: make class abstract for plots to reuse code
    updatePlot() {
        if (this.props.startDate && this.props.endDate) {
            fetch(`/${this.props.endpoint}?from=${this.props.startDate}&to=${this.props.endDate}`)
                .then(resp => resp.json())
                .then(timeData => {
                    this._isMounted && this.setState({
                        timeData: common.getTimeDataBy(timeData, "temperature"),
                    });
                })
                .catch(e => console.error(e));
        }
    }

    componentDidUpdate() {
        this.updatePlot();
    }

    componentDidMount() {
        setTimeout(100, () => this._isMounted && this.updatePlot());
    }

    componentWillUnmount() {
        this._isMounted = false;
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
                    }
                }}
            
                data={{
                    datasets: [
                        {
                            label: this.props.datasetLabel,
                            data: this.state.timeData,
                            fill: false,
                            borderDash: [5, 5]
                        },
                    ],
                }} />
        );
    }
}