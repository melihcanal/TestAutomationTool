import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestScenario from './test-scenario';
import TestExecution from './test-execution';
import StepDefinition from './step-definition';

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        <Route path="test-scenario/*" element={<TestScenario />} />
        <Route path="test-execution/*" element={<TestExecution />} />
        <Route path="step-definition/*" element={<StepDefinition />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};
