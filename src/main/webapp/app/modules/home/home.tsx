import './home.scss';

import React from 'react';

import { Button, Col, Row } from 'reactstrap';
import { useAppDispatch } from 'app/config/store';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const Home = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

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
        </Col>
      </Row>
    </>
  );
};

export default Home;
