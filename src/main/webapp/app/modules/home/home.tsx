import './home.scss';

import React, { useEffect } from 'react';

import { Row, Col, Button, Table } from 'reactstrap';

import { REDIRECT_URL } from 'app/shared/util/url-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from 'app/entities/test-scenario/test-scenario.reducer';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { TextFormat } from 'react-jhipster';

export const Home = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const testScenarioList = useAppSelector(state => state.testScenario.entities);
  const loading = useAppSelector(state => state.testScenario.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <div>
          <h2 id="test-scenario-heading" data-cy="TestScenarioHeading">
            Test Scenarios
            <div className="d-flex justify-content-end">
              <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
                <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
              </Button>
              <Link to="/test-scenario/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
                <FontAwesomeIcon icon="plus" />
                &nbsp; Create a new Test Scenario
              </Link>
            </div>
          </h2>
          <div className="table-responsive">
            {testScenarioList && testScenarioList.length > 0 ? (
              <Table responsive>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Test Steps</th>
                    <th>Number Of Execution</th>
                    <th>Number Of Passed</th>
                    <th>Number Of Failed</th>
                    <th>Created By</th>
                    <th>Created Date</th>
                    <th>Last Modified By</th>
                    <th>Last Modified Date</th>
                    <th>User</th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {testScenarioList.map((testScenario, i) => (
                    <tr key={`entity-${i}`} data-cy="entityTable">
                      <td>
                        <Button tag={Link} to={`/test-scenario/${testScenario.id}`} color="link" size="sm">
                          {testScenario.id}
                        </Button>
                      </td>
                      <td>{testScenario.title}</td>
                      <td>{testScenario.description}</td>
                      <td>{testScenario.testSteps}</td>
                      <td>{testScenario.numberOfExecution}</td>
                      <td>{testScenario.numberOfPassed}</td>
                      <td>{testScenario.numberOfFailed}</td>
                      <td>{testScenario.createdBy}</td>
                      <td>
                        {testScenario.createdDate ? (
                          <TextFormat type="date" value={testScenario.createdDate} format={APP_DATE_FORMAT} />
                        ) : null}
                      </td>
                      <td>{testScenario.lastModifiedBy}</td>
                      <td>
                        {testScenario.lastModifiedDate ? (
                          <TextFormat type="date" value={testScenario.lastModifiedDate} format={APP_DATE_FORMAT} />
                        ) : null}
                      </td>
                      <td>{testScenario.user ? testScenario.user.id : ''}</td>
                      <td className="text-end">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`/test-scenario/${testScenario.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                            <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                          </Button>
                          <Button
                            tag={Link}
                            to={`/test-scenario/${testScenario.id}/edit`}
                            color="primary"
                            size="sm"
                            data-cy="entityEditButton"
                          >
                            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                          </Button>
                          <Button
                            tag={Link}
                            to={`/test-scenario/${testScenario.id}/delete`}
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
              !loading && <div className="alert alert-warning">No Test Scenarios found</div>
            )}
          </div>
        </div>
      </Col>
    </Row>
  );
};

export default Home;
