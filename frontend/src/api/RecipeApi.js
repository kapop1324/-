import http from "@/util/http-common";
import store from '@/vuex/store.js'

const achieveRecipe = (callback, errorCallback) =>{
    http.get('/feed/write', {
        headers: {
          'access-token': store.state.token
        }
      })
    .then(
         res=>callback(res)
     ).catch(
         err=>errorCallback(err)
     )
 }

const deleteRecipe = (data, callback, errorCallback) => {
    http.delete('/feed/delete',{
        params: {
            idx: data.recipe_idx
        }
    }, {
        headers: {
          'access-token': store.state.token
        }
      }).then(
        res => callback(res)
    ).catch(
        err=>errorCallback(err)
    )
}

const updateRecipe = (data, callback, errorCallback) => {
    http.put('/feed/update',data,{
        headers: {
            'Content-Type': 'multipart/form-data',
            'access-token': store.state.token
        }
    }).then(
        res => callback(res)
    ).catch(
        err=>errorCallback(err)
    )
}

const likeRecipe = (data, callback, errorCallback) => {
    http.post('/feed/like',data, {
        headers: {
          'access-token': store.state.token
        }
      }).then(
        res => callback(res)
    ).catch(
        err => errorCallback(err)
    )
}

const unlikeRecipe = (data, callback, errorCallback) => {
    http.delete('/feed/like',{
        params: {
            recipe_idx: data.recipe_idx,
            recipe_user_id: data.recipe_user_id
        }
    }, {
        headers: {
          'access-token': store.state.token
        }
      }).then(
        res => callback(res)
    ).catch(
        err => errorCallback(err)
    )
}


// Comment CRUD + 좋아요

const writeComment = (data, callback, errorCallback) => {
    http.post('/feed/comment/write',data, {
        headers: {
          'access-token': store.state.token
        }
      }).then(
        res=>callback(res)
    ).catch(
        err=>errorCallback(err)
    )
}

const updateComment = (data,callback, errorCallback) => {
    http.put('/feed/comment/update',data, {
        headers: {
          'access-token': store.state.token
        }
      }).then(
        res=>callback(res)
    ).catch(
        err=>errorCallback(err)
    )
}

const deleteComment = (data,callback, errorCallback) => {
    http.delete('/feed/comment/delete',{
        params: {
            idx: data.idx,
        }
    }, {
        headers: {
          'access-token': store.state.token
        }
      }).then(
        res=>callback(res)
    ).catch(
        err=>errorCallback(err)
    )
}

const recipeItemComments = (data,callback, errorCallback) => {
    http.get('/feed/comment',{
      params: {
        content_idx: data.content_idx,
        id: data.id,
      }
    }, {
        headers: {
          'access-token': store.state.token
        }
      }).then(
        res=>callback(res)
    ).catch(
        err=>errorCallback(err)
    )
}

const commentLike = (data,callback, errorCallback) => {
    http.post('/feed/comment/like',data, {
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

const commentUnlike = (data,callback, errorCallback) => {
    http.delete('/feed/comment/like',{
        params: {
            comment_idx: data.comment_idx,
            id: data.id,
        }
    }, {
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


const RecipeApi = {
    achieveRecipe: (callback, errorCallback) => achieveRecipe(callback, errorCallback),
    deleteRecipe: (data, callback, errorCallback) => deleteRecipe(data, callback, errorCallback),
    updateRecipe: (data, callback, errorCallback) => updateRecipe(data, callback, errorCallback),
    likeRecipe: (data, callback, errorCallback) => likeRecipe(data, callback, errorCallback),
    unlikeRecipe: (data, callback, errorCallback) => unlikeRecipe(data, callback, errorCallback),
    writeComment: (data, callback, errorCallback) => writeComment(data, callback, errorCallback),
    updateComment: (data, callback, errorCallback) => updateComment(data, callback, errorCallback),
    deleteComment: (data, callback, errorCallback) => deleteComment(data, callback, errorCallback),
    recipeItemComments: (data, callback, errorCallback) => recipeItemComments(data, callback, errorCallback),
    commentLike: (data, callback, errorCallback) => commentLike(data, callback, errorCallback),
    commentUnlike: (data, callback, errorCallback) => commentUnlike(data, callback, errorCallback),
}
export default RecipeApi