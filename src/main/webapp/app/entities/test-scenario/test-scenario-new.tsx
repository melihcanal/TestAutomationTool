import React, { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getCurrentUser } from 'app/shared/reducers/user-management';
import { createEntity, reset } from './test-scenario.reducer';

export const TestScenarioNew = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const testScenarioEntity = useAppSelector(state => state.testScenario.entity);
  const loading = useAppSelector(state => state.testScenario.loading);
  const updating = useAppSelector(state => state.testScenario.updating);
  const updateSuccess = useAppSelector(state => state.testScenario.updateSuccess);
  const currentUser = useAppSelector(state => state.userManagement.user);

  const handleClose = () => {
    navigate('/test-scenario');
  };

  useEffect(() => {
    dispatch(getCurrentUser());
  }, []);

  useEffect(() => {
    dispatch(reset());
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (testScenarioEntity.id !== undefined) {
      navigate(`/test-scenario/record/${testScenarioEntity.id}`);
    }
  }, [testScenarioEntity]);

  const saveEntity = values => {
    const entity = {
      ...values,
      user: currentUser,
    };

    dispatch(createEntity(entity));
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
            <ValidatedForm onSubmit={saveEntity}>
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
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TestScenarioNew;
