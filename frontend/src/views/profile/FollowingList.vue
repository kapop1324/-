<template>
  <div class="follower-page"> 
    <h1>팔로잉</h1>
    <hr>
    <div class="followers">
      <FollowingItem v-for="(following, idx) in followings" :key="idx" :idx="idx" :following="following" :followings="followings" :profileId="profileId"/>
    </div>
  </div>
</template>
<script>
import UserApi from '../../api/UserApi';
import FollowingItem from "../../components/profile/FollowingItem.vue";

export default {
  data: () => {
    return {
      profileId: null,
      followings: null,
    }
  },
  components: { FollowingItem },
  methods: {
    backBack: function() {
      history.back();
    }
  },
  created: function () {
    this.profileId = this.$route.query.profileId
    let data = {
      id: this.profileId
    }
    UserApi.getFollowing(
      data,
      res => {
        this.followings = res.data
      },
      error=>{
        console.log(error)
      }
    )
  }
}
</script>

<style scoped>
.follower-page{
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.followers{
  width: 100%;
  max-width: 616px;
}
</style>