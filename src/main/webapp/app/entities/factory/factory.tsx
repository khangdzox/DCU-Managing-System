import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFactory } from 'app/shared/model/factory.model';
import { getEntities } from './factory.reducer';

export const Factory = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const factoryList = useAppSelector(state => state.factory.entities);
  const loading = useAppSelector(state => state.factory.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="factory-heading" data-cy="FactoryHeading">
        <Translate contentKey="akathonApp.factory.home.title">Factories</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="akathonApp.factory.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/factory/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="akathonApp.factory.home.createLabel">Create new Factory</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {factoryList && factoryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="akathonApp.factory.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="akathonApp.factory.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="akathonApp.factory.dateCreated">Date Created</Translate>
                </th>
                <th>
                  <Translate contentKey="akathonApp.factory.companyName">Company Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {factoryList.map((factory, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/factory/${factory.id}`} color="link" size="sm">
                      {factory.id}
                    </Button>
                  </td>
                  <td>{factory.name}</td>
                  <td>{factory.dateCreated ? <TextFormat type="date" value={factory.dateCreated} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{factory.companyName ? <Link to={`/company/${factory.companyName.id}`}>{factory.companyName.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/factory/${factory.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/factory/${factory.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/factory/${factory.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="akathonApp.factory.home.notFound">No Factories found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Factory;
