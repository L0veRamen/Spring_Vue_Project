<template>
  <el-menu :default-openeds="['1', '3']" style="min-height: 100%; overflow-x: hidden;"
           background-color="rgb(48,65,86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
           @select="handleSelect"
  >
    <div style="height: 60px; line-height: 60px; text-align: center">
      <img src="../assets/logo.png" alt="logo"
           style="width: 20px; position: relative; top: 3px; margin-right: 5px">
      <b style="color: whitesmoke" v-show="logoTextShow">Admin System</b>
    </div>

    <el-menu-item index="/">
      <template slot="title">
        <i class="el-icon-house"></i>
        <span slot="title">Home</span>
      </template>
    </el-menu-item>

    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">Management</span>
      </template>
      <el-menu-item index="/user">
        <i class="el-icon-s-custom"></i>
        <span slot="title">User</span>
      </el-menu-item>
      <el-menu-item index="/role">
        <i class="el-icon-s-claim"></i>
        <span slot="title">Role</span>
      </el-menu-item>
      <el-menu-item index="/menu">
        <i class="el-icon-s-order"></i>
        <span slot="title">Menu</span>
      </el-menu-item>
      <el-menu-item index="/file">
        <i class="el-icon-document"></i>
        <span slot="title">File</span>
      </el-menu-item>

    </el-submenu>

  </el-menu>

</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  },
  methods: {
    handleSelect(index) {
      console.log(index)
      console.log(this.$router.options.routes)
    }
  }
}
</script>

<style scoped>

</style>