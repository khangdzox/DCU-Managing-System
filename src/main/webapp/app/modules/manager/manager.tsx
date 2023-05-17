import './manager.scss';

import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table, Card, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFactory } from 'app/shared/model/factory.model';
import { getEntitiesInParent as getFactoriesInCompany } from 'app/entities/factory/factory.reducer';

import { getEntity as getCompany } from 'app/entities/company/company.reducer';

export const Manager = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const companyId = useAppSelector(state => state.authentication.account.companyId);
  const companyEntity = useAppSelector(state => state.company.entity);
  const factoryList = useAppSelector(state => state.factory.entities);
  const loading = useAppSelector(state => state.factory.loading);

  useEffect(() => {
    dispatch(getFactoriesInCompany(companyId));
    dispatch(getCompany(companyId));
  }, []);

  const handleSyncList = () => {
    dispatch(getFactoriesInCompany(companyId));
  };

  return (
    !loading && (
      <>
        <Card>
          <Row>
            <Col md="8">
              <h2 data-cy="HomeHeading">Home</h2>
              <dl className="jh-entity-details">
                <Row>
                  <Col>
                    <dt>
                      <span id="id">Id</span>
                    </dt>
                  </Col>
                  <Col>
                    <dd>{companyEntity.id}</dd>
                  </Col>
                </Row>
                <Row>
                  <Col>
                    <dt>
                      <span id="name">Name</span>
                    </dt>
                  </Col>
                  <Col>
                    <dd>{companyEntity.name}</dd>
                  </Col>
                </Row>
                <Row>
                  <Col>
                    <dt>
                      <span id="numOfFactories">Number of factories</span>
                    </dt>
                  </Col>
                  <Col>
                    <dd>{factoryList.length}</dd>
                  </Col>
                </Row>
                <Row>
                  <Col>
                    <dt>
                      <span id="dateCreated">Date Created</span>
                    </dt>
                  </Col>
                  <Col>
                    <dd>
                      {companyEntity.dateCreated ? (
                        <TextFormat value={companyEntity.dateCreated} type="date" format={APP_DATE_FORMAT} />
                      ) : null}
                    </dd>
                  </Col>
                </Row>
              </dl>
              <Button tag={Link} to={`/manager/update`} replace color="primary">
                <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Update</span>
              </Button>
            </Col>
          </Row>
        </Card>
        <Card>
          <div>
            <h2 id="factory-heading" data-cy="FactoryHeading">
              Factories
              <div className="d-flex justify-content-end">
                <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
                  <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
                </Button>
                <Link to="/manager/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
                  <FontAwesomeIcon icon="plus" />
                  &nbsp;Create new Factory
                </Link>
              </div>
            </h2>
            <div className="table-responsive">
              {factoryList && factoryList.length > 0 ? (
                <Table responsive>
                  <thead>
                    <tr>
                      <th>Id</th>
                      <th>Name</th>
                      <th />
                    </tr>
                  </thead>
                  <tbody>
                    {factoryList.map((factory, i) => (
                      <tr key={`entity-${i}`} data-cy="entityTable">
                        <td>{factory.id}</td>
                        <td>{factory.name}</td>
                        <td className="text-end">
                          <div className="btn-group flex-btn-group-container">
                            <Button tag={Link} to={`/manager/${factory.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                              <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                            </Button>
                            <Button tag={Link} to={`/manager/${factory.id}/update`} color="primary" size="sm" data-cy="entityEditButton">
                              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                            </Button>
                            <Button tag={Link} to={`/manager/${factory.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                              <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              ) : (
                !loading && <div className="alert alert-warning">No Factories found</div>
              )}
            </div>
          </div>
        </Card>
      </>
    )
  );
};

export default Manager;
