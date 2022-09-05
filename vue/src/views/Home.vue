<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 60px">
      <el-col :span="6">
        <el-card style="color: #409EFF">
          <div><i class="el-icon-user-solid" /> Total User</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #F56C6C">
          <div><i class="el-icon-money" /> Total Sales</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            $1000000
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67C23A">
          <div><i class="el-icon-bank-card" /> Total Revenue</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            $300000
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #E6A23C">
          <div><i class="el-icon-s-shop" /> Total Store</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            20
          </div>
        </el-card>
      </el-col>
    </el-row>


    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px; height: 450px"></div>
      </el-col>
      <el-col  :span="12">
        <div id="pie" style="width: 500px; height: 400px"></div>
      </el-col>
    </el-row>





  </div>
</template>

<script>
import * as echarts from 'echarts';



export default {
  name: "Home",
  data(){
    return{

    }
  },
  mounted() { // 页面元素渲染之后再触发

    let option = {
      title: {
        text: 'Total User by 4Q ',
        subtext: 'Bar Line Chart',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      xAxis: {
        type: 'category',
        data: ["Q1", "Q2", "Q3", "Q4"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name:"StarBucks",
          data: [],
          type: 'line'
        },
        {
          name:"StarBucks",
          data: [],
          type: 'bar'
        },
        {
          name:"TimHortons",
          data: [],
          type: 'line'
        },
        {
          name:"TimHortons",
          data: [],
          type: 'bar'
        }
      ]
    };

    // pie chart

    let pieOption = {
      title: {
        text: 'Total Q4 User Statistics',
        subtext: 'Pie Chart',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name: 'TimHortons: ',
          type: 'pie',
          radius: '55%',
          center: ['25%', '70%'],
          label:{            //饼图图形上的文本标签
            normal:{
              show:true,
              position:'inner', //标签的位置
              textStyle : {
                fontWeight : 300 ,
                fontSize : 14,    //文字的字体大小
                color: "#fff"
              },
              formatter:'{c}({d}%)'
            }
          },
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        },
        {
          name: "StarBucks",
          type: 'pie',
          radius: '50%',
          center: ['75%', '50%'],
          label:{
            normal:{
              show:true,
              position:'inner',
              textStyle : {
                fontWeight : 300 ,
                fontSize : 14,
                color: "#fff"
              },
              formatter:'{d}%'
            }
          },
          data: [
            {name: "Q1", value: 5},
            {name: "Q2", value: 6},
            {name: "Q3", value: 7},
            {name: "Q4", value: 8}
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };
    let chartDom = document.getElementById('main');
    let myChart = echarts.init(chartDom);

    let pieDom = document.getElementById('pie');
    let pieChart = echarts.init(pieDom);

    this.request.get("/echarts/members").then(res => {
      console.log(res.data)
      //fill data
      // option.xAxis.data = res.data.x
      //StarBucks
      option.series[0].data = res.data
      option.series[1].data = res.data
      // TimHortons
      option.series[2].data = [5,6,7,8]
      option.series[3].data = [5,6,7,8]

      myChart.setOption(option);

      pieOption.series[0].data =[
        {name: "Q1", value: res.data[0]},
        {name: "Q2", value: res.data[1]},
        {name: "Q3", value: res.data[2]},
        {name: "Q4", value: res.data[3]},
      ]
      pieChart.setOption(pieOption)
    })

  }

}


</script>


<style scoped>

</style>