import dayjs from 'dayjs';
import { IFactory } from 'app/shared/model/factory.model';

export interface ICompany {
  id?: number;
  name?: string;
  dateCreated?: string | null;
  factoryNames?: IFactory[] | null;
}

export const defaultValue: Readonly<ICompany> = {};
