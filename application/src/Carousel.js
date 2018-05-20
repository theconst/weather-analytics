import React, { Component } from 'react';
import * as back from './back.svg';
import * as next from './next.svg';

export class Carousel extends Component {

    constructor(props) {
        super(props);
        this.state = {
            currentPlotNo : 0
        }
    }
 
     next(direction) {
         this.setState((prevState, props) => {
             const len = props.children.length;
             const current = prevState.currentPlotNo;
             const maybeNext = current + ((direction === 'right') ? 1 : -1);
             const next = (maybeNext < 0) || (maybeNext >= len) ? current : maybeNext;
             return {
                 currentPlotNo: next,
             };
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