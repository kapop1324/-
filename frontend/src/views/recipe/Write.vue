<template>
  <div class="write">
    <div class="write-form">
      <h1>레시피 작성</h1>
      <input v-model="title" id="title" placeholder="제목을 입력하세요" type="text" />
      <div id="image-des">
        <h2>CARDs</h2>
        <div v-for="(data, idx) in fields" :key="fields[idx].idx" class="write-card">
          <div class="check-box">
            <div v-if="idx === thumbnailNumber" class="thumbnail" @click="isThumbnail(idx)">
              <i class="fas fa-thumbtack"></i>
            </div>
            <div v-else class="thumbnail-none" @click="isThumbnail(idx)">
              <i class="fas fa-thumbtack"></i>
            </div>
            <button @click="deleteContent(idx)">
              <i class="fas fa-minus-circle" style="color:#FF5C4D;"></i>
            </button>
          </div>
          <div
            class="image-box"
            :style="{ backgroundImage: `url(${data.img})` }"
            @click="updateCard(idx)"
          >
            <!-- <img class="image" :src="data.img" @click="updateCard(idx)"/> -->
          </div>
          <div class="black-box" v-if="showUpdate" @click="showUpdate = !showUpdate"></div>
          <div v-if="showUpdate" class="inbox">
            <label for="file">
              <img :src="fields[tempidx].img" />
            </label>
            <input type="file" accept="image/*" id="file" @change="updateImg" />
            <textarea v-model="fields[tempidx].des"></textarea>
            <button class="submit" @click="updateImage(idx)">수정</button>
          </div>
          <div class="leftright">
            <button @click="leftContent(idx)">
              <i class="fas fa-angle-double-left"></i>
            </button>
            <button @click="rightContent(idx)">
              <i class="fas fa-angle-double-right"></i>
            </button>
          </div>
        </div>
        <div class="addcard" id="add" @click="tempimage">
          +
        </div>
      </div>

      <div class="black-box" v-if="showcard" @click="showcard = !showcard"></div>
      <div v-if="showcard" class="inbox">
        <label for="file">
          <img v-if="tempImg" :src="tempImg" style="cursor: pointer;"/>
          <img
              v-else
              :src="require(`@/assets/images/write_img.png`)"
              style="object-fit:cover; border: 0.1px solid darkgray; cursor: pointer;"
            />
        </label>
        <input type="file" accept="image/*" id="file" @change="uploadImg" />
        <textarea v-model="tempDes"></textarea>
        <button class="submit" @click="addimage">등록</button>
      </div>

      <div id="image-des">
        <div class="hash-input-group">
          <h2>TAGs</h2>
          <div class="hash-input">
            <input
              type="text"
              v-model="temphash"
              @keyup.enter="createHash"
              placeholder="태그를 입력하세요"
            />
            <button @click="createHash"><i class="fas fa-pen hash-icon"></i></button>
          </div>
        </div>
        <div v-for="(h, idx) in HashList" :key="idx">
          <div class="hash">
            <span># {{ h.content }}</span>
            <span @click="deleteHash(idx)"><i id="hashDelete" class="fas fa-times"></i></span>
          </div>
        </div>
      </div>
      <div id="image-des">
        <h2>Challenges</h2>
        <div class="selectbox">
          <select
            name="master_name"
            id="master_name"
            v-model="masterSelected"
            @change="updateMaster"
          >
            <option value="" selected disabled hidden> 대분류 선택 </option>
            <option v-for="(master_name, idx) in master_names" :key="idx" :value="master_name">
              {{ master_name }}
            </option>
          </select>
          <select name="slave_name" id="slave_name" v-model="slaveSelected">
            <option value="" selected disabled hidden> 소분류 선택 </option>
            <option v-for="(slave_name, idx) in slave_names" :key="idx" :value="slave_name">
              {{ slave_name }}
            </option>
          </select>
        </div>
      </div>
      <button class="submit" @click="check">
        <h1><i class="fas fa-pen pen-icon"></i></h1>
      </button>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import UserApi from '../../api/UserApi';
import RecipeApi from '../../api/RecipeApi';
import FirebaseApi from '../../api/FirebaseApi';
import swal from 'sweetalert';

var frm = new FormData();

