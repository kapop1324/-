import axios from 'axios';
import store from '@/vuex/store.js'

// axios 객체 생성
export default axios.create({
  baseURL: 'http://127.0.0.1:8080/', //백엔드 포트번호
  headers: {
    'Content-type': 'application/json',
    'access-token': store.state.token
    
  },
});
