import './home.scss';

import React from 'react';

import { Button, Col, Row } from 'reactstrap';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getTestExecutionReport } from 'app/entities/test-execution/test-execution.reducer';
import { useAppDispatch } from 'app/config/store';

export const Home = () => {
  const dispatch = useAppDispatch();

  const showTestExecutionReport = () => {
    dispatch(getTestExecutionReport(1));
  };

  return (
    <>
      <Row>
        <Col md="9">
          <div>Home Page</div>
        </Col>
      </Row>
      <Row>
        <Col md="9">
          <Button tag={Link} to={`/test-scenario/create`} color="primary" size="sm">
            <FontAwesomeIcon icon="camera-alt" /> <span className="d-none d-md-inline">Record Test Scenario</span>
          </Button>
          <Button className="me-2" color="danger" onClick={showTestExecutionReport}>
            Report
          </Button>
        </Col>
      </Row>
    </>
  );
};

export default Home;
