import * as React from 'react';
import message from '../../../util/message';
import merger from '../../../util/merger';
import Icon from '../../../ui/icon';
import { Prop, Page, Operate } from '../../meta';
import { service } from '../../service';
import { PageComponent, PageProps, PageState, Toolbar, getSuccess } from '../index';
import './i18n';
import './index.less';
import { FormEvent } from 'react';

export default class Grid extends PageComponent<PageProps, PageState> {
    constructor(props: PageProps) {
        super(props);
        this.state = {
            data: props.data || []
        };
    }

    render(): JSX.Element {
        let pagination = this.state.data.hasOwnProperty('list');
        let list: object[] = pagination ? this.state.data['list'] : this.state.data;
        let page: Page = this.props.meta[this.props.service.substring(this.props.service.lastIndexOf('.') + 1)];
        let props: Prop[] = [];
        this.props.meta.props.map(prop => {
            if (prop.type !== 'hidden' && prop.type !== 'editor' && !this.ignore(prop))
                props.push(prop);
        });

        return (
            <div className="grid">
                {this.search(props, page)}
                {this.table(props, page, list)}
                {this.pagination(pagination)}
                <Toolbar meta={this.props.meta} ops={page.toolbar} />
            </div>
        );
    }

    private search(props: Prop[], page: Page): JSX.Element | null {
        if (!page.search || page.search.length === 0)
            return null;

        return (
            <form action="javascript:void(0);" onSubmit={(event) => this.submitSearch(event)}>
                {page.search.map((prop, index) =>
                    <div key={index} className="search">
                        <label>{prop.label}</label>
                        {this.input(this.findProp(props, prop), this.props.parameter, 'select.all')}
                    </div>
                )}
                <button>{message.get('grid.search')}</button>
            </form>
        );
    }

    private submitSearch(event: FormEvent<HTMLFormElement>): void {
        let form: HTMLFormElement = event.currentTarget;
        let parameter = {};
        for (let i = 0; i < form.elements.length; i++)
            if (form.elements[i]['name'])
                parameter[form.elements[i]['name']] = form.elements[i]['value'];
        service.to(this.props.service, parameter);
    }

    private table(props: Prop[], page: Page, list: object[]): JSX.Element {
        return (
            <table cellSpacing="1">
                <thead>
                    <tr>
                        <th />
                        {props.map((prop, index) => <th key={index}>{prop.label}</th>)}
                        {page.ops ? <th /> : null}
                    </tr>
                </thead>
                <tbody>
                    {list.map((row, i) =>
                        <tr key={i * 100}>
                            <th>{i + 1}</th>
                            {props.map((prop, j) => <td key={i * 100 + j + 1} className={'data ' + (prop.type || '')}>{this.td(row, prop)}</td>)}
                            {this.ops(page.ops, row)}
                        </tr>
                    )}
                    {this.empty(list, props, page.ops)}
                </tbody>
            </table>
        );
    }

    private td(row: object, prop: Prop): any {
        let value = this.findValue(prop, row);
        if (prop.type === 'image')
            return <img src={value} />;

        if (prop.labels)
            return prop.labels[value] || value;

        if (prop.values)
            return prop.values[value] || value;

        return value;
    }

    private ops(ops: Operate[] | undefined, row: object): JSX.Element | null {
        if (!ops || ops.length == 0)
            return null;

        let operates: Operate[] = [];
        ops.map((op, index) => {
            if (!op.when || eval(op.when))
                operates.push(op);
        });

        return (
            <td className="ops">
                {operates.map((op, index) => <a key={index} href="javascript:void(0);" onClick={() => this.op(op, row)}>{op.label}</a>)}
            </td>
        );
    }

    private op(op: Operate, row: object): void {
        if (op.type === 'modify' || op.type === 'lite-modify') {
            service.to(this.props.meta.key + '.' + op.type, {}, row);

            return;
        }

        if (op.type === 'pass') {
            let remark = prompt(message.get('grid.audit.remark'), '');
            if (remark === null)
                return;

            service.execute(this.props.meta.key + '.pass', {}, {
                ids: row['id'],
                auditRemark: remark
            }, getSuccess(this.props.meta, op, ".query"));

            return;
        }

        if (op.type === 'reject') {
            let remark = prompt(message.get('grid.audit.remark'), '');
            if (remark === null)
                return;

            service.execute(this.props.meta.key + '.reject', {}, {
                ids: row['id'],
                auditRemark: remark
            }, getSuccess(this.props.meta, op, ".query"));

            return;
        }

        if (op.type === 'delete') {
            service.execute(this.props.meta.key + '.delete', {}, { id: row['id'] }, getSuccess(this.props.meta, op, ".query"));

            return;
        }

        let key: string = op.service || '';
        if (key.charAt(0) == '.')
            key = this.props.meta.key + key;
        if (op.type === 'post-id') {
            service.execute(key, {}, merger.merge({ id: row['id'] }, op.parameter || {}), getSuccess(this.props.meta, op, ".query"));

            return;
        }

        service.to(key, this.parameter(row, op.parameter));
    }

    private parameter(row: object, param?: object): object {
        if (!param)
            return row;

        let parameter = {};
        for (let key in param)
            parameter[key] = row[param[key]] || param[key];
        service.setParameter(parameter);

        return parameter;
    }

    private empty(list: object[], props: Prop[], ops?: Operate[]): JSX.Element | null {
        if (list && list.length > 0)
            return null;

        return (
            <tr>
                <th>0</th>
                <td colSpan={props.length + (ops && ops.length > 0 ? 1 : 0)} className="empty">{message.get('grid.empty')}</td>
            </tr>
        );
    }

    private pagination(enable: boolean): JSX.Element | null {
        if (!enable || this.state.data.pageEnd <= 1)
            return null;

        let numbers: JSX.Element[] = [];
        for (let i = this.state.data.pageStart; i <= this.state.data.pageEnd; i++) {
            let props: any = {
                key: i,
                href: 'javascript:void(0);'
            };
            if (i === this.state.data.number)
                props.className = 'active';
            numbers.push(<a {...props} onClick={() => this.pageTo(i)}>{i}</a>);
        }

        return (
            <div className="pagination">
                <a href="javascript:void(0);" onClick={() => this.pageTo(this.state.data.number - 1)}><Icon code="&#xe608;" /></a>
                {numbers}
                <a href="javascript:void(0);" onClick={() => this.pageTo(this.state.data.number + 1)}><Icon code="&#xe609;" /></a>
            </div>
        );
    }

    private pageTo(num: number): void {
        service.execute(this.props.service, {}, merger.merge({}, this.props.parameter, { pageNum: num }))
            .then(data => this.setState({ data: data }));
    }
}