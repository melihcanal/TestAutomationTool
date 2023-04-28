import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-execution.reducer';

export const TestExecutionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testExecutionEntity = useAppSelector(state => state.testExecution.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testExecutionDetailsHeading">Test Execution</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testExecutionEntity.id}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{testExecutionEntity.status ? 'true' : 'false'}</dd>
          <dt>
            <span id="message">Message</span>
          </dt>
          <dd>{testExecutionEntity.message}</dd>
          <dt>
            <span id="reportUrl">Report Url</span>
          </dt>
          <dd>{testExecutionEntity.reportUrl}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{testExecutionEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">Created Date</span>
          </dt>
          <dd>
            {testExecutionEntity.createdDate ? (
              <TextFormat value={testExecutionEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">Last Modified By</span>
          </dt>
          <dd>{testExecutionEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">Last Modified Date</span>
          </dt>
          <dd>
            {testExecutionEntity.lastModifiedDate ? (
              <TextFormat value={testExecutionEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Test Scenario</dt>
          <dd>{testExecutionEntity.testScenario ? testExecutionEntity.testScenario.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-execution" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-execution/${testExecutionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestExecutionDetail;
