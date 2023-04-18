import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITestScenario } from 'app/shared/model/test-scenario.model';
import { getEntities as getTestScenarios } from 'app/entities/test-scenario/test-scenario.reducer';
import { IStepDefinition } from 'app/shared/model/step-definition.model';
import { ActionType } from 'app/shared/model/enumerations/action-type.model';
import { getEntity, updateEntity, createEntity, reset } from './step-definition.reducer';

export const StepDefinitionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testScenarios = useAppSelector(state => state.testScenario.entities);
  const stepDefinitionEntity = useAppSelector(state => state.stepDefinition.entity);
  const loading = useAppSelector(state => state.stepDefinition.loading);
  const updating = useAppSelector(state => state.stepDefinition.updating);
  const updateSuccess = useAppSelector(state => state.stepDefinition.updateSuccess);
  const actionTypeValues = Object.keys(ActionType);

  const handleClose = () => {
    navigate('/step-definition');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestScenarios({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...stepDefinitionEntity,
      ...values,
      testScenario: testScenarios.find(it => it.id.toString() === values.testScenario.toString()),
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
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          actionType: 'CLICK',
          ...stepDefinitionEntity,
          createdDate: convertDateTimeFromServer(stepDefinitionEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(stepDefinitionEntity.lastModifiedDate),
          testScenario: stepDefinitionEntity?.testScenario?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="testAutomationToolApp.stepDefinition.home.createOrEditLabel" data-cy="StepDefinitionCreateUpdateHeading">
            Create or edit a Step Definition
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
                <ValidatedField name="id" required readOnly id="step-definition-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Action Type" id="step-definition-actionType" name="actionType" data-cy="actionType" type="select">
                {actionTypeValues.map(actionType => (
                  <option value={actionType} key={actionType}>
                    {actionType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Message" id="step-definition-message" name="message" data-cy="message" type="text" />
              <ValidatedField
                label="Xpath Or Css Selector"
                id="step-definition-xpathOrCssSelector"
                name="xpathOrCssSelector"
                data-cy="xpathOrCssSelector"
                type="text"
              />
              <ValidatedField label="Keyword" id="step-definition-keyword" name="keyword" data-cy="keyword" type="text" />
              <ValidatedField label="Scroll Left" id="step-definition-scrollLeft" name="scrollLeft" data-cy="scrollLeft" type="text" />
              <ValidatedField label="Scroll Top" id="step-definition-scrollTop" name="scrollTop" data-cy="scrollTop" type="text" />
              <ValidatedField label="Url" id="step-definition-url" name="url" data-cy="url" type="text" />
              <ValidatedField label="Expected" id="step-definition-expected" name="expected" data-cy="expected" type="text" />
              <ValidatedField label="Created By" id="step-definition-createdBy" name="createdBy" data-cy="createdBy" type="text" />
              <ValidatedField
                label="Created Date"
                id="step-definition-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Last Modified By"
                id="step-definition-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label="Last Modified Date"
                id="step-definition-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="step-definition-testScenario"
                name="testScenario"
                data-cy="testScenario"
                label="Test Scenario"
                type="select"
              >
                <option value="" key="0" />
                {testScenarios
                  ? testScenarios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/step-definition" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default StepDefinitionUpdate;
