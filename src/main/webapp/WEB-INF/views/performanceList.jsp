<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>生产实绩管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#searchForm").validate();
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function CheckSelect() {
			var all = document.getElementById("SelectAll");
			var checkbox = document.getElementsByName("Select");
			for (var i = 0; i < checkbox.length; i++) {
				if (all.checked == true) {
					checkbox[i].checked = true;
				} else {
					checkbox[i].checked = false;
				}
			}
		}
		function edit_click() {
			var rusult = 0;
			var number = 0;
			var check_array = document.getElementsByName("Select");
			for (var i = 0; i < check_array.length; i++) {
				if (check_array[i].checked == true) {
					rusult = check_array[i].value;
					number++;
				}
			}
			if (number == 0) {
				alert("请选中一条需要修改的信息");
				return;
			} else if (number > 1) {
				alert("一次只能修改一条信息");
				return;
			}
			
			window.location.href = "${ctx}/pp/performance/form?id="
					+ rusult;
		}
		
		function del_click() {
			var rusult = 0;
			var number = 0;
			var check_array = document.getElementsByName("Select");
			for (var i = 0; i < check_array.length; i++) {
				if (check_array[i].checked == true) {
					rusult = check_array[i].value;
					number++;
				}
			}
			if (number == 0) {
				alert("请选中一条需要删除的信息");
				return;
			} else if (number > 1) {
				var idAll = new Array();
				var count = 0;
				var herfs = new Array();
				for (var i = 0; i < check_array.length; i++) {
					if (check_array[i].checked == true) {
						idAll[count] = check_array[i].value;
						count++;
					}
				}
				for (var j = 0; j < count; j++) {
					herfs[j] = idAll[j];
				}
				var herfss = "${ctx}/pp/performance/deletes?id="
						+ herfs.toString();
				confirmx('确认要删除这些生产计划吗？', herfss);
			} else {
				var herf = "${ctx}/pp/performance/delete?id=" + rusult;
				confirmx('确认要删除该生产计划吗？', herf);
			}
		}
		
		function publish_click() {
			var rusult = 0;
			var number = 0;
			var check_array = document.getElementsByName("Select");
			for (var i = 0; i < check_array.length; i++) {
				if (check_array[i].checked == true) {
					rusult = check_array[i].value;
					number++;
				}
			}
			if (number == 0) {
				alert("请选中一条需要发布的信息");
				return;
			} else if (number > 1) {
				var idAll = new Array();
				var count = 0;
				var herfs = new Array();
				for (var i = 0; i < check_array.length; i++) {
					if (check_array[i].checked == true) {
						idAll[count] = check_array[i].value;
						count++;
					}
				}
				for (var j = 0; j < count; j++) {
					herfs[j] = idAll[j];
				}
				var herfss = "${ctx}/pp/performance/publish?id="
						+ herfs.toString();
				confirmx('确认要发布这些生产计划吗？', herfss);
			} else {
				var herf = "${ctx}/pp/performance/publish?id=" + rusult;
				confirmx('确认要发布该生产计划吗？', herf);
			}
		}
		
		
		
		
		$(document).ready(function() {
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出数据吗？","系统提示",function(v,h,f){
					if(v == "ok"){
						exportTable('contentTable',"","","生产计划数据",'${ctx}');
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
		});	
		
		
		window.onload = function() {
			var sequencer = document.getElementsByClassName("Sequence");
			for (var i = 0; i < sequencer.length; i++) {
				sequencer[i].innerHTML = i + 1;
			}
		}
	</script>
	<script src="${ctxStatic}/echarts/js/echarts.js"></script>
	<script src="${ctxStatic}/echarts/js/macarons.js"></script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/pp/performance/">生产实绩表</a></li>
			 <li class="pull-right">			
		</li>
 	</ul>
 	<table>
	<form:form id="searchForm" modelAttribute="performance" action="${ctx}/pp/performance/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<tr>
			<td><label>工作中心：</label>
				<tags:treeselect id="gzzx_id" name="workcenter.id" value="${performance.workcenter.id}"
					labelName="workcenter.gzzxmc" labelValue="${performance.workcenter.gzzxmc}" title="工作中心"
					url="/gzzx/gzzxwh/treeData?type=1"  cssClass="required"/>
			
			<td>
				<label>计划周期名称：</label>
						
						<tags:treeselect id="xzjhzq" name="planningcycle.id"
								value="${performance.planningcycle.id}" labelName="planningcycle.jhzqmc"
								labelValue="${performance.planningcycle.jhzqmc}" title="部门"
								url="/pp/productionPlan/treeData"/>
			</td>
			<td>
				<label>产品名称：</label>
				<tags:treeselect id="id" name="material.id" value="${performance.material.id}" labelName="material.wlmc"
						labelValue="${performance.material.wlmc}" title="产品编码" url="/ip/technique/wlbmtreeData"/>
			</td>
			
			<td>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			</td>
		</tr>
		 
			
	</form:form>
	</table>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr><th><input name="check" type="checkbox" id="SelectAll"
					onclick="CheckSelect()" />全选</th>
				<th>计划周期顺序号</th><th>计划周期名称</th><th>计划周期编码</th><th>产品名称</th><th>产品编码</th><th>任务数量</th><th>生产数量</th><th>合格品数量</th><th>一次合格品数量</th><th>废品数量</th><th>计量单位</th><th>最后编辑时间</th>
				<th>是否按时完成</th><th>相关任务单</th>				 
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="performance">
			<tr><td><input type="checkbox" value="${performance.id}"
						name="Select" class="Select" /></td>				
				<td class="Sequence"></td>				
				<td>${performance.planningcycle.jhzqmc}</td>
				<td>${performance.planningcycle.jhzqbm}</td>
				<td>${performance.material.wlmc}</td>
				<td>${performance.material.wlbm}</td>
				<td>${performance.rwsl}</td>
				<td>${performance.scsl}</td>
				<td>${performance.hgpsl}</td>
				<td>${performance.ychgpsl}</td>
				<td>${performance.fpsl}</td>
				<td>${performance.jldw}</td>
				<td><fmt:formatDate value="${performance.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${fns:getDictLabel(performance.sfwc, 'yes_no', ' ')}</td>
				<td><a href="${ctx}/pp/productionTaskList/relevantTaskTable?id=${performance.productiontasklist.id}">查询</a></td>
					
				 
			</tr>
			
		</c:forEach>
		</tbody>
	</table>
	<input id="btnQuery" class="btn btn-primary" type="button" value="编辑" onclick="edit_click()" />&nbsp;
	<button class="btn btn-primary" id="btnExport">导出</button>
	<input id="btnrefresh" class="btn btn-primary" type="button" value="刷新" onclick="" />&nbsp;
	<div class="pagination">${page}</div>
	<form:form id="EchartsForm" modelAttribute="production"  method="post" class="breadcrumb form-search">
	<label class="control-label">选择产品：</label>
	<tags:treeselect id="id1" name="material.id" value="${performance.material.id}" labelName="material.wlmc"
						labelValue="${performance.material.wlmc}" title="产品编码" url="/ip/technique/wlbmtreeData"/>
	<label class="control-label">选择产品类别：</label>					
	<tags:treeselect id="id2" name="material.id" value="" labelName="material.wlmc"
						labelValue="" title="产品编码" url="/ip/wllb/treeData"/>
    <label class="control-label">时间粒度：</label>
	<select  id="scope">
		<option value="1">年</option>
		<option value="2">季度</option>
		<option value="3">月</option>
	</select>
	<input id="sdfsdf" class="btn btn-primary" type="button" value="刷新" onclick="EchartsRefresh();"></input>
	<input type='button' class="btn btn-primary" value='重置' onClick="$('#id1Id').val(undefined);$('#id1Name').val(undefined); ">
	</form:form>
	<div id="main" style="width: 1000px;height:400px;"></div>
	<script>
	
	var myChart = echarts.init(document.getElementById('myRadio'), 'macarons');
	var labelA={ normal:{show:true,position:'top'}};
    var option = {
        title: {
            text: '产品数量统计'
        },
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            data:['任务数量','生产数量','合格品数量','一次合格品数量','废品数量']
        },
        xAxis: {
            data: []
        },
        yAxis: {
        	 splitArea:{
					show:true,
				}
	    	 },
	     
	     
	     
	     itemStyle: {
	    	
	     
             normal: {
                 
                 label: {
                     show: true,
                     formatter: '{b}\n{c}'
                 },
                 shadowBlur: 80,
                 shadowColor: 'rgba(0, 0, 0, 0.5)'
             }
         
        },
        series: [{
            name: '数量',
            type: 'bar',
            data: []
        },
       
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    
	function EchartsRefresh(){
		
		var val1=document.getElementById('scope');
		
		
		$.ajax({
			type : "post",
			dataType : "json",
			url : '${ctx}/pp/performance/Echarts?val1='+$("#id2Id").val()+'&val2='+val1.value+'&val3='+$("#id1Id").val(),
			beforeSend : function(XMLHttpRequest) {
			},
			success : function(data){
				var fpsl = new Array();
				var rwsl = new Array();
				var scsl = new Array();
				var hgpsl = new Array();
				var ychgpsl = new Array();
				var nameArray = new Array();
				for(var i=0;i<data.length;i++){
					
					nameArray.push(data[i].name);
			  		
			  		//二维数组- 保存数据
			  		
			  		rwsl.push(data[i].rwsl);
			  		scsl.push(data[i].scsl);
			  		hgpsl.push(data[i].hgpsl);
			  		ychgpsl.push(data[i].ychgpsl);
			  		fpsl.push(data[i].fpsl);
				}
				myChart.setOption({
				     xAxis : {
				      data : nameArray
				     },
				     series : [ {
				        	barGap: 0,
				            name:'任务数量',
				            type:'bar',
				            data:rwsl,
				            label:labelA
				            
				        },
				        {
				        	barGap: 0,
				            name:'生产数量',
				            type:'bar',
				            data:scsl,
				            label:labelA
				            
				        },
				        {
				        	barGap: 0,
							
				            name:'合格品数量',
				            type:'bar',
				            data:hgpsl,
				            label:labelA
				        },
				        {
				        	barGap: 0,
							
				            name:'一次合格品数量',
				            type:'bar',
				            data:ychgpsl,
				            label:labelA
				        },
				        {
				        	barGap: 0,
							
				            name:'废品数量',
				            type:'bar',
				            data:fpsl,
				            label:labelA
				        }]
				    	  
				    
				    });
				
			}
		})
	}
	
	</script>
</body>
</html>
