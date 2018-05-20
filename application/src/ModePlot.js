import React, { Component } from 'react';
import { Bar } from 'react-chartjs-2';
import * as common from './common.js';

export class ModePlot extends Component {

    constructor(props) {
        super(props);

        this._isMounted = true;

        this.state = {
            labels: [],
            data: [],
        }
    }

    updatePlot() {
        if (this.props.startDate && this.props.endDate) {
            fetch(`/${this.props.endpoint}?from=${this.props.startDate}&to=${this.props.endDate}`)
                .then(resp => resp.json())
                .then(timeData => {
                    const totalHours = common.getTotalHoursBy(timeData, this.props.functionName);
                    this._isMounted && this.setState({
                        labels: totalHours.labels,
                        data: totalHours.data,
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
            <Bar 
                data={{
                    labels: this.state.labels,
                    datasets: [
                        {
                            label: this.props.datasetLabel,
                            data: this.state.data,
                        },
                    ],
                }} />
        );
    }
}