import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from 'app/entities/factory/factory.reducer';

export const ManagerFactoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const factoryEntity = useAppSelector(state => state.factory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="factoryDetailsHeading">
          <Translate contentKey="akathonApp.factory.detail.title">Factory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="akathonApp.factory.id">Id</Translate>
            </span>
          </dt>
          <dd>{factoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="akathonApp.factory.name">Name</Translate>
            </span>
          </dt>
          <dd>{factoryEntity.name}</dd>
          <dt>
            <span id="dateCreated">
              <Translate contentKey="akathonApp.factory.dateCreated">Date Created</Translate>
            </span>
          </dt>
          <dd>
            {factoryEntity.dateCreated ? <TextFormat value={factoryEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="akathonApp.factory.companyName">Company Name</Translate>
          </dt>
          <dd>{factoryEntity.companyName ? factoryEntity.companyName.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/factory" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/factory/${factoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ManagerFactoryDetail;
