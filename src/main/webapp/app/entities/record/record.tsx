import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRecord } from 'app/shared/model/record.model';
import { getEntities } from './record.reducer';

export const Record = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const recordList = useAppSelector(state => state.record.entities);
  const loading = useAppSelector(state => state.record.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="record-heading" data-cy="RecordHeading">
        <Translate contentKey="akathonApp.record.home.title">Records</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="akathonApp.record.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/record/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="akathonApp.record.home.createLabel">Create new Record</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {recordList && recordList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="akathonApp.record.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="akathonApp.record.dcuid">Dcuid</Translate>
                </th>
                <th>
                  <Translate contentKey="akathonApp.record.current">Current</Translate>
                </th>
                <th>
                  <Translate contentKey="akathonApp.record.voltage">Voltage</Translate>
                </th>
                <th>
                  <Translate contentKey="akathonApp.record.timestamp">Timestamp</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {recordList.map((record, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/record/${record.id}`} color="link" size="sm">
                      {record.id}
                    </Button>
                  </td>
                  <td>{record.dcuid}</td>
                  <td>{record.current}</td>
                  <td>{record.voltage}</td>
                  <td>{record.timestamp ? <TextFormat type="date" value={record.timestamp} format={APP_DATE_FORMAT} /> : null}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/record/${record.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/record/${record.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/record/${record.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="akathonApp.record.home.notFound">No Records found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Record;
