import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col, Card, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity as getFactory } from 'app/entities/factory/factory.reducer';

import { IDcu } from 'app/shared/model/dcu.model';
import { getEntitiesInParent as getDcusInFactory } from 'app/entities/dcu/dcu.reducer';

export const ManagerFactoryDetail = () => {
  const dispatch = useAppDispatch();

  const { fid: factoryId } = useParams<'fid'>();

  const factoryEntity = useAppSelector(state => state.factory.entity);
  const dcuList = useAppSelector(state => state.dcu.entities);
  const loading = useAppSelector(state => state.dcu.loading);

  useEffect(() => {
    dispatch(getDcusInFactory(factoryId));
    dispatch(getFactory(factoryId));
  }, []);

  const handleSyncList = () => {
    dispatch(getDcusInFactory(factoryId));
  };

  return (
    <>
      <Card>
        <Row>
          <Col md="8">
            <h2 data-cy="factoryDetailsHeading">
              Home <span className="separator">&gt;</span> Factory
            </h2>
            <dl className="jh-entity-details">
              <Row>
                <Col>
                  <dt>
                    <span id="id">
                      <Translate contentKey="akathonApp.factory.id">Id</Translate>
                    </span>
                  </dt>
                </Col>
                <Col>
                  <dd>{factoryEntity.id}</dd>
                </Col>
              </Row>
              <Row>
                <Col>
                  <dt>
                    <span id="name">
                      <Translate contentKey="akathonApp.factory.name">Name</Translate>
                    </span>
                  </dt>
                </Col>
                <Col>
                  <dd>{factoryEntity.name}</dd>
                </Col>
              </Row>
              <Row>
                <Col>
                  <dt>
                    <span id="numOfDcus">Number of DCUs</span>
                  </dt>
                </Col>
                <Col>
                  <dd>{dcuList.length}</dd>
                </Col>
              </Row>
              <Row>
                <Col>
                  <dt>
                    <span id="dateCreated">
                      <Translate contentKey="akathonApp.factory.dateCreated">Date Created</Translate>
                    </span>
                  </dt>
                </Col>
                <Col>
                  <dd>
                    {factoryEntity.dateCreated ? (
                      <TextFormat value={factoryEntity.dateCreated} type="date" format={APP_DATE_FORMAT} />
                    ) : null}
                  </dd>
                </Col>
              </Row>
            </dl>
            <Button tag={Link} to="/manager" replace color="info" data-cy="entityDetailsBackButton">
              <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
            </Button>
            &nbsp;
            <Button tag={Link} to={`/manager/${factoryId}/update`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
            </Button>
          </Col>
        </Row>
      </Card>
      <Card>
        <div>
          <h2 id="dcu-heading" data-cy="DcuHeading">
            DCUs
            <div className="d-flex justify-content-end">
              <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
                <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
              </Button>
              <Link
                to={`/manager/${factoryId}/new`}
                className="btn btn-primary jh-create-entity"
                id="jh-create-entity"
                data-cy="entityCreateButton"
              >
                <FontAwesomeIcon icon="plus" />
                &nbsp;Create new DCU
              </Link>
            </div>
          </h2>
          <div className="table-responsive">
            {dcuList && dcuList.length > 0 ? (
              <Table responsive>
                <thead>
                  <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {dcuList.map((dcu, i) => (
                    <tr key={`entity-${i}`} data-cy="entityTable">
                      <td>{dcu.id}</td>
                      <td>{dcu.name}</td>
                      <td className="text-end">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`/manager/${factoryId}/${dcu.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                            <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                          </Button>
                          <Button
                            tag={Link}
                            to={`/manager/${factoryId}/${dcu.id}/update`}
                            color="primary"
                            size="sm"
                            data-cy="entityEditButton"
                          >
                            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                          </Button>
                          <Button
                            tag={Link}
                            to={`/manager/${factoryId}/${dcu.id}/delete`}
                            color="danger"
                            size="sm"
                            data-cy="entityDeleteButton"
                          >
                            <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
              !loading && <div className="alert alert-warning">No Dcus found</div>
            )}
          </div>
        </div>
      </Card>
    </>
  );
};

export default ManagerFactoryDetail;
