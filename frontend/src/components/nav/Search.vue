<template>
  <div class="navigation-search-container">
    <i class="fa fa-search"></i>
    <input class="search-field" type="text" placeholder="Search" v-model="InputText" @keyup="searchInput" @keyup.enter="search">
    <div v-if="InputText" class="search-container">
      <div class="search-container-box">
        <div class="search-results">
          <ul v-for="(user,idx) in UserList" :key="idx">
            <li class="user-list" v-if="user.id != userId" @click="searchmember(user.id)">
              <img :src="user.img||defaultProfile" alt="최고">
              <span class="to-profile">{{user.nickname}}</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script src="https://use.fontawesome.com/releases/v5.2.0/js/all.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>


<script>
import UserApi from '../../api/UserApi';
import defaultProfile from "../../assets/images/profile_default.png";
import { mapState } from 'vuex'
import swal from 'sweetalert';

export default {
  data:()=>{
    return {
      defaultProfile,
      InputText:'',
      msg:'',
      UserList:[]
    }
  },
  computed:{
    userId(){
      return this.$store.state.userId
    }
  },
  mounted(){
    this.UserList=[]
    $(document).on("scroll", function () {
      if ($(document).scrollTop() > 50) {
        $(".search-container").addClass("shrink");
        $(".user-list").addClass("ul-scroll");
        $(".to-profile").addClass("scb-scroll");

      } else {
        $(".search-container").removeClass("shrink");
        $(".user-list").removeClass("ul-scroll");
        $(".to-profile").removeClass("scb-scroll");
      }
    });
  },
  created(){
    this.UserList=[]
  },
  methods:{
    searchInput(){
      let data = {
        nickname : this.InputText,
      };


      if (this.InputText.length != 0) {
        this.UserList=[]
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
    search(){
      this.UserList=[]
      if(this.InputText.substr(0,1) == '#'){
        if(this.InputText.length < 2){
          swal({title:"해시태그를 똑바로 입력해주세요", icon:"warning"})
        }else{
          this.$router.push({name:'Hashtagsearch', query: {hashtag: this.InputText}})
          this.InputText = ''
          this.UserList=[]
        }
        
      }else{
        this.$router.push({name:'Allmember', query: {searchname: this.InputText,user_id: this.userId}})
        this.InputText = ''
        this.UserList=[]
      }
    },
    searchmember(id){
      this.$router.push({ name: 'Profile' , params: {user_id: id}})
      this.InputText = ''
      this.UserList = []
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
.search-container{
  position: absolute;
  top: 53px;
  left: -50px;
  width: 300px;
  z-index: 300;
  transition: all 0.4s ease-in-out;
  -webkit-transition: all 0.4s ease-in-out;
  -moz-transition: all 0.4s ease-in-out;
}

.shrink {
  top: 39px;
  
}

.search-container-box{
  border-radius: 0 0 6px 6px;
  -webkit-box-shadow: 0 2px 3px 0 rgb(0 1 3 / 7%);
  box-shadow: 0 2px 3px 0 rgb(0 1 3 / 7%);
  letter-spacing: -.25px;
  line-height: 1.8rem;
  background-color: #fff;
}

.search-results ul{
  margin: 0%;
  padding: 0px 0px;
}

.user-list{
  display: flex;
  justify-content: space-between;
  padding: 10px 15px;
  margin: 0px;
  transition: all 0.4s ease-in-out;
  -webkit-transition: all 0.4s ease-in-out;
  -moz-transition: all 0.4s ease-in-out;
}
.user-list:hover {
  background-color: #ffbe76;
  border-radius: 5px;
}

.user-list img{
  height: 30px;
  width: 30px;
  object-fit: cover;
  border-radius: 50px;
}

.ul-scroll{
  padding: 5px 10px;
}

.to-profile{
  font-size: 18px;
  transition: all 0.4s ease-in-out;
  -webkit-transition: all 0.4s ease-in-out;
  -moz-transition: all 0.4s ease-in-out;
}
.scb-scroll{
  font-size: 15px;
}
</style>