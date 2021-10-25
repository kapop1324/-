<template>
  <div class="update">
    <div class="update-form">
      <h1>레시피 수정</h1>
      <!-- {{recipeContent}} -->
      <input v-model="title" id="title" placeholder="제목을 입력하세요" type="text" />

      <div id="image-des">
        <h2>CARDs</h2>
        <!-- <div class="" id="add">
        <button @click="addimagedes">+</button>
      </div> -->

        <div v-for="(recipe_file, idx) in recipe" :key="idx" class="write-card">
          <update-form
            :recipe_file="recipe_file"
            :recipe="recipe"
            :idx="recipe_file.content_order"
          ></update-form>
        </div>
        <div class="addcard" id="add" @click="tempimage">
          +
        </div>
      </div>

      <div class="black-box" v-if="showcard" @click="showcard = !showcard"></div>
      <div v-if="showcard" class="inbox">
        <label for="file">
          <img :src="tempImg || defaultImage" />
        </label>
        <input type="file" accept="image/*" id="file" @change="uploadImg" />
        <textarea v-model="tempDes"></textarea>
        <button class="submit" @click="addimagedes">등록</button>
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
            <button @click="createHash"><i class="fas fa-pen nav-icon"></i></button>
          </div>
        </div>
        <div v-for="(h, idx) in recipeContent.tag" :key="idx">
          <div class="hash">
            <span># {{ h }}</span>
            <span @click="deleteHash(recipeContent.hashtag_idx[idx], idx)"
              ><i id="hashDelete" class="fas fa-times"></i
            ></span>
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
            <option v-for="(master_name, idx) in master_names" :key="idx" :value="master_name">
              {{ master_name }}
            </option>
          </select>
          <select name="slave_name" id="slave_name" v-model="slaveSelected">
            <option v-for="(slave_name, idx) in slave_names" :key="idx" :value="slave_name">
              {{ slave_name }}
            </option>
          </select>
        </div>
      </div>
      <button class="submit pen" @click="check">
        <h1><i class="fas fa-pen nav-icon"></i></h1>
      </button>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import RecipeApi from '../../api/RecipeApi';
import UserApi from '../../api/UserApi';
import UpdateForm from '../../components/recipe/UpdateForm.vue';
import FirebaseApi from '../../api/FirebaseApi';
import swal from 'sweetalert';

