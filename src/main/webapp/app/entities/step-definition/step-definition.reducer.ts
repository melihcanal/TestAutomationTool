import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { createEntitySlice, EntityState, IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { defaultValue, IStepDefinition } from 'app/shared/model/step-definition.model';
import { ITestScenario } from 'app/shared/model/test-scenario.model';
import { IStepDefinitionRequest } from 'app/shared/request/step-definition-request';

const initialState: EntityState<IStepDefinition> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/step-definitions';

// Actions

export const getEntities = createAsyncThunk('stepDefinition/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?cacheBuster=${new Date().getTime()}`;
  return axios.get<IStepDefinition[]>(requestUrl);
});

export const startWebDriver = createAsyncThunk('stepDefinition/start_web_driver', async (url: string | null) => {
  const requestUrl = `${apiUrl}/record-start?url=${url}`;
  return axios.get<void>(requestUrl);
});

export const stopWebDriver = createAsyncThunk('stepDefinition/stop_web_driver', async () => {
  const requestUrl = `${apiUrl}/record-stop`;
  return axios.get<IStepDefinition[]>(requestUrl);
});

export const saveStepDefinitions = createAsyncThunk(
  'stepDefinition/save_step_definitions',
  async (entities: IStepDefinitionRequest, thunkAPI) => {
    const requestUrl = `${apiUrl}/save-all`;
    return await axios.post<IStepDefinition[]>(requestUrl, cleanEntity(entities));
  },
  { serializeError: serializeAxiosError }
);

export const getEntitiesByTestScenario = createAsyncThunk(
  'stepDefinition/fetch_entity_list_by_test_scenario',
  async (entity: ITestScenario, thunkAPI) => {
    const requestUrl = `${apiUrl}/find-by-test-scenario`;
    return await axios.post<IStepDefinition[]>(requestUrl, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError }
);

export const getEntity = createAsyncThunk(
  'stepDefinition/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IStepDefinition>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'stepDefinition/create_entity',
  async (entity: IStepDefinition, thunkAPI) => {
    const result = await axios.post<IStepDefinition>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'stepDefinition/update_entity',
  async (entity: IStepDefinition, thunkAPI) => {
    const result = await axios.put<IStepDefinition>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'stepDefinition/partial_update_entity',
  async (entity: IStepDefinition, thunkAPI) => {
    const result = await axios.patch<IStepDefinition>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'stepDefinition/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IStepDefinition>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const StepDefinitionSlice = createEntitySlice({
  name: 'stepDefinition',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities, stopWebDriver, getEntitiesByTestScenario, saveStepDefinitions), (state, action) => {
        const { data } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity, stopWebDriver, getEntitiesByTestScenario, saveStepDefinitions), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = StepDefinitionSlice.actions;

// Reducer
export default StepDefinitionSlice.reducer;
