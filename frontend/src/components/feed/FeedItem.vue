<template>
  <div class="feed-item">
    <div class="top-a">
      <div class="user-infomation"  @click="goUserProfile">
        <div class="user-img">
          <img :src="latestFeed.profileImg ||  defaultProfile" alt="">
        </div>
        <div class="userName">{{ latestFeed.nickname }}</div>
      </div>
      <div class="title">
        <h2 @click="goRecipeDetail">{{ latestFeed.title | longTitle}}</h2>
      </div>
      <div class="btn-group-a">
        <div class="like">
          <i class="fas fa-heart full"></i>
          <span>{{ latestFeed.like||latestFeed.likes || 0}}</span>
        </div>
        <div class="comment">
          <i class="fas fa-comment-alt full"></i>
          <span>{{ latestFeed.comment }}</span>
        </div>
        <div class="share" @click="clip()">
          <i class="fas fa-share"></i>
        </div>
      </div>
    </div>
    <div class="feed-img" @click="goRecipeDetail">
      <img :src="latestFeed.img || defaultImage" alt="기본 이미지">
    </div>
    <div class="hashList">
      <div v-for="(hash, idx) in latestFeed.hashtags" :key="idx" style="margin-top: 3px;">
        <span class="hash"># {{ hash }}</span>
      </div>
    </div>
  </div>
</template>
<script>
import defaultImage from "../../assets/images/img-placeholder.png";
import defaultProfile from "../../assets/images/profile_default.png";
import RecipeApi from '../../api/RecipeApi';
import { mapState } from "vuex";
import swal from 'sweetalert';

export default {  
  data: () => {
    return { defaultImage, defaultProfile };
  },
  props: {
    latestFeed: {
      type: [Array, Object],
    },
    idx: Number,
  },
  methods: {
    goRecipeDetail() {
      this.$router.push({name:'RecipeDetail', params: {recipe_idx:this.latestFeed.idx}})
    },
    goUserProfile() {
      this.$router.push({name:'Profile', params: {user_id: this.latestFeed.id}})
    },
        updateRecipe() {
      this.$router.push({name:'UpdateRecipe', params: {recipe_idx:this.latestFeed.idx}})
    },
    deleteRecipe() {
      const newFeed = this.latestFeeds
      let data = {
        recipe_idx: this.latestFeed.idx
      }
      RecipeApi.deleteRecipe(
        data,
        res => {
          newFeed.splice(this.idx,1)
        },
        err => {
          console.log(err)
        }
      )
      this.lastetFeeds = newFeed
    },
    clip(){
      var url = '';
      var textarea = document.createElement("textarea");
      document.body.appendChild(textarea);
      url = window.document.location.origin;
      textarea.value = url+`/recipe/${this.latestFeed.idx}`;
      textarea.select();
      document.execCommand("copy");
      document.body.removeChild(textarea);
      swal({title:"URL이 복사되었습니다.",icon:'success'})

    }
  },
  filters:{
    longTitle: function(title){
      if (title.length > 8){
        return `${title.substring(0,7)}...`
      } else {
        return title
      }
    }
  },
  computed: {
    ...mapState([
      'userId',
    ]),
  }
}
</script>

<style scoped>
@font-face {
    font-family: 'BBTreeGB';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_nine_@1.1/BBTreeGB.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
*{
  margin: 0%;
  padding: 0%;
  color: rgba(0, 0, 0, 0.7);
  /* font-family: 'Pretendard-Regular'; */
}
h2{
  font-family: 'BBTreeGB';
  font-weight: 500;
}
svg{
  color: #ffbe76;
}
.user-img img{
  border-radius: 50%;
  width: 40px;
  height:40px;
  margin-left: 5px;
  margin-top: 7px;
  object-fit: cover;
}
.userName {
  margin-left: 4px;
  font-weight: 900;
  font-size: 14px;
}
.feed-item{
  width: 100%;
  max-width: 616px;
  border: none;
  padding: 0%;
  padding-bottom: 50px;
  border-bottom: 1px solid rgba(0, 0,0, 0.1);
}
.top-a{
  width: 100%;
  height: 80px;
  display: flex;
  margin: 0%;
}

.user-infomation{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 0px 15px;
  cursor: pointer;
}
.user-name{
  font-size: 0.8rem;
}
.title{
  display: flex;
  width: 70%;
  justify-content: flex-start;
  align-items: center;
  padding-left: 5px;
  cursor: pointer;
}
.title>h1{
  margin: 0%;
}
.btn-group-a{
  margin-right: 20px;
  width: 120px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.btn-group-a div{
  color: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}
.share{
  cursor: pointer;
}
.like svg{
  margin-right: 5px;
}
.comment svg{
  margin-right: 5px;
}

.feed-img{
  width: 100%;
  height: 64vh;
  cursor: pointer;
}
.feed-img>img{
  margin: 0%;
  padding: 0%;
  width: 100%;
  height: 100%;
  border-radius: 5px;
  object-fit: cover;
}
.hashList{
  padding: 5px;
  top: 40%;
  left: 15px;
  text-align: left;
  display: flex;
  /* overflow: auto; */
  width: 100%;
  flex-wrap: wrap;
}
.hash{
  background-color: #FFCD58;
  /* background-color: rgba( 255, 255, 255, 0.2); */
  padding: 3px 10px;
  margin-right: 10px;
  margin-bottom: 10px;
  cursor: pointer;
  color: #fff;
  font-size: 1rem;
  font-weight: 500;
  min-width: 50px;
  border-radius: 25px;
  }
</style>
