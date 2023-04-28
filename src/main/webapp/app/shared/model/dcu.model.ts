import dayjs from 'dayjs';
import { IFactory } from 'app/shared/model/factory.model';

export interface IDcu {
  id?: number;
  name?: string;
  dateCreated?: string | null;
  factoryName?: IFactory | null;
}

export const defaultValue: Readonly<IDcu> = {};
