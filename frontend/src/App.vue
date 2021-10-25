<template>
  <div id="app">
    <Navbar v-if="userId"/>
    <div class="main">
      <router-view :key="$route.fullPath"></router-view>
  </div>
  </div>
</template>


<script>
import "./components/css/style.scss";
import { mapState } from 'vuex'
import Navbar from '@/components/nav/Navbar.vue'
// import Sidebar from '@/components/sidebar/Sidebar.vue'

export default {
  components:{
    Navbar,
    // Sidebar
  },
  computed: {
    ...mapState([
      'userId',
    ]),
  },
  name: "app"
};
</script>
<style scoped>
.main{
  position: relative;
  top: 100px;
}
</style>
