import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface ITestScenario {
  id?: number;
  title?: string | null;
  status?: boolean | null;
  message?: string | null;
  reportUrl?: string | null;
  createdBy?: string | null;
  createdDate?: string | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ITestScenario> = {
  status: false,
};
