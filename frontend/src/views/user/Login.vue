
<template>
  
    <div class="wrapC">
      <div class="row login-page" id="login">
      <div class="row" id="login_box1">
        <img style="width:150px; height:130px;float: none; margin:0 auto;" :src="require(`@/assets/images/mainlogo.png`)" />
      </div>
      <div class="row" id="login_box7">
        <h1>요리조리</h1>
      </div>
      <div class="row" id="login_box2">
        <input v-model="id" @keyup.enter="onLogin" id="id" placeholder="아이디를 입력하세요."  type="text"/>
      </div>
      <div class="row" id="login_box3">
        <input v-model="password" type="password" v-bind:class="{error : error.password, complete:!error.password&&password.length!==0}" id="password"  @keyup.enter="onLogin"  placeholder="비밀번호 8자리 이상 입력하세요."/>
      </div>
      <div class="row" id="login_box4">
          <button
        class="btn btn--back btn--login"
        @click="onLogin"
        :disabled="!isSubmit"
        :class="{disabled : !isSubmit}"
      >로그인</button>

      </div>
      <div class="row" id="login_box5">
        <div class="row" style="border-bottom:1px solid lightgray; margin-left:0px;">
        </div>
        <div class="row" style=" margin-top: 30px;">
          <kakaoLogin :component="component" />
        </div>
      </div>
      <div class="row" id="login_box6">
        <div class="row">
         
          <router-link to="/user/joinselect" id="routerlink" style="margin-top:0px;" class="btn--text">아직 회원이 아니신가요?</router-link>
        
        </div>
        <div class="row">
         
          <router-link to="/user/findpw"  id="routerlink" style="margin-left:10px; margin-top:12px;" class="btn--text">비밀번호를 잊어버리셨나요?</router-link>
          
        </div>
      </div>
    
    </div>
  </div>
</template>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript" src="/resource/js/bootstrap.js"></script>

<script>
import "../../components/css/user.scss";
import PV from "password-validator";
import KakaoLogin from "../../components/user/snsLogin/Kakao.vue";
import UserApi from "../../api/UserApi";
import { mapState } from 'vuex'
import { mapActions } from 'vuex'
import swal from 'sweetalert';



export default {
  components: {
    KakaoLogin, 
  },
  created() {
    this.component = this;

    this.passwordSchema
      .is()
      .min(8)
      .is()
      .max(100)
      .has()
      .digits()
      .has()
      .letters();

    // 토큰이 있는 경우(=로그인 된 사용자) 메인페이지로 redirect
    if (this.$store.state.token){
      this.$router.push({name:'FeedMain'})
    }
  },
  watch: {
    password: function(v) {
      this.checkForm();
    },
  },
  methods: {
    checkForm() {

      if (
        this.password.length >= 0 &&
        !this.passwordSchema.validate(this.password)
      )
      this.error.password = "영문,숫자 포함 8 자리이상이어야 합니다.";
      else this.error.password = false;

      let isSubmit = true;
      Object.values(this.error).map(v => {
        if (v) isSubmit = false;
      });
      this.isSubmit = isSubmit;
    },
    onLogin() {
      if (this.isSubmit) {
      
        let data = {
          id : this.id,
          pw : this.password
        };

       UserApi.requestLogin(
          data,
          res => {

           
             if(res.data.result == "success"){
            swal({title:"로그인 되었습니다.",icon:'success'});
           this.$store.dispatch("login",res);

              this.$store.state.userId = data.id
              
              this.$router.push({name:'FeedMain'});
            }else if(res.data.result == "fail"){
              swal({title:"로그인 실패.",icon:'error'});
          
            }else{
              swal("로그인 실패","아이디가 존재하지 않거나 비밀번호가 틀렸습니다.",'error');

            }
          },
          error => {
            //요청이 끝나면 버튼 활성화
            swal("로그인 실패","아이디가 존재하지 않거나 비밀번호가 틀렸습니다.",'error');
          }
        );
      }
    }
  },
  computed: {
    ...mapState([
      'userId',
    ]),
  },
  data: () => {
    return {
      id: "",
      password: "",
      passwordSchema: new PV(),
      error: {
        passowrd: false
      },
      isSubmit: false,
      component: this
    };
  }
};
</script>



<style>
.login-page{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

#login{
  margin-top: 0px;
  
  padding:50px;
}

input {
  border : 1px solid darkgray;
}

#login_box2,#login_box3,#login_box4,#login_box5,#login_box6{
  margin-top: 20px;
}

#login_box4{
  margin-bottom: 50px;
  
}

#login_box4 button{
  background-color: #ffbe76;
  box-shadow: none;
  color: white;
}

#login_box7{
  margin-top: 30px;
  text-align: center;
  margin-bottom: 30px;
}

#login_box6{
  text-align: center;
}



#login_box5 img{
  width:30px;
  height: 30px;
  margin: 0px auto;
}

#routerlink {
 text-decoration:none; 
 color: gray;
 font-weight: 500;
}


</style>

