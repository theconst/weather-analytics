import React, { Component } from 'react';
import { Line, Bar } from 'react-chartjs-2';
import * as config from './config.json';
import * as back from './back.svg';
import * as next from './next.svg';

//TODO: pass all style names down, redo the plot carousel

export class TabbedPane extends Component {

    constructor(props) {
        super(props);
        this.state = {
            //TODO: fetch initial date range from server
            startDate: new Date(),
            endDate: new Date(),
            //TODO: remove this stub
            temperatureTimeData: [
                {
                    x: 10,
                    y: 20
                },
                {
                    x: 15,
                    y: 10
                }
            ],
            modeLabels: ['Label 1', 'Label 2'],
            modeData: [300, 400],
        }
    }

    handleFromChange(event) {
        this.setState({ startDate: event.target.value });
    }

    handleToChange(event) {
        this.setState({ endDate: event.target.value });
    }

    updatePlots(event) {
        fetch(`/temperatureData?from=${this.state.startDate}&to=${this.state.endDate}`)
            .then(resp => resp.json())
            .then(temperatureTimeData => {
                const timePlotData = temperatureTimeData
                    .map(({ temperature, time }) => {
                        return { x: time, y: temperature }
                    });

                const totalHoursByTemperature = temperatureTimeData
                    .reduce((map, { temperature, time }, i) => {
                        // time of the first item is always zero
                        const prevDate = new Date(temperatureTimeData[Math.max(0, i - 1)].time);
                        const nextDate = new Date(temperatureTimeData[i].time);
                        const samplingHours = (nextDate - prevDate) / 1000 / 3600;
                        return map.set(temperature, (map.get(temperature) || 0.0) + samplingHours);
                    }, new Map());
                const modeLabels = Array.from(totalHoursByTemperature.keys()).sort();
                const modeData = modeLabels.map(l => totalHoursByTemperature.get(l));

                this.setState({
                    temperatureTimeData: timePlotData,
                    modeLabels: modeLabels,
                    modeData: modeData
                });
            })
            .catch(e => console.error(e));  //TODO: provide feedback
        event.preventDefault();
    }

    render() {
        return (
            <div className="tabbed-pane">
                <div key="nav-bar" className="tabbed-pane__navigation-bar">
                    <ul>
                        <li><a>{config.heading}</a></li>
                        {config.tabNames.map((name, i) => <li key={i}><a>{name}</a></li>)}
                    </ul>
                </div>
                <main className="tabbed-pane__area">
                    <PlotCarousel>
                        <Line data={{
                            datasets: [
                                {
                                    label: config.plotsNames.timePlot,
                                    data: this.state.temperatureTimeData,
                                    fill: false,
                                    borderDash: [5, 5]
                                },
                            ],
                        }} />
                        <Bar data={{
                            labels: this.state.modeLabels,
                            datasets: [
                                {
                                    label: config.plotsNames.modePlot,
                                    data: this.state.modeData,
                                },
                            ],
                        }} />
                    </PlotCarousel>
                    <div className="tabbed-pane__area__text-area">
                        <h2>Дані</h2>
                        <form onSubmit={this.updatePlots.bind(this)} >
                            <label htmlFor="from">Початок:</label>
                            <input name="from" type="datetime-local" value={this.state.startDate} onChange={this.handleFromChange.bind(this)} />
                            <label htmlFor="to">Кінець:</label>
                            <input name="to" type="datetime-local" value={this.state.endDate} onChange={this.handleToChange.bind(this)} />
                            <input type="submit" value="Завантажити" />
                        </form>
                    </div>
                </main>
            </div>
        );
    }
}

//TODO: rename it to sound better (it also handles info)
export class PlotCarousel extends Component {

    state = {
        currentPlotNo: 0,
    }

    next(direction) {
        const inc = (direction === 'right') ? 1 : -1;
        this.setState((prevState, props) => {
            const len = props.children.length;
            const next = prevState.currentPlotNo + inc;
            if (next < 0 || next >= len) {
                return prevState;
            } else {
                return {
                    currentPlotNo: next,
                }
            }
        });
    }

    render() {
        return (
            <div key="plot" className="tabbed-pane__area__plot-carousel">
                <img className="tabbed-pane__area__plot-carousel__btn-left"
                    onClick={this.next.bind(this, 'left')}
                    src={back}
                    alt="Go Left" />
                <div className="tabbed-pane__area__plot-carousel__plot">
                    {this.props.children[this.state.currentPlotNo]}
                </div>
                <img className="tabbed-pane__area__plot-carousel__btn-right"
                    onClick={this.next.bind(this, 'right')}
                    src={next}
                    alt="Go Right" />
            </div>
        );
    }
}