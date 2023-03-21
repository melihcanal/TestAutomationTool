import './home.scss';

import React, { useEffect } from 'react';

import { Row, Col, Button, Table } from 'reactstrap';

import { REDIRECT_URL } from 'app/shared/util/url-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from 'app/entities/test-scenario/test-scenario.reducer';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { TextFormat } from 'react-jhipster';

export const Home = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const testScenarioList = useAppSelector(state => state.testScenario.entities);
  const loading = useAppSelector(state => state.testScenario.loading);

  return (
    <Row>
      <Col md="9">
        <div>Home Page</div>
      </Col>
    </Row>
  );
};

export default Home;
