import React, { useEffect } from 'react';
import { Button, Table } from 'reactstrap';
import { saveStepDefinitions, startWebDriver, stopWebDriver } from 'app/entities/step-definition/step-definition.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getCurrentUser } from 'app/shared/reducers/user-management';
import { useParams } from 'react-router-dom';
import { getEntity } from 'app/entities/test-scenario/test-scenario.reducer';

export const TestScenarioRecord = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getCurrentUser({}));
  }, []);

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const stepDefinitionList = useAppSelector(state => state.stepDefinition.entities);
  const loading = useAppSelector(state => state.stepDefinition.loading);
  const testScenario = useAppSelector(state => state.testScenario.entity);
  const currentUser = useAppSelector(state => state.userManagement.user);

  const startRecording = () => {
    dispatch(startWebDriver({}));
  };

  const stopRecording = () => {
    dispatch(stopWebDriver({}));
  };

  const saveTestScenario = () => {
    const list = stepDefinitionList.slice();
    list.forEach(stepDefinition => {
      stepDefinition.testScenario = testScenario;
      stepDefinition.user = currentUser;
    });
    dispatch(saveStepDefinitions(list));
  };

  const cancelRecording = () => {};

  return (
    <div>
      <div className="d-flex justify-content-start">
        <Button className="me-2" color="info" onClick={startRecording}>
          Start Recording
        </Button>
        <Button className="me-2" color="danger" onClick={stopRecording}>
          Stop Recording
        </Button>
      </div>
      <div className="table-responsive">
        {stepDefinitionList && stepDefinitionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
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
                      <Button color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">Click Start Recording button to begin recording test</div>
        )}
      </div>
      <div className="d-flex justify-content-start">
        <Button className="me-2" color="info" onClick={saveTestScenario}>
          Save
        </Button>
        <Button className="me-2" color="danger" onClick={cancelRecording}>
          Cancel
        </Button>
      </div>
    </div>
  );
};
