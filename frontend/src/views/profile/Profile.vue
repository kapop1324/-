<template>
  <div class="profile-page" v-if="profileUser">
    <div class="profile" v-if="profileUser">
      <div class="profile-image">
        <img :src="profileUser.img||defaultProfile" alt="">
      </div>
      <div class="profile-user-settings">
        <h1 class="profile-user-name">{{ profileUser.nickname }}</h1>
        <div v-if="profileUser.id == userId">
          <router-link style="text-decoration:none; border:none; color:black;" to="/user/update" class="profile-edit-btn">회원정보</router-link>/
          <router-link style="text-decoration:none; border:none; color:black;" to="/user/changepassword" class="profile-edit-btn">비밀번호</router-link>
        </div>
        <div v-else>
          <button class="profile-edit-btn" v-if="follow_already.includes(profileUser.id) " @click="senddeletefollow(profileUser)">팔로잉</button>
          <button class="profile-edit-btn request" v-if="!follow_already.includes(profileUser.id)  " @click="sendrequest(profileUser)">팔로우</button>
        </div>
      </div>
      <div class="profile-stats">
        <ul>
          <li><span class="profile-stat-count">{{profileUser.recipecnt}}</span> 레시피</li>
          <li @click="showFollowerList"><span class="profile-stat-count">{{ profileUser.follower }}</span> 팔로워</li>
          <li @click="showFollowingList"><span class="profile-stat-count">{{ profileUser.following }}</span> 팔로잉</li>
          <li><span class="profile-stat-count">{{mypoint}}</span> RP</li>
          <li><span class="profile-stat-count">{{myrank}}</span> 위</li>
        </ul>
      </div>
      <div class="profile-bio">
        <p><span class="profile-real-name">소개 : </span> {{profileUser.des}}</p>
      </div>
    </div>
    <div class="achieve-section" style="margin-top:30px;">
      <div class="achieve-title">
        <h4 style="margin-bottom:10px;"><i class="fas fa-medal"></i>칭호</h4>
      </div>
      <div class="achieve-list" style="margin-bottom:100px;">
        <span v-for="(clear, idx) in achieve" :key="idx" style="width:100px; height:100px;">
          <img id="achieve_img" v-if="clear.percent == 100" :title="`${clear.title}%`" :src="require(`@/assets/images/${clear.clear_img}.png`)" />
          <img id="achieve_img" v-else :title="`${clear.title} ${clear.percent}%`" :src="require(`@/assets/images/${clear.fail_img}.png`)" />
        </span>
      </div>
    </div>
    <div class="article-sections">
      <div class="article-title" >
        <h4 style="margin-bottom:10px;"><i class="fas fa-pen"></i>요리책</h4>
      </div>
      <div class="article-list">
        <table class="table">
          <thead>
            <th>no</th>
            <th>제목</th>
            <th>분류</th>
            <th>좋아요</th>
          </thead>
          <tbody>
            <my-recipe-item v-for="(myRecipe, idx) in myRecipes" :key="idx" :myRecipe="myRecipe" :idx="idx"></my-recipe-item>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import UserApi from '../../api/UserApi';
import RankApi from '../../api/RankApi';
import AchieveApi from '../../api/AchieveApi';
import MyRecipeItem from '../../components/profile/MyRecipeItem.vue';
import FirebaseApi from '../../api/FirebaseApi';
import swal from 'sweetalert';
import defaultProfile from "../../assets/images/profile_default.png";

