export default {
  login: function({commit}, res){
    commit('LOGIN',res)
  },
  logout: function({commit}, res){
    commit('LOGOUT',res)
  },
  writeRecipeItem({commit}, res){
    commit('WRITERECIPEITEM',res)
  },
  uploadImg({commit}, res){
    commit('UPLOADIMG',res)
  },
  choiceThumbnail({commit}, res){
    commit('CHOICETHUMBNAIL',res)
  },
  clearFormdata({commit}){
    commit('CLEARFORMDATA')
  },
  selectRecipe({commit}, res){
    commit('SELECTRECIPE',res)
  },
  deleteContent({commit},res){
    commit('DELETECONTENT',res)
  },
  countComment({commit},res){
    var commentSum = 0
    for (var comment of res){
      commentSum += comment
    }
    commit('COUNTCOMMENT',commentSum)
  },
  getCommentCountList({commit,dispatch},res){
    commit('GETCOMMENTCOUNTLIST',res)
    dispatch('countComment',res)
  },
  getCommentCount({commit},res){
    commit('GETCOMMENTCOUNT',res)
  },
  getComments({commit},res){
    commit('GETCOMMENTS',res)
  }
}
