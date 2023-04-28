import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getTestScenarios } from 'app/entities/test-scenario/test-scenario.reducer';
import { createEntity, getEntity, reset, updateEntity } from './test-execution.reducer';

export const TestExecutionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testScenarios = useAppSelector(state => state.testScenario.entities);
  const testExecutionEntity = useAppSelector(state => state.testExecution.entity);
  const loading = useAppSelector(state => state.testExecution.loading);
  const updating = useAppSelector(state => state.testExecution.updating);
  const updateSuccess = useAppSelector(state => state.testExecution.updateSuccess);

  const handleClose = () => {
    navigate('/test-execution');
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
      ...testExecutionEntity,
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
          ...testExecutionEntity,
          createdDate: convertDateTimeFromServer(testExecutionEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(testExecutionEntity.lastModifiedDate),
          testScenario: testExecutionEntity?.testScenario?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="testAutomationToolApp.testExecution.home.createOrEditLabel" data-cy="TestExecutionCreateUpdateHeading">
            Create or edit a Test Execution
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
                <ValidatedField name="id" required readOnly id="test-execution-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Status" id="test-execution-status" name="status" data-cy="status" check type="checkbox" />
              <ValidatedField label="Message" id="test-execution-message" name="message" data-cy="message" type="text" />
              <ValidatedField label="Report Url" id="test-execution-reportUrl" name="reportUrl" data-cy="reportUrl" type="text" />
              <ValidatedField label="Created By" id="test-execution-createdBy" name="createdBy" data-cy="createdBy" type="text" />
              <ValidatedField
                label="Created Date"
                id="test-execution-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Last Modified By"
                id="test-execution-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label="Last Modified Date"
                id="test-execution-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="test-execution-testScenario"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-execution" replace color="info">
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

export default TestExecutionUpdate;
