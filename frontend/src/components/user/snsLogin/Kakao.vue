<template>
    <div id="kakao-login" style="text-align:center;">

      
        <button @click="kakaologin">
           <img :src="require(`@/assets/images/kakao.png`)" />
            <span style="margin-left:20px;margin-top:20px;color: gray; font-size:17px;">카카오톡 로그인</span>
        </button>
        
    </div>
</template>

<script>
import swal from 'sweetalert';
import UserApi from "../../../api/UserApi";
import { mapState } from 'vuex'

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
                            nickname : kakao_account.profile.nickname
                            };
                        UserApi.snsLogin(
                            data,
                            res => {

                                if(res.data.result == "success"){
                                swal({title:"로그인 되었습니다.",icon:'success'});
                                this.$store.dispatch("login",res);
                                this.$store.state.userId = res.data.id
                                this.$router.push({name:'FeedMain'});
                                }else if(res.data.result == "fail"){
                                swal({title:"로그인 실패.",icon:'error'});
                            
                                }else{
                                swal("로그인 실패","아이디가 존재하지 않거나 비밀번호가 틀렸습니다.",'error');
                                this.$router.push({name:'ErrorPage'});

                                }
                            },
                            error => {
                                //요청이 끝나면 버튼 활성화
                                swal("로그인 실패","아이디가 존재하지 않거나 비밀번호가 틀렸습니다.",'error');

                                this.$router.push({name:'ErrorPage'});
                            }
                            );


                        

                        
                    } 
                });

            },
        },

          computed: {
            ...mapState([
            'userId',
            ]),
        },

    }
</script>
