<template lang="">
  <div class="">
    <!-- 레시피 제목 : {{ recipeTitle }} -->
    <RecipeDetailList :recipe="recipe" :recipeContent="recipeContent" :thumbnailImg="thumbnailImg"/>
  </div>
</template>

<script>
import RecipeDetailList from "../../components/recipe/RecipeDetailList.vue";
import UserApi from '../../api/UserApi';
import { mapState } from "vuex";


export default {
  components:{
    RecipeDetailList,
  },
  data: () => {
    return {
      recipe: [],
      recipeTitle: '',
      recipeContent: [],
      thumbnailImg: '',
    }
  },
  mounted: function() {
    let data= {
      recipeIdx: this.$route.params.recipe_idx,
      id: this.userId
    }
    UserApi.singleRecipe(
      data,
      res => {
        this.recipe = res.data.recipeContent.recipe_contents
        this.recipeContent =  res.data.recipeContent
        this.recipeTitle = res.data.recipeContent.title
        this.$store.dispatch('getCommentCountList',res.data.recipeContent.commentCount)
        for (let i=0; i<this.recipe.length; i++){
          if(this.recipe[i].thumbnail === "true"){
            this.thumbnailImg = this.recipe[i].img
          }
        }
      },
      error => {
        console.log(error)
      }
    )
  },
  computed: {
    ...mapState([
      'userId',
      'commentCount',
      'commentCountList'
    ]),
  }
}
</script>

<style lang="">
  
</style>