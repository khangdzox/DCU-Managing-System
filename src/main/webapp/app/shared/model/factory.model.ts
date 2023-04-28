import dayjs from 'dayjs';
import { IDcu } from 'app/shared/model/dcu.model';
import { ICompany } from 'app/shared/model/company.model';

export interface IFactory {
  id?: number;
  name?: string;
  dateCreated?: string | null;
  deviceNames?: IDcu[] | null;
  companyName?: ICompany | null;
}

export const defaultValue: Readonly<IFactory> = {};
