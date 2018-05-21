import { Component } from 'react';

const LOAD_DELAY = 100;

export class AbstractUnmountablePlot extends Component {

    constructor(props) {
        super(props);

        this._isMounted = true;
    }

    _handleDataset(data) {
        throw new Error("'_handleDataset' is not implemented");
    }

    _handleError(error) {
        console.error(error);
        throw new Error("'_handleError' not implemented");
    }

    updatePlot() {
        if (this.props.startDate != null && this.props.endDate != null) {
            fetch(`${this.props.endpoint}?from=${this.props.startDate}&to=${this.props.endDate}`)
                .then(resp => resp.json())
                .then(data => this._isMounted && this._handleDataset(data))
                .catch(e => this._handleError(e));
        }
    }

    componentDidMount() {
        setTimeout(() => this._isMounted && this.updatePlot(), LOAD_DELAY);
    }

    componentDidUpdate(prevProps) {
        if (JSON.stringify(prevProps) !== JSON.stringify(this.props)) {
            this.updatePlot();
        }
     } 

    componentWillUnmount() {
        this._isMounted = false;
    }
}