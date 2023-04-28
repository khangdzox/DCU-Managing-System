import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Factory from './factory';
import FactoryDetail from './factory-detail';
import FactoryUpdate from './factory-update';
import FactoryDeleteDialog from './factory-delete-dialog';

const FactoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Factory />} />
    <Route path="new" element={<FactoryUpdate />} />
    <Route path=":id">
      <Route index element={<FactoryDetail />} />
      <Route path="edit" element={<FactoryUpdate />} />
      <Route path="delete" element={<FactoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FactoryRoutes;
