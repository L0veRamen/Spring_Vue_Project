<template>
  <div class="wrapper">
    <div
        style="margin: 200px auto; background-color: #fff; width: 350px; height: 300px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>LOG IN</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: center ">
          <el-button  round type="warning" size="medium" autocomplete="off" @click="$router.push('/register')">Register</el-button>
          <el-button  round type="primary" size="medium" autocomplete="off" @click="login">Login</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>

export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        username: [
          {required: true, message: 'Please enter username', trigger: 'blur'},
          {min: 3, max: 10, message: '3 to 5 characters', trigger: 'blur'}
        ],
        password: [
          {required: true, message: 'Please enter password', trigger: 'blur'},
          {min: 1, max: 20, message: '5 to 20 characters', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) { // log in form is valid
          this.request.post("/user/login", this.user).then(res => {
            if (res.code === "200") {
              localStorage.setItem("user", JSON.stringify(res.data)) // save user info to chrome
              localStorage.setItem("menus", JSON.stringify(res.data.menus))
              this.$router.push("/")
              this.$message.success("Successfully Login !")
            } else {
              this.$message.error("Username or Password is not correct !")
            }
          })
        }
      });
    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #7FBCD2, #FFAE6D);
  overflow: hidden;
}
</style>
