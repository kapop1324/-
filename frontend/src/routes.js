

import Login from './views/user/Login.vue'
import Join from './views/user/Join.vue'
import ChangePassword from './views/user/ChangePassword.vue'
import JoinComplete from './views/user/JoinComplete.vue'
import FeedMain from './views/feed/Main.vue'
import StarMain from './views/feed/Star.vue'
import Components from './views/Components.vue'
import Pagenotfound from './views/error/PageNotFound.vue'
import Profile from './views/profile/Profile.vue'
import Update from './views/user/Update.vue'
import Findpw from './views/user/Findpw.vue'
import Recipewrite from './views/recipe/Write.vue'
import FollowingList from './views/profile/FollowingList.vue'
import FollowerList from './views/profile/FollowerList.vue'
import RecipeDetail from './views/recipe/RecipeDetail.vue'
import Allmember from './views/feed/Allmember.vue'
import Likeposts from './views/feed/Likeposts.vue'
import Hashtagsearch from './views/feed/Hashtagsearch.vue'
import UpdateRecipe from './views/recipe/UpdateRecipe.vue'
import AchieveList from './views/achieve/AchieveList.vue'
import Rankinglist from './views/ranking/ranking.vue'
import AchieveDetail from './views/achieve/AchieveDetail.vue';
import Joinselect from './views/user/Joinselect.vue';

export default [
    

    {
        path : '/',
        name : 'Login',
        component : Login
    },
    {
        path : '/user/join',
        name : 'Join',
        component : Join
    },
    {
        path : '/user/update',
        name : 'Update',
        component : Update
    },
    {
        path : '/feed/main',
        name : 'FeedMain',
        component : FeedMain
    },
    {
        path : '/components',
        name : 'Components',
        component : Components
    },
    {
        path:'/user/changepassword',
        name:'ChangePassword',
        component:ChangePassword
    },
    {
        path:'/user/join/complete',
        name:'JoinComplete',
        component:JoinComplete
    },
    {
        path:'/user/findpw',
        name:'Findpw',
        component:Findpw
    },
    {
        path: '/recipe/write',
        name: 'Recipewrite',
        component: Recipewrite
    },
    {
        path: '/user/followerlist/:user_id',
        name: 'FollowerList',
        component: FollowerList
    },
    {
        path: '/user/followinglist/:user_id',
        name: 'FollowingList',
        component: FollowingList
    },
    {
        path: '/user/profile/:user_id',
        name: 'Profile',
        component: Profile
    },
    {
        path: '/recipe/:recipe_idx',
        name: 'RecipeDetail',
        component: RecipeDetail
    },
    {
        path: '/recipe/update/:recipe_idx',
        name: 'UpdateRecipe',
        component: UpdateRecipe
    },
    {
        path: '/feed/allmember',
        name: 'Allmember',
        component: Allmember
    },
    {
        path: '/feed/likes',
        name: 'Likeposts',
        component: Likeposts
    },
    {
        path: '/feed/hashtag',
        name: 'Hashtagsearch',
        component: Hashtagsearch
    },
    {
        path: '/feed/stars',
        name: 'StarMain',
        component: StarMain
    },
    {
        path: '*',
        redirect:'/404'
    },
    {
        path: '/404',
        component:Pagenotfound
    },
    {
        path:'/achievelist',
        name:'Achievelist',
        component:AchieveList
    },
    {
        path:'/ranking/rankinglist',
        name:'Rankinglist',
        component:Rankinglist
    },

    {
        path: '/achievelist/:idx',
        name: 'AchieveDetail',
        component: AchieveDetail,
    },
    {
        path: '/user/joinselect',
        name: 'Joinselect',
        component: Joinselect,
    },

    


    

]
