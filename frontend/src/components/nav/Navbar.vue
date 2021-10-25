<template>
  <div id="navbar">  
    <div class="navigation">
      <div class="logo">
        <a href="#" class="menu-bars" id="show-menu" @click="sideShow">
          <i class="fas fa-bars"></i>
        </a>
        <router-link class="no-underline" :to="{name:'FeedMain'}"><img class="logo-img" :src="logo" alt=""></router-link>
      </div>
      <div></div>
      <SearchBox/>
      <Notification/>
    </div>
    <nav id="nav-menu">
      <ul class="nav-menu-items">
        <div class="nav-toggle" @click="sideShow">
          <a href="#" class="menu-bars" id="hide-menu">
            <i class="fas fa-bars nav-icon"></i>
          </a>
          <router-link class="no-underline" :to="{name:'FeedMain'}"><img class="logo-img" :src="logo" alt=""></router-link>
        </div>
        <div class="nav-section" @click="sideShow">
          <router-link :to="{name:'FeedMain'}" active-class="active" tag="button">
            <li class="nav-text"><span><i width="100px" class="fas fa-home nav-icon"></i>Home</span></li>
          </router-link>
          <router-link :to="{name:'StarMain'}" active-class="active" tag="button">
            <li class="nav-text"><span><i class="fas fa-star nav-icon"></i>인기글</span></li>
          </router-link>
          <router-link :to="{name:'Recipewrite'}" active-class="active" tag="button">
            <li class="nav-text"><span><i class="fas fa-pen nav-icon"></i>레시피 작성</span></li>
          </router-link>
          <router-link :to="{name:'Likeposts'}" active-class="active" tag="button">
            <li class="nav-text"><span><i class="fas fa-heart nav-icon"></i>보관함</span></li>
          </router-link>
          <router-link :to="{name:'Achievelist', params:{user_id: userId}}" active-class="active" tag="button">
            <li class="nav-text"><span><i class="fas fa-medal nav-icon"></i>업적</span></li>
          </router-link>
          <router-link :to="{name:'Rankinglist', params:{user_id: userId}}" active-class="active" tag="button">
            <li class="nav-text"><span><i class="fas fa-trophy nav-icon"></i>순위</span></li>
          </router-link>
          <router-link :to="{name:'Profile',params: {user_id: userId}}" active-class="active" tag="button">
            <li class="nav-text"><span><i class="fas fa-fire nav-icon"></i>프로필</span></li>
          </router-link>
        </div>
        <div class="nav-section nav-logout">
          <button>
            <li class="nav-text" @click="logout"><span><i class="fas fa-sign-out-alt nav-icon"></i>로그아웃</span></li>
          </button>
        </div>
      </ul>
    </nav>
  </div>
</template>

<script src="https://use.fontawesome.com/releases/v5.2.0/js/all.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>

<script>
import SearchBox from './Search.vue'
import Notification from './Notice.vue'
import { mapState } from 'vuex'
import logo from '@/assets/logo.png'
import swal from 'sweetalert';

export default {
  components:{
    SearchBox,
    Notification,
  },
  data: ()=>{
    return{
      isShow: false,
      logo
    }
  },
  mounted(){
    $(".fa-heart").dblclick(function () {
      $(".notification-bubble").show(400);
    });
    $(document).on("scroll", function () {
      if ($(document).scrollTop() > 50) {
        $(".navigation").addClass("shrink");
        $(".logo").addClass("shrink");
        $(".nav-toggle").addClass("shrink");
      } else {
        $(".navigation").removeClass("shrink");
        $(".logo").removeClass("shrink");
        $(".nav-toggle").removeClass("shrink");
      }
    });
  },
  methods: {
    backBack: function() {
      history.back();
    },
    sideShow(event){
      event.preventDefault()
      const sideMenu = document.querySelector('#nav-menu')
      if (this.isShow==false){
        sideMenu.classList.add('active')
        this.isShow = true
      } else {
        sideMenu.classList.remove('active')
        this.isShow = false
      }
    },
    logout(event){
      event.preventDefault()
      swal({title : '로그아웃 되었습니다.', icon:'success'});
      this.$store.dispatch('logout');
      this.$router.push({name:'Login'});
    },
  },
  computed: {
    ...mapState([
      'userId',
    ]),
  }
}
</script>


