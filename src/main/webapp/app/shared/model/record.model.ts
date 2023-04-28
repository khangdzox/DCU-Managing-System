import dayjs from 'dayjs';

export interface IRecord {
  id?: number;
  dcuid?: number;
  current?: number;
  voltage?: number;
  timestamp?: string;
}

export const defaultValue: Readonly<IRecord> = {};
