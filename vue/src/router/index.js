import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";
Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Manage',
        component: () => import("../views/Manage.vue"),
        redirect: "/home",
        children: [
            {path: 'home', name: 'Home', component: () => import("../views/Home.vue")},
            {path: 'user', name: 'User', component: () => import("../views/User.vue")},
            {path: 'role', name: 'Role', component: () => import("../views/Role.vue")},
            {path: 'menu', name: 'Menu', component: () => import("../views/Menu.vue")},
            {path: 'person', name: 'Person', component: () => import("../views/Person.vue")},
            {path: 'file', name: 'File', component: () => import("../views/File.vue")},

        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue')
    },
    {
        path: '/register',
        name: 'register',
        component: () => import('../views/Register.vue')
    },

]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

//路由守卫
router.beforeEach((to, from, next) => {
    localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称
    store.commit("setPath") // 触发store的数据更新

    // // 未找到路由的情况
    // if (!to.matched.length) {
    //     const storeMenus = localStorage.getItem("menus")
    //     if (storeMenus) {
    //         next("/404")
    //     } else {
    //         // 跳回登录页面
    //         next("/login")
    //     }
    // }
    // // 其他的情况都放行
    next()
})


export default router
