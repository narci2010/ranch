import * as React from 'react';
import { Prop, Page, Operate } from '../../meta';
import { service } from '../../service';
import { PageComponent, PageProps, PageState, Toolbar, getSuccess } from '../index';
import './index.less';

export default class Grid extends PageComponent<PageProps, PageState> {
    constructor(props: PageProps) {
        super(props);
        this.state = {
            data: []
        };
    }

    render(): JSX.Element {
        this.refresh();
        let pagination = this.state.data.hasOwnProperty('list');
        let list: object[] = pagination ? this.state.data['list'] : this.state.data;
        let page: Page = this.props.meta[this.props.service.substring(this.props.service.lastIndexOf('.') + 1)];
        let props: Prop[] = [];
        this.props.meta.props.map(prop => {
            if (prop.type !== 'hidden')
                props.push(prop);
        });

        return (
            <div className="grid">
                <table cellSpacing="1">
                    <thead>
                        <tr>
                            <th></th>
                            {props.map((prop, index) => <th key={index}>{prop.label}</th>)}
                            {page.ops ? <th></th> : ''}
                        </tr>
                    </thead>
                    <tbody>
                        {list.map((row, index) =>
                            <tr key={index}>
                                <th>{index + 1}</th>
                                {props.map((prop, index) => <td key={index} className={'data ' + (prop.type || '')}>{this.td(row, prop)}</td>)}
                                {this.ops(page.ops, row)}
                            </tr>
                        )}
                    </tbody>
                </table>
                <Toolbar meta={this.props.meta} ops={page.toolbar} />
            </div>
        );
    }

    private td(row: object, prop: Prop): any {
        if (!row.hasOwnProperty(prop.name))
            return '';

        let value = row[prop.name];
        if (prop.type === 'image')
            return <img src={value} />;

        if (prop.labels)
            return prop.labels[value] || value;

        if (prop.values)
            return prop.values[value] || value;

        return value;
    }

    private ops(ops: Operate[], data: object): JSX.Element | string {
        if (!ops || ops.length == 0)
            return '';

        return (
            <td className="ops">
                {ops.map((op, index) => <a key={index} href="javascript:void(0);" onClick={() => this.op(op, data)}>{op.label}</a>)}
            </td>
        );
    }

    private op(op: Operate, data: object): void {
        if (op.type === 'modify') {
            service.to(this.props.meta.key + '.modify', {}, data);

            return;
        }

        if (op.type === 'delete') {
            service.execute(this.props.meta.key + '.delete', {}, { id: data['id'] }, getSuccess(this.props.meta, op, ".query"));

            return;
        }

        let key: string = op.service || '';
        if (key.charAt(0) == '.')
            key = this.props.meta.key + key;
        service.to(key, this.parameter(data, op.parameter));
    }

    private parameter(data: object, param?: object): object {
        if (!param)
            return data;

        let parameter = {};
        for (const key in param)
            parameter[key] = data[param[key]] || '';
        service.setParameter(parameter);

        return parameter;
    }
}