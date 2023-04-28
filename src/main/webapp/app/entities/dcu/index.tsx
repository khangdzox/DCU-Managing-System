import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Dcu from './dcu';
import DcuDetail from './dcu-detail';
import DcuUpdate from './dcu-update';
import DcuDeleteDialog from './dcu-delete-dialog';

const DcuRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Dcu />} />
    <Route path="new" element={<DcuUpdate />} />
    <Route path=":id">
      <Route index element={<DcuDetail />} />
      <Route path="edit" element={<DcuUpdate />} />
      <Route path="delete" element={<DcuDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DcuRoutes;
