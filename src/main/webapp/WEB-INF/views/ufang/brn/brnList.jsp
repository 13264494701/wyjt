<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>机构管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${fns:toJson(list)}, rootId = "1";
			addRow("#treeTableList", tpl, data, rootId, true);
			$("#treeTable").treeTable({expandLevel : 5});
		});
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){

				var row = data[i];
				if (row.parentId == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
							type: getDictLabel(${fns:toJson(fns:getDictList('sys_brn_type'))}, row.type)
						}, 
						pid: (root?0:pid), 
						row: row,
						showAdd:(row.grade !=3?true:false),
						showUpdate:(row.grade ==3?true:false),
						showDel:(row.type=='custom'&&row.grade ==3?true:false)
					}));
					addRow(list, tpl, data, row.id);
				}
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ufang}/brn/list">机构列表</a></li>
	</ul>
	<sys:message content="${message}" />
	<table id="treeTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th  style="text-align: center">机构名称</th>
				<th  style="text-align: center">归属区域</th>
				<th  style="text-align: center">机构编码</th>
				<shiro:hasPermission name="ufang:brn:edit">
					<th  style="text-align: center">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td  style="text-align: center"><a href="${ufang}/brn/query?id={{row.id}}">{{row.brnName}}</a></td>
			<td  style="text-align: center">{{row.area.name}}</td>
			<td  style="text-align: center">{{row.brnNo}}</td>
			<td  style="text-align:center">
			 <div>
				<shiro:hasPermission name="ufang:brn:edit">
                    {{#showUpdate}}
					<a href="${ufang}/brn/update?id={{row.id}}">修改</a>
                    {{/showUpdate}}
  					{{#showDel}}
					<a href="${ufang}/brn/delete?id={{row.id}}" onclick="return confirmx('要删除该机构及所有子机构项吗？', this.href)">删除</a>
    		  		{{/showDel}}
					{{#showAdd}}
				  		<a href="${ufang}/brn/add?parent.id={{row.id}}">添加部门</a>
			   		{{/showAdd}} 
				</shiro:hasPermission>
			</div>
			</td>
		</tr>
	</script>
</body>
</html>