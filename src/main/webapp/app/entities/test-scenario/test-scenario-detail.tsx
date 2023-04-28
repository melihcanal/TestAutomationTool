import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-scenario.reducer';
import { getEntitiesByTestScenario as getTestExecutions } from '../test-execution/test-execution.reducer';
import { getEntitiesByTestScenario as getStepDefinitions } from '../step-definition/step-definition.reducer';

export const TestScenarioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  // entity gelince step definitions ve test executions da getir
  const testScenarioEntity = useAppSelector(state => state.testScenario.entity);
  const testExecutionList = useAppSelector(state => state.testExecution.entities);
  const stepDefinitionList = useAppSelector(state => state.stepDefinition.entities);

  useEffect(() => {
    // if statement makes problem at first
    if (testScenarioEntity && testScenarioEntity.id) {
      dispatch(getTestExecutions(testScenarioEntity));
      dispatch(getStepDefinitions(testScenarioEntity));
    }
  }, [testScenarioEntity]);

  const printLists = () => {};

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testScenarioDetailsHeading">Test Scenario</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testScenarioEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{testScenarioEntity.title}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{testScenarioEntity.description}</dd>
          <dt>
            <span id="numberOfExecution">Number Of Execution</span>
          </dt>
          <dd>{testScenarioEntity.numberOfExecution}</dd>
          <dt>
            <span id="numberOfPassed">Number Of Passed</span>
          </dt>
          <dd>{testScenarioEntity.numberOfPassed}</dd>
          <dt>
            <span id="numberOfFailed">Number Of Failed</span>
          </dt>
          <dd>{testScenarioEntity.numberOfFailed}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{testScenarioEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">Created Date</span>
          </dt>
          <dd>
            {testScenarioEntity.createdDate ? (
              <TextFormat value={testScenarioEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">Last Modified By</span>
          </dt>
          <dd>{testScenarioEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">Last Modified Date</span>
          </dt>
          <dd>
            {testScenarioEntity.lastModifiedDate ? (
              <TextFormat value={testScenarioEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>User</dt>
          <dd>{testScenarioEntity.user ? testScenarioEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-scenario" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-scenario/${testScenarioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
        <Button className="me-2" color="info" onClick={printLists}>
          Start Recording
        </Button>
      </Col>
    </Row>
  );
};

export default TestScenarioDetail;
