import { IStepDefinition } from 'app/shared/model/step-definition.model';

export interface IStepDefinitionRequest {
  stepDefinitionList?: IStepDefinition[] | null;
}
