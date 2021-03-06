import * as React from 'react';
import { ComponentProps, Component } from '../basic/component';
import Icon from '../icon';
import './index.less';

interface Props extends ComponentProps {
    name: string;
    list: any[];
    valueName?: string;
    labelName?: string;
}

interface State {
    value: string;
}

export default class Radio extends Component<Props, State> {
    constructor(props: Props) {
        super(props);

        this.state = {
            value: this.getDefaultValue()
        };
    }

    render(): JSX.Element[] {
        let elements: JSX.Element[] = [];
        elements.push(<input type="hidden" name={this.props.name} value={this.state.value} />);
        if (!this.props.list)
            return elements;

        this.props.list.map((kv, index) => {
            let value: string = typeof kv === 'string' ? kv : kv[this.props.valueName || 'value'];
            let checked: boolean = value === this.state.value;
            let label: string = typeof kv === 'string' ? kv : kv[this.props.labelName || 'label'];
            elements.push(
                <div className={this.getClassName('radio')} onClick={() => { this.setState({ value: checked ? '' : value }) }}>
                    <Icon code="&#xe633;" className="uncheck" style={{ display: checked ? 'none' : '' }} />
                    <Icon code="&#xe634;" className="checked" style={{ display: checked ? '' : 'none' }} />
                    {label}
                </div>
            );
        });

        return elements;
    }
}