import company from 'app/entities/company/company.reducer';
import factory from 'app/entities/factory/factory.reducer';
import dcu from 'app/entities/dcu/dcu.reducer';
import record from 'app/entities/record/record.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  company,
  factory,
  dcu,
  record,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
