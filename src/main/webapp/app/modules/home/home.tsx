import './home.scss';

import React, { useEffect } from 'react';
import { Row, Col, Alert } from 'reactstrap';

import { REDIRECT_URL } from 'app/shared/util/url-utils';
import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  useEffect(() => {
    const redirectURL = localStorage.getItem(REDIRECT_URL);
    if (redirectURL) {
      localStorage.removeItem(REDIRECT_URL);
      location.href = `${location.origin}${redirectURL}`;
    }
  });
  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <h2>Test Automation Tool Homepage</h2>
        {account?.login ? (
          <div>
            <Alert color="success">You are logged in as user &quot;{account.login}&quot;.</Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">Please login by clicking the sign in button on the top right.</Alert>
          </div>
        )}
        <ul>
          <li>
            <a href="localhost:8080/" target="_blank" rel="noopener noreferrer">
              Test Automation Tool homepage
            </a>
          </li>
          <li>
            <a href="https://github.com/melihcanal/TestAutomationTool" target="_blank" rel="noopener noreferrer">
              Github Project Repository
            </a>
          </li>
          <li>
            <a href="https://twitter.com/melihcanal" target="_blank" rel="noopener noreferrer">
              Author&apos;s Twitter profile
            </a>
          </li>
        </ul>
      </Col>
    </Row>
  );
};

export default Home;
