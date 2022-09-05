<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px;" placeholder="Please enter name" suffix-icon="el-icon-search"
                v-model="username"></el-input>
      <el-input style="width: 200px;" placeholder="Please enter email" suffix-icon="el-icon-message"
                v-model="email"></el-input>
      <el-input style="width: 200px;" placeholder="Please enter address" suffix-icon="el-icon-location"
                v-model="address"></el-input>
      <el-button class="ml-5" type="primary" @click="load">Search</el-button>
      <el-button class="ml-5" type="warning" @click="reset">Reset</el-button>

    </div>
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd"  >New<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='Confirm'
          cancel-button-text='Cancel'
          icon="el-icon-info"
          icon-color="red"
          title="Are you confirm to delete datas?"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference" class="ml-5">Multiple Delete<i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <el-upload
          action="http://localhost:9090/user/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-10">Import<i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" class="ml-10" @click="exp">Export<i class="el-icon-top"></i></el-button>

    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>

      <el-table-column prop="id" label="ID" width="80"/>
      <el-table-column prop="username" label="User Name" width="120"/>
      <el-table-column prop="role" label="Role">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.role === 'ROLE_ADMIN'">Admin</el-tag>
          <el-tag type="info" v-if="scope.row.role === 'ROLE_USER'">User</el-tag>
          <el-tag type="warning" v-if="scope.row.role === 'ROLE_TEACHER'">Teacher</el-tag>
          <el-tag type="success" v-if="scope.row.role === 'ROLE_STUDENT'">Student</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="NickName" />
      <el-table-column prop="email" label="Email" />
      <el-table-column prop="phone" label="Phone" />
      <el-table-column prop="address" label="Address"/>



      <el-table-column label="Edit" align="center" width="220" >
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">Edit<i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Confirm'
              cancel-button-text='Cancel'
              icon="el-icon-info"
              icon-color="red"
              title="Are you confirm to delete data?"
              @confirm="handleDelete(scope.row.id)"
          >
            <el-button type="danger" slot="reference">Delete<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
    <el-dialog title="User Info" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="90px" size="small">
        <el-form-item label="User Name" >
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Role Name">
          <el-select clearable v-model="form.role" placeholder="Please Select Role" style="width: 100%">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Nick Name" >
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Email" >
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Phone" >
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Address" >
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "User",
  data(){
    return{
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10, //show how many item on one page
      username: '',
      nickname: '',
      email: '',
      address: '',
      form: {},
      multipleSelection: [],
      dialogFormVisible: false,
      roles: [],
    }
  },
  created(){
    this.load();// load back end data
  },
  methods:{
    reset() {
      this.username = '';
      this.email = '';
      this.address = '';
      this.load();
    },
    load() {
      // this.request.get("http://localhost:9090/user/page?pageNum="+this.pageNum+ "&pageSize="+this.pageSize+"&username="+this.username)
      //     .then(res =>{
      //       console.log(res);
      //     })
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          email: this.email,
          address: this.address
        }
      }).then(res => {
        console.log(res);
        this.tableData = res.data.records;
        this.total = res.data.total;
      })

      this.request.get("/role").then(res => {
        this.roles = res.data
      })
    },
    save(){
      this.request.post("/user", this.form).then(res => {
        console.log(res);
        if (res.code === '200'){
          this.$message.success("Save Form Data Successfully! ");
          this.dialogFormVisible = false;
          this.load();
        }else{
          this.$message.error("Save Form Data Fail! ")
        }
      })
    },
    handleEdit(row){
      this.form = row;
      this.dialogFormVisible = true;
    },
    handleDelete(id){
      this.request.delete(`/user/${id}`).then(res => {

        if (res.code === '200'){
          this.$message.success("Delete Data Successfully! ");
          this.dialogFormVisible = false;
          this.load();
        }else{
          this.$message.error("Delete Data Fail! ")
        }
      })
    },
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id); // [{},{}] => [1,2,3]
      this.request.post("/user/del/batch",ids).then(res => {
        console.log(res);
        if (res.code === '200'){
          this.$message.success("Delete Data Successfully! ");
          this.dialogFormVisible = false;
          this.load();
        }else{
          this.$message.error("Delete Data Fail! ")
        }
      })
    },
    handleSelectionChange(val){
      console.log(val);
      this.multipleSelection = val;
    },
    handleAdd(){
      this.dialogFormVisible = true;
      this.form = {};
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
    exp(){
      window.open("http://localhost:9090/user/export")
    },
    handleExcelImportSuccess(){
      this.$message.success("Import Successfully ! ")
      this.load()
    }
  }
}
</script>

<style>
.headerBg {
  background-color: #ccc !important;
}
</style>