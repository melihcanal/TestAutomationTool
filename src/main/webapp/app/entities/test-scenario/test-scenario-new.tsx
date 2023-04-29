import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUsers } from 'app/shared/reducers/user-management';
import { createEntity, getEntity, reset, updateEntity } from './test-scenario.reducer';

export const TestScenarioNew = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const testScenarioEntity = useAppSelector(state => state.testScenario.entity);
  const loading = useAppSelector(state => state.testScenario.loading);
  const updating = useAppSelector(state => state.testScenario.updating);
  const updateSuccess = useAppSelector(state => state.testScenario.updateSuccess);

  const handleClose = () => {
    navigate('/test-scenario');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
      ...testScenarioEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
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
          ...testScenarioEntity,
          createdDate: convertDateTimeFromServer(testScenarioEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(testScenarioEntity.lastModifiedDate),
          user: testScenarioEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="testAutomationToolApp.testScenario.home.createOrEditLabel" data-cy="TestScenarioCreateUpdateHeading">
            Create a New Test Scenario
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
                <ValidatedField name="id" required readOnly id="test-scenario-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Title" id="test-scenario-title" name="title" data-cy="title" type="text" />
              <ValidatedField label="Description" id="test-scenario-description" name="description" data-cy="description" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-scenario" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
              <Button tag={Link} to={`/test-scenario/:id/edit`} color="primary" size="sm" data-cy="entityEditButton">
                <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TestScenarioNew;
