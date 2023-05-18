import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Manager from './manager';
import ManagerCompanyUpdate from './manager-company-update';
import ManagerDcuDetail from './manager-dcu';
import ManagerDcuDelete from './manager-dcu-delete';
import ManagerDcuUpdate from './manager-dcu-update';
import ManagerFactoryDetail from './manager-factory';
import ManagerFactoryDelete from './manager-factory-delete';
import ManagerFactoryUpdate from './manager-factory-update';

const ManagerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Manager />} />
    <Route path="update" element={<ManagerCompanyUpdate />} />
    <Route path="new" element={<ManagerFactoryUpdate />} />
    <Route path=":fid">
      <Route index element={<ManagerFactoryDetail />} />
      <Route path="update" element={<ManagerFactoryUpdate />} />
      <Route
        path="delete"
        element={
          <>
            <ManagerFactoryDetail />
            <ManagerFactoryDelete />
          </>
        }
      />
      <Route path="new" element={<ManagerDcuUpdate />} />
      <Route path=":did">
        <Route index element={<ManagerDcuDetail />} />
        <Route path="update" element={<ManagerDcuUpdate />} />
        <Route
          path="delete"
          element={
            <>
              {' '}
              <ManagerDcuDetail /> <ManagerDcuDelete />{' '}
            </>
          }
        />
      </Route>
    </Route>
  </ErrorBoundaryRoutes>
);

export default ManagerRoutes;
