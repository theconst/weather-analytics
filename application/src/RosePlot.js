import React from 'react';
import { AbstractUnmountablePlot } from './AbstractUnmountablePlot.js';
import { Polar } from 'react-chartjs-2';

const STARTING_ANGLE = -5 * Math.PI / 8;

export class RosePlot extends AbstractUnmountablePlot {

    constructor(props) {
        super(props);

        this.labels = ["N", "NE", "E", "SE", "S", "SW", "W", "NW"];

        this.state = {
            data: [0, 0, 0, 0, 0, 0, 0, 0],
        }
    }

    _handleDataset(data) {
        this.setState({
            data: this.labels.map(label => (data[label] || 0).toFixed(2)),
        });
    }

    render() {
        return (
            <Polar
                data = {{
                    labels: this.labels,
                    datasets: [{
                        data: this.state.data,
                    }],
                }}
                options = {{
                    startAngle: STARTING_ANGLE,
                    title: {
                        display: true,
                        text: this.props.title,
                    },
                    legend: {
                        display: false
                    },
                }} />
        );
    }
}