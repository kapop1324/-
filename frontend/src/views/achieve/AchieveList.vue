<template>
  <div class="feed newsfeed">
    <div class="wrapB">
      <div style="margin-top:50px;">
        <h1>업적</h1>
      </div>
      <b-card title="전체 칭호 달성도">
        <b-progress :max="totalCount" height="4rem" style="margin:30px 10px">
          <b-progress-bar class="bar-color" :value="userCount">
            <span>
              <strong style="margin-left:3px;"
                >{{ Math.floor((userCount / totalCount) * 100) }}%</strong
              ></span
            >
          </b-progress-bar>
        </b-progress>
        <div class="row">
          <div class="row">
            <label class="titlefont">칭호</label>
          </div>
          <div class="row">
            <span v-for="(clear, id) in achieveTitle" :key="id" style="width:100px; height:100px;">
              <img
                id="achieve_img"
                v-if="clear.percent == 100"
                :title="`${clear.title}%`"
                :src="require(`@/assets/images/${clear.clear_img}.png`)"
                @click="showDetail(clear.title)"
              />
              <img
                id="achieve_img"
                v-else
                :title="`${clear.title} ${clear.percent}%`"
                :src="require(`@/assets/images/${clear.fail_img}.png`)"
                @click="showDetail(clear.title)"
              />
            </span>
          </div>
        </div>
      </b-card>
      <b-card title="세부 칭호 달성도">
        <div class="submenu" v-for="(master, idx) in achieveTitle" :key="idx">
          <div class="line">
            <div class="box">
              <h4>{{ master.title }}</h4>
            </div>
            <div @click="showDetail(master.title)">
              <i class="fas fa-clipboard-list"></i>
            </div>
          </div>
          <b-progress :max="master.achieve_cnt" height="3rem" style="margin:10px">
            <b-progress-bar class="bar-color" :value="master.clear_cnt">
              <span>
                <strong>{{ master.percent }}% </strong>
              </span>
            </b-progress-bar>
          </b-progress>
        </div>
      </b-card>
    </div>
  </div>
</template>

<script>
import UserApi from '../../api/UserApi';
import AchieveApi from '../../api/AchieveApi';
import { mapState } from 'vuex';

// import "../../components/css/feed/feed-item.scss";
// import "../../components/css/feed/newsfeed.scss";
// import FeedItem from "../../components/feed/FeedItem.vue";

export default {
  props: ['keyword'],
  data: () => {
    return {
      achieveTitle: [],
      totalCount: 0,
      userCount: 0,
    };
  },
  components: {},
  methods: {
    showDetail(title) {
      this.$router.push({ name: 'AchieveDetail', query: { title: title } });
    },
  },
  created: function() {
    let data = {
      id: this.userId,
    };

    AchieveApi.achieveTitle(
      data,
      (res) => {
        for (let i = 0; i < res.data.list.length; i++) {
          this.totalCount += res.data.list[i].achieve_cnt;
          this.userCount += res.data.list[i].clear_cnt;
        }
        this.achieveTitle = res.data.list;
      },
      (error) => {
        console.log(error);
      }
    );
  },
  computed: {
    ...mapState(['userId']),
  },
};
</script>

<style scoped>
@font-face {
  font-family: 'BBTreeGB';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_nine_@1.1/BBTreeGB.woff')
    format('woff');
  font-weight: normal;
  font-style: normal;
}
h1 {
  font-size: 35px;
}
* {
  font-family: 'BBTreeGB' !important;
}
.bar-color {
  background-color: #ffbe76;
  color: black;
  font-size: 1.1rem;
  min-width: 9%;
}
.line {
  display: flex;
  flex-direction: row;
}
.font {
  font-family: 'BBTreeGB';
  font-size: 20px;
  text-align: right;
}
.box {
  min-width: 88%;
}
.fa-clipboard-list {
  width: 100%;
  height: 35px;
  color: #ffbe76;
  margin-left: 10px;
}
.submenu {
  margin-top: 40px;
}
.card-title {
  font-size: 30px;
  font-family: 'BBTreeGB';
}
.card-body {
  padding: 30px;
}
.card {
  margin: 40px 0px;
  min-width: 100%;
}
.progress-bar bar-color {
  min-width: 80px;
}
#achieve_img {
  width: 100px;
  height: 100px;
  padding: 2px;
  margin: 2px;
}
.titlefont {
  font-size: 28px;
  margin-left: 0.3%;
}
</style>
