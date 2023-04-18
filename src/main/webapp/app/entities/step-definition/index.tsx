import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StepDefinition from './step-definition';
import StepDefinitionDetail from './step-definition-detail';
import StepDefinitionUpdate from './step-definition-update';
import StepDefinitionDeleteDialog from './step-definition-delete-dialog';

const StepDefinitionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StepDefinition />} />
    <Route path="new" element={<StepDefinitionUpdate />} />
    <Route path=":id">
      <Route index element={<StepDefinitionDetail />} />
      <Route path="edit" element={<StepDefinitionUpdate />} />
      <Route path="delete" element={<StepDefinitionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StepDefinitionRoutes;
