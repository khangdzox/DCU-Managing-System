import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFactory } from 'app/shared/model/factory.model';
import { getEntities as getFactories } from 'app/entities/factory/factory.reducer';
import { IDcu } from 'app/shared/model/dcu.model';
import { getEntity, updateEntity, createEntity, reset } from 'app/entities/dcu/dcu.reducer';

export const ManagerDcuUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { did: id } = useParams<'did'>();
  const { fid: factoryId } = useParams<'fid'>();
  const isNew = id === undefined;

  const factoryEntity = useAppSelector(state => state.factory.entity);
  const dcuEntity = useAppSelector(state => state.dcu.entity);
  const loading = useAppSelector(state => state.dcu.loading);
  const updating = useAppSelector(state => state.dcu.updating);
  const updateSuccess = useAppSelector(state => state.dcu.updateSuccess);

  const handleClose = () => {
    navigate(-1);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateCreated = convertDateTimeToServer(values.dateCreated);

    const entity = {
      ...dcuEntity,
      ...values,
      factoryName: factoryEntity,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateCreated: displayDefaultDateTime(),
        }
      : {
          ...dcuEntity,
          dateCreated: convertDateTimeFromServer(dcuEntity.dateCreated),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="akathonApp.dcu.home.createOrEditLabel" data-cy="DcuCreateUpdateHeading">
            {isNew ? 'Create a new DCU' : 'Edit a DCU'}
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              <ValidatedField
                label={translate('akathonApp.dcu.name')}
                id="dcu-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('akathonApp.dcu.dateCreated')}
                id="dcu-dateCreated"
                name="dateCreated"
                data-cy="dateCreated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <input type="hidden" name="factoryName" value={factoryEntity.id} />
              {/* <ValidatedField
                id="dcu-factoryName"
                name="factoryName"
                data-cy="factoryName"
                label={translate('akathonApp.dcu.factoryName')}
                type="select"
              >
                <option value="" key="0" />
                {factories
                  ? factories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField> */}
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to={`/manager/${factoryId}`} replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ManagerDcuUpdate;
