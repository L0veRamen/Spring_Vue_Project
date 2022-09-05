<template>
  <div>
    <el-container style="min-height: 100vh">
      <el-aside :width=" sideWidth +'px'"
                style="background-color: rgb(238, 241, 246); box-shadow: 2px 0 6px rgb(0, 21, 41, 0.35);">
        <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow"/>
      </el-aside>

      <el-container>
        <el-header style="border: 1px solid #ccc; ">
          <Header :collapse-btn-class="collapseBtnClass" @asideCollapse="collapse" :user="user"/>
        </el-header>

        <el-main>
          <!--          current page children router will display inside router view-->
          <router-view @refreshUser="getUser"/>
        </el-main>

      </el-container>
    </el-container>
  </div>
</template>

<script>
import Aside from "@/components/Aside"
import Header from "@/components/Header"


export default {
  name: 'Manage',
  components: {
    Aside,
    Header
  },
  created() {
    // 从后台获取最新的User数据
    this.getUser()
  },
  data() {
    return {
      collapseBtnClass: 'el-icon-s-unfold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      user: {}
    }
  },
  methods: {
    collapse() {
      this.isCollapse = !this.isCollapse;
      if (this.isCollapse) {
        this.sideWidth = 64;
        this.collapseBtnClass = 'el-icon-s-unfold';
        this.logoTextShow = false;
      } else {
        this.sideWidth = 200;
        this.collapseBtnClass = 'el-icon-s-fold';
        this.logoTextShow = true;
      }
    },
    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      if (username) {
        // retrieve data from database
        this.request.get("/user/username/" + username).then(res => {
          // replace latest user data
          this.user = res.data
        })
      }
    }
  }
}
</script>
