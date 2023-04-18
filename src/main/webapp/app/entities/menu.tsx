import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/test-scenario">
        Test Scenario
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-execution">
        Test Execution
      </MenuItem>
      <MenuItem icon="asterisk" to="/step-definition">
        Step Definition
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
