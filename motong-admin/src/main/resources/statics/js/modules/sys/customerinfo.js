$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/customerinfo/list',
        datatype: "json",
        colModel: [			
			// { label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '昵称', name: 'nickName', index: 'nick_name', width: 80 }, 			
			{ label: '用户名', name: 'userName', index: 'user_name', width: 80 }, 			
			{ label: '密码', name: 'password', index: 'password', width: 80 }, 			
			// { label: '微信唯一id', name: 'weChatOpenId', index: 'we_chat_open_id', width: 80 },
			{ label: '微信昵称', name: 'weChatNickName', index: 'we_chat_nick_name', width: 80 }, 			
			{ label: '参数A', name: 'paramA', index: 'param_a', width: 80 }, 			
			{ label: '参数B', name: 'paramB', index: 'param_b', width: 80 }, 			
			{ label: '生效状态', name: 'validStatusName', index: 'valid_status', width: 80 },
			{ label: '绑定状态', name: 'bindeStatusName', index: 'binde_status', width: 80 },
			// { label: '删除状态，0-删除，1-未删除', name: 'status', index: 'status', width: 80 },
			// { label: '创建时间', name: 'gmtCreate', index: 'gmt_create', width: 80 },
			// { label: '修改时间', name: 'gmtUpdate', index: 'gmt_update', width: 80 },
			// { label: '创建人姓名', name: 'userCreate', index: 'user_create', width: 80 },
			// { label: '创建人id', name: 'userCreateId', index: 'user_create_id', width: 80 }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		customerInfo: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.customerInfo = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.customerInfo.id == null ? "sys/customerinfo/save" : "sys/customerinfo/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.customerInfo),
                    success: function(r){
                        if(r.code === 0){
                             layer.msg("操作成功", {icon: 1});
                             vm.reload();
                             $('#btnSaveOrUpdate').button('reset');
                             $('#btnSaveOrUpdate').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
               if(!lock) {
                    lock = true;
		            $.ajax({
                        type: "POST",
                        url: baseURL + "sys/customerinfo/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function(r){
                            if(r.code == 0){
                                layer.msg("操作成功", {icon: 1});
                                $("#jqGrid").trigger("reloadGrid");
                            }else{
                                layer.alert(r.msg);
                            }
                        }
				    });
			    }
             }, function(){
             });
		},
		getInfo: function(id){
			$.get(baseURL + "sys/customerinfo/info/"+id, function(r){
                vm.customerInfo = r.customerInfo;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});