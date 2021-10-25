<template>
  <div class="newsfeed"
  v-infinite-scroll="loadMore" 
  infinite-scroll-disabled="busy" 
  infinite-scroll-distance="20"
  >
    <h1>#{{hashtag}}</h1>
    <div class='feed' v-for="(latestFeed, idx) in hashtagfeed" :key="idx">
        <FeedItem :latestFeed="latestFeed" :idx="idx"/>
    </div>
  </div>
</template>

<script>
import FeedApi from '../../api/FeedApi';
import UserApi from '../../api/UserApi';
import { mapState } from "vuex";
import FeedItem from "../../components/feed/FeedItem.vue";

export default {
  props: ["keyword"],
  data:() => {
    return {
      hashtagfeed: [],
      page:1,
      busy:false,
      hashtag:null,
    }
  },
  components: { 
    FeedItem,
  },
  methods: {
    getPost(){
      this.hashtag = this.$route.query.hashtag.substr(1,this.$route.query.hashtag.length-1);
      
        let data ={
          hashtag: this.hashtag,
          page: this.page++
        }
        FeedApi.hashtagsearch(
        data,
        res => {
          const items = res.data.hashtagfeed.map(item => {
            return item
          })
          if (res.data.hashtagfeed.length == 0){
            this.busy = true
          } else {
            this.busy = false
          }
          this.hashtagfeed = this.hashtagfeed.concat(items)
        },
        error=>{
          console.log(error)
        }
      )
    },
    loadMore(){
      this.busy = true
      setTimeout(()=> {
        this.getPost()
      }, 500);
    }
  },
  created() {
    this.getPost()
  },
  computed: {
    ...mapState([
      'userId',
    ]),
  }
}
</script>
<style scoped>
.newsfeed{
  display: flex;
  align-items: center;
  flex-direction: column;
}
.feed{
  width: 100%;
  display: flex;
  justify-content: center;
}
</style>
