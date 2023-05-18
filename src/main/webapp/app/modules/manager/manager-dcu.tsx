import React, { useEffect, useState } from 'react';
import { Link, useParams, NavLink as RouterNavLink } from 'react-router-dom';
import { Button, Row, Col, Card, Nav, NavItem, NavLink, TabContent, TabPane } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from 'app/entities/dcu/dcu.reducer';
import { ManagerDcuRealtime } from './manager-dcu-realtime';
import ManagerDcuHistory from './manager-dcu-history';

export const ManagerDcuDetail = () => {
  const dispatch = useAppDispatch();

  // State for current active Tab
  const [currentActiveTab, setCurrentActiveTab] = useState('1');

  // Toggle active state for Tab
  const toggle = tab => {
    if (currentActiveTab !== tab) setCurrentActiveTab(tab);
  };

  const { did: id } = useParams<'did'>();
  const { fid: factoryId } = useParams<'fid'>();

  const dcuEntity = useAppSelector(state => state.dcu.entity);

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  return (
    <>
      <Card>
        <Row>
          <Col md="8">
            <h2 data-cy="dcuDetailsHeading">
              Home <span className="separator">&gt;</span> Factory <span className="separator">&gt;</span> DCU
            </h2>
            <dl className="jh-entity-details">
              <Row>
                <Col>
                  <dt>
                    <span id="id">Id</span>
                  </dt>
                </Col>
                <Col>
                  <dd>{dcuEntity.id}</dd>
                </Col>
              </Row>
              <Row>
                <Col>
                  <dt>
                    <span id="name">Name</span>
                  </dt>
                </Col>
                <Col>
                  <dd>{dcuEntity.name}</dd>
                </Col>
              </Row>
              <Row>
                <Col>
                  <dt>
                    <span id="dateCreated">Date Created</span>
                  </dt>
                </Col>
                <Col>
                  <dd>
                    {dcuEntity.dateCreated ? <TextFormat value={dcuEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}
                  </dd>
                </Col>
              </Row>
            </dl>
            <Button tag={Link} to={`/manager/${factoryId}`} replace color="info" data-cy="entityDetailsBackButton">
              <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
            </Button>
            &nbsp;
            <Button tag={Link} to={`/manager/${factoryId}/${id}/update`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
            </Button>
          </Col>
        </Row>
      </Card>
      <Card>
        <Nav fill tabs>
          <NavItem>
            <NavLink active={currentActiveTab === '1'} onClick={() => toggle('1')}>
              Realtime
            </NavLink>
          </NavItem>
          <NavItem>
            <NavLink active={currentActiveTab === '2'} onClick={() => toggle('2')}>
              History
            </NavLink>
          </NavItem>
        </Nav>
        <TabContent activeTab={currentActiveTab}>
          <TabPane tabId="1">
            <ManagerDcuRealtime />
          </TabPane>
          <TabPane tabId="2">
            <ManagerDcuHistory />
          </TabPane>
        </TabContent>
      </Card>
    </>
  );
};

export default ManagerDcuDetail;
