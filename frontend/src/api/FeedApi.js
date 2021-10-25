import http from "@/util/http-common";
import store from '@/vuex/store.js'
import { mapState } from 'vuex'

const likeposts = (data, callback, errorCallback) => {
  http.get('/feed/likedposts', {
      params:{
          user_id : data.id,
          page: data.page
      }}, {
        headers: {
          'access-token': store.state.token
        }
      }
  ).then(
      res=>callback(res)
  ).catch(
      err=>errorCallback(err)
  )
}
const popularposts = (data, callback, errorCallback) => {
  http.get('/feed/popularposts', {
      params:{
          user_id : data.id,
          page: data.page
      }}, {
        headers: {
          'access-token': store.state.token
        }
      }
  ).then(
      res=>callback(res)
  ).catch(
      err=>errorCallback(err)
  )
}

const hashtagsearch = (data, callback, errorCallback) => {
  http.get('/feed/hashtagsearch', {
    params:{
        hashtag : data.hashtag,
        page: data.page
    }}, {
      headers: {
        'access-token': store.state.token
      }
    }
).then(
    res=>callback(res)
).catch(
    err=>errorCallback(err)
)
}

const FeedApi = {
  likeposts: (data, callback, errorCallback) => likeposts(data, callback, errorCallback),
  popularposts: (data, callback, errorCallback) => popularposts(data, callback, errorCallback),
  hashtagsearch: (data, callback, errorCallback) => hashtagsearch(data, callback, errorCallback),
}

export default FeedApi