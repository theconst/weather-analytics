import './App.css';

import React, { Component } from 'react';
import * as config from './config.json';
import { TimePlot } from './TimePlot.js';
import { ModePlot } from './ModePlot.js';
import { RosePlot } from './RosePlot.js';
import { Carousel } from './Carousel.js'

class App extends Component {

  state = {
    startDate: '',
    endDate: '',
  }

  handleFromChange(event) {
      this.setState({ startDate: event.target.value });
  }

  handleToChange(event) {
      this.setState({ endDate: event.target.value });
  }

  updatePlots(event) {
      this.setState((prevState, props) => {
          return {
              submittedStart: prevState.startDate,
              submittedEnd: prevState.endDate,
          }
      })

      event.preventDefault();
  }

  componentDidMount() {
    fetch(`${config.server}/timeData/range`)
      .then(resp => resp.json())
      .then(timeRange => {
          this.setState({
              startDate: timeRange.from,
              endDate: timeRange.to,
          });
      })
      .catch(e => console.error(e));
  }

  render() {
      return [
          <nav key="nav-bar" className="navigation-bar">
              <ul className="navigation-bar__items">
                  <li key="-1" className="navigation-bar__item navigation-bar__item--heading"><a>{config.heading}</a></li>
                  {config.tabNames.map((name, i) => <li key={i} className="navigation-bar__item"><a>{name}</a></li>)}
              </ul>
          </nav>,
          <Carousel key="plot">
              <TimePlot 
                  key="temperatureTimePlot" 
                  endpoint={`${config.server}/temperatureData`}
                  functionName="temperature"
                  datasetLabel={config.plotsNames.tempTimePlot}
                  startDate={this.state.submittedStart} 
                  endDate={this.state.submittedEnd} />
              <ModePlot 
                  key="temperatureModePlot" 
                  endpoint={`${config.server}/temperatureData`}
                  functionName="temperature"
                  datasetLabel={config.plotsNames.tempModePlot}
                  startDate={this.state.submittedStart} 
                  endDate={this.state.submittedEnd} />
              <ModePlot 
                  key="windModePlot"
                  endpoint={`${config.server}/windData/speed`}
                  functionName="windSpeed"
                  datasetLabel={config.plotsNames.windModePlot}
                  startDate={this.state.submittedStart} 
                  endDate={this.state.submittedEnd} />
              <RosePlot 
                  key="windRosePlot"
                  endpoint={`${config.server}/windData/rose`}
                  datasetLabel={config.plotsNames.windModePlot}
                  startDate={this.state.submittedStart} 
                  endDate={this.state.submittedEnd} />
          </Carousel>,
          <aside key="input" className="input-area">
            <h2 className="input-area__title">Вибір інтервалу</h2>
            <form onSubmit={this.updatePlots.bind(this)} >
                <input 
                    name="from" 
                    title="Початок"
                    className="input-area__item"
                    type="datetime-local" 
                    value={this.state.startDate} 
                    onChange={this.handleFromChange.bind(this)} />
                <input 
                    name="to"
                    title="Кінець"
                    className="input-area__item"
                    type="datetime-local" 
                    value={this.state.endDate} 
                    onChange={this.handleToChange.bind(this)} />
                <input className="input-area__item input-area__item--submit" type="submit" value="Завантажити" />
            </form>
          </aside>,
          <footer key="footer" className="footer">
            Application by <a href="https://github.com/TheConst">Kostiantyn Kovalchuk</a>. Icons made by <a href="https://www.flaticon.com/authors/gregor-cresnar" title="Gregor Cresnar">Gregor Cresnar</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a>
          </footer>
      ];
  }
}

export default App;