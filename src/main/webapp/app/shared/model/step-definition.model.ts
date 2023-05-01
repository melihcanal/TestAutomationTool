import { ITestScenario } from 'app/shared/model/test-scenario.model';
import { ActionType } from 'app/shared/model/enumerations/action-type.model';

export interface IStepDefinition {
  id?: number;
  actionType?: ActionType | null;
  message?: string | null;
  xpathOrCssSelector?: string | null;
  keyword?: string | null;
  scrollLeft?: number | null;
  scrollTop?: number | null;
  url?: string | null;
  expected?: string | null;
  createdBy?: string | null;
  createdDate?: string | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: string | null;
  testScenario?: ITestScenario | null;
}

export const defaultValue: Readonly<IStepDefinition> = {};
