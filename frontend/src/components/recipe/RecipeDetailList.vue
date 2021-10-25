<template lang="">
  <div class="box">
    <!-- {{ recipeContent}} -->
    <div class="content_box">
    <hooper :progress="true" id="hooper">
      <slide class="thumbNail" :style="{ backgroundImage: `url(${thumbnailImg})` }">
        <div class="bg">
        </div>
          <div class="menubar">
            <span class="back" @click="$router.go(-1)">
             <i class="fas fa-arrow-left"></i>
            </span>
            <span v-if="recipeContent.id == userId" class="menu" @click="showMenuFunc">
              <i class="fas fa-ellipsis-v"></i>
            </span>
            <ul v-if="showMenu" class="dropdown">
              <li @click="updateRecipe">수정</li>
              <hr style="margin:0px;">
              <li @click="deleteRecipe">삭제</li>
            </ul>
          </div>
          <div class="name_time">
            <p class="recipe_content_nickname" style="color:white; margin-bottom:0px;" @click="goProfile">작성자 : {{ recipeContent.nickname}}</p>
            <p class="time" style="color:white;">{{ recipeContent.regdate | timeFor }}</p>
          </div>
          <h1 class="title" style="color:white;">{{ recipeContent.title}}</h1>
          <div class="hashlist">
            <div v-for="(h, idx) in recipeContent.tag" :key="idx">
              <span class="hash"># {{h}}</span>
            </div>
          </div>
        <div class="footer">
          <div class="comment">
            <span class="comment_icon">
              <i class="fas fa-comment-dots"></i>
            </span>
            <p class="comment_name">
              {{ commentCount }}
            </p>
            </div>
          <div class="like">
            <span v-show="recipeContent.likecheck == false">
              <button class="submit" @click="like">
                <i class="far fa-heart"></i>
              </button>
            </span>
            <span v-show="recipeContent.likecheck == true">
              <button class="submit" @click="Unlike">
                <i class="fas fa-heart"></i>
              </button>   
            </span>
            <p class="like_count" style="font-size:14px;">
              {{ recipeContent.like }}
            </p>
          </div>
        </div>
      </slide>
      <slide class="contents" v-for="(recipeItem, idx) in recipe" :key="idx">
        <span v-if="recipe.length === idx+1" class="out" @click="$router.go(-1)">
          <i class="fas fa-sign-out-alt"></i>
        </span>
        <!-- {{ recipeItem }} -->
        <!-- {{ idx+1 }} -->
        <div class="recipe">
          <div class="recipe_imgbox">
            <img :src='recipeItem.img' width="" class="recipeImg"/>
          </div>
          <div class="recipe_desbox">
            <p class="recipe_des" >{{ recipeItem.des }}
            </p>
          </div>
        
        </div>
        <div class="footer2" style="height: 5vh;">
          <div class="bg2"></div>
          <div class="comment2" @click="showCommentFunc(idx)">
            <span class="comment_icon">
              <i class="fas fa-comment-dots"></i>
            </span>
            <p class="comment_name2">{{ recipeContent.commentCount[idx] }}</p>
          </div>
          <div class="like2">
            <span v-show="recipeContent.likecheck == false">
              <button class="submit" @click="like">
                <i class="far fa-heart"></i>
              </button>
            </span>
            <span v-show="recipeContent.likecheck == true">
              <button class="submit" @click="Unlike">
                <i class="fas fa-heart"></i>
              </button>   
            </span>
            <p class="like_count2">
              {{ recipeContent.like }}
            </p>
          </div>
        </div>
      </slide>
      <hooper-navigation v-if="showComment == false" slot="hooper-addons"></hooper-navigation>
    </hooper>
    </div>
        <div class="comment_box" v-if="showComment">
          <div class="comment_header">
            <span class="comment_back" @click="backComment">
              <i class="fas fa-arrow-left"></i>
            </span>
          </div>
          <RecipeCommentList :recipeItem="recipe[selectedContent]" :recipeContent="recipeContent" :selectedContent="selectedContent"/>
    </div>
    </div>
</template>

<script>
import { mapState } from 'vuex'
import RecipeApi from '../../api/RecipeApi';
import FirebaseApi from '../../api/FirebaseApi';
import RecipeCommentList from "../../components/recipe/RecipeCommentList.vue";
import { Hooper, Slide, Navigation as HooperNavigation } from "hooper";
import 'hooper/dist/hooper.css';

