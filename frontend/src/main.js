import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router';
import routes from './routes'
import store from './vuex/store'
import VueSession from "vue-session";
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import firebase from 'firebase';


Vue.use(BootstrapVue)

Vue.config.productionTip = false
// Vue.use(VueSession, { persist: true });

Vue.use(VueRouter)

// var firebaseConfig = {
//     apiKey: "AIzaSyByJs9HYDo22JOmuffA7s6aEYsILgHwDyc",
//     authDomain: "websns-5f1d6.firebaseapp.com",
//     projectId: "websns-5f1d6",
//     storageBucket: "websns-5f1d6.appspot.com",
//     messagingSenderId: "351362417253",
//     appId: "1:351362417253:web:6854606b89e8f946767c8c"
//   };
//   // Initialize Firebase
// firebase.initializeApp(firebaseConfig);
import infiniteScroll from 'vue-infinite-scroll'
Vue.use(infiniteScroll)

const router = new VueRouter({
    mode: 'history',
    routes,
});

new Vue({
    router,
    store,
    render: h => h(App),
}).$mount('#app');
window.Kakao.init("cd0727684037fa18dbfb547d7ad9eb2c");