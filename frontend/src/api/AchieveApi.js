import http from '@/util/http-common';
import store from '@/vuex/store.js'
import { mapState } from 'vuex';
import { mapActions } from 'vuex';

//리스트에서 사용
const AchievemasterList = (data, callback, errorCallback) => {
  http
    .get('/achieve/list' , {
      headers: {
        'access-token': store.state.token
      }
    })
    .then((res) => callback(res))
    .catch((err) => errorCallback(err));
};

//리스트에서 사용
const getAchievecurrent = (data, callback, errorCallback) => {
  http
    .get('/achieve/getAchievecurrent', {
      params: {
        id: data.id,
      },
    }, {
      headers: {
        'access-token': store.state.token
      }
    })
    .then((res) => callback(res))
    .catch((err) => errorCallback(err));
};

const AchievemasterCount = (data, callback, errorCallback) => {
  http
    .get('/achieve/mastercount', {
      headers: {
        'access-token': store.state.token
      }
    })
    .then((res) => callback(res))
    .catch((err) => errorCallback(err));
};

const myMasterCount = (data, callback, errorCallback) => {
  http
    .get('/feed/mastercount', {
      params: {
        id: data.id,
      },
    }, {
      headers: {
        'access-token': store.state.token
      }
    })
    .then((res) => callback(res))
    .catch((err) => errorCallback(err));
};

const AchievemasterCategoryList = (data, callback, errorCallback) => {
  http
    .get('/achieve/categorylist', {
      params: {
        master: data.master,
      },
    }, {
      headers: {
        'access-token': store.state.token
      }
    })
    .then((res) => callback(res))
    .catch((err) => errorCallback(err));
};

const myRecipe = (data, callback, errorCallback) => {
  http
    .get('/achieve/myRecipe', {
      params: {
        id: data.id,
        master: data.master,
      },
    }, {
      headers: {
        'access-token': store.state.token
      }
    })
    .then((res) => callback(res))
    .catch((err) => errorCallback(err));
};

const achieveTitle = (data, callback, errorCallback) => {
  http
    .get('/achieve/titleList', {
      params: {
        id: data.id,
      },
    })
    .then((res) => callback(res))
    .catch((err) => errorCallback(err));
};
const achieveTitleOne = (data, callback, errorCallback) => {
  http
    .get('/achieve/titleOne', {
      params: {
        id: data.id,
        title: data.title,
      },
    })
    .then((res) => callback(res))
    .catch((err) => errorCallback(err));
};
const achieveTitleDetail = (data, callback, errorCallback) => {
  http
    .get('/achieve/titleDetail', {
      params: {
        id: data.id,
        title: data.title,
      },
    })
    .then((res) => callback(res))
    .catch((err) => errorCallback(err));
};

const AchieveApi = {
  AchievemasterList: (data, callback, errorCallback) =>
    AchievemasterList(data, callback, errorCallback),
  AchievemasterCount: (data, callback, errorCallback) =>
    AchievemasterCount(data, callback, errorCallback),
  getAchievecurrent: (data, callback, errorCallback) =>
    getAchievecurrent(data, callback, errorCallback),
  myMasterCount: (data, callback, errorCallback) => myMasterCount(data, callback, errorCallback),
  AchievemasterCategoryList: (data, callback, errorCallback) =>
    AchievemasterCategoryList(data, callback, errorCallback),
  myRecipe: (config, data, callback, errorCallback) =>
    myRecipe(config, data, callback, errorCallback),
  achieveTitle: (config, data, callback, errorCallback) =>
    achieveTitle(config, data, callback, errorCallback),
  achieveTitleOne: (config, data, callback, errorCallback) =>
    achieveTitleOne(config, data, callback, errorCallback),
  achieveTitleDetail: (config, data, callback, errorCallback) =>
    achieveTitleDetail(config, data, callback, errorCallback),
};
export default AchieveApi;
