import React, { useEffect, useState } from 'react';
import { Button, Table } from 'reactstrap';
import { reset, saveStepDefinitions, startWebDriver, stopWebDriver } from 'app/entities/step-definition/step-definition.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link, useParams } from 'react-router-dom';
import { getEntity } from 'app/entities/test-scenario/test-scenario.reducer';
import { IStepDefinition } from 'app/shared/model/step-definition.model';
import { IStepDefinitionRequest } from 'app/shared/request/step-definition-request';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { isValidURL } from 'app/shared/util/url-utils';
import 'app/shared/css/common-style.scss';
import StepDefinitionDialog from 'app/entities/step-definition/step-definition-dialog';

export const TestScenarioRecord = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  const stepDefinitionList = useAppSelector(state => state.stepDefinition.entities);
  const loading = useAppSelector(state => state.stepDefinition.loading);
  const currentTestScenario = useAppSelector(state => state.testScenario.entity);

  const [rows, setRows] = useState(null);
  const [firstStep, setFirstStep] = useState(null);
  const [valueFromDialogBox, setValueFromDialogBox] = useState(null);
  const [showComponent, setShowComponent] = useState(false);
  const [index, setIndex] = useState(0);

  useEffect(() => {
    dispatch(reset());
  }, []);

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  useEffect(() => {
    if (valueFromDialogBox) {
      const newRow = { ...valueFromDialogBox };
      setRows(prevRows => {
        const updatedRows = [...prevRows];
        updatedRows.splice(index + 1, 0, newRow);
        return updatedRows;
      });
    }
  }, [valueFromDialogBox]);

  useEffect(() => {
    setRows(stepDefinitionList);
  }, [stepDefinitionList]);

  const setUrl = values => {
    if (values.url !== undefined && isValidURL(values.url)) {
      const step: IStepDefinition = { ...values, testScenario: currentTestScenario };
      setFirstStep(step);
    } else {
      window.alert('Please enter a valid URL');
    }
  };

  const handleShowDialogBox = (i: number) => {
    setIndex(i);
    setShowComponent(true);
  };

  const handleHideDialogBox = () => {
    setShowComponent(false);
  };

  const handleDialogBoxValue = value => {
    setValueFromDialogBox(value);
  };

  const startRecording = () => {
    dispatch(startWebDriver(firstStep.url));
  };

  const stopRecording = () => {
    dispatch(stopWebDriver());
  };

  const saveTestScenario = () => {
    const list: IStepDefinition[] = [];
    list.push(firstStep);
    rows.forEach(stepDefinition => {
      const obj: IStepDefinition = { ...stepDefinition };
      obj.testScenario = currentTestScenario;
      list.push(obj);
    });
    const request: IStepDefinitionRequest = { stepDefinitionList: list };
    dispatch(saveStepDefinitions(request));
  };

  const cancelRecording = () => {};

  return (
    <div>
      <StepDefinitionDialog isOpen={showComponent} addValidationStep={handleDialogBoxValue} showDialogBox={handleHideDialogBox} />
      {firstStep && firstStep.url !== undefined ? (
        <div>
          <div className="d-flex justify-content-start record-buttons">
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
                  {rows.map((stepDefinition, i) => (
                    <React.Fragment key={stepDefinition.id}>
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
                          <Button color="primary" onClick={() => handleShowDialogBox(i)}>
                            Add Row
                          </Button>
                        </td>
                      </tr>
                    </React.Fragment>
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
      ) : (
        <ValidatedForm onSubmit={setUrl}>
          <ValidatedField
            hidden
            value="NAVIGATE_TO"
            label="Action Type"
            id="step-definition-actionType"
            name="actionType"
            data-cy="actionType"
            type="text"
          />
          <ValidatedField label="Starting Page Url" id="step-definition-url" name="url" data-cy="url" type="text" />
          <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-scenario" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />
            &nbsp;
            <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit">
            <FontAwesomeIcon icon="save" />
            &nbsp; Save
          </Button>
        </ValidatedForm>
      )}
    </div>
  );
};
