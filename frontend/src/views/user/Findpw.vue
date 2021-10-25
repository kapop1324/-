
<template>
    <div class="wrapC" id="findpw">
      <div class="row" id="findpw_box0">
       <img :src="require(`@/assets/images/roundlogo.png`)" />
      </div>
      <div class="row" id="findpw_box1">
        <h1>비밀번호 찾기</h1>
      </div>
      <div class="row" id="findpw_box2">
        <input v-model="id" @keyup.enter="findpw" id="id" placeholder="아이디를 입력하세요." type="text"/>
      </div>
      <div class="row" id="findpw_box3">
        <input v-model="email" @keyup.enter="findpw" id="email" placeholder="이메일을 입력하세요." type="text"/>
      </div>
      <div class="row" id="findpw_box4">
          <button class="btn btn--back btn--login" @click="findpw">비밀번호 찾기</button>
      </div>
    </div>
</template>

<script>
import "../../components/css/user.scss";
import UserApi from "../../api/UserApi";
import { mapState } from 'vuex'
import { mapActions } from 'vuex'
import swal from 'sweetalert';


export default {
  components: {
   
  },
  created() {
    this.component = this;

    
  },
  watch: {
    
  },
  methods: {
    
    findpw() {
        let data = {
          id : this.id,
          email : this.email
        };

       UserApi.emailfindpw(
          data,
          res => {
             if(res.data == "success"){
            swal("이메일이 발송되었습니다.",{icon:'success'});
           this.$router.push("/");
         


          }else if(res.data == "fail"){
            swal("아이디 혹은 이메일이 틀렸습니다.",{icon:'warning'});
          
          }else{

            swal("오류발생.",{icon:'error'});
          
          }
          },
          error => {
            //요청이 끝나면 버튼 활성화
            swal("에러발생",{icon:'error'});
          }
        );
      //}
    }
  },
  data: () => {
    return {

      id: "",
      email: "",
      isSubmit: false,
    };
  }
};
</script>

<style>

#findpw{
  padding:50px;
  max-width: 580px;
  width: 100%;
  margin: 0 auto;

}
@media (max-width: 580px) {
  #findpw{
    border: none;
  }
}

input {
  border : 1px solid darkgray;
}

#findpw_box1, #findpw_box2, #findpw_box3{
  margin-top: 20px;
  margin-left: 5px;
}

#findpw_box4{
  margin-top: 50px;
  margin-left: 5px;
  margin-bottom: 50px;
}

#findpw_box4 button{
  background-color: #ffbe76;
  color: white;
  height: 50px;
}

#findpw_box1{
  margin-top: 30px;
  text-align: center;
  margin-bottom: 30px;
}

#findpw_box0{
  display: flex;
  justify-content: center;
  align-items: center;
}
#findpw_box0 img{
  width: 200px;
}

</style>


