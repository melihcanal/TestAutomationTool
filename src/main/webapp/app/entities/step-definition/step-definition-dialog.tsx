import React, { useState } from 'react';
import { Button, Input, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { IStepDefinition } from 'app/shared/model/step-definition.model';
import { ActionType } from 'app/shared/model/enumerations/action-type.model';
import 'app/shared/css/common-style.scss';

export const StepDefinitionDialog = ({ isOpen, addValidationStep, showDialogBox }) => {
  const [xPath, setXPath] = useState('');
  const [expectedValue, setExpectedValue] = useState('');

  const handleClose = () => {
    showDialogBox(false);
  };

  const confirmAdd = () => {
    const value: IStepDefinition = {
      actionType: ActionType.ASSERT_EQUALS,
      xpathOrCssSelector: xPath,
      expected: expectedValue,
    };
    addValidationStep(value);
    showDialogBox(false);
  };

  return (
    <Modal isOpen={isOpen} toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Modal Title</ModalHeader>
      <ModalBody>
        <Input type="text" placeholder="Enter XPath" value={xPath} className="input-spacing" onChange={e => setXPath(e.target.value)} />
        <Input type="text" placeholder="Enter Expected Value" value={expectedValue} onChange={e => setExpectedValue(e.target.value)} />
      </ModalBody>
      <ModalFooter>
        <Button color="primary" onClick={confirmAdd}>
          Save
        </Button>
        <Button color="secondary" onClick={handleClose}>
          Cancel
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default StepDefinitionDialog;
