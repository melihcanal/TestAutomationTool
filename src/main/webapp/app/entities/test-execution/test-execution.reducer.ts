import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { createEntitySlice, EntityState, IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { defaultValue, ITestExecution } from 'app/shared/model/test-execution.model';
import { ITestScenario } from 'app/shared/model/test-scenario.model';

const initialState: EntityState<ITestExecution> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/test-executions';

// Actions

export const getEntities = createAsyncThunk('testExecution/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?cacheBuster=${new Date().getTime()}`;
  return axios.get<ITestExecution[]>(requestUrl);
});

export const getEntitiesByTestScenario = createAsyncThunk(
  'testExecution/fetch_entity_list_by_test_scenario',
  async (entity: ITestScenario, thunkAPI) => {
    const requestUrl = `${apiUrl}/find-by-test-scenario`;
    return await axios.post<ITestExecution[]>(requestUrl, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError }
);

export const getEntity = createAsyncThunk(
  'testExecution/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<ITestExecution>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'testExecution/create_entity',
  async (entity: ITestExecution, thunkAPI) => {
    const result = await axios.post<ITestExecution>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'testExecution/update_entity',
  async (entity: ITestExecution, thunkAPI) => {
    const result = await axios.put<ITestExecution>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'testExecution/partial_update_entity',
  async (entity: ITestExecution, thunkAPI) => {
    const result = await axios.patch<ITestExecution>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'testExecution/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<ITestExecution>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const TestExecutionSlice = createEntitySlice({
  name: 'testExecution',
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
      .addMatcher(isFulfilled(getEntities, getEntitiesByTestScenario), (state, action) => {
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
      .addMatcher(isPending(getEntities, getEntity, getEntitiesByTestScenario), state => {
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

export const { reset } = TestExecutionSlice.actions;

// Reducer
export default TestExecutionSlice.reducer;
