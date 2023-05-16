import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Container, Row, Table } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { executeTestScenario, getEntities, getEntitiesByTestScenario as getTestExecutions } from '../test-execution/test-execution.reducer';
import { getEntitiesByTestScenario as getStepDefinitions } from '../step-definition/step-definition.reducer';
import { getEntity } from 'app/entities/test-scenario/test-scenario.reducer';

export const TestScenarioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testScenarioEntity = useAppSelector(state => state.testScenario.entity);
  const testExecutionList = useAppSelector(state => state.testExecution.entities);
  const stepDefinitionList = useAppSelector(state => state.stepDefinition.entities);
  const loading = useAppSelector(state => state.testExecution.loading);

  useEffect(() => {
    if (testScenarioEntity.id !== undefined) {
      dispatch(getTestExecutions(testScenarioEntity));
      dispatch(getStepDefinitions(testScenarioEntity));
    }
  }, [testScenarioEntity]);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getTestExecutions(testScenarioEntity));
  };

  const handleStartTest = () => {
    dispatch(executeTestScenario(testScenarioEntity.id));
  };

  return (
    <div>
      <Row>
        <Col md="3">
          <h2 data-cy="testScenarioDetailsHeading">Test Scenario</h2>
          <dl className="jh-entity-details">
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
              <span id="numberOfExecution">Success Rate</span>
            </dt>
            <dd>% {Math.round((testScenarioEntity.numberOfPassed / testScenarioEntity.numberOfExecution) * 1000) / 10}</dd>
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
          <Button tag={Link} to="/test-scenario" replace color="success" onClick={handleStartTest}>
            <FontAwesomeIcon icon="play" /> <span className="d-none d-md-inline">Start</span>
          </Button>
          &nbsp;
          <Button tag={Link} to="/test-scenario" replace color="info" data-cy="entityDetailsBackButton">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/test-scenario/${testScenarioEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
        <Col>
          <Container fluid>
            <h2 id="step-definition-heading" data-cy="StepDefinitionHeading">
              Test Steps
            </h2>
            <div className="table-responsive">
              {stepDefinitionList && stepDefinitionList.length > 0 ? (
                <Table responsive>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Action Type</th>
                      <th>Message</th>
                      <th>Xpath Or Css Selector</th>
                      <th>Keyword</th>
                      <th>Scroll Left</th>
                      <th>Scroll Top</th>
                      <th>Url</th>
                      <th>Expected</th>
                      <th />
                    </tr>
                  </thead>
                  <tbody>
                    {stepDefinitionList.map((stepDefinition, i) => (
                      <tr key={`entity-${i}`} data-cy="entityTable">
                        <td>{i + 1}</td>
                        <td>{stepDefinition.actionType}</td>
                        <td>{stepDefinition.message}</td>
                        <td>{stepDefinition.xpathOrCssSelector}</td>
                        <td>{stepDefinition.keyword}</td>
                        <td>{stepDefinition.scrollLeft}</td>
                        <td>{stepDefinition.scrollTop}</td>
                        <td>{stepDefinition.url}</td>
                        <td>{stepDefinition.expected}</td>
                        <td className="text-end">
                          <div className="btn-group flex-btn-group-container">
                            <Button
                              tag={Link}
                              to={`/step-definition/${stepDefinition.id}`}
                              color="info"
                              size="sm"
                              data-cy="entityDetailsButton"
                            >
                              <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                            </Button>
                            <Button
                              tag={Link}
                              to={`/step-definition/${stepDefinition.id}/edit`}
                              color="primary"
                              size="sm"
                              data-cy="entityEditButton"
                            >
                              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                            </Button>
                            <Button
                              tag={Link}
                              to={`/step-definition/${stepDefinition.id}/delete`}
                              color="danger"
                              size="sm"
                              data-cy="entityDeleteButton"
                            >
                              <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              ) : (
                !loading && <div className="alert alert-warning">No Step Definitions found</div>
              )}
            </div>
          </Container>
        </Col>
      </Row>
      <Row className="pt-4">
        <Col>
          <Container fluid>
            <div>
              <h2 id="test-execution-heading" data-cy="TestExecutionHeading">
                Test Executions
                <div className="d-flex justify-content-end">
                  <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
                    <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
                  </Button>
                </div>
              </h2>
              <div className="table-responsive">
                {testExecutionList && testExecutionList.length > 0 ? (
                  <Table responsive>
                    <thead>
                      <tr>
                        <th>#</th>
                        <th>Status</th>
                        <th>Message</th>
                        <th>Report Url</th>
                        <th>Created By</th>
                        <th>Created Date</th>
                        <th>Last Modified By</th>
                        <th>Last Modified Date</th>
                        <th />
                      </tr>
                    </thead>
                    <tbody>
                      {testExecutionList.map((testExecution, i) => (
                        <tr key={`entity-${i}`} data-cy="entityTable">
                          <td>{i + 1}</td>
                          <td>{testExecution.status ? 'true' : 'false'}</td>
                          <td>{testExecution.message}</td>
                          <td>{testExecution.reportUrl}</td>
                          <td>{testExecution.createdBy}</td>
                          <td>
                            {testExecution.createdDate ? (
                              <TextFormat type="date" value={testExecution.createdDate} format={APP_DATE_FORMAT} />
                            ) : null}
                          </td>
                          <td className="text-end">
                            <div className="btn-group flex-btn-group-container">
                              <Button
                                tag={Link}
                                to={`/test-execution/${testExecution.id}`}
                                color="info"
                                size="sm"
                                data-cy="entityDetailsButton"
                              >
                                <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                              </Button>
                              <Button
                                tag={Link}
                                to={`/test-execution/${testExecution.id}/delete`}
                                color="danger"
                                size="sm"
                                data-cy="entityDeleteButton"
                              >
                                <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                              </Button>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </Table>
                ) : (
                  <div className="alert alert-warning">No Test Executions found</div>
                )}
              </div>
            </div>
          </Container>
        </Col>
      </Row>
    </div>
  );
};

export default TestScenarioDetail;
