<template>
    <div id="kakao-login">

        <button @click="kakaologin">
              <img :src="require(`@/assets/images/kakao.jpg`)" />
        </button>
    </div>
</template>

<script>
import swal from 'sweetalert';
import UserApi from "../../../api/UserApi";
    export default {
       
        methods: {
            kakaologin(){
                window.Kakao.Auth.login({
                    scope: 'profile_nickname, account_email,birthday',
                    success: this.getprofile
                })
            },

            getprofile(authobj){

                
                window.Kakao.API.request({
                    url:'/v2/user/me',
                    success: res=> {
                        const kakao_account = res.kakao_account;

                        let data = {
                            email : kakao_account.email,
                            nickname : kakao_account.profile.nickname,
                            sns:'kakao',
                            birthday:kakao_account.birthday
                            };

                        UserApi.snsregister(
                            data,
                            res => {
                                if(res.data == "success"){
                                    swal("회원가입에 성공하였습니다!",{icon:'success'});
                                    this.$router.push({name:'Login'});
                                 
                                }else if(res.data == "fail"){
                                    swal("회원가입에 실패하셨습니다.!",{icon:'warning'});
                                    
                                }else{
                                    swal("에러발생",{icon:'error'});
                                    
                                }
                            },
                            error => {
                                //요청이 끝나면 버튼 활성화
                                swal("에러발생",{icon:'error'});
                                

                                this.$router.push({name:'ErrorPage'});
                            }
                            );


                        

                        
                    } 
                });

            },
        },

    }
</script>
