import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestExecution from './test-execution';
import TestExecutionDetail from './test-execution-detail';
import TestExecutionUpdate from './test-execution-update';
import TestExecutionDeleteDialog from './test-execution-delete-dialog';
import { AUTHORITIES } from 'app/config/constants';
import PrivateRoute from 'app/shared/auth/private-route';

const TestExecutionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route
      index
      element={
        <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN]}>
          <TestExecution />
        </PrivateRoute>
      }
    />
    <Route path="new" element={<TestExecutionUpdate />} />
    <Route path=":id">
      <Route index element={<TestExecutionDetail />} />
      <Route path="edit" element={<TestExecutionUpdate />} />
      <Route path="delete" element={<TestExecutionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestExecutionRoutes;
