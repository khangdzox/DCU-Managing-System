import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Record from './record';
import RecordDetail from './record-detail';
import RecordUpdate from './record-update';
import RecordDeleteDialog from './record-delete-dialog';

const RecordRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Record />} />
    <Route path="new" element={<RecordUpdate />} />
    <Route path=":id">
      <Route index element={<RecordDetail />} />
      <Route path="edit" element={<RecordUpdate />} />
      <Route path="delete" element={<RecordDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RecordRoutes;
