<template>
  <div class="wrapC" id="join">
    <div class="row" id="join_box0">
       <img :src="require(`@/assets/images/roundlogo.png`)" />
    </div>
    <div class="row" id="join_box1">
      <h1>가입하기</h1>
    </div>
    <div class="row" id="join_box2">
      <div class="row">
        <input class="col-md-10" v-model="id" id="id" placeholder="아이디를 입력하세요." type="text"/>
        <div class="col-md-2 p-0 d-flex justify-content-end" >
          <button class="btn d-flex align-items-center" style="font-size:14px; margin-left: 0px;" @click="checkid">중복체크</button>
        </div>
        
      </div>
      <div class="row">
        <div class="error-text" v-if="error.id"></div>
      </div>
    </div>
      <div class="row" id="join_box3">
      <div class="row">
        <input class="col-md-10" v-model="nickname" id="nickname" placeholder="닉네임을 입력하세요." type="text" />  
        <div class="col-md-2 p-0 d-flex justify-content-end" >
          <button class="btn d-flex align-items-center" style="font-size:14px; margin-left: 0px;" @click="checkid">중복체크</button>
        </div>
      
      </div>
      <div class="row">
        <div class="error-text" v-if="error.nickname"></div>
      </div>
    </div>
    <div class="row" id="join_box4">
      <div class="row">
        <input v-model="email" 
        v-bind:class="{error : error.email, complete:!error.email&&email.length!==0}"
        id="email" 
        placeholder="이메일을 입력하세요." 
        type="text" 
        />
      </div>
      <div class="row">
        <div class="error-text" v-if="error.email">{{error.email}}</div>
      </div>
    </div>

    <div class="row" id="join_box4">
      <div class="row">
        <input 
        v-model="password" 
        id="password" 
        :type="passwordType" 
        v-bind:class="{error : error.password, complete:!error.password&&password.length!==0}"
        placeholder="비밀번호를 입력하세요." />
      </div>
      <div class="row">
        <div class="error-text" v-if="error.password">{{error.password}}</div>
      </div>
    </div>

    <div class="row" id="join_box5">
      <div class="row">
        <input
          v-model="passwordConfirm"
          :type="passwordConfirmType"
          v-bind:class="{error : error.password, complete:!error.password&&password.length!==0}"
          id="password-confirm"
          placeholder="비밀번호를 다시한번 입력하세요."
        />
      </div>
      <div class="row">
        <div class="error-text" v-if="error.passwordConfirm">{{error.passwordConfirm}}</div>
      </div>
        <div class="row" id="join_box6">
        <button class="button"
          @click="onJoin"
          :disabled="!isSubmit"
          :class="{disabled : !isSubmit}"
          >가입하기</button>
    </div>
    </div>
  
  </div>
</template>

<script>
import UserApi from '../../api/UserApi';
import PV from "password-validator";
import * as EmailValidator from "email-validator";
import swal from 'sweetalert';


export default {
 
  created(){
    this.passwordSchema
      .is()
      .min(8)
      .is()
      .max(100)
      .has()
      .digits()
      .has()
      .letters();    
  },
  data: () => {
    return {
      id:"",
      email: "",
      password: "",
      passwordConfirm: "",
      passwordSchema: new PV(),
      nickname: "",
      isTerm: false,
      isLoading: false,
      error: {
        email: false,
        password: false,
        nickname: false,
        passwordConfirm: false,
        term: false
      },
      isSubmit: false,
      passwordType: "password",
      passwordConfirmType: "password",
      termPopup: false
    };
  },
  watch:{ 

    password: function(v) {
      this.checkForm();
    },
    email: function(v) {
      this.checkForm();
      this.lowercase();
    },
    passwordConfirm:function(v) {
      this.checkForm();
    }
  },
  methods:{
    lowercase(){
      this.email= this.email.toLowerCase();
    },

    checkid(){

         let data = {
        id:this.id,
     
      };
         
        UserApi.checkid(
        data,
        res=>{
          if(res.data == "success"){
            swal("사용가능합니다.",{icon:'success'});
            this.error.id = false;
          }else if(res.data == "fail"){
            swal("중복된 아이디입니다.",{icon:'warning'});
            this.error.id = true;
          }else{
            swal("에러가 발생하였습니다.",{icon:'error'});
            this.error.id = true;
          }
        },
        error=>{
          swal("에러가 발생하였습니다.",{icon:'error'});
          this.error.id = true;
          
        }
      );
    },

    checknickname(){

         let data = {
        nickname:this.nickname,
     
      };
         
        UserApi.checknickname(
        data,
        res=>{
          if(res.data == "success"){
            swal("사용가능합니다.",{icon:'success'});
            this.error.nickname = false;
          }else if(res.data == "fail"){
            swal("중복된 닉네임입니다.",{icon:'warning'});
            this.error.nickname = true;
          }else{
            swal("에러가 발생하였습니다.",{icon:'error'});
            this.error.nickname = true;
          }
        },
        error=>{
          swal("에러가 발생하였습니다.",{icon:'error'});
          this.error.nickname = true;
          
        }
      );
    },

    checkForm() {
      
      if (this.email.length >= 0 && !EmailValidator.validate(this.email))
        this.error.email = "이메일 형식이 아닙니다.";
      else this.error.email = false;

      if (
        this.password.length >= 0 &&
        !this.passwordSchema.validate(this.password)
      )
        this.error.password = "영문,숫자 포함 8 자리이상이어야 합니다.";
      else this.error.password = false;

      if(
        this.password!=this.passwordConfirm
      )this.error.passwordConfirm="두 비밀번호가 일치하지 않습니다.";
      else this.error.passwordConfirm=false;

      let isSubmit = true;
      Object.values(this.error).map(v => {
        if (v) isSubmit = false;
      });
      this.isSubmit = isSubmit;
    },
    onJoin(){
      if(this.isSubmit){
        let data = {
        id:this.id,
        nickname: this.nickname,
        email:this.email,
        pw:this.password,
        passwordConfirm:this.passwordConfirm,
      };
      this.isSubmit=false;

      UserApi.requestJoin(
        data,
        res=>{
          if(res.data =="success"){
            swal("회원가입에 성공하였습니다!",{icon:'success'});
            this.$router.push({name:'Login'});
          }else if(res.data =="fail"){
            swal("회원가입에 실패하셨습니다.!",{icon:'warning'});
          }else{
            swal("에러발생",{icon:'error'});
          }

          
        },
        error=>{
          swal("에러발생",{icon:'error'});
          this.isSubmit = true;
        }
      );
      }
      
     
    }

  }
};
</script>

<style>
#join{
  padding:50px;
}

input {
  border : 1px solid darkgray;
}

#join_box1,#join_box2,#join_box3,#join_box4,#join_box5{
  margin-top: 20px;
  margin-left: 5px;
}

#join_box6{
  margin-top: 50px;
  margin-bottom: 50px;
  
}

#join_box6 button{
  background-color: #ffbe76;
  color: white;
  height: 50px;
}

#join_box2 button, #join_box3 button{
  margin-top: 5px;
  height: 40px;
  margin-left: 20px;
}

#join button{
  background-color: #ffbe76;
  box-shadow: none;
  color: white;
}

#join_box1{
  margin-top: 30px;
  text-align: center;
  margin-bottom: 30px;
}
#join_box0{
  display: flex;
  justify-content: center;
}

#join_box0 img{
  width:200px;
}

</style>
