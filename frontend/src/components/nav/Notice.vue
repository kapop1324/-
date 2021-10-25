<template>
  <div class="notification">
    <span href="#" id="search-show" @click="searchShow">
      <i class="fa fa-2x fa-search"></i>
    </span>
    <a v-on:click="show = !show" class="tooltip-bell">
      <i class="far fa-2x fa-bell"></i>
      <span id="circle" v-if="notice > 0 && !show"></span>
    </a>
    <div v-if="show" class="Tooltip">
      <div id="heading">
        <div class="heading-left">
          <h5 class="heading-title">알림</h5>
        </div>
        <div class="heading-right">
          <a class="notification-link" href="#" @click="deleteAll">전체 삭제</a>
        </div>
      </div>
      <ul class="notification-list" v-on:click="show = !show">
        <li class="notification-item" v-for="(user,idx) of unreadnotice.slice().reverse()" :key="idx">
          <div class="img-left" @click="searchmember(user.ReqUser)">
            <img class="user-photo" alt="User Photo" v-bind:src="user.img ||defaultProfile" />
          </div>
          <div class="user-content" v-if="user.type === 'comment'" @click="[toRecipe(user.articleID),deleteDoc(user.date)]">
            <p class="user-info"><span class="name">{{user.ReqUser}}</span>님이 댓글을 달았습니다.</p>
            <p class="time">{{user.date | timeFor}}</p>
          </div>
          <div class="user-content" v-if="user.type === 'like'" @click="[toRecipe(user.articleID),deleteDoc(user.date)]">
            <p class="user-info"><span class="name">{{user.ReqUser}}</span>님이 게시글을 좋아합니다.</p>
            <p class="time">{{user.date | timeFor}}</p>
          </div>
          <div class="user-content" v-if="user.type === 'follow'" @click="[deleteDoc(user.date)]">
            <p class="user-info"><span class="name">{{user.ReqUser}}</span>님이 팔로우합니다.</p>
            <p class="time">{{user.date | timeFor}}</p>
          </div>
        </li>
      </ul>
    </div>
    <div id="search">
      <div class="search-top">
        <span href="#" id="back" @click="searchShow">
          <i class="fas fa-2x fa-arrow-left"></i>
        </span>
        <div class="input-box">
          <input class="search-field" type="text" placeholder="검색" v-model="InputText" @keyup="searchInput" @keyup.enter="search">
          <button @click="search" >검색</button>
        </div>
      </div>
        <div class="search-mid">
          <div class="search-container">
            <div class="search-container-box">
              <div class="search-results">
                <ul v-for="(user,idx) in UserList" :key="idx">
                  <li class="user-list" v-if="user.id != userId" @click="[searchShow(),searchmember(user.id)]">
                    <img :src="user.img||defaultProfile">
                    <span class="to-profile">{{user.nickname}}</span>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      <div class="search-bottom">
        <button class="cancle" @click="searchShow">검색창 닫기</button>
      </div>
    </div>
  </div>
</template>


<script src="https://use.fontawesome.com/releases/v5.2.0/js/all.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script>
import firebase from 'firebase';
import FirebaseApi from '../../api/FirebaseApi';
import defaultProfile from "../../assets/images/profile_default.png";
import UserApi from '../../api/UserApi';
import { mapState } from 'vuex'
export default {
  data:()=>{
    return {
      notice:0,
      unreadnotice:[],
      defaultProfile,
      show: false,
      isShow:false,
      defaultProfile,
      InputText:'',
      msg:'',
      UserList:[]
    }
  },
  mounted(){
    this.onNotice()
  },
  watch :{
    getUserId(){
      this.onNotice()
    }
  },
  methods:{
    searchInput(){
      let data = {
        nickname : this.InputText,
      };
      if (this.InputText.length != 0) {
        UserApi.searchByNickname(
          data, 
          res=>{
            this.UserList = res.data.nicknameList
          },
          error=>{
            console.log(error)
          }
        )
      } else {
        this.UserList = []
      }
    },

    searchmember(id){
      this.searchShow
      this.$router.push({ name: 'Profile' , params: {user_id: id}})
    },
    onNotice(){
      const db = firebase.firestore();
      db.collection("notice"+this.$store.state.userId)
        .onSnapshot((doc) => {
          this.unreadnotice = doc.docs.map(v=>{
            return v.data()
          })
          this.notice = this.unreadnotice.length
      });
    },
    deleteAll(event){
      event.preventDefault()
      const db = firebase.firestore();
      db.collection("notice"+this.$store.state.userId)
      .get()
      .then(res => {
        res.forEach(element => {
          element.ref.delete();
        });
      });
    },
    deleteDoc(doc){
      const db = firebase.firestore();
      db.collection("notice"+this.$store.state.userId)
      .get()
      .then(res => {
        res.forEach(element => {
          if (element.id === doc.toString()){
            element.ref.delete();
          }
        });
      });
    },
    searchShow(){
      const Search = document.querySelector('#search')
      if (this.isShow==false){
        Search.classList.add('active')
        this.isShow = true
      } else {
        Search.classList.remove('active')
        this.isShow = false
      }
      this.InputText = ''
      this.UserList = []
    },
    search(){
      const Search = document.querySelector('#search')
      if (this.isShow==false){
        Search.classList.add('active')
        this.isShow = true
      } else {
        Search.classList.remove('active')
        this.isShow = false
      }
      if(this.InputText.substr(0,1) == '#'){
        if(this.InputText.length < 2){
          swal({title:"해시태그를 똑바로 입력해주세요", icon:"warning"})
        }else{
          this.$router.push({name:'Hashtagsearch', query: {hashtag: this.InputText}})
          this.InputText = ''
          this.UserList = []
        }
        
      }else{
          this.$router.push({name:'Allmember', query: {searchname: this.InputText,user_id: this.userId}})
          this.InputText = ''
          this.UserList = []
      }

    
    },
    toRecipe(idx){
      this.$router.push({ name: 'RecipeDetail' , params: {recipe_idx: idx}})
    }
  },
  filters : {
    timeFor : function(created_at){
			const today = new Date();
			const timeValue = new Date(created_at);

			const betweenTime = Math.floor((today.getTime() - timeValue.getTime()) / 1000 / 60);
			if (betweenTime < 1) return '방금전';
			if (betweenTime < 60) {
					return `${betweenTime}분전`;
			}

			const betweenTimeHour = Math.floor(betweenTime / 60);
			if (betweenTimeHour < 24) {
					return `${betweenTimeHour}시간전`;
			}

			const betweenTimeDay = Math.floor(betweenTime / 60 / 24);
			if (betweenTimeDay < 365) {
					return `${betweenTimeDay}일전`;
			}

			return `${Math.floor(betweenTimeDay / 365)}년전`;
		}
  },
   computed: {
    ...mapState([
      'userId',
    ]),
  },
}
</script>

