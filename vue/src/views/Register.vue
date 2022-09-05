<template>
  <div class="wrapper">
    <div style="margin: 100px auto; background-color: #fff; width: 350px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>Registration</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input placeholder="Please enter username" size="medium" style="margin: 5px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="Please enter password" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="Confirm enter password" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="user.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item style="margin: 5px 0; text-align: right">
          <el-button round type="primary" size="medium"  autocomplete="off" @click="login">Register</el-button>
          <el-button round type="warning" size="medium"  autocomplete="off" @click="$router.push('/login')">Back To Login</el-button>
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
          { required: true, message: 'Please enter username', trigger: 'blur' },
          { min: 3, max: 10, message: 'characters from 3 to 10', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Please enter password', trigger: 'blur' },
          { min: 1, max: 20, message: 'characters from 1 to 20', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: 'Confirm enter password', trigger: 'blur' },
          { min: 1, max: 20, message: 'characters from 1 to 20', trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("Please make sure enter password correctly")
            return false
          }
          this.request.post("/user/register", this.user).then(res => {
            if(res.code === '200') {
              this.$message.success("Registration Successfully !")
            } else {
              this.$message.error(res.msg)
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
