import React, { Component } from 'react';
import * as back from './back.svg';
import * as next from './next.svg';

export class Carousel extends Component {

    constructor(props) {
        super(props);
        this.state = {
            currentNo : 0
        }
    }
 
    next(direction) {
        this.setState((prevState, props) => {
            const len = props.children.length;
            const current = prevState.currentNo;
            const maybeNext = current + ((direction === 'right') ? 1 : -1);
            const next = (maybeNext < 0) || (maybeNext >= len) ? current : maybeNext;
            return {
                currentNo: next,
            };
         });
     }
 
     render() {
         return (
             //TODO: parametrize css to make carousel reusable
             <div className="carousel">
                <img className="carousel__btn carousel__btn--left"
                    onClick={this.next.bind(this, 'left')}
                    src={back}
                    alt="Left" />
                <div className="carousel__item">
                    {this.props.children[this.state.currentNo]}
                </div>
                <img className="carousel__btn carousel__btn--right"
                    onClick={this.next.bind(this, 'right')}
                    src={next}
                    alt="Right" />
             </div>
         );
     }
 }