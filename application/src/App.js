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
                  <Carousel>
                      <TimePlot 
                          key="temperatureTimePlot" 
                          endpoint="temperatureData"
                          functionName="temperature"
                          datasetLabel={config.plotsNames.tempTimePlot}
                          startDate={this.state.submittedStart} 
                          endDate={this.state.submittedEnd} />
                      <ModePlot 
                          key="temperatureModePlot" 
                          endpoint="temperatureData"
                          functionName="temperature"
                          datasetLabel={config.plotsNames.tempModePlot}
                          startDate={this.state.submittedStart} 
                          endDate={this.state.submittedEnd} />
                      <ModePlot 
                          key="windModePlot"
                          endpoint="windData/speed"
                          functionName="windSpeed"
                          datasetLabel={config.plotsNames.windModePlot}
                          startDate={this.state.submittedStart} 
                          endDate={this.state.submittedEnd} />
                      <RosePlot 
                          key="windRosPlot"
                          endpoint="windData/rose"
                          datasetLabel={config.plotsNames.windModePlot}
                          startDate={this.state.submittedStart} 
                          endDate={this.state.submittedEnd} />
                  </Carousel>
                  <div className="tabbed-pane__area__text-area">
                      <h2>Дані</h2>
                      <form onSubmit={this.updatePlots.bind(this)} >
                          <label htmlFor="from">Початок:</label>
                          <input 
                              name="from" 
                              type="datetime-local" 
                              value={this.state.startDate} 
                              onChange={this.handleFromChange.bind(this)} />
                          <label htmlFor="to">Кінець:</label>
                          <input 
                              name="to" 
                              type="datetime-local" 
                              value={this.state.endDate} 
                              onChange={this.handleToChange.bind(this)} />
                          <input type="submit" value="Завантажити" />
                      </form>
                  </div>
              </main>
              <footer>
                Icons made by <a href="https://www.flaticon.com/authors/gregor-cresnar" title="Gregor Cresnar">Gregor Cresnar</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a>
              </footer>
          </div>
      );
  }
}

export default App;