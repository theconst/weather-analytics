import React, { Component } from 'react';
import { Line, Bar, Polar } from 'react-chartjs-2';
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
            tempTimeData: [
                { x: 10, y: 20 },
                { x: 15, y: 10 }
            ],
            tempModeLabels: ['Temp Label 1', 'Temp Label 2'],
            tempModeData: [300, 400],

            windModeLabels: ['Wind Label 1', 'Wind Label 2'],
            windModeData: [500, 600],
            windRoseData:  [10, 10, 10, 10, 10, 10, 10, 30],
        }
    }

    handleFromChange(event) {
        this.setState({ startDate: event.target.value });
    }

    handleToChange(event) {
        this.setState({ endDate: event.target.value });
    }

    getTotalHoursBy(timeData, fName) {
        const totalHours = timeData
                    .reduce((map, point, i) => {
                        const fValue = point[fName];

                        // time of the first item is always zero
                        const prevDate = new Date(timeData[Math.max(0, i - 1)].time);
                        const nextDate = new Date(timeData[i].time);
                        const samplingHours = (nextDate - prevDate) / 1000 / 3600;
                        return map.set(fValue, (map.get(fValue) || 0.0) + samplingHours);
                    }, new Map());

        const labels = Array.from(totalHours.keys()).sort((a, b) => a - b);
        return {
            labels : labels,
            data : labels.map(l => totalHours.get(l))
        };
    }

    getTimeDataBy(timeData, fName) {
        return timeData.map(point => {
                return { x: point.time, y: point[fName] };
        });
    }

    updatePlots(event) {
        fetch(`/temperatureData?from=${this.state.startDate}&to=${this.state.endDate}`)
            .then(resp => resp.json())
            .then(temperatureTimeData => {
                const totalHoursByTemperature = this.getTotalHoursBy(temperatureTimeData, "temperature");

                this.setState({
                    tempTimeData: this.getTimeDataBy(temperatureTimeData, "temperature"),
                    tempModeLabels: totalHoursByTemperature.labels,
                    tempModeData: totalHoursByTemperature.data
                });
            })
            .catch(e => console.error(e));  //TODO: provide feedback


            // TODO: generify this, remove copying and pasting
        fetch(`/windData/speed?from=${this.state.startDate}&to=${this.state.endDate}`)
            .then(resp => resp.json())
            .then(windTimeData => {
                const totalbHoursByWindSpeed = this.getTotalHoursBy(windTimeData, "windSpeed");
                this.setState({
                    windModeLabels: totalbHoursByWindSpeed.labels,
                    windModeData: totalbHoursByWindSpeed.data,
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
                        <Line
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
                                    label: config.plotsNames.tempTimePlot,
                                    data: this.state.tempTimeData,
                                    fill: false,
                                    borderDash: [5, 5]
                                },
                            ],
                        }} />
                        <Bar data={{
                            labels: this.state.tempModeLabels,
                            datasets: [
                                {
                                    label: config.plotsNames.tempModePlot,
                                    data: this.state.tempModeData,
                                },
                            ],
                        }} />
                        <Bar data={{
                            labels: this.state.windModeLabels,
                            datasets: [
                                {
                                    label: config.plotsNames.windModePlot,
                                    data: this.state.windModeData,
                                },
                            ],
                        }} />  
                        <Polar 
                            data = {{
                                labels: ["N", "NE", "E", "SE", "S", "SW", "W", "NW"],
                                datasets: [{
                                  data: this.state.windRoseData,
                                }]
                            }}
                            options = {{
                                startAngle: - 5 * Math.PI / 8,
                            }}
                        />
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