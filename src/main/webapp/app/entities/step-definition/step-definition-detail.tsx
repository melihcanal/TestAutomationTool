import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './step-definition.reducer';

export const StepDefinitionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const stepDefinitionEntity = useAppSelector(state => state.stepDefinition.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="stepDefinitionDetailsHeading">Step Definition</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{stepDefinitionEntity.id}</dd>
          <dt>
            <span id="actionType">Action Type</span>
          </dt>
          <dd>{stepDefinitionEntity.actionType}</dd>
          <dt>
            <span id="message">Message</span>
          </dt>
          <dd>{stepDefinitionEntity.message}</dd>
          <dt>
            <span id="xpathOrCssSelector">Xpath Or Css Selector</span>
          </dt>
          <dd>{stepDefinitionEntity.xpathOrCssSelector}</dd>
          <dt>
            <span id="keyword">Keyword</span>
          </dt>
          <dd>{stepDefinitionEntity.keyword}</dd>
          <dt>
            <span id="scrollLeft">Scroll Left</span>
          </dt>
          <dd>{stepDefinitionEntity.scrollLeft}</dd>
          <dt>
            <span id="scrollTop">Scroll Top</span>
          </dt>
          <dd>{stepDefinitionEntity.scrollTop}</dd>
          <dt>
            <span id="url">Url</span>
          </dt>
          <dd>{stepDefinitionEntity.url}</dd>
          <dt>
            <span id="expected">Expected</span>
          </dt>
          <dd>{stepDefinitionEntity.expected}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{stepDefinitionEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">Created Date</span>
          </dt>
          <dd>
            {stepDefinitionEntity.createdDate ? (
              <TextFormat value={stepDefinitionEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">Last Modified By</span>
          </dt>
          <dd>{stepDefinitionEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">Last Modified Date</span>
          </dt>
          <dd>
            {stepDefinitionEntity.lastModifiedDate ? (
              <TextFormat value={stepDefinitionEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Test Scenario</dt>
          <dd>{stepDefinitionEntity.testScenario ? stepDefinitionEntity.testScenario.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/step-definition" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/step-definition/${stepDefinitionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default StepDefinitionDetail;
