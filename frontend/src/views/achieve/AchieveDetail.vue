<template>
  <div class="feed newsfeed">
    <div class="wrapB">
      <h1></h1>
      <b-card>
        <div class="title">
          <label class="titlefont">{{ title }}</label>

          <div>
            <img
              id="achieve_img"
              v-if="achieveTitleOne[0].percent == 100"
              :title="`${achieveTitleOne[0].title}%`"
              :src="require(`@/assets/images/${achieveTitleOne[0].clear_img}.png`)"
            />
            <img
              id="achieve_img"
              v-else
              :title="`${achieveTitleOne[0].title} ${achieveTitleOne[0].percent}%`"
              :src="require(`@/assets/images/${achieveTitleOne[0].fail_img}.png`)"
            />
          </div>
        </div>
        <div style="margin-top:50px;">
          <b-progress :max="achieveTitleOne[0].achieve_cnt" height="4rem" style="margin:30px 10px">
            <b-progress-bar class="bar-color" :value="achieveTitleOne[0].clear_cnt">
              <span>
                <strong>{{ achieveTitleOne[0].percent }}%</strong>
              </span>
            </b-progress-bar>
          </b-progress>
        </div>
      </b-card>
      <b-card title="세부 항목">
        <b-list-group>
          <div v-for="(slave, idx) in achieveTitleDetail" :key="idx">
            <b-list-group-item class="menu-1" v-show="slave.achieve_slave != null">
              <h5 class="submenu">{{ slave.achieve_slave_name }}</h5>
              <span>
                <i class="fas fa-utensils" />
              </span>
            </b-list-group-item>
            <b-list-group-item class="menu-2" v-show="slave.achieve_slave == null">
              <h5 class="submenu">{{ slave.achieve_slave_name }}</h5>
            </b-list-group-item>
          </div>
        </b-list-group>
      </b-card>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import AchieveApi from '../../api/AchieveApi';
export default {
  data: () => {
    return {
      achieveTitleOne: [],
      achieveTitleDetail: [],
    };
  },
  created: function() {
    let data = {
      id: this.userId,
      title: this.$route.query.title,
    };
    this.title = this.$route.query.title;
    //사용
    AchieveApi.achieveTitleOne(
      data,
      (res) => {
        this.achieveTitleOne = res.data.list;
      },
      (error) => {
        console.log(error);
      }
    );
    //사용
    AchieveApi.achieveTitleDetail(
      data,
      (res) => {
        this.achieveTitleDetail = res.data.list;
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

* {
  font-family: 'BBTreeGB' !important;
}
.bar-color {
  background-color: #ffbe76;
  color: black;
  font-size: 1.1rem;
  min-width: 9%;
}
.menu-1 {
  display: flex;
  flex-direction: row;
  background-color: #ffbe76;
  margin: 5%;
  padding: 10px;
  min-height: 2rem;
  border: none;
}
.menu-2 {
  display: flex;
  flex-direction: row;
  background-color: #b2bec3;
  margin: 5%;
  padding: 10px;
  min-height: 2rem;
  border: none;
}
.submenu {
  margin: 10px;
  min-width: 87%;
  font-size: 24px;
}
.fa-utensils {
  width: 80%;
  height: 30px;
  margin-top: 5px;
  color: chocolate;
  opacity: 0.9;
}
.card-title {
  font-size: 30px;
}
.card-body {
  padding: 30px;
}
.card {
  margin: 40px 0px;
  min-width: 100%;
}
.title {
  text-align: center;
  align-content: center;
}
.titlefont {
  font-size: 38px;
  margin-left: 0.3%;
  text-align: center;
}
#achieve_img {
  width: 180px;
  height: 180px;
}
</style>
