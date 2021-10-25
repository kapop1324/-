
<!--
    가입하기는 기본적인 폼만 제공됩니다
    기능명세에 따라 개발을 진행하세요.
    Sub PJT I에서는 UX, 디자인 등을 포함하여 백엔드를 제외하여 개발합니다.
 -->
<template>
  <div class="pwchange-page">
    <div class="row" id="pwupdate">
      <div class="row" id="updatepw_box0">
       <img :src="require(`@/assets/images/roundlogo.png`)" />
      </div>
      <div class="row" id="updatepw">
         <h1>비밀번호 변경</h1>
      </div>
      <div class="row" id="updatepw_box1">
        <input v-model="oldpw" 
        v-bind:class="{error : error.oldpw, complete:!error.oldpw&&oldpw.length!==0}"
        id="oldpw" 
        placeholder="현재 비밀번호를 입력하세요." 
        :type="passwordType"  
        />
        <div class="error-text" v-if="error.oldpw">{{error.oldpw}}</div>
      </div>

      <div class="row" id="updatepw_box2">
        <input 
        v-model="newpw" 
        id="newpw" 
        :type="passwordType" 
        v-bind:class="{error : error.newpw, complete:!error.newpw&&newpw.length!==0}"
        placeholder="새 비밀번호를 입력하세요." />
        <div class="error-text" v-if="error.newpw">{{error.newpw}}</div>
      </div>

      <div class="row" id="updatepw_box3">
        <input
          v-model="passwordConfirm"
          :type="passwordConfirmType"
          v-bind:class="{error : error.passwordConfirm, complete:!error.passwordConfirm&&passwordConfirm.length!==0}"
          id="password-confirm"
          placeholder="새 비밀번호를 다시한번 입력하세요."
        />
       
        <div class="error-text" v-if="error.passwordConfirm">{{error.passwordConfirm}}</div>
      </div>
    <div class="row" id="updatepw_box4">
        <button class="button"
        @click="changepw"
        :disabled="!isSubmit"
        :class="{disabled : !isSubmit}"
        >변경하기</button>
    </div>


  


    </div>
   
  </div>
 
</template>

<script>
import { mapState } from 'vuex'
import UserApi from '../../api/UserApi';
import PV from "password-validator";
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
      oldpw:"",
      newpw: "",
      passwordConfirm: "",
      passwordSchema: new PV(),
      nickname: "",
      isTerm: false,
      isLoading: false,
      error: {
        newpw: false,
        oldpw: false,
        passwordConfirm: false,
      
      },
      isSubmit: false,
      passwordType: "password",
      passwordConfirmType: "password",
      termPopup: false
    };
  },
  watch:{ 

    oldpw: function(v) {
      this.checkForm();
    },

    newpw: function(v) {
      this.checkForm();
    },
  
    passwordConfirm:function(v) {
      this.checkForm();
    }
  },
  methods:{
    lowercase(){
      this.email= this.email.toLowerCase();
    },


    checkForm() {
      
      if (
        this.oldpw.length >= 0 &&
        !this.passwordSchema.validate(this.oldpw)
      )
        this.error.oldpw = "영문,숫자 포함 8 자리이상이어야 합니다.";
      else this.error.oldpw = false;
     
      if (
        this.newpw.length >= 0 &&
        !this.passwordSchema.validate(this.newpw)
      )
        this.error.newpw = "영문,숫자 포함 8 자리이상이어야 합니다.";
      else this.error.newpw = false;

      if(
        this.newpw!=this.passwordConfirm
      )this.error.passwordConfirm="두 비밀번호가 일치하지 않습니다.";
      else this.error.passwordConfirm=false;

      let isSubmit = true;
      Object.values(this.error).map(v => {
        if (v) isSubmit = false;
      });
      this.isSubmit = isSubmit;
    },
    changepw(){
      if(this.isSubmit){
        let data = {
        id:this.userId,
        oldpw:this.oldpw,
        newpw:this.newpw,
        token:this.$store.state.token,
      };
      this.isSubmit=false;
     
      UserApi.changepw(
        data,
        res=>{
          
          if(res.data =="success"){
            swal("비밀번호 변경에 성공하였습니다!",{icon:'success'});
            this.$router.push("/");
          }else if(res.data =="fail2"){
            swal("비밀번호 변경에 실패하셨습니다!",{icon:'warning'});
          }
          else if(res.data =="fail1"){
            swal("현재 비밀번호가 아닙니다.",{icon:'warning'});
          }
          else{
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

  },
   computed: {
    ...mapState([
      'userId',
    ]),
  }
};
</script>

<style>
.pwchange-page{
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
#pwupdate{
  padding:0px 50px;
  max-width: 616px;
}

input {
  border : 1px solid darkgray;
}

#updatepw_box1, #updatepw_box2, #updatepw_box3{
  margin-top: 20px;
  margin-left: 5px;
}

#updatepw_box4{
  margin-top: 50px;
  margin-left: 5px;
  margin-bottom: 50px;
}

#updatepw_box4 button{
  background-color: #ffbe76;
  color: white;
  height: 50px;
}

#updatepw{
  margin-top: 30px;
  text-align: center;
  margin-bottom: 30px;
}
#updatepw_box0{
  display: flex;
  justify-content: center;
}

#updatepw_box0 img{
  width:200px;
  display: flex;
  justify-content: center;
}

</style>

