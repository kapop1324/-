

<template>
    <div class="form">
    <div class="check-box">
      <div v-if="recipe_file.thumbnail===true" class="thumbnail" @click="isThumbnail(idx)"><i class="fas fa-thumbtack"></i></div>
      <div v-else class="thumbnail-none" @click="isThumbnail(idx)"><i class="fas fa-thumbtack"></i></div>
      <button @click="deleteContent(idx)">
        <i class="fas fa-minus-circle" style="color:#FF5C4D;"></i>
      </button>
    </div>
    <div class="image-box" :style="{ backgroundImage: `url(${recipe_file.img})` }"  @click="updateCard(idx)">
    </div>
    <div class="black-box" v-if="showUpdate" @click="showUpdate = !showUpdate"></div>
      <div v-if="showUpdate" class="inbox">
      <label for="file">
        <img :src="recipe_file.img" />
      </label>
      <input type="file" accept="image/*" id="file" @change="uploadImg" />
      <textarea v-model="recipe_file.des"></textarea>
      <button class="submit" @click="updateImage">수정</button>
    </div>
    <div class="leftright">
      <button @click="leftContent">
        <i class="fas fa-angle-double-left"></i>
      </button>
      <button @click="rightContent">
        <i class="fas fa-angle-double-right"></i>
      </button>
    </div>
    </div>
</template>

<script>
import { mapActions } from 'vuex'
import UserApi from '../../api/UserApi';
import FirebaseApi from '../../api/FirebaseApi';

export default {
  props: {
    recipe_file: {
      type: [Array, Object]
    },
    recipe: {
      type: [Array, Object]
    },
    idx: Number
  },
  data: () => {
    return {
        recipeItemDes: [],
        recipeItem: [],
        showUpdate:false,
    }
  },
  mounted: function(){
    if (this.recipe_file.thumbnail ==="true"){
      this.recipe_file.thumbnail = true
    }
  },
  methods: {
    leftContent() {
      if (this.idx >= 1) {
        const content = this.recipe[this.idx - 1]
        this.idx -= 1
        this.recipe_file.content_order -= 1
        
        this.recipe[this.idx] = this.recipe[this.idx + 1]
        this.recipe[this.idx + 1] = content
        this.recipe[this.idx + 1].content_order += 1
      }
    },
    rightContent() {
      if (this.idx < this.recipe.length-1) {
        const content = this.recipe[this.idx + 1]
        this.idx += 1
        this.recipe_file.content_order += 1

        this.recipe[this.idx] = this.recipe[this.idx - 1]
        this.recipe[this.idx - 1] = content
        this.recipe[this.idx - 1].content_order -= 1
      }
    },
    uploadImg(e){
      let file = e.target.files[0];
      FirebaseApi.upLoad(
        file,
        res=>{
          this.recipe_file.img = res
        })
    },
    deleteContent: function (idx) {
      this.recipe.splice(idx,1)
      this.$store.dispatch('deleteContent',this.recipe_file.idx)
    },
    isThumbnail(idx) {
      this.recipe_file.thumbnail = true
      for(let i=0;i<this.recipe.length; i++){
        if(idx!=i){
          this.recipe[i].thumbnail = false
        }
      }
    },
    updateCard(idx) {
      this.showUpdate = true
    },
    updateImage(){
      this.showUpdate = !this.showUpdate
    },
  },
}
</script>
<style scoped>
.form{
  width: 100%;
  height: 100%;
}
.thumbnail,
.thumbnail-none {
  cursor: pointer;
  display: flex;
  justify-content: center;
  margin: 5px;
}
.thumbnail-none svg{
  color: rgba(155, 155, 155, 0.5);
}
.image-box {
  width: 100%;
  height: 80%;
  overflow: hidden;
  background-size:cover;
  background-repeat: no-repeat;
  background-position: center center;
}
.image {
  object-fit:contain;
  transform: translate(-20%);
}
#file {
  width: 100%;
}
.content {
  width: 100%;
}
.check-box {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 10%;
  padding-left: 2px;
  padding-right: 4px;
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
  height: 10%;
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
</style>