export default {
  data: () => {
    return {
      recipe: null,
      defaultImage: 'https://t1.daumcdn.net/cfile/tistory/24611E4853FDAE0B14',
      recipeContent: [],
      title: '',
      master_names: [],
      slave_names: [],
      masterSelected: '',
      slaveSelected: '',
      deleteHashtag: [],
      temphash: '',
      showcard: false,
      tempImg: '',
      tempDes: '',
    };
  },
  components: {
    UpdateForm,
  },
  methods: {
    tempimage() {
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
      for (let i = 0; i < this.recipeContent.tag.length; i++) {
        if (this.recipeContent.tag[i] === this.temphash) {
          swal({ title: '이미 등록된 해시태그 입니다.', icon: 'warning' });
          this.temphash = '';
          return;
        }
      }
      this.recipeContent.tag.push(this.temphash);
      this.recipeContent.hashtag_idx.push(-1);
      this.temphash = '';
    },
    deleteHash(tagidx, idx) {
      this.recipeContent.tag.splice(idx, 1);
      this.recipeContent.hashtag_idx.splice(idx, 1);
      this.deleteHashtag.push(tagidx);
    },
    updateMaster() {
      this.slave_names = [];
      if (this.masterSelected) {
        for (let achieve of this.Achieves) {
          if (this.masterSelected === achieve.achieve_master_name) {
            this.slave_names.push(achieve.achieve_slave_name);
          }
        }
      }
    },
    uploadImg(e) {
      let file = e.target.files[0];
      FirebaseApi.upLoad(file, (res) => {
        this.tempImg = res;
      });
    },
    addimagedes() {
      this.recipe.push({
        idx: -1,
        img: this.tempImg,
        des: this.tempDes,
        thumbnail: 'false',
      });
      this.tempDes = '';
      this.tempImg = '';
      this.showcard = !this.showcard;
    },
    check() {
      var frm = new FormData();
      frm.append('recipe_idx', this.$route.params.recipe_idx);
      frm.append('title', this.title);
      frm.append('achieve_master', this.masterSelected);
      frm.append('achieve_slave', this.slaveSelected);
      for (let i = 0; i < this.recipe.length; i++) {
        frm.append('content_idx[' + i + ']', this.recipe[i].idx);
        frm.append('des[' + i + ']', this.recipe[i].des);
        frm.append('img[' + i + ']', this.recipe[i].img);
        frm.append('thumbnail[' + i + ']', this.recipe[i].thumbnail);
        frm.append('content_order[' + i + ']', i);
      }
      for (let i = 0; i < this.recipeContent.hashtag_idx.length; i++) {
        frm.append('hashtag_idx[' + i + ']', this.recipeContent.hashtag_idx[i]);
        frm.append('tag[' + i + ']', this.recipeContent.tag[i]);
      }
      if (this.deleteContents.length == 0) {
        const deleteContent = [-1];
        frm.append('deleteContents[0]', deleteContent[0]);
      } else {
        for (let i = 0; i < this.deleteContents.length; i++) {
          frm.append('deleteContents[' + i + ']', this.deleteContents[i]);
        }
      }

      if (this.deleteHashtag.length == 0) {
        this.deleteHashtag = [-1];
        frm.append('deletehashtag[0]', this.deleteHashtag[0]);
      } else {
        for (let i = 0; i < this.deleteHashtag.length; i++) {
          frm.append('deletehashtag[' + i + ']', this.deleteHashtag[i]);
        }
      }

      RecipeApi.updateRecipe(
        frm,
        (res) => {
          this.$router.push({ name: 'FeedMain' });
          this.$store.dispatch('clearFormdata');
        },
        (err) => {
          console.log(err);
          frm = new FormData();
        }
      );
    },
  },
  mounted: function() {
    let data = {
      recipeIdx: this.$route.params.recipe_idx,
      id: this.userId,
    };
    UserApi.singleRecipe(
      data,
      (res) => {
        this.recipe = res.data.recipeContent.recipe_contents;
        this.recipeContent = res.data.recipeContent;
        this.title = res.data.recipeContent.title;
        this.masterSelected = res.data.recipeContent.achieve_master;
        this.slaveSelected = res.data.recipeContent.achieve_slave;
      },
      (error) => {
        console.log(error);
      }
    );
    this.$store.dispatch('clearFormdata');
    RecipeApi.achieveRecipe(
      (res) => {
        this.Achieves = res.data.achieveList;

        for (let achieve of this.Achieves) {
          this.master_names.push(achieve.achieve_master_name);
        }
        this.master_names = new Set(this.master_names);

        for (let achieve of this.Achieves) {
          if (this.masterSelected == achieve.achieve_master_name) {
            this.slave_names.push(achieve.achieve_slave_name);
          }
        }
      },
      (err) => {
        console.log(err);
      }
    );
  },
  computed: {
    ...mapState(['deleteContents']),
  },
};
</script>
<style scoped>
* {
  font-family: 'NanumBarunGothic', sans-serif;
}
.update {
  margin: 0%;
  padding: 0%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.update-form {
  width: 100%;
  max-width: 616px;
  display: flex;
  flex-direction: column;
  align-items: center;
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
  border: 1px solid #ff9636;
  border-radius: 3px;
}
.hash-input input {
  height: 70%;
  background-color: #fafafa;
  border: none;
  margin-right: 2px;
}
.hash-input button {
  height: 70%;
  border-radius: 3px;
  align-items: center;
}
.hash-input svg {
    margin-top: 5px;
  margin-right: 13px;
}

.hash {
  background-color: #ff9636;
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
#hashDelete {
  margin-top: 6px;
  margin-left: 8px;
  color: #fff;
}
.submit {
  width: 95%;
  display: inline;
  background-color: #dad870;
  border-radius: 3px;
  margin-bottom: 5%;
}
#title {
  width: 98%;
  display: inline;
  margin-left: 1%;
  margin-right: 1%;
  margin-bottom: 20px;
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
.submit {
  width: 95%;
  display: inline;
  background-color: #ffbe76;
  color: #ffffff;
  border-radius: 3px;
  margin-bottom: 5%;
}
#file {
  width: 100%;
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
}
.selectbox {
  width: 100%;
}
.pen svg {
  margin-top: 7px;
  padding-top: 4px;
  color: #fff;
}
</style>
