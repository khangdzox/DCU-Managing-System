import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Row } from 'reactstrap';

const Footer = () => (
  <div className="footer page-content">
    <Row className="text-center align-items-center m-0">
      <Col md="12">&copy; 2023 Swinburne Vietnam Alliance Program. All rights reserved.</Col>
    </Row>
  </div>
);

export default Footer;
