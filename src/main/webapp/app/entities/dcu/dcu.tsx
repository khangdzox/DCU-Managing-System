import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDcu } from 'app/shared/model/dcu.model';
import { getEntities } from './dcu.reducer';

export const Dcu = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const dcuList = useAppSelector(state => state.dcu.entities);
  const loading = useAppSelector(state => state.dcu.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="dcu-heading" data-cy="DcuHeading">
        <Translate contentKey="akathonApp.dcu.home.title">Dcus</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="akathonApp.dcu.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/dcu/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="akathonApp.dcu.home.createLabel">Create new Dcu</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {dcuList && dcuList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="akathonApp.dcu.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="akathonApp.dcu.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="akathonApp.dcu.dateCreated">Date Created</Translate>
                </th>
                <th>
                  <Translate contentKey="akathonApp.dcu.factoryName">Factory Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {dcuList.map((dcu, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/dcu/${dcu.id}`} color="link" size="sm">
                      {dcu.id}
                    </Button>
                  </td>
                  <td>{dcu.name}</td>
                  <td>{dcu.dateCreated ? <TextFormat type="date" value={dcu.dateCreated} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{dcu.factoryName ? <Link to={`/factory/${dcu.factoryName.id}`}>{dcu.factoryName.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/dcu/${dcu.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/dcu/${dcu.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/dcu/${dcu.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="akathonApp.dcu.home.notFound">No Dcus found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Dcu;
