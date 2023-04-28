import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dcu.reducer';

export const DcuDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dcuEntity = useAppSelector(state => state.dcu.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dcuDetailsHeading">
          <Translate contentKey="akathonApp.dcu.detail.title">Dcu</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="akathonApp.dcu.id">Id</Translate>
            </span>
          </dt>
          <dd>{dcuEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="akathonApp.dcu.name">Name</Translate>
            </span>
          </dt>
          <dd>{dcuEntity.name}</dd>
          <dt>
            <span id="dateCreated">
              <Translate contentKey="akathonApp.dcu.dateCreated">Date Created</Translate>
            </span>
          </dt>
          <dd>{dcuEntity.dateCreated ? <TextFormat value={dcuEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="akathonApp.dcu.factoryName">Factory Name</Translate>
          </dt>
          <dd>{dcuEntity.factoryName ? dcuEntity.factoryName.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/dcu" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dcu/${dcuEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DcuDetail;
