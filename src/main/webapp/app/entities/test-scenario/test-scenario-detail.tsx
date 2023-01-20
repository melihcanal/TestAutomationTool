import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-scenario.reducer';

export const TestScenarioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testScenarioEntity = useAppSelector(state => state.testScenario.entity);
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
            <span id="status">Status</span>
          </dt>
          <dd>{testScenarioEntity.status ? 'true' : 'false'}</dd>
          <dt>
            <span id="message">Message</span>
          </dt>
          <dd>{testScenarioEntity.message}</dd>
          <dt>
            <span id="reportUrl">Report Url</span>
          </dt>
          <dd>{testScenarioEntity.reportUrl}</dd>
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
      </Col>
    </Row>
  );
};

export default TestScenarioDetail;
