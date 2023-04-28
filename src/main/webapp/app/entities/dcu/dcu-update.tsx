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
import { getEntity, updateEntity, createEntity, reset } from './dcu.reducer';

export const DcuUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const factories = useAppSelector(state => state.factory.entities);
  const dcuEntity = useAppSelector(state => state.dcu.entity);
  const loading = useAppSelector(state => state.dcu.loading);
  const updating = useAppSelector(state => state.dcu.updating);
  const updateSuccess = useAppSelector(state => state.dcu.updateSuccess);

  const handleClose = () => {
    navigate('/dcu');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFactories({}));
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
      factoryName: factories.find(it => it.id.toString() === values.factoryName.toString()),
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
          factoryName: dcuEntity?.factoryName?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="akathonApp.dcu.home.createOrEditLabel" data-cy="DcuCreateUpdateHeading">
            <Translate contentKey="akathonApp.dcu.home.createOrEditLabel">Create or edit a Dcu</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="dcu-id"
                  label={translate('akathonApp.dcu.id')}
                  validate={{ required: true }}
                />
              ) : null}
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
              <ValidatedField
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
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/dcu" replace color="info">
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

export default DcuUpdate;
