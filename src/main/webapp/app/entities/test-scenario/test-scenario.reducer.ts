import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { createEntitySlice, EntityState, IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { defaultValue, ITestScenario } from 'app/shared/model/test-scenario.model';

const initialState: EntityState<ITestScenario> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/test-scenarios';

// Actions

export const getEntities = createAsyncThunk('testScenario/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?cacheBuster=${new Date().getTime()}`;
  return axios.get<ITestScenario[]>(requestUrl);
});

export const getEntitiesByCurrentUser = createAsyncThunk('testScenario/fetch_entity_list_by_current_user', async () => {
  const requestUrl = `${apiUrl}/user`;
  return axios.get<ITestScenario[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'testScenario/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<ITestScenario>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'testScenario/create_entity',
  async (entity: ITestScenario, thunkAPI) => {
    const result = await axios.post<ITestScenario>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntitiesByCurrentUser());
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'testScenario/update_entity',
  async (entity: ITestScenario, thunkAPI) => {
    const result = await axios.put<ITestScenario>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntitiesByCurrentUser());
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'testScenario/partial_update_entity',
  async (entity: ITestScenario, thunkAPI) => {
    const result = await axios.patch<ITestScenario>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntitiesByCurrentUser());
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'testScenario/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<ITestScenario>(requestUrl);
    thunkAPI.dispatch(getEntitiesByCurrentUser());
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const TestScenarioSlice = createEntitySlice({
  name: 'testScenario',
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
      .addMatcher(isFulfilled(getEntities, getEntitiesByCurrentUser), (state, action) => {
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
      .addMatcher(isPending(getEntities, getEntity, getEntitiesByCurrentUser), state => {
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

export const { reset } = TestScenarioSlice.actions;

// Reducer
export default TestScenarioSlice.reducer;