<style>
@font-face {
    font-family: 'BBTreeGB';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_nine_@1.1/BBTreeGB.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
*{
  font-family: 'BBTreeGB';
}

hr{
  background-color: #ffbe76;
  height: 1px;
  border: none;
}
svg{
  color:#ffbe76;
}
.navigation {
  background-color: #ffffff;
  height: 80px;
  position: fixed;
  width: 100%;
  top: 0;
  left: 0;
  border-bottom: 1px solid #ffbe76;
  display: flex;
  align-items: center;
  justify-content: space-around;
  padding: 0px 50px;
  box-sizing: border-box;
  z-index: 2;
  /* animation magic */
  transition: all 0.4s ease-in-out;
  -webkit-transition: all 0.4s ease-in-out;
  -moz-transition: all 0.4s ease-in-out;
}


.logo{
  width: 230px;
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0%;
  transition: 850ms;
  z-index: 10000;
}

.navigation .logo a {
  position: relative;
  color: #464646;
  font-size: 30px;
  font-family: 'BBTreeGB';
  text-decoration: none;
}

.navigation-search-container {
  position: relative;
}

.navigation-search-container input {
  background-color: #fafafa;
  padding: 3px 20px;
  padding-left: 25px;
  height: 30px;
  box-sizing: border-box;
  border: 1px solid rgba(0, 0, 0, 0.0975);
  border-radius: 3px;
  font-size: 14px;
}

.navigation-search-container .fa-search {
  position: absolute;
  top: 10px;
  left: 10px;
  font-size: 11px;
  color: #ffbe76;
}


.navigation-search-container input:focus {
  outline: none;
}

.navigation-search-container input::placeholder {
  text-align: center;
}

.menu-bars{
  margin-right: 1rem;
  margin-left: 1.5rem;
  font-size: 2rem;
  color: #464646;
}

#nav-menu{
  color: #464646;
  background-color: #fff;
  width: 230px;
  height: 100%;
  display: flex;
  justify-content: center;
  position: fixed;
  top: 0;
  left: -100%;
  transition: 850ms;
  z-index: 10000;
}

#nav-menu.active{
  left: 0;
  transition: 350ms;
}

.nav-text{
  display: flex;
  justify-content: flex-start;
  list-style: none;
  height: 50px;
  padding: 1rem;
}

.nav-text span{
  text-decoration: none;
  color: #464646;
  font-size: 1.2rem;
  margin-left: 1rem;
}
.nav-text:hover {
  background-color: rgba(144, 144, 144, 0.219);
  cursor: pointer;
}

.nav-menu-items{
  padding: 0%;
  margin: 0%;
  width: 100%;
  background-color: #fff;
  border-right: 1px solid rgba(0, 0, 0, 0.05);
}

.nav-icon{
  margin-right: 1rem;
  color: #ffbe76;
}

.nav-toggle{
  width: 100%;
  height: 80px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  border-bottom: 1px solid #ffbe76;
  transition: all 0.4s ease-in-out;
  -webkit-transition: all 0.4s ease-in-out;
  -moz-transition: all 0.4s ease-in-out;
}
.nav-toggle a {
  position: relative;
  color: #000000;
  font-size: 30px;
  font-family: 'BBTreeGB';
  text-decoration: none;
}
.fa-bars{
  color: #ffbe76;
}
.nav-section{
  background-color: #fff;
  /* height: 250px; */
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.nav-logout{
  width: 100%;
  position: absolute;
  bottom: 0%;
}
.menu-items{
  background-color: rgb(255, 255, 255);
  display: flex;
  transform: translate(0%,-50%);
  flex-direction: row;
  justify-content: space-around;
  width: 100%;
}

.shrink {
  height: 50px;
}

.logo-img{
  height: 40px;
  margin: 15% 0%;
  padding: 0%;
}
.nav-toggle svg{
  margin: 0%;
}
@media only screen and (min-width: 800px){
  #sidebar{
    display: none;
  }
  #hide-menu svg{
    color: #ffbe76;
  }
}
@media only screen and (min-width: 320px) and (max-width: 650px) {
  /* Navigation */
  .navigation {
    padding: 0 20px;
    justify-content: space-between;
  }
  .navigation-search-container {
    display: none;
  }
  .notification {
    display: flex;
  }
  .menu-bars{
    margin: 0%;
  }
  .nav-toggle{
    padding-left: 30px;
  }
}
</style>
