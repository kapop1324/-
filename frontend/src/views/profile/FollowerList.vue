<template>
  <div class="follower-page"> 
    <h1> 팔로워</h1>
    <hr>
    <div class="followers">
      <FollowItem v-for="(follower, idx) in followers" :key="idx" :follower="follower" :idx="idx" :followers="followers" :profileId="profileId"/>
    </div>
  </div>
</template>
<script>
import UserApi from '../../api/UserApi';
import FollowItem from "../../components/profile/FollowItem.vue";

export default {
  data: () => {
    return {
      profileId: null,
      followers: null,
    }
  },
  components: { FollowItem },
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
    UserApi.getFollower(
      data,
      res => {
        this.followers = res.data
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