export default {
  components: { MyRecipeItem },
  data: () => {
    return {
      defaultProfile,
      profileUser: null,
      profileId: null,
      follower: null,
      following: null,
      myRecipes: null,
      waiting: null,
      follow_wait:[],
      follow_already:[],
      rankpoint:null,
      achieve_cnt:null,
      follower_cnt:null,
      recipe_cnt:null,
      recipe_comment_cnt:null,
      recipe_comment_like_cnt:null,
      recipe_like_cnt:null,
      achieve:[],
       ranking:[],
        myrank:null,
        mypoint:null,
        mynickname:null,
    }
  },
  created() {
    this.profileId = this.$route.params.user_id
    
    let data = {
      id: this.profileId
    }
    
    UserApi.getUser(
      data,
      res => {
        this.profileUser = res.data
        this.follower = res.data.follower
        this.following = res.data.following
      },
      error=>{
        console.log(error)
      }
    )

    RankApi.getRanking(
        res => {
            this.ranking = res.data.rankinglist
            for(let i = 0; i < this.ranking.length; i++){
                if(this.ranking[i].id == this.profileId){
            this.myrank = this.ranking[i].rank
            this.mypoint = this.ranking[i].rankpoint
            this.mynickname = this.ranking[i].nickname
            
        }
    }
        },
        error=>{
        console.log(error)
        }
    )


    UserApi.myAllRecipes(
      data,
      res => {
        this.myRecipes = res.data.latestFeed 
      },
      error=>{
        console.log(error)
      }
    )
    data = {
      id: this.userId
    }

    UserApi.follow_already(
      data,
      res => {
        this.follow_already = res.data;
      },
      error=>{
        console.log(error)
      }
    )

    data = {
      id: this.profileId
    }
    
      AchieveApi.getAchievecurrent(
      data,
      res => {
        if(res.data.message == "SUCCESS"){
          this.achieve = res.data.achieve;
        }else if(res.data.message == "error"){
          swal("에러발생");
        }
      },
      error=>{
        console.log(error);
      }
    )

   
    

  

  },
  methods: {
    sendrequest(member){
      let notice = {
        user:this.$route.params.user_id,
        img:this.$store.state.userImg,
        ReqUser:this.$store.state.userId,
        type:'follow',
        articleID:0
      }
      FirebaseApi.noticeAdd(notice)

      let data = {
        loginid : this.userId,
        memberid : member.id,
        token : this.$store.state.token,
      }
      UserApi.sendfollowrequest(
      data,
      res => {
        if(res.data == "success"){
          this.follow_already.push(this.profileUser.id)
          this.profileUser.follower ++
        }else if(res.data == "fail"){
          swal("팔로우 신청이 보내지지 않았습니다.",{icon:'warning'})
        }else{
          swal("에러발생",{icon:'error'});
        }
        },
        error=>{
          swal("에러발생",{icon:'error'});
        }
      )

    
    },

    senddeletefollow(member){

      let data = {
        loginid : this.userId,
        memberid : member.id,
        token : this.$store.state.token,
        
      }
      UserApi.sendfollowdelete(
      data,
      res => {
        if(res.data == "success"){
          swal("팔로우를 취소했습니다.",{icon:'success'})
          for(let i = 0; i < this.follow_already.length; i++) {
            if(this.follow_already[i] === this.profileUser.id)  {
              this.follow_already.splice(i, 1);
              i--;
            }
          }
          this.profileUser.follower --
        }else if(res.data == "fail"){
          swal("팔로우 취소신청이 보내지지 않았습니다.",{icon:'warning'})
        }else{
          swal("에러발생",{icon:'error'});
        }
      },
      error=>{
        swal("에러발생",{icon:'error'});
      }
    )
    },

    updaterank(){
      let data = {
        id : this.userId,
        rankpoint : this.rankpoint
      }

      RankApi.Updatemyrank(
        data,
        res => {
          if(res.data == "success"){
            swal("갱신되었습니다.")
            this.$router.go();
          }else if(res.data == "fail"){
            swal("갱신에 실패하였습니다.")
          }else{
            swal("에러발생");
          }
        },
        error=>{
          swal("에러발생");
        }
      )
    },

    showFollowerList: function() {
      this.$router.push({ name: 'FollowerList' , query: {profileId: this.profileId}})
    },
    showFollowingList: function() {
      this.$router.push({ name: 'FollowingList' , query: {profileId: this.profileId}})
    },
  },
  computed: {
    ...mapState([
      'userId',
    ]),
  }
}
</script>

<style scoped>
svg{
  margin: 3px;
}

*{
  font-family: 'BBTreeGB';
  font-weight: 100;
}
.profile-page{
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  margin: 0%;
  padding: 0%;
}

.profile {
  padding: 0;
  width: 100%;
  max-width: 616px;
}

.profile-image {
  width: 100%;
  height: 152px;
  height: width;
  display: flex;
  justify-content: center;
  margin-right: 3rem;
  overflow: hidden;
  border: none;
}

.profile-image img {
  padding: 0%;
  margin: 0%;
  width: 152px;
  height: 152px;
  border-radius: 50%;
  background-color: #fafafa;
  object-fit: cover;
  border: none;

}

.profile-user-settings {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.profile-user-name {
    display: inline-block;
    font-weight: 300;
}

.profile-edit-btn {
    border: 0.1rem solid #dbdbdb;
    border-radius: 3px;
    margin-left: 5px;
    padding: 0px 3px;
}

.profile-settings-btn {
    margin-left: 1rem;
}

.profile-stats {
  border-top: 1px solid;
  border-bottom: 1px solid;
  display: flex;
  margin-top: 2.3rem;
  padding: 3% 0%;
}
.profile-stats ul {
  width: 100%;
  padding: 0% 5%;
  margin: 0%;
  display: flex;
  justify-content: space-between;
}

.profile-stats li {
    display: inline-block;
    line-height: 1.5;
    margin-right: 4rem;
    cursor: pointer;
    font-size: 1.2rem;
}
@media (max-width: 555px) {
  .profile-stats ul {
    width: 100%;
    padding: 0%;
    display: flex;
    flex-direction: row;
    justify-content: space-around;
  }
  .profile-stats li{
    margin-right: 1rem;
  }
}
.profile-stats li:last-of-type {
    margin-right: 0;
}

.profile-bio {
  width: 100%;
    font-weight: 400;
    line-height: 1.5;
    margin-top: 2.3rem;
}

.profile-real-name,
.profile-stat-count{
    font-weight: 600;
}
.achieve-section{
  width: 100%;
  display: flex;
  flex-direction: column;
  max-width: 616px;
}
.achieve-list{
  display: flex;
  flex-wrap: wrap;
}

#achieve_img{
  width:100px;
  height: 100px;
}
.article-sections{
  width: 100%;
  max-width: 616px;
}
.request{
  background-color: #ffbe76;
  color: #fafafa;
}

table thead{
  text-align: center;
  padding-left: 5px;
  padding-right: 5px;
}

</style>
