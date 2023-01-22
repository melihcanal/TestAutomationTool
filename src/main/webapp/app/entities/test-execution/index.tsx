import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestExecution from './test-execution';
import TestExecutionDetail from './test-execution-detail';
import TestExecutionUpdate from './test-execution-update';
import TestExecutionDeleteDialog from './test-execution-delete-dialog';

const TestExecutionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestExecution />} />
    <Route path="new" element={<TestExecutionUpdate />} />
    <Route path=":id">
      <Route index element={<TestExecutionDetail />} />
      <Route path="edit" element={<TestExecutionUpdate />} />
      <Route path="delete" element={<TestExecutionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestExecutionRoutes;
