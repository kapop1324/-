<template>
  <div id="Sidebar">
    <div class="nav">
      <a href="#" class="menu-bars" id="show-menu" @click="sideShow">
        <i class="fas fa-bars"></i>
      </a>
    </div>
    <nav id="nav-menu">
      <ul class="nav-menu-items">
        <div id="nav-toggle">
          <a href="#" class="menu-bars" id="hide-menu" @click="sideShow">
            <i class="fas fa-bars nav-icon"></i>
          </a>
        </div>
        <div class="nav-section">
          <li class="nav-text"><a href="#"><i class="fas fa-home nav-icon"></i>Home</a></li>
          <li class="nav-text"><a href="#"><i class="fas fa-fire nav-icon"></i>Trending</a></li>
          <li class="nav-text"><a href="#"><i class="fab fa-youtube nav-icon"></i>Subscription</a></li>
        </div>
        <hr>
        <div class="nav-section">
          <li class="nav-text"><a href="#"><i class="fas fa-play nav-icon"></i>Lib</a></li>
          <li class="nav-text"><a href="#"><i class="fas fa-history nav-icon"></i>His</a></li>
        </div>
      </ul>
    </nav>
    <!-- <nav id="sidebar">
      <div class="menu-items">
        <router-link :to="{name:'FeedMain'}" active-class="active" tag="button">
          <div class="link-container">
            Home
          </div>
        </router-link>
        <router-link :to="{name:'FeedMain'}" active-class="active" tag="button">
          <div class="link-container">
            Star
          </div>
        </router-link>
        <router-link :to="{name:'Recipewrite'}" active-class="active" tag="button">
          <div class="link-container">
            Write
          </div>
        </router-link>
        <router-link :to="{name:'FeedMain'}" active-class="active" tag="button">
          <div class="link-container">
            Likes
          </div>
        </router-link>
        <router-link :to="{name:'Profile',params: {user_id: userId}}" active-class="active" tag="button">
          <div class="link-container">
            Profile
          </div>
        </router-link>
      </div>
    </nav> -->
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  data: ()=>{
    return{
      isShow: false,
    }
  },
  computed: {
    ...mapState([
      'userId',
    ]),
  },
  methods : {
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
    }
  }
  
}
</script>

<style scoped>
*{
  box-sizing: border-box;
  margin: 0%;
  padding: 0%;
}

hr{
  background-color: #464646;
  height: 1px;
  border: none;
}

#Sidebar{
  position: absolute;
  z-index: 10000;
}

.nav{
  height: 80px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
.menu-bars{
  margin-left: 2rem;
  font-size: 2rem;
  color: #464646;
}
#nav-menu{
  color: #464646;
  background-color: #fff;
  width: 250px;
  height: 100%;
  display: flex;
  justify-content: center;
  position: fixed;
  top: 0;
  left: -100%;
  transition: 850ms;
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
.nav-text a{
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
  width: 100%;
  background-color: #fff;
}
.nav-icon{
  margin-right: 1rem;
  color: #909090;
}
#nav-toggle{
  width: 100%;
  height: 80px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  border-bottom: 1px solid rgba(0, 0, 0, 0.0975);
}
#nav-toggle a {
  text-decoration: none;
}
.fa-bars{
  color: #464646;
}
.nav-section{
  background-color: #fff;
  height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

#sidebar{
  transform: translate(0%,-50%);
  position: fixed;
  top: 50%;
}
.menu-items{
  display: flex;
  flex-direction: column;
  background-color: rgb(255, 255, 255);
}
.link-container{
  background-color: rgb(255, 255, 255);
  line-height: 50px;
  height: 50px;
  width: 50px;
}

.shrink {
  height: 50px;
}

@media only screen and (min-width: 320px) and (max-width: 650px) {
  /* Navigation */
  #menu-bars{
    position: fixed;
    display: none;
  }
  #sidebar{
    transform: translate(-50%,-50%);
    position: fixed;
    top: 100%;
    left: 50%;
    width: 100%;
    z-index: 10000;
  }
  .menu-items{
    background-color: rgb(255, 255, 255);
    border: 1px solid rgba(0, 0, 0, 0.0975);
    border-radius: 3px;
    display: flex;
    transform: translate(0%,-50%);
    flex-direction: row;
    justify-content: space-around;
    width: 100%;
  }
}
</style>