<style scoped>

h2 {
  font-size: 1.6rem;
  font-weight: 400;
  line-height: 2rem;
  color: #1f2022;
}

ul {
  list-style-type: none;
  padding: 0.5rem 1rem;
  margin: 0 0 0.5rem;
}

li {
  display: inline-block;
  padding: 0.85rem 1rem;
  color: #1f2022;
}

h6 {
  font-size: 1rem;
  font-weight: 500;
  line-height: 1.1;
  margin: 0.45rem 0;
}

a {
  color: #4aaee7;
  background: transparent;
  outline: 0;
  text-decoration: none;
  cursor: pointer;
}

svg{
  color: rgba(144, 144, 144, 0.3);
}

.tooltip-bell {
  display: block;
  z-index: 200;
}

.Tooltip {
  position: absolute;
  top: 80px;
  right: 10px;
  line-height: 1.5;
  color: #27303d;
  width: 20rem;
  background: #fff;
  border-radius: 5px;
  -webkit-box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1);
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1);
}


#circle {
  position: absolute;
  top: 0;
  left: 1rem;
  width: 0.75rem;
  height: 0.75rem;
  border-radius: 100%;
  background: #FF9636;
}

.notification-list{
  max-height: 500px;
  overflow: scroll;
  overflow-x: hidden;
}
.notification-item {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: start;
  -ms-flex-align: start;
  align-items: flex-start;
  display: -ms-grid;
  display: grid;
  padding: 0.65rem 0;
  cursor: pointer;
}

#heading {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: start;
  -ms-flex-align: start;
  align-items: flex-start;
  display: -ms-grid;
  display: grid;
  padding: 0.5rem 1rem;
  border-bottom: 0.01rem solid #eee;
}

.notification-link {
  position: absolute;
  margin: 0.4rem 0;
}

.heading-left, .img-left {
  -ms-flex-preferred-size: auto;
  flex-basis: auto;
  -webkit-box-flex: 0;
  -ms-flex-positive: 0;
  flex-grow: 0;
  -ms-flex-negative: 0;
  flex-shrink: 0;
  -ms-grid-row: 1;
  grid-row: 1;
  margin: 0 1rem 0 0;
}

.heading-left, .user-content {
  grid-column: span 9;
  width: 14rem;
}

.heading-right, .img-left {
  grid-column: auto;
}

.heading-right, .user-content {
  -ms-flex-preferred-size: auto;
  flex-basis: auto;
  -webkit-box-flex: 1;
  -ms-flex-positive: 1;
  flex-grow: 1;
  -ms-flex-negative: 1;
  flex-shrink: 1;
  -ms-grid-row: 1;
  grid-row: 1;
}

.heading-right {
  width: 3rem;
}

.img-left {
  width: 3rem;
}

.user-photo {
  display: inline-block;
  vertical-align: middle;
  height: 3rem;
  width: 3rem;
  margin: 0 0.5rem 0 0;
  border-radius: 50%;
  max-width: 100%;
}

p .user-info {
    margin: 0.15rem 0 0;
}
p .time {
  margin: 0;
  color: #9da4ae;
}

span.name {
  font-weight: 500;
}

#search{
  color: #464646;
  background-color: #fff;
  width: 100vw;
  height: 100vh;
  position: fixed;
  display: flex;
  flex-direction: column;
  align-items: center;
  top: -120%;
  left: 0%;
  transition: 350ms;
  z-index: 10000;
  border-bottom: 1px solid #FF9636;
}
#search.active{
  top: 0%;
  transition: 500ms;
}

@media only screen and (min-width: 650px){

  #search-show {
    display: none;
  }
}
.search-top{
  width: 100%;
}
#back svg{
  margin: 5px 10px;
}
.input-box{
  width: 100%;
  padding-left: 5%;
  padding-right: 5%;
}
.input-box input{
  width: 90%;
  border-radius: 3px;
}
.input-box a{
  margin-left: 1%;
  width: 9%;
}
.search-mid{
  width: 100%;
}
.user-list{
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.user-list img{
  width: 50px;
  height: 50px;
  border-radius: 50%;
}
.user-list span{
  padding-right: 20px;
}
.search-bottom{
  width: 100%;
}
.cancle{
  position: absolute;
  bottom: 0%;
  cursor: pointer;
  display: inline;
  background-color: #FF5C4D;
  width: 100%;
  height: 5vh;
  color: #464646;
}

</style>