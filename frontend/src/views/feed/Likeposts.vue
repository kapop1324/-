<template>
  <div class="newsfeed"
  v-infinite-scroll="loadMore" 
  infinite-scroll-disabled="busy" 
  infinite-scroll-distance="10"
  >
    <div 
    class='feed' 
    v-for="(latestFeed, idx) in latestFeeds" 
    :key="idx"
    >
      <FeedItem
        :latestFeed="latestFeed" 
        :idx="idx" 
        />
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
      latestFeeds: [],
      page:1,
      busy:false
    }
  },
  components: { 
    FeedItem,
  },
  methods: {
    getPost(){
      let data = {
        id: this.userId,
        page: this.page++
      }
      FeedApi.likeposts(
        data,
        res => {
          const items = res.data.latestPosts.map(item => {
            return item
          })
          if (res.data.latestPosts.length == 0){
            this.busy = true
          } else {
            this.busy = false
          }
          this.latestFeeds = this.latestFeeds.concat(items)
        },
        error => {
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
  created: function() {
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
