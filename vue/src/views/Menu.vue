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

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" row-key="id" default-expand-all @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55"></el-table-column>

      <el-table-column prop="id" label="ID" width="80"/>
      <el-table-column prop="name" label="Menu Name" />
      <el-table-column prop="path" label="Path"/>
      <el-table-column label="Icon" class-name="fontSize30" align="center" label-class-name="fontSize12">
        <template slot-scope="scope">
          <span :class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="description" label="Description"/>

      <el-table-column label="Edit"  width="340" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid && !scope.row.path">New Menu<i class="el-icon-menu"></i></el-button>
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

    <el-dialog title="Menu Info" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="90px" size="small">
        <el-form-item label="Menu Name" >
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Path">
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Icon">
          <el-select clearable v-model="form.icon" placeholder="Please Select" style="width: 100%">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value" /> {{ item.name }}
            </el-option>
          </el-select>
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

    <!--    <el-dialog title="menu" :visible.sync="menuDialogVis" width="30%">-->
    <!--      <el-tree-->
    <!--          :props="props"-->
    <!--          :data="menuData"-->
    <!--          show-checkbox-->
    <!--          node-key="id"-->
    <!--          ref="tree"-->
    <!--          :default-expanded-keys="expends"-->
    <!--          :default-checked-keys="checks">-->
    <!--         <span class="custom-tree-node" slot-scope="{ node, data }">-->
    <!--            <span><i :class="data.icon"></i> {{ data.name }}</span>-->
    <!--         </span>-->
    <!--      </el-tree>-->
    <!--      <div slot="footer" class="dialog-footer">-->
    <!--        <el-button @click="menuDialogVis = false">取 消</el-button>-->
    <!--        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>-->
    <!--      </div>-->
    <!--    </el-dialog>-->
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
      path:"",
      icon:"",
      description:"",
      form: {},
      multipleSelection: [],
      dialogFormVisible: false,
      options:[]
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
      this.request.get("/menu", {
        params: {
          name: this.name,

        }
      }).then(res => {
        console.log(res);
        this.tableData = res.data
      })
    },
    save(){
      this.request.post("/menu", this.form).then(res => {
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
    handleAdd(pid){
      this.dialogFormVisible = true;
      this.form = {};
      if (pid) {
        this.form.pid = pid
      }
    },

    handleEdit(row){
      this.form = row;
      this.dialogFormVisible = true;

      // 请求图标的数据
      this.request.get("/menu/icons").then(res => {
        this.options = res.data
      })

    },
    handleDelete(id){
      this.request.delete(`/menu/${id}`).then(res => {

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
      this.request.post("/menu/del/batch",ids).then(res => {
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

    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
    // exp(){
    //   window.open("http://localhost:9090/menu/export")
    // },
    // handleExcelImportSuccess(){
    //   this.$message.success("Import Successfully ! ")
    //   this.load()
    // }
  }
}
</script>

<style>
.headerBg {
  background-color: #ccc !important;
}
.fontSize30{
  font-size: 30px;
}
.fontSize12{
  font-size: 12px;
}
</style>