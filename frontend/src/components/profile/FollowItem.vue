<template>
  <li >
    <div class="user">
      <div class="user-info" @click="moveTo">
        <div class="user-img">
          <img :src="follower.img||defaultProfile" alt="ddd">
        </div>
        <div class="user-des">
          <div class="user-id">
            {{follower.id}}
          </div>
          <div class="user-nickname">
            {{follower.nickname}}
          </div>
        </div>
      </div>
      <div v-if="userId == profileId" class="delete">
        <button @click="deleteFollower(follower, idx)"> remove </button>
      </div>
    </div>
  </li>
</template>

<script>
import defaultProfile from "../../assets/images/profile_default.png";
import UserApi from '../../api/UserApi';
import { mapState } from 'vuex'

export default {
  data: () => {
    return { defaultProfile };
  },
  props: {
    follower: {
      type: [Array, Object],
    },
    idx: Number,
    followers: {
      type: [Array, Object],
    },
    profileId: String
  },

  methods: {
    deleteFollower: function(follower, idx) {
      const newFollowers = this.followers
      let data = {
        loginId : this.profileId,
        followerId : follower.id
      }
      UserApi.deleteFollower(
      data,
      res => {
        this.$router.go();
      },
      error=>{
        console.log(error)
      }
      )
      this.followers = newFollowers
    },
    moveTo: function(){
      this.$router.push({name:'Profile', params: {user_id:this.follower.id}})
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
li{
  display: flex;
  padding: 8px 16px;
}
.user{
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.user-info{
  align-items: center;
  display: flex;
  flex: 1 1 100px;
  flex-direction: row;
  overflow: hidden;
}
.user-img{
  align-items: center;
  align-self: center;
  display: block;
  flex: none;
  justify-content: center;
  width: 40px;
  height: 40px;
  background-color: gray;
  margin: 4px;
  margin-right: 13px;
  border-radius: 50%;
}
.user-img img{
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}
.user-des{
  flex: 1 1 70px;
  justify-content: center;
  min-width: 70px;
}
.user-id{
  align-content: center;
  display: flex;
  flex-direction: row;
}
.user-nickname{
  color: rgba(var(--f52,142,142,142),1);
  display: block;
  font-size: 14px;
  font-weight: 400;
  line-height: 18px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.delete{
  display: flex;
  align-content: center;
  flex: 0 0 auto;
  justify-content: center;
  margin-left: 15px;
}
.delete button{
  appearance: none;
  background: 0 0;
  border: 0;
  box-sizing: border-box;
  cursor: pointer;
  display: block;
  font-weight: 600;
  padding: 0px 9px;
  text-align: center;
  text-transform: inherit;
  text-overflow: ellipsis;
  width: auto;
  appearance: auto;
  text-rendering: auto;
  letter-spacing: normal;
  word-spacing: normal;
  text-indent: 0px;
  text-align: center;
  align-items: flex-start;
  font: 400 13.3333px Arial;
  border: 1px solid rgba(var(--ca6,219,219,219),1);
  border-radius: 4px;
}
</style>
