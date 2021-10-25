<template lang="">
  <div class="comment">
    <div class="commentForm" id="commentForm">
      <!-- {{ comments }} -->
      <RecipeCommentItem v-for="(commentItem, idx) in comments" :comments="comments" :key="idx" :idx="idx" :commentItem="commentItem" :recipeItem="recipeItem" :selectedContent="selectedContent"/>
    </div>
    <div class="commentInput">
      <input type="text" placeholder="댓글을 입력하시오" v-model.trim="comment.content" @keypress.enter="createComment" style="height: 5vh;">
      <button @click="createComment">작성</button>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import RecipeApi from '../../api/RecipeApi';
import FirebaseApi from '../../api/FirebaseApi';
import RecipeCommentItem from "../../components/recipe/RecipeCommentItem.vue";

export default {
  components:{
    RecipeCommentItem,
  },
  props: {
    recipeItem: {
      type: [Array, Object]
    },
    recipeContent: {
      type: [Array, Object]
    },
    selectedContent: Number,
  },
  data: function() {
    return {
      comment: {
        content: '',
      },
      comments: [],
    }
  },
  mounted: function() {
    this.getComment()
  },
  methods: {
    createComment() {
      const newComments = this.comments
      let data = {
        content_idx: this.recipeItem.idx,
        comment: this.comment.content,
        id: this.userId,
        nickname: this.userNickname,
      }
      RecipeApi.writeComment(
        data,
        res => {
          this.getComment()
          this.comment.content = ''
          document.getElementById("commentForm").scrollTop   = 0;
        },
        error=> {
        }
      )
      this.comments = newComments
      let notice = {
        user:this.recipeContent.id,
        img:this.$store.state.userImg,
        ReqUser:this.$store.state.userId,
        type:'comment',
        articleID:this.recipeContent.recipe_contents[0].recipe_idx
      }
      FirebaseApi.noticeAdd(notice)
    },
    getComment() {
      let data = {
      content_idx: this.recipeItem.idx,
      id: this.userId,
    }
    RecipeApi.recipeItemComments(
      data,
      res => {
        if(res.data.commentList){
          this.comments = res.data.commentList.reverse()
        }
        this.$store.dispatch('getCommentCount',{idx: this.selectedContent, count: this.comments.length})
        this.$store.dispatch('countComment',this.commentCountList)
      },
      err => {
        console.log(err)
      }
    )
    }
  },
  computed: {
    ...mapState([
      'userId',
      'userNickname',
      'selectRecipeId',
      'commentCountList'
    ])
  }
}
</script>

<style scoped>
  .commentForm {
    margin-top: 10px;    
    overflow-y: scroll;
    overflow-x: hidden;
    flex-wrap: wrap;
    width: 100%;
    height: 74vh;
  }
  .commentInput{
  position: absolute;
  bottom: 0px;
  width: 100%;
  height: 5vh;
  display: flex;
  }

  .commentInput input{
    border: 0px;
    width: 59vh;
  }
  .commentInput button{
    background-color: #FF9636;
    width: 10vh;
    height: 4vh;
    border-radius: 15px;
    color: #fff;
    padding-top: 2px;
    font-size: 18px;
  }

  .commentForm::-webkit-scrollbar {
    width: 6px;
  }
  .commentForm::-webkit-scrollbar-track {
    background-color: transparent;
  }
  .commentForm::-webkit-scrollbar-thumb {
    border-radius: 3px;
    background-color: gray;
  }
  .commentForm::-webkit-scrollbar-button {
    width: 0;
    height: 0;
  }
</style>
