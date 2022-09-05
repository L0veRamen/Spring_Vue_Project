<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px;" placeholder="Please enter name" suffix-icon="el-icon-search"
                v-model="name"></el-input>
<!--      <el-input style="width: 200px;" placeholder="Please enter description" suffix-icon="el-icon-message"-->
<!--                v-model="description"></el-input>-->

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
<!--      <el-upload-->
<!--          action="http://localhost:9090/user/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">-->
<!--        <el-button type="primary" class="ml-10">Import<i class="el-icon-bottom"></i></el-button>-->
<!--      </el-upload>-->
<!--      <el-button type="primary" class="ml-10" @click="exp">Export<i class="el-icon-top"></i></el-button>-->

    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55"></el-table-column>

      <el-table-column prop="id" label="ID" width="80"/>
      <el-table-column prop="name" label="Role Name" />
      <el-table-column prop="flag" label="Unique"/>
      <el-table-column prop="description" label="Description"/>

      <el-table-column label="Edit"  width="350" align="center">
        <template slot-scope="scope">
          <el-button type="info" @click="selectMenu(scope.row)">Menu<i class="el-icon-menu"></i></el-button>
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
    <el-dialog title="Role Info" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="90px" size="small">
        <el-form-item label="Role Name" >
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Unique">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Info" >
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Menu Management" :visible.sync="menuDialogVis" width="30%">
      <el-tree
          :props="props"
          :data="menuData"
          show-checkbox
          node-key="id"
          ref="tree"
          :default-expanded-keys="expends"
          :default-checked-keys="checks">
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span><i :class="data.icon"></i> {{ data.name }}</span>
         </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVis = false">Cancel</el-button>
        <el-button type="primary" @click="saveRoleMenu">Confirm</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Role",
  data(){
    return{
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10, //show how many item on one page
      name:"",
      form: {},
      multipleSelection: [],
      dialogFormVisible: false,
      menuDialogVis: false,
      menuData:[],
      props:{
        label: 'name',
      },
      checks:[],
      expends: [],
      roleId: 0,
      ids: []
    }
  },
  created(){
    this.load();// load back end data
  },
  methods:{
    reset() {
      this.name = '';
      this.load();
    },
    load() {
      this.request.get("/role/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        console.log(res);
        this.tableData = res.data.records;
        this.total = res.data.total;
      })

      // this.request.get("/menu/ids").then(r => {
      //   this.ids = r.data
      // })
    },
    save(){
      this.request.post("/role", this.form).then(res => {
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
    saveRoleMenu() {
      // console.log(this.$refs.tree.getCheckedKeys())
     this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
       console.log(res)
       if (res.code === '200') {
         this.$message.success("Bind successfully !")
         this.menuDialogVis = false

       } else {
         this.$message.error(res.msg)
       }
     })

    },
    handleEdit(row){
      this.form = row;
      this.dialogFormVisible = true;
    },
    handleDelete(id){
      this.request.delete(`/role/${id}`).then(res => {

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
      this.request.post("/role/del/batch",ids).then(res => {
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
    async selectMenu(role){
      this.menuDialogVis = true;
      this.roleId = role.id

      // 请求菜单数据
      this.request.get("/menu").then(res => {
        this.menuData = res.data

        // 把后台返回的菜单数据处理成 id数组
        this.expends = this.menuData.map(v => v.id)
      })

      this.request.get("/role/roleMenu/" + this.roleId).then(res => {
        console.log(res)
        this.checks = res.data
        this.ids.forEach(id => {
          if (!this.checks.includes(id)) {
            // 可能会报错：Uncaught (in promise) TypeError: Cannot read properties of undefined (reading 'setChecked')
            this.$nextTick(() => {
              this.$refs.tree.setChecked(id, false)
            })
          }
        })
        this.menuDialogVis = true
      })
    },

  }
}
</script>

<style>
.headerBg {
  background-color: #ccc !important;
}
</style>