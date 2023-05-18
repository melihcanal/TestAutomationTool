import { ITestScenario } from 'app/shared/model/test-scenario.model';

export interface ITestExecution {
  id?: number;
  status?: boolean | null;
  message?: string | null;
  reportUrl?: string | null;
  createdBy?: string | null;
  createdDate?: string | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: string | null;
  testScenario?: ITestScenario | null;
}

export const defaultValue: Readonly<ITestExecution> = {
  status: false,
};
