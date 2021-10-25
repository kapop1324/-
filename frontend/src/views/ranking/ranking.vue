<template>
    <div class="ranking">
        <h1>My Rank</h1>
        <div  id="myrank">
            <div id="myrank-content">
                <div  id="myrank_head">
                    <!-- <p>My Rank</p> -->
                    <div id="myrank_img">
                        <div>
                        <img :src="userImg || defaultProfile" alt=""  width="100%" height="100%" style="border-radius: 50%;">
                        </div>
                    </div>
                    <p>{{ userNickname }}</p>
                </div>
                <div id="myrank_des">
                    <!-- <p>닉네임 : {{mynickname}}</p> -->
                    <p>나의 순위는..</p>
                    <p class="rank">{{myrank}}위</p>
                    <p class="point">{{mypoint}}포인트</p>
                </div>
            </div>
        </div>
        <div  id="top100_head">
            <h2>TOP 100</h2>
        </div>

        <div  id="top100_table">
            <table class="table table-hover">
                <thead>
                    <th scope="col">순위</th>
                    <th scope="col">닉네임</th>
                    <th scope="col">포인트</th>
                </thead>
                <tbody>
                    <tr v-for="(rank, idx) in ranking" :key="idx">
                        <td v-if="rank.rank == 1"><i class="fas fa-trophy" style="color:#f1c40f"></i></td>
                        <td v-else-if="rank.rank == 2"><i class="fas fa-medal" style="color:#bdc3c7"></i></td>
                        <td v-else-if="rank.rank == 3"><i class="fas fa-medal" style="color:#cd7f32"></i></td>
                        <td v-else>{{rank.rank}}</td>
                        <td @click="goProfile(rank)" style="cursor:pointer;">{{rank.nickname}}</td>
                        <td>{{rank.rankpoint}}</td>
                    </tr>
                </tbody>
            </table>
        
        </div>
    </div>
</template>

<script>
import { mapState } from 'vuex'
import UserApi from '../../api/UserApi';
import RankApi from '../../api/RankApi';
import defaultProfile from "../../assets/images/profile_default.png";

export default {
    components: { },
    data: () => {
    return {
        defaultProfile,
        ranking:[],
        myrank:0,
        mypoint:0,
        mynickname:null,
    }
    },
created() {

    RankApi.getRanking(
        res => {
            this.ranking = res.data.rankinglist
            for(let i = 0; i < this.ranking.length; i++){
                if(this.ranking[i].id == this.userId){
            this.myrank = this.ranking[i].rank
            this.mypoint = this.ranking[i].rankpoint
            this.mynickname = this.ranking[i].nickname
            
        }
    }
        },
        error=>{
        console.log(error)
        }
    )

   

    },
methods: {
    goProfile(rank) {
        this.$router.push({name:'Profile', params: {user_id: rank.id}})
    }


    },
    computed: {
    ...mapState([
        'userId','userImg',
        'userNickname',
    ]),
    }
}
</script>

<style>
.ranking{
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 0%;
    margin: 0%;
}
#myrank{
    margin: 10px;
    background-color: #ffbe76;
    border-radius: 25px;
    height: 30%;
    width: 94%;
    max-width: 616px;
    color:white;
    padding: 10px;
}
.rank{
    font-size: 3rem;
    font-weight: bold;
}
.point{
    font-size: 1.5rem;
}
#myrank_head{
    font-size: 2rem;
    max-width: 150px;
    
    /* display: inline; */
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

#myrank_head p{
    padding-top: 10px;
}
#myrank-content{
    display: flex;
    justify-content: space-evenly;
    height: 100%;
}
#myrank_img { 
    margin-top: 30px;
    position: relative; 
    width: 130px; 
} 
#myrank_img:before { 
    content: ""; 
    display: block; 
    padding-top: 100%; 
} 
#myrank_img > div { 
    position: absolute; 
    top: 0; 
    left: 0; 
    width: 100%; 
    height: 100%;
} 
#myrank_des{
    display: flex;
    flex-direction: column;
    justify-content: center;
    font-size: 1.2rem;
    padding: 10px;
    align-items: center;
    margin-top: 1rem;
}

#top100_head{
    margin-top: 100px;
}

#top100_table{
    width: 100%;
    max-width: 616px;
}

#top100_table table thead{
    
    height: 70px;
    color: white;
    font-size: 25px;
    line-height: 60px;
    text-align: center;

}

thead th {
    background-color: #ffbe76;
}

.table  tbody  td{
    border-bottom: 1px solid lightgray;
}

#top100_table table tbody{
    height: 40px;

    font-size: 20px;
    line-height: 40px;
    text-align: center;
    
}





</style>
