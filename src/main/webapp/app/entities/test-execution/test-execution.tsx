import React, { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './test-execution.reducer';

export const TestExecution = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const testExecutionList = useAppSelector(state => state.testExecution.entities);
  const loading = useAppSelector(state => state.testExecution.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="test-execution-heading" data-cy="TestExecutionHeading">
        Test Executions
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/test-execution/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Test Execution
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {testExecutionList && testExecutionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Status</th>
                <th>Message</th>
                <th>Report Url</th>
                <th>Created By</th>
                <th>Created Date</th>
                <th>Last Modified By</th>
                <th>Last Modified Date</th>
                <th>Test Scenario</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {testExecutionList.map((testExecution, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/test-execution/${testExecution.id}`} color="link" size="sm">
                      {testExecution.id}
                    </Button>
                  </td>
                  <td>{testExecution.status ? 'true' : 'false'}</td>
                  <td>{testExecution.message}</td>
                  <td>{testExecution.reportUrl}</td>
                  <td>{testExecution.createdBy}</td>
                  <td>
                    {testExecution.createdDate ? (
                      <TextFormat type="date" value={testExecution.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{testExecution.lastModifiedBy}</td>
                  <td>
                    {testExecution.lastModifiedDate ? (
                      <TextFormat type="date" value={testExecution.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {testExecution.testScenario ? (
                      <Link to={`/test-scenario/${testExecution.testScenario.id}`}>{testExecution.testScenario.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/test-execution/${testExecution.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/test-execution/${testExecution.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
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
          !loading && <div className="alert alert-warning">No Test Executions found</div>
        )}
      </div>
    </div>
  );
};

export default TestExecution;
