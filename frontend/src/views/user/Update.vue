
<template>
  <div class=" wrapC">
    
    <div class="row" id="userupdate">
      <div class="row" id="userupdatebox0">
       <img :src="require(`@/assets/images/roundlogo.png`)" />
    </div>
      <div class="row" id='userupdatebox1'>
        <h1>회원 수정</h1>
      </div>

      <div class="row" id='userupdatebox2'>
        <label>프로필 이미지</label><br>
        <img :src="userImg">
        <input type="file" accept="image/*" id="file" class="inputfile" @change="uploadImg">
      </div>
        
      
      <div class="row" id='userupdatebox3'>
        <label for="nickName">닉네임</label>
        <input v-model="nickName" id="nickName" placeholder="닉네임을 입력하세요." type="text"/>
      </div>
      <div class="row" id="userupdatebox9">
        <label for="des">한줄소개</label>
        <input v-model="des" id="des" placeholder="한줄소개를 입력하세요." type="text"/>
        
      </div>
      <div class="row" id='userupdatebox4'>
        <label for="cellphone">전화번호</label>
        <input v-model="cellphone" id="cellphone" placeholder="전화번호를 입력하세요." 
        type="text"
        />
      </div>
      <div class="row" id='userupdatebox5'>
        <label for="birthday">생일</label>
        <input v-model="birthday" id="birthday" placeholder="생일을 입력하세요." 
        type="text" 
        />
        
      </div>
      
      <div class="row" id='userupdatebox6'>
        <div>
          <label>주소</label>
          <button class="btn btn-primary" v-show="addresscheck" @click="saveaddressopen">저장</button>
          <button class="btn btn-secondary" v-show="!addresscheck" @click="saveaddressopen">열기</button>
        </div>
        <div>
          <DaumPostcode v-show="addresscheck"
          :on-complete=handleAddress
          />
        </div>
      </div>
      <div class="row" id='userupdatebox7'>
        <label for="address2">상세주소</label>
        <input v-model="address2" id="address2" type="text" placeholder="상세주소를 입력하세요." />
      </div>
    
    <div class="row" id='userupdatebox8'>
      <button class="button" @click="update">변경하기</button>
    </div>
  </div>

  </div>
</template>

<script>
import UserApi from '../../api/UserApi';
import DaumPostcode from 'vuejs-daum-postcode'
import FirebaseApi from '../../api/FirebaseApi';
import swal from 'sweetalert';

export default {
  data: () => {
    return {
      addresscheck: false,
      userImg:"",
      email: "",
      nickName: "",
      userId:"",
      des:"",
      birthday:"",
      cellphone:"",
      address1:"",
      address2:"",
      address3:"",
      isLoading: false,
      error: {
        email: false,
        nickName: false,
      },
      isSubmit: false,
    };
  },
  components: {
    DaumPostcode,
  },
  created() {
    if (this.$store.state.token === ''){
      this.$router.push({name:'Login'})
    }
    this.getUser()
  },
  watch:{
  },
  methods:{
    saveaddressopen(){  
      this.addresscheck = !this.addresscheck;
    },
    saveaddressclose(){
      this.addresscheck = !this.addresscheck;
    },
    handleAddress(data) {
      let fullAddress = data.address
      let extraAddress = ''
      if (data.addressType === 'R') {
        if (data.bname !== '') {
          extraAddress += data.bname
        }
        if (data.buildingName !== '') {
          extraAddress += (extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName)
        }
        fullAddress += (extraAddress !== '' ? ` (${extraAddress})` : '')
      }
      this.address1 = fullAddress
    },
    uploadImg(e){
      let file = {
        img:e.target.files[0],
        userId:this.$store.state.userId
      };
      FirebaseApi.upLoadProfile(
        file,
        res=>{
          this.userImg = res
        })
    },
    getUser(){
      const config = {
        headers:{
          'access-token': this.$store.state.token,
          'id':this.$store.state.userId
        }
      }
      UserApi.requestUser(
        config,
        res=>{
          this.userId = res.data.userInfo.id
          this.nickName = res.data.userInfo.nickname
          this.des = res.data.userInfo.des
          this.cellphone = res.data.userInfo.cellphone
          this.birthday = res.data.userInfo.birthday
          this.email = res.data.userInfo.email
          this.userImg = res.data.userInfo.img
        },
        error=>{
          swal("회원정보 요청에 실패하셨습니다.",{icon:'warning'});
        }
      )
    },
    update(){
      let data = {
        img:this.userImg,
        email:this.email,
        nickname:this.nickName,
        id:this.$store.state.userId,
        des:this.des,
        birthday:this.birthday,
        cellphone:this.cellphone,
        address1:this.address1,
        address2:this.address2,
        address3:this.address3,
        token : this.$store.state.token,
      };
      UserApi.requestUpdate(
        data,
        res=>{
          swal('회원수정이 완료 되었습니다.',{icon:'success'})
          this.$router.push(`/user/profile/${this.userId}`);
          
        },
        error=>{
          swal("실패","회원수정에실패하였습니다.","error");
          this.$router.push("/error");
        }
      );
    }
  }
};
</script>
<style>
#userupdate{
  padding:50px;
  display: flex;
  align-items: center;
  justify-content: center;
}

input {
  border : 1px solid darkgray;
}

label{
  font-size: 20px;
  margin-left: -10px;
  margin-bottom: 5px;
}

#userupdatebox1, #userupdatebox9, #userupdatebox2, #userupdatebox3, #userupdatebox4, #userupdatebox5, #userupdatebox6, #userupdatebox7{
  margin-top: 30px;
  margin-left: 3px;
 
}

#userupdatebox8{
  margin-top: 50px;
  margin-left: 3px;
  margin-bottom: 50px;
  
}

#userupdatebox2 input{
  margin-top: 30px;
  border:none;
}

#userupdatebox8 button{
  background-color: #ffbe76;
  color: white;
  height: 50px;
  
}

#userupdatebox6 button{
  height: 30px;
  line-height: 18px;
  margin-left: 25px;
}

#userupdatebox6{
  margin-bottom: 30px;
  margin-bottom: 50px;
}

#userupdatebox1{
  margin-top: 30px;
  text-align: center;
  margin-bottom: 30px;
}
#userupdatebox0{
  display: flex;
  justify-content: center;
}
#userupdatebox0 img{
  padding: 0%;
  margin: 0%;
  width:200px;
}

</style>