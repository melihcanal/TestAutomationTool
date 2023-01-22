import dayjs from 'dayjs';
import { ITestExecution } from 'app/shared/model/test-execution.model';
import { IUser } from 'app/shared/model/user.model';

export interface ITestScenario {
  id?: number;
  title?: string | null;
  description?: string | null;
  testSteps?: string | null;
  numberOfExecution?: number | null;
  numberOfPassed?: number | null;
  numberOfFailed?: number | null;
  createdBy?: string | null;
  createdDate?: string | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: string | null;
  testExecutions?: ITestExecution[] | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ITestScenario> = {};
