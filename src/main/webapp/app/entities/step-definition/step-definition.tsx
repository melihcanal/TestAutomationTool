import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStepDefinition } from 'app/shared/model/step-definition.model';
import { getEntities } from './step-definition.reducer';

export const StepDefinition = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const stepDefinitionList = useAppSelector(state => state.stepDefinition.entities);
  const loading = useAppSelector(state => state.stepDefinition.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="step-definition-heading" data-cy="StepDefinitionHeading">
        Step Definitions
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/step-definition/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Step Definition
          </Link>
        </div>
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
                <th>Created By</th>
                <th>Created Date</th>
                <th>Last Modified By</th>
                <th>Last Modified Date</th>
                <th>Test Scenario</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {stepDefinitionList.map((stepDefinition, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/step-definition/${stepDefinition.id}`} color="link" size="sm">
                      {stepDefinition.id}
                    </Button>
                  </td>
                  <td>{stepDefinition.actionType}</td>
                  <td>{stepDefinition.message}</td>
                  <td>{stepDefinition.xpathOrCssSelector}</td>
                  <td>{stepDefinition.keyword}</td>
                  <td>{stepDefinition.scrollLeft}</td>
                  <td>{stepDefinition.scrollTop}</td>
                  <td>{stepDefinition.url}</td>
                  <td>{stepDefinition.expected}</td>
                  <td>{stepDefinition.createdBy}</td>
                  <td>
                    {stepDefinition.createdDate ? (
                      <TextFormat type="date" value={stepDefinition.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{stepDefinition.lastModifiedBy}</td>
                  <td>
                    {stepDefinition.lastModifiedDate ? (
                      <TextFormat type="date" value={stepDefinition.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {stepDefinition.testScenario ? (
                      <Link to={`/test-scenario/${stepDefinition.testScenario.id}`}>{stepDefinition.testScenario.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/step-definition/${stepDefinition.id}`} color="info" size="sm" data-cy="entityDetailsButton">
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
    </div>
  );
};

export default StepDefinition;
