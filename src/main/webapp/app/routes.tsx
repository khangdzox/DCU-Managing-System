import React from 'react';
import { Route, useLocation } from 'react-router-dom';
import Loadable from 'react-loadable';

import Login from 'app/modules/login/login';
import Register from 'app/modules/account/register/register';
import Activate from 'app/modules/account/activate/activate';
import PasswordResetInit from 'app/modules/account/password-reset/init/password-reset-init';
import PasswordResetFinish from 'app/modules/account/password-reset/finish/password-reset-finish';
import Logout from 'app/modules/login/logout';
import Home from 'app/modules/home/home';
import EntitiesRoutes from 'app/entities/routes';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PageNotFound from 'app/shared/error/page-not-found';
import { AUTHORITIES } from 'app/config/constants';
import { sendActivity } from 'app/config/websocket-middleware';
import Manager from 'app/modules/manager/manager';
import ManagerCompanyUpdate from 'app/modules/manager/manager-company-update';
import ManagerFactoryUpdate from './modules/manager/manager-factory-update';
import ManagerFactoryDelete from './modules/manager/manager-factory-delete';
import ManagerFactoryDetail from './modules/manager/manager-factory';
import ManagerDcuDetail from './modules/manager/manager-dcu';
import { ManagerDcuUpdate } from './modules/manager/manager-dcu-update';
import ManagerDcuDelete from './modules/manager/manager-dcu-delete';

const loading = <div>loading ...</div>;

const Account = Loadable({
  loader: () => import(/* webpackChunkName: "account" */ 'app/modules/account'),
  loading: () => loading,
});

const Admin = Loadable({
  loader: () => import(/* webpackChunkName: "administration" */ 'app/modules/administration'),
  loading: () => loading,
});

const AppRoutes = () => {
  const location = useLocation();
  React.useEffect(() => {
    sendActivity(location.pathname);
  }, [location]);
  return (
    <div className="view-routes">
      <ErrorBoundaryRoutes>
        <Route index element={<Home />} />
        <Route path="manager">
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
        </Route>
        <Route
          path="login"
          element={
            <>
              <Login />
              <Home />
            </>
          }
        />
        <Route path="logout" element={<Logout />} />
        <Route path="account">
          <Route
            path="*"
            element={
              <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]}>
                <Account />
              </PrivateRoute>
            }
          />
          <Route path="register" element={<Register />} />
          <Route path="activate" element={<Activate />} />
          <Route path="reset">
            <Route path="request" element={<PasswordResetInit />} />
            <Route path="finish" element={<PasswordResetFinish />} />
          </Route>
        </Route>
        <Route
          path="admin/*"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN]}>
              <Admin />
            </PrivateRoute>
          }
        />
        <Route
          path="*"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.USER]}>
              <EntitiesRoutes />
            </PrivateRoute>
          }
        />
        <Route path="*" element={<PageNotFound />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};

export default AppRoutes;
