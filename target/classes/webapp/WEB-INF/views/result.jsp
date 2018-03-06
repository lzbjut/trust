
<%--
  Created by IntelliJ IDEA.
  User: inst1
  Date: 2017/8/6
  Time: 10:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/echarts.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/macarons.js"></script>
</head>
<body>
<div id="myRadio"  style="height:400px"></div>
<script type="text/javascript">
    var home ="${home}";

    //获取到的数据
    var context =eval('${result}');
    var labelA={ normal:{show:true,position:'top'}};

    //名称数组
    var nameArray = new Array();
    //数据数组
    var goodArray= new Array();
    var badArray= new Array();

    //将数据进行处理
    for(var i=0;i<context.length/2;i++)
    {
        nameArray.push(context[i].round);

        //二维数组- 保存数据

        goodArray.push(context[i].trust);

    }
    for(var i=0;i<context.length/2;i++)
    {

        //二维数组- 保存数据

        badArray.push(context[i+context.length/2].trust);

    }


            var myChart =echarts.init(document.getElementById('myRadio'));
            var option = {
                title: {
                    text: '可信变化值'
                },
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'line'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                grid: {
                    containLabel: true
                },
                legend: {
                    data:['可信值']
                },
                xAxis: {
                    data: nameArray
                },
                series : [ {

                    type:'line',
                    data:goodArray,


                },
                    {

                        type:'line',
                        data:badArray,


                    }],
                yAxis: {
                    splitArea:{
                        show:true,
                    }
                },



//                itemStyle: {
//
//
//                    normal: {
//
//                        label: {
//                            show: true,
//                            formatter: '{b}\n{c}'
//                        },
//                        shadowBlur: 80,
//                        shadowColor: 'rgba(0, 0, 0, 0.5)'
//                    }
//
//                },
//                series : [ {
//                    barGap: 0,
//                    name:'任务数量',
//                    type:'bar',
//                    data:dataArray,
//                    label:labelA
//
//                }],


            }
             myChart.setOption(option);









</script>
</body>
</html>
