import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo-jhipster.png" alt="Logo" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    {/* <BrandIcon /> */}
    <span className="brand-title">
      {/* <Translate contentKey="global.title">DCU Managing System</Translate> */}
      DMS
    </span>
    <span className="navbar-version">{VERSION}</span>
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/manager" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>
        {/* <Translate contentKey="global.menu.home">Home</Translate> */}
        Dashboard
      </span>
    </NavLink>
  </NavItem>
);

export const LoginButton = () => (
  <NavItem>
    <NavLink tag={Link} to="/login" className="d-flex align-items-center">
      <FontAwesomeIcon icon="sign-in-alt" />
      <span>Log in</span>
    </NavLink>
  </NavItem>
);

export const RegisterButton = () => (
  <NavItem>
    <NavLink tag={Link} to="/account/register" className="d-flex align-items-center">
      <FontAwesomeIcon icon="user-plus" />
      <span>Register</span>
    </NavLink>
  </NavItem>
);
