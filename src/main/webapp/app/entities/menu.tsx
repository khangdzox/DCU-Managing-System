import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/company">
        <Translate contentKey="global.menu.entities.company" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/factory">
        <Translate contentKey="global.menu.entities.factory" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dcu">
        <Translate contentKey="global.menu.entities.dcu" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/record">
        <Translate contentKey="global.menu.entities.record" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
