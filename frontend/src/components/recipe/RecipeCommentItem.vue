<template lang="">
  <div>
    <div class="comment">
      <!-- {{ commentItem }} -->
      <div class="commentContent_header">
        <!-- {{ commentItem }} -->
        <div class="commentContent_left">
          <p style="margin-left:5px;">{{ commentItem.nickname }}</p>
          <p style="font-size:11px; margin-left:6px; margin-top:4px;">{{ commentItem.regdate | timeFor }}</p>
          <div v-if="update == false">
            <div v-if="commentItem.id == userId && update==false">
              <button @click="update = true">수정</button>
              <button @click="deleteComment(idx)">삭제</button>
            </div>
          </div>
          <div v-if="update">
            <button @click="update = false" >완료</button>
          </div>
        </div>
        <div class="commentContent_right">
          <span v-show="commentItem.likecheck == false">
            <button class="submit" @click="like">
             <i class="far fa-thumbs-up"></i>
            </button>
          </span>
          <span v-show="commentItem.likecheck">
            <button class="submit" @click="unlike">
              <i class="fas fa-thumbs-up"></i>
            </button>   
          </span>
          <p style="margin-left: 2px; margin-right: 10px;">{{ commentItem.like }}</p>
        </div>
      </div>
      <p v-if="update==false" class="comment_des">{{ commentItem.comment }}</p>
    </div>
    <div v-if="update">
      <input style="margin-left: 5px;" @keyup.enter="update=false" type="text" v-model.trim="commentItem.comment">
    </div>
    <hr>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import RecipeApi from '../../api/RecipeApi';

export default {
  data: () => {
    return {
      update: false,
    }
  },
  props: {
    comments : {
      type: [Array, Object],
    },
    commentItem: {
      type: [Array, Object],
    },
    recipeItem: {
      type: [Array, Object],
    },
    selectedContent: Number,
    idx: Number,
  },
  methods: {
    like() {
      let data = {
        comment_idx: this.commentItem.idx,
        id: this.userId
      }
      RecipeApi.commentLike(
        data,
        res => {
          this.commentItem.likecheck = true
          this.commentItem.like += 1
        },
        err => {
          console.log(err)
        }
      )
    },
    unlike() {
      let data = {
        comment_idx: this.commentItem.idx,
        id: this.userId
      }
      RecipeApi.commentUnlike(
        data,
        res => {
          this.commentItem.likecheck = false
          this.commentItem.like -= 1
        },
        err => {
          console.log(err)
        }
      )
    },
    updateComment() {
      let data = {
        idx: this.commmentItem.idx,
        comment: this.commentItem.comment,
      }
      RecipeApi.updateComment(
        data,
        res => {
        },
        error=> {
          console.log(error)
        }
      )

    },
    deleteComment(idx) {
      const newComments = this.comments
      let data = {
        idx: this.commentItem.idx
      }
      RecipeApi.deleteComment(
        data,
        res => {
          newComments.splice(idx,1)
          this.getComment()
        },
        error=> {
          console.log(error)
        }
      )
      this.comments = newComments
    },
    getComment() {
      let data = {
      content_idx: this.recipeItem.idx,
      id: this.userId,
    }
    RecipeApi.recipeItemComments(
      data,
      res => {
        if(res.data){
        this.$store.dispatch('getCommentCount',{idx: this.selectedContent, count: res.data.commentList.length})
        }
        else{
        this.$store.dispatch('getCommentCount',{idx: this.selectedContent, count: 0})
        }
        this.$store.dispatch('countComment',this.commentCountList)
      }
    )
    },
  },
  filters : {
    timeFor : function(created_at){
      if(created_at){
      var a = created_at.slice(0,4)
      a += '.'
      a += created_at.slice(5,7)
      a += '.'
      a += created_at.slice(8,10)
      a += ' '
      a += created_at.slice(11,16)
      return a
      }
    }
  },
  computed: {
    ...mapState([
      'userId',
      'userNickname',
      'commentCountList'
    ])
  }
}
</script>

<style scoped>
.comment{
  padding: 5px;
}
.comment p{
  margin: 0px;
}
.commentContent_header{
  display:flex;
  justify-content: space-between;
}
.commentContent_left{
  display: flex;
}
.commentContent_right{
  display: flex;
}
.comment_des{
  padding: 7px; 
  display: flex; 
  flex-wrap: wrap;
  word-break: normal;
}
hr {
    background: gray;
    margin: 0.5rem 0;
}
.commentContent_left button{
  background: rgba(0,0,0,0.5);
  width: 30px;
  height: 20px;
  border-radius: 3px;
  font-size: 14px;
  margin-left:4px;
  color: #fff;
}
</style>