export default {
  data: () => {
    return {
      title: '',
      fields: [],
      count: 0,
      thumbnailNumber: 0,
      ThumbNailList: [],
      HashList: [],
      temphash: '',
      showcard: false,
      tempImg: '',
      tempDes: '',
      defaultImage: 'https://t1.daumcdn.net/cfile/tistory/24611E4853FDAE0B14',
      Achieves: [],
      Achieves_two: [],
      master_names: [],
      slave_names: [],
      masterSelected: '',
      slaveSelected: '',
      showUpdate: false,
      tempidx: 0,
    };
  },
  mounted: function() {
    RecipeApi.achieveRecipe(
      (res) => {
        this.Achieves = res.data.achieveList;
        for (let i = 0; i < this.Achieves.length; i++) {
          this.master_names.push(this.Achieves[i].achieve_master_name);
        }
        this.master_names = new Set(this.master_names);
      },
      (err) => {
        console.log(err);
      }
    );
  },
  methods: {
    deleteHash(idx) {
      this.HashList.splice(idx, 1);
    },
    updateMaster() {
      this.slave_names = [];
      if (this.masterSelected) {
        for (let i = 0; i < this.Achieves.length; i++) {
          if (this.masterSelected == this.Achieves[i].achieve_master_name) {
            this.slave_names.push(this.Achieves[i].achieve_slave_name);
          }
        }
      }
    },
    tempimage() {
      this.showcard = !this.showcard;
    },
    check() {
      if (this.title.length === 0) {
        swal('글작성','제목이 없습니다','warning')
      } else {
        var frm = new FormData();
        let l = this.fields.length;
        for (let i = 0; i < this.fields.length; i++) {
          if (this.thumbnailNumber == i) {
            this.ThumbNailList.push('true');
          } else {
            this.ThumbNailList.push('false');
          }
        }
        frm.append('title', this.title);
        frm.append('id', this.userId);
        frm.append('nickname', this.userNickname);
        frm.append('achieve_master', this.masterSelected);
        frm.append('achieve_slave', this.slaveSelected);
        for (let i = 0; i < l; i++) {
          frm.append('des[' + i + ']', this.fields[i].des);
          frm.append('img[' + i + ']', this.fields[i].img);
          frm.append('thumbnail[' + i + ']', this.ThumbNailList[i]);
        }
        for (let i = 0; i < this.HashList.length; i++) {
          frm.append('hashtags[' + i + ']', this.HashList[i].content);
        }
  
        UserApi.createRecipe(
          frm,
          (res) => {
            this.$router.push({ name: 'FeedMain' });
            this.$store.dispatch('clearFormdata');
          },
          (error) => {
            console.log(error);
            frm = new FormData();
          }
        );
      }
    },
    uploadImg(e) {
      let file = e.target.files[0];
      FirebaseApi.upLoad(file, (res) => {
        this.tempImg = res;
        frm.append('file', res);
        this.$store.dispatch('uploadImg', { file: res, idx: this.idx });
      });
    },
    addimage() {
      this.fields.push({
        idx: this.count++,
        img: this.tempImg,
        des: this.tempDes,
      });
      this.tempDes = '';
      this.tempImg = '';
      this.showcard = !this.showcard;
    },
    createHash() {
      if (this.temphash.length == 0) {
        swal({ title: '글자를 입력해주세요', icon: 'warning' });
        return;
      }
      if (this.temphash.length > 10) {
        swal({ title: '10글자 이하로 입력해주세요', icon: 'warning' });
        this.temphash = '';
        return;
      }
      for (let i = 0; i < this.HashList.length; i++) {
        if (this.HashList[i].content == this.temphash) {
          swal({ title: '이미 등록된 해시태그 입니다.', icon: 'warning' });
          this.temphash = '';
          return;
        }
      }
      this.HashList.push({
        content: this.temphash,
      });
      this.temphash = '';
    },
    updateCard(idx) {
      this.showUpdate = true;
      this.tempidx = idx
    },
    updateImg(e) {
      let file = e.target.files[0];
      FirebaseApi.upLoad(file, (res) => {
        this.fields[this.tempidx].img = res;
        frm.append('file', res);
        this.$store.dispatch('uploadImg', { file: res, idx: this.idx });
      });
    },
    updateImage(idx) {
      // if (this.tempImg != '') {
      //   this.fields[idx].img = this.tempImg;
      // }
      this.tempImg = '';
      this.showUpdate = !this.showUpdate;
    },
    isThumbnail(idx) {
      this.thumbnailNumber = idx;
    },
    deleteContent(idx) {
      this.fields.splice(idx, 1);
    },
    leftContent(idx) {
      if (idx >= 1) {
        let content = this.fields[idx];
        this.fields[idx] = this.fields[idx - 1];
        this.fields[idx].idx += 1;
        this.fields[idx - 1] = content;
        this.fields[idx - 1].idx -= 1;
      }
    },
    rightContent(idx) {
      if (idx < this.fields.length - 1) {
        let content = this.fields[idx];
        this.fields[idx] = this.fields[idx + 1];
        this.fields[idx].idx -= 1;
        this.fields[idx + 1] = content;
        this.fields[idx + 1].idx += 1;
      }
    },
  },
  computed: {
    ...mapState(['userId', 'img', 'userNickname']),
  },
};
</script>
<style scoped>
* {
  font-family: 'BBTreeGB';
}
@font-face {
    font-family: 'BBTreeGB';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_nine_@1.1/BBTreeGB.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
.write {
  margin: 0%;
  padding: 0%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.write-form {
  width: 100%;
  max-width: 616px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
#title {
  width: 98%;
  display: inline;
  margin-left: 1%;
  margin-right: 1%;
  margin-bottom: 20px;
  font-family: 'NanumBarunGothic', sans-serif;
}
#image-des {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  padding: 2%;
}
#image-des h2 {
  width: 100%;
}
#image-des .hash-input-group {
  width: 100%;
  display: flex;
  justify-content: space-around;
  align-items: center;
}
.hash-input {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  border: 1px solid #ffbe76;
  border-radius: 3px;
}
.hash-input input {
  height: 70%;
  background-color: #ffffff;
  border: none;
  margin-right: 2px;
  font-family: 'NanumBarunGothic', sans-serif;
}
.hash-input button {
  height: 70%;
  border-radius: 3px;
  align-items: center;
  margin-right: 6px;
}
.hash-input svg {
  margin-right: 2px;
}
.write-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 31%;
  height: 40vw;
  max-height: 250px;
  margin: 1%;
  border: 1px solid #ffbe76;
  border-radius: 5px;
}
.thumbnail,
.thumbnail-none {
  cursor: pointer;
  display: flex;
  justify-content: center;
  margin: 5px;
}
.thumbnail-none svg {
  color: rgba(155, 155, 155, 0.5);
}
.image-box {
  width: 100%;
  height: 80%;
  overflow: hidden;
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center center;
}
.image {
  object-fit: contain;
  transform: translate(-20%);
}
#file {
  width: 100%;
}
.content {
  width: 100%;
}
.addcard {
  display: flex;
  margin: 5px;
  justify-content: center;
  cursor: pointer;
  font-size: 50px;
  align-items: center;
  width: 31%;
  height: 40vw;
  max-height: 250px;
  border: 1px solid #191919;
  border-radius: 5px;
}
.black-box {
  position: fixed;
  top: 0%;
  left: 0%;
  height: 100vh;
  width: 100vw;
  background-color: rgba(155, 155, 155, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2;
}
.inbox {
  position: fixed;
  top: 50%;
  left: 50%;
  width: 90vw;
  max-width: 616px;
  transform: translate(-50%, -50%);
  display: flex;
  padding: 10px 0px;
  background-color: #fff;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  z-index: 3;
}
.inbox label {
  max-width: 95%;
  max-height: 50vh;
  display: flex;
  width: 500px;
  height: 500px;
  justify-content: center;
}
.inbox img {
  background-color: #191919;
  min-width: 100%;
  min-height: 100%;
  margin-bottom: 20px;
  object-fit: contain;
}
.inbox input {
  display: none;
}
.inbox textarea {
  width: 95%;
  margin: 20px 0px;
  background-color: rgba(165, 175, 182, 0.5);
}
.hash {
  background-color: #ffbe76;
  padding: 0px 10px;
  margin-right: 10px;
  margin-bottom: 10px;
  cursor: pointer;
  color: #fff;
  font-size: 1.2rem;
  font-weight: 500;
  min-width: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 25px;
}
.submit {
  width: 95%;
  display: inline;
  background-color: #ffbe76;
  color: #ffffff;
  border-radius: 3px;
  margin-bottom: 5%;
}
.leftright {
  display: flex;
  justify-content: space-around;
  width: 100%;
}
.check-box {
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding-left: 2px;
  padding-right: 4px;
}
#hashDelete {
  margin-top: 6px;
  margin-left: 8px;
  color: #fff;
}
.pen-icon {
  margin-top: 7px;
  padding-top: 4px;
  color: #fff;
}
.hash-icon {
  margin-top: 5px;
  margin-right: 13px;
}
select {
  width: 40%;
  height: calc(1.5em + 0.75rem + 15px);
  border: 1px solid #ced4da;
  border-radius: 0.25rem;
  padding: 0.375rem 1.75rem 0.375rem 0.75rem;
  font-size: 1.1rem;
  font-weight: 400;
  line-height: 1.5;
  margin: 0.5rem;
  -webkit-appearance: none; /* 화살표 없애기 for chrome*/
  -moz-appearance: none; /* 화살표 없애기 for firefox*/
  appearance: none; /* 화살표 없애기 공통*/
  background: url('../../assets/images/arrow.gif') no-repeat 96% 50%/15px auto;
  font-family: 'NanumBarunGothic', sans-serif;
}
.selectbox {
  width: 100%;
}
</style>
