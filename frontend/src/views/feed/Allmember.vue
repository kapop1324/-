<template>
  <div class="feed newsfeed">
    <div class="wrapC">
      
      <h1>유저 목록</h1>
      <li   v-for="(member, idx) in members" :key="idx">
        <div v-if="member " class="user">
          <div class="user-info">
            <div class="user-img">
                <router-link :to="{name:'Profile', params: {user_id: member.id}}" style="text-decoration:none; color:black;" > <img :src="member.img||defaultProfile" alt="ddd"></router-link>
             
            </div>
            <div class="user-des">
              <div class="user-id">
                <router-link :to="{name:'Profile', params: {user_id: member.id}}" style="text-decoration:none; color:black;" >{{member.id}}</router-link>
              </div>
              <div class="user-nickname">
                {{member.nickname}}
              </div>
            </div>
          </div>
          <div class="delete">
            <button  v-if="follow_already.includes(member.id)" @click="senddeletefollow(member)">팔로잉</button>
            <button  v-if="!follow_already.includes(member.id)" @click="sendrequest(member)">팔로우</button>
          </div>
        </div>
      </li>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import UserApi from '../../api/UserApi';
import FirebaseApi from '../../api/FirebaseApi';
import "../../components/css/feed/feed-item.scss";
import "../../components/css/feed/newsfeed.scss";
import swal from 'sweetalert';
import defaultProfile from "../../assets/images/profile_default.png";
export default {
  props: ["keyword"],

  data: ()=>{
    return{
      defaultProfile,
      profileId: null,
      members: {
      type: [Array, Object],
      
    },
  
     member: {
      
    },
    
    searchnickname:'',
    follow_wait:[],
    follow_already:[],
    }
    
  },

  components: {  },

  computed: {
    ...mapState([
      'userId',
    ]),
  },
  methods: {
    sendrequest(member){
    
      let data = {
        loginid : this.$route.query.user_id,
        memberid : member.id,
        token : this.$store.state.token,
        
      }
      UserApi.sendfollowrequest(
        data,
        res => {
          if(res.data == "success"){
            let notice = {
              user:member.id,
              img:this.$store.state.userImg,
              ReqUser:this.$store.state.userId,
              type:'follow',
              articleID:0
            }
            FirebaseApi.noticeAdd(notice)
            this.follow_already.push(member.id)
            // this.$router.go();
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
          swal("팔로우를 취소했습니다..",{icon:'success'})
          for(let i = 0; i < this.follow_already.length; i++) {
            if(this.follow_already[i] === data.memberid)  {
              this.follow_already.splice(i, 1);
              i--;
            }
          }
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
  },

  created() {
    this.profileId = this.$route.query.user_id
    this.searchnickname = this.$route.query.searchname
    if (this.searchnickname){
      UserApi.searchByNickname(
        {nickname:this.searchnickname}, 
        res=>{
          this.members = res.data.nicknameList
        },
        error=>{
          console.log(error)
        }
      )
      UserApi.follow_already(
        {id: this.profileId},
        res => {
          this.follow_already = res.data;
              
       
        },
        error=>{
          console.log(error)
        }
      )
    }
    else {
      let data = {
        id: this.profileId
      }
  
  
  
      UserApi.getAllmember(
        
        data,
        res => {
          this.members = res.data
          
        },
        error=>{
          console.log(error)
        }
      )
      UserApi.follow_already(
        data,
        res => {
          this.follow_already = res.data;
              
       
        },
        error=>{
          console.log(error)
        }
      )
    }
  },
};
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
