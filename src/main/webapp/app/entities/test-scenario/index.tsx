import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestScenario from './test-scenario';
import TestScenarioDetail from './test-scenario-detail';
import TestScenarioUpdate from './test-scenario-update';
import TestScenarioDeleteDialog from './test-scenario-delete-dialog';
import { TestScenarioRecord } from './test-scenario-record';
import TestScenarioNew from 'app/entities/test-scenario/test-scenario-new';
import TestScenarioAll from 'app/entities/test-scenario/test-scenario-all';
import { AUTHORITIES } from 'app/config/constants';
import PrivateRoute from 'app/shared/auth/private-route';

const TestScenarioRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestScenario />} />
    <Route
      path="all"
      element={
        <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN]}>
          <TestScenarioAll />
        </PrivateRoute>
      }
    />
    <Route path="new" element={<TestScenarioUpdate />} />
    <Route path="record/:id" element={<TestScenarioRecord />} />
    <Route path="create" element={<TestScenarioNew />} />
    <Route path=":id">
      <Route index element={<TestScenarioDetail />} />
      <Route path="edit" element={<TestScenarioUpdate />} />
      <Route path="delete" element={<TestScenarioDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestScenarioRoutes;
