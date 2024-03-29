import configureStore from 'redux-mock-store';
import axios from 'axios';
import thunk from 'redux-thunk';
import sinon from 'sinon';

import userManagement, { getUsers } from 'app/shared/reducers/user-management';

describe('User management reducer tests', () => {
  const initialState = {
    users: [],
    errorMessage: null,
  };

  describe('Common', () => {
    it('should return the initial state', () => {
      expect(userManagement(undefined, { type: 'unknown' })).toEqual({ ...initialState });
    });
  });

  describe('Failures', () => {
    it('should set state to failed and put an error message in errorMessage', () => {
      expect(
        userManagement(undefined, {
          type: getUsers.rejected.type,
          payload: 'something happened',
          error: { message: 'error happened' },
        })
      ).toEqual({ ...initialState, errorMessage: 'error happened' });
    });
  });

  describe('Success', () => {
    it('should update state according to a successful fetch users request', () => {
      const payload = { data: 'some handsome users' };
      const toTest = userManagement(undefined, { type: getUsers.fulfilled.type, payload });
      expect(toTest).toMatchObject({
        users: payload.data,
      });
    });
  });

  describe('Actions', () => {
    let store;

    const resolvedObject = { value: 'whatever' };
    beforeEach(() => {
      const mockStore = configureStore([thunk]);
      store = mockStore({});
      axios.get = sinon.stub().returns(Promise.resolve(resolvedObject));
    });

    it('dispatches FETCH_USERS_PENDING and FETCH_USERS_FULFILLED actions', async () => {
      const expectedActions = [
        {
          type: getUsers.pending.type,
        },
        {
          type: getUsers.fulfilled.type,
          payload: resolvedObject,
        },
      ];
      await store.dispatch(getUsers({}));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
    });

    it('dispatches FETCH_USERS_PENDING and FETCH_USERS_FULFILLED actions with pagination options', async () => {
      const expectedActions = [
        {
          type: getUsers.pending.type,
        },
        {
          type: getUsers.fulfilled.type,
          payload: resolvedObject,
        },
      ];
      await store.dispatch(getUsers({ page: 1, size: 20, sort: 'id,desc' }));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
    });
  });
});
