import React from 'react';
import { Nav } from 'react-bootstrap';
import { BsPlay, BsPencil, BsGrid3X3Gap, BsGear } from 'react-icons/bs';
import './sidebar.scss';

const Sidebar = () => {
  return (
    <div className="sidebar">
      <Nav className="flex-column">
        <Nav.Link href="/test-scenario">
          <BsPlay className="sidebar-icon" />
          Test Scenarios
        </Nav.Link>
        <Nav.Link href="/test-scenario/create">
          <BsPencil className="sidebar-icon" />
          Record Test Scenario
        </Nav.Link>
        <Nav.Link href="#elements">
          <BsGrid3X3Gap className="sidebar-icon" />
          Elements
        </Nav.Link>
        <Nav.Link href="#parameters">
          <BsGear className="sidebar-icon" />
          Parameters
        </Nav.Link>
      </Nav>
    </div>
  );
};

export default Sidebar;
