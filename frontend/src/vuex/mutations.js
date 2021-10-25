
import UserApi from '../api/UserApi';
export default {
  LOGIN: function (state, res) {
    const userId = res.data['id']
    const token = res.data['access-token']
    const nickname = res.data['nickname']
    const userImg = res.data['img']
    state.token = token
    state.userId = userId
    state.userNickname = nickname
    state.userImg = userImg
  },
  LOGOUT: function(state){
    state.token = ''
    state.userId = ''
  },
  WRITERECIPEITEM: function(state, res){
    state.recipeItems[res.idx] = res.des
  },
  UPLOADIMG: function(state, res){
    state.img[res.idx] = res.file
  },
  CHOICETHUMBNAIL(state, res){
    state.thumbnailNumber = res
  },
  CLEARFORMDATA(state){
    state.img = []
    state.recipeItems = []
    state.thumnailNumbers = 0 
    state.deleteContents = []  
  },
  SELECTRECIPE(state,res){
    state.selectRecipeId = res
  },
  DELETECONTENT(state,res){
    state.deleteContents.push(res)
  },
  COUNTCOMMENT(state,res){
    state.commentCount = res
  },
  GETCOMMENTCOUNTLIST(state,res){
    state.commentCountList = res
  },
  GETCOMMENTCOUNT(state,res){
    state.commentCountList[res.idx] = res.count
  },
  GETCOMMENTS(state,res){
    state.comments = res
  }
}