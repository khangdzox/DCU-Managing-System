import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './record.reducer';

export const RecordDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const recordEntity = useAppSelector(state => state.record.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="recordDetailsHeading">
          <Translate contentKey="akathonApp.record.detail.title">Record</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="akathonApp.record.id">Id</Translate>
            </span>
          </dt>
          <dd>{recordEntity.id}</dd>
          <dt>
            <span id="dcuid">
              <Translate contentKey="akathonApp.record.dcuid">Dcuid</Translate>
            </span>
          </dt>
          <dd>{recordEntity.dcuid}</dd>
          <dt>
            <span id="current">
              <Translate contentKey="akathonApp.record.current">Current</Translate>
            </span>
          </dt>
          <dd>{recordEntity.current}</dd>
          <dt>
            <span id="voltage">
              <Translate contentKey="akathonApp.record.voltage">Voltage</Translate>
            </span>
          </dt>
          <dd>{recordEntity.voltage}</dd>
          <dt>
            <span id="timestamp">
              <Translate contentKey="akathonApp.record.timestamp">Timestamp</Translate>
            </span>
          </dt>
          <dd>{recordEntity.timestamp ? <TextFormat value={recordEntity.timestamp} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/record" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/record/${recordEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RecordDetail;
