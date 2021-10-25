import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import actions from './actions'
import mutations from './mutations'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)

let state = {
    isUser: false,
    token:'',
    userId: '',
    userImg:'',
    userNickname: '',
    sns: '',
    email:'',
    recipeItems: [],
    img: [],
    thumbnailNumber: 0,
    deleteContents: [],
    selectRecipeId: '',
    commentCount: 0,
    commentCountList: [],
}

export default new Vuex.Store({
    plugins:[
        createPersistedState()
    ],
    state,
    mutations,
    getters,
    actions
})