import React, { Component } from 'react';
import { Polar } from 'react-chartjs-2';

const STARTING_ANGLE = -5 * Math.PI / 8;

export class RosePlot extends Component {

    constructor(props) {
        super(props);

        this.labels = ["N", "NE", "E", "SE", "S", "SW", "W", "NW"];
        this._isMounted = true;

        this.state = {
            data: [0, 0, 0, 0, 0, 0, 0, 0],
        }
    }


    updatePlot() {
        if (this.props.startDate && this.props.endDate) {
            fetch(`/${this.props.endpoint}?from=${this.props.startDate}&to=${this.props.endDate}`)
                .then(resp => resp.json())
                .then(data => {
                    this._isMounted && this.setState({
                        data: this.labels.map(label => (data[label] || 0).toFixed(2)),
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
            <Polar
                data = {{
                    labels: this.labels,
                    datasets: [{
                        data: this.state.data,
                    }],
                }}
                options = {{
                    startAngle: STARTING_ANGLE,
                }} />
        );
    }
}