export default {
  components: {
    RecipeCommentList,
    Hooper, Slide, HooperNavigation,
  },
  props: {
    recipe: {
      type: [Array, Object]
    },
    recipeContent: {
      type: [Array, Object]
    },
    thumbnailImg: String,
  },
  data: () => {
    return {
      showComment: false,
      selectedContent: 0,
      showMenu: false,
    }
  },
  filters : {
    timeFor : function(created_at){
      if(created_at){
      var a = ''
      a = created_at.slice(0,4)
      a += '.'
      a += created_at.slice(5,7)
      a += '.'
      a += created_at.slice(8,10)
      a += ' '
      a += created_at.slice(11,16)
      return a
      }
    }
  },
  methods: {
    backComment() {
      this.showComment = false
    },
    updateRecipe() {
      this.$router.push({name:'UpdateRecipe', params: {recipe_idx:this.recipe[0].recipe_idx}})
    },
    deleteRecipe() {
      let data = {
        recipe_idx: this.recipe[0].recipe_idx
      }
      RecipeApi.deleteRecipe(
        data,
        res => {
          this.$router.push({name:'FeedMain'})
        },
        err => {
          console.log(err)
        }
      )
    },
    showMenuFunc(){
      if (this.showMenu) {
        this.showMenu = false
      }
      else {
        this.showMenu = true
      }
    },
    showCommentFunc(idx){
      this.selectedContent = idx
      this.showComment = true
    },
    goProfile(){
      this.$router.push({name:'Profile', params: {user_id: this.recipeContent.id}})
    },
    like() {
      let data = {
        recipe_idx: this.recipe[0].recipe_idx,
        recipe_user_id: this.userId
      }
      RecipeApi.likeRecipe(
        data,
        res => {
          this.recipeContent.like += 1
          this.recipeContent.likecheck = true
        },
        err => {
          console.log(err)
        }
      )
      let notice = {
        user:this.recipeContent.id,
        img:this.$store.state.userImg,
        ReqUser:this.$store.state.userId,
        type:'like',
        articleID:this.recipe[0].recipe_idx
      }
      FirebaseApi.noticeAdd(notice)
    },
    Unlike() {
      let data = {
        recipe_idx: this.recipe[0].recipe_idx,
        recipe_user_id: this.userId
      }
      RecipeApi.unlikeRecipe(
        data,
        res => {
          this.recipeContent.like -= 1
          this.recipeContent.likecheck = false
        },
        err => {
          console.log(err)
        }
      )
    },
  },
  computed: {
    ...mapState([
      'userId',      
      'userNickname',
      'selectRecipeId',
      'commentCount'
    ])
  }
}
</script>
<style scoped>
  .content_box{
  display: flex;
  justify-content: center;
  align-items: center;
  }
  .menubar {
    display: flex;
    justify-content: space-between;
    /* position: absolute;  */
    z-index:1000;
    color: #fff;
  }
  .out{
    z-index: 1000;
    display: flex;
    position: absolute;
    right: 0px;
    margin: 7px 9px 0px 0px;
    font-size: 25px;
    cursor: pointer;
  }
  .back {
    margin-top: 7px;
    margin-left: 9px;
    font-size: 25px;
    cursor: pointer;
  }
  .menu {
    margin-top: 7px;
    margin-right: 9px;
    font-size: 25px;
    position: relative;
    cursor: pointer;
  }
  .dropdown {
    position: absolute;
    top: 20px;
    right: 25px;
    background: rgba(0, 0, 0, 0.5);
    color: #fff;
    padding: 0px;
    cursor: pointer;
  }
  .dropdown li {
    margin: 5px 7px 5px 7px;
  }
  img {
    /* margin-top: 10px; */
    max-width: 100%;
  }
  #hooper {
    height: 85vh;
    max-width: 70vh;
    position: absolute;
    top:50%;

  }
  .contents {
    position: relative;
  }
  .contents:before {
    display: block;
    padding-top: 100%;
  }
  .recipeImg {
    height: 60vh;
    width: 100%;
    object-fit: cover;
    display:block;
  }
  .thumbNail {
    background-position: center;
    background-repeat : no-repeat;
    background-size: cover;
    position: relative;
  }
  .bg{
    background-color:rgba(0, 0, 0, 0.5);
    width: 100%;
    height: 100%;
    position: absolute;
  }
  h1{
    margin-top: 10px;
    margin-bottom: 10px;
    position: relative; 
    z-index:1000;
    top: 40%;
    left: 20px;
    text-align: left;
  }
  .recipe {
    flex-wrap: wrap;
    height: 79vh;
    /* overflow: auto; */
  }

  .recipe_des{
    padding: 8px 8px 0px 12px;
    font-size: 20px;
    word-break:normal;
    text-overflow:ellipsis;
    max-height: 19vh;
    margin: 0px;
    overflow: auto;
    
  }
  .name_time{
    margin: 0px;
    position: relative; 
    z-index:1000;
    top: 42%;
    left: 20px;
    text-align: left;
  }
  .recipe_content_nickname {
    cursor: pointer;
  }
  .time {
    font-size: 12px;
  }
  .hashlist{
    position: relative; 
    z-index:1000;
    top: 40%;
    left: 15px;
    text-align: left;
    display: flex;
    /* overflow: auto; */
    width: 100%;
    flex-wrap: wrap;
  }
  .title {
    word-break: normal;
    margin-right: 40px;
  }
  .hash{
  background-color: rgba(0, 0, 0, 0.5);
  padding: 2px 10px;
  margin-right: 10px;
  margin-bottom: 10px;
  cursor: pointer;
  color: #fff;
  font-size: 0.9rem;
  font-weight: 500;
  min-width: 50px;
  border-radius: 25px;
  }
  .footer{
    z-index:1000;
    position: absolute;
    color: #fff;
    display: flex;
    justify-content: space-around;
    align-items: center;
    bottom: 7px;
    text-align: center;
    width: 100%;
  }
  .like{
    display: flex;
    justify-content: center;
  }
  .fa-comment-dots{
    color: #fff;
  }
  .fa-heart {
    color: #fff;
    margin-top: 5px;
    font-size: 20px;
    cursor: pointer;
  }
  .like_count {
    margin-bottom: 0px;
    font-size: 15px;
    margin-left: 4px;
    margin-top: 5px;
  }
  .comment{
    display: flex;
    justify-content: center;
  }
  .comment_name{
    margin-top: 9px;
    margin-bottom: 0px;
    margin-left: 4px;
    font-size: 15px;
  }
  .bg2{
    background-color:rgba(0, 0, 0, 0.5);
    width: 100%;
    height: 6vh;
    bottom: 0px;
    position: absolute;
  }
  .footer2{
    /* bottom: 0px; */
    /* position: absolute;  */
    z-index:1000;
    color: #fff;
    display: flex;
    justify-content: space-around;
    align-items: center;
    text-align: center;
    font-size: 15px;
  }
  .like2{
    display: flex;
    margin-top: 7px;
    justify-content: center;
    font-size: 20px;
  }
  .like_count2 {
    margin-bottom: 0px;
    margin-left: 4px;
    margin-top:3px;
    font-size: 15px;
  }
  .comment2{
    margin-top: 2px;
    display: flex;
    justify-content: center;
    /* height: 100%; */
    cursor: pointer;
  }
  .comment_name2{
    margin: 8px 0px 0px 0px;
    padding-left: 3px;
    font-size: 15px;
  }
  .comment_icon {
    margin-top: 7px;
    font-size: 20px;
  }

  .recipe_des::-webkit-scrollbar {
    width: 6px;
  }
  .recipe_des::-webkit-scrollbar-track {
    background-color: transparent;
  }
  .recipe_des::-webkit-scrollbar-thumb {
    border-radius: 3px;
    background-color: gray;
  }
  .recipe_des::-webkit-scrollbar-button {
    width: 0;
    height: 0;
  }
  .comment_box{
      background-color:rgba(255, 255, 255);
      z-index: 1000;
      width: 100%;
      height: 100%;
      height: 85vh;
      max-width: 70.5vh;
      position: absolute;
      top:50%;
      left: 50%;
      transform: translate(-50%, 0%);
  }
  .comment_header{
    width: 100%;
    height: 4vh;
  }
  .comment_back{
    padding-top: 5px;
    margin-left: 4px;
    font-size: 25px;
    cursor: pointer;
  }
</style>
