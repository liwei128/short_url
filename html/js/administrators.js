$(function(){
    // 设置全局变量
    var shortParamas = {keyUrl:"",status:"",startDate:"",endDate:"",currentPage:0, pageSize:10},shortIsFirst = false;
    var monitoParamas = {url:"",startDate:"",endDate:"",currentPage:0, pageSize:10},monitoIsFirst = false;
    var userParamas = {name:"", mail:"",startDate:"",endDate:"",status:"",currentPage:0, pageSize:10},userIsFirst = false;
	var blacklistParamas = {domain:"",status:"",currentPage:0, pageSize:10},blacklistIsFirst = false;
	var admin = false;

    topBarAction();//topBar的一些动作
    ajaxPost("/manage/user/currentUser","",function(data){//获取当前用户
        if(!data.code){
            $(".token .currentToken b").text(data.data.token);
            $(".token .selfInfo .name span").text(data.data.name);
            $(".token .selfInfo .mail span").text(data.data.mail);
            $(".token .selfInfo .time span").text(data.data.createTime);
            $(".topBar .beforLogin").siblings(".afterLogin").show().find(".peopleName").text(data.data.name);
            if(data.data.id == 1){
				admin = true;
                $(".leftLab .userManage").show();
				$(".leftLab .blacklistManage").show();
            }else{
				admin = false;
                $(".leftLab .userManage").hide();
				$(".leftLab .blacklistManage").hide();
            }
        }else {
            $(".topBar .afterLogin").siblings(".beforLogin").show();
        }
    });
    getshortList(shortParamas);//获取短网址列表
    // 初始化时间插件
    jeDate("#shortTime")
    jeDate("#monitoTime")
    jeDate("#userTime")

    // 点击左侧table栏
    $(".leftLab .shortManage").click(function(){
        $(this).addClass("active").siblings().removeClass("active");
        $(".right .shortManage").show().siblings().hide();
    })
    $(".leftLab .monitor").click(function(){
        if(!monitoIsFirst){
            getmonitorList(monitoParamas)
        }
        $(this).addClass("active").siblings().removeClass("active");
        $(".right .monitor").show().siblings().hide();
    })
    $(".leftLab .userManage").click(function(){
        if(!userIsFirst){
            getuserList(userParamas)
        }
        $(this).addClass("active").siblings().removeClass("active");
        $(".right .userManage").show().siblings().hide();
    })
	$(".leftLab .blacklistManage").click(function(){
	    if(!blacklistIsFirst){
	        getblacklist(blacklistParamas)
	    }
	    $(this).addClass("active").siblings().removeClass("active");
	    $(".right .blacklistManage").show().siblings().hide();
	})
    $(".leftLab .token").click(function(){
        $(this).addClass("active").siblings().removeClass("active");
        $(".right .token").show().siblings().hide();
    })

    // 点击搜索/重置
    $(".shortManage .searchBar .searchBtn").click(function(){
        shortParamas.currentPage = 0;
        shortParamas.keyUrl = $(this).siblings(".keyUrl").val();
        var time = $(this).siblings(".time").val();
        getTime(time,shortParamas)
        getshortList(shortParamas,true);//获取短网址列表
    })
    $(".shortManage .searchBar .resetBtn").click(function(){
        resetCondition(shortParamas)
        $(this).siblings("").val("");
        getshortList(shortParamas,true);//获取短网址列表
    })
    $(".monitor .searchBar .searchBtn").click(function(){
        monitoParamas.currentPage = 0;
        monitoParamas.url = $(this).siblings(".url").val();
        var time = $(this).siblings(".time").val();
        getTime(time,monitoParamas)
        getmonitorList(monitoParamas,true);//获取短网址列表
    })
    $(".monitor .searchBar .resetBtn").click(function(){
        resetCondition(monitoParamas)
        $(this).siblings("").val("");
        getmonitorList(monitoParamas,true);//获取短网址列表
    })
    $(".userManage .searchBar .searchBtn").click(function(){
        userParamas.currentPage = 0;
        userParamas.name = $(this).siblings(".name").val();
        userParamas.mail = $(this).siblings(".mail").val();
        var time = $(this).siblings(".time").val();
        getTime(time,userParamas)
        getuserList(userParamas,true);//获取短网址列表
    })
    $(".userManage .searchBar .resetBtn").click(function(){
        resetCondition(userParamas)
        $(this).siblings("").val("");
        getuserList(userParamas,true);//获取短网址列表
    })
	
	 $(".blacklistManage .searchBar .searchBtn").click(function(){
	    blacklistParamas.currentPage = 0;
	    blacklistParamas.domain = $(this).siblings(".name").val();
	    getblacklist(blacklistParamas,true);
	})
	$(".blacklistManage .searchBar .resetBtn").click(function(){
	    resetCondition(blacklistParamas)
	    $(this).siblings("").val("");
	    getblacklist(blacklistParamas,true);
	})
	// 改变状态(thead里面)
	$(".blacklistManage thead .status select").change(function(){
	    blacklistParamas.currentPage = 0;
	    blacklistParamas.status = $(this).val();
	    getblacklist(blacklistParamas,true);
	})
    

    // 改变状态(thead里面)
    $(".shortManage thead .status select").change(function(){
        shortParamas.currentPage = 0;
        shortParamas.status = $(this).val();
        getshortList(shortParamas,true);//获取短网址列表
    })
    $(".userManage thead .status select").change(function(){
        userParamas.currentPage = 0;
        userParamas.status = $(this).val();
        getuserList(userParamas);//获取短网址列表
    })

    // 修改每一条里面的状态（tbody里面）
    $(".shortManage tbody").on("click",".status img",function(){
        var that = $(this);
        if(that.parent("").data("status") == "NORMAL"){
            creatConfirmPop($(".deletPop"),{message:"确定要禁用吗？禁用后短网址将不可访问"},function(){
                setDisable(that,"/manage/shortUrl/modifyStatus")
            })
        }else {
            setDisable(that,"/manage/shortUrl/modifyStatus")
        }
    })
    $(".userManage tbody").on("click",".status img",function(){
        var that = $(this);
        if(that.parent("").data("status") == "NORMAL"){
            creatConfirmPop($(".deletPop"),{message:"确定要禁用吗？"},function(){
                setDisable(that,"/manage/user/modifyStatus")
            })
        }else {
            setDisable(that,"/manage/user/modifyStatus")
        }
    })
	
	$(".blacklistManage tbody").on("click",".status img",function(){
	    var that = $(this);
	    if(that.parent("").data("status") == "NORMAL"){
	        creatConfirmPop($(".deletPop"),{message:"确定要禁用吗？"},function(){
	            setDisable(that,"/manage/blacklist/modifyStatus")
	        })
	    }else {
	        setDisable(that,"/manage/blacklist/modifyStatus")
	    }
	})

    // 点击每一条里面的流量分析
    $(".shortManage tbody").on("click",".toChart img",function(){
        var id = $(this).parents("tr").data("id");
        window.open("./flowData.html?urlId=a-"+id)
    })
    $(".monitor tbody").on("click",".toChart img",function(){
        var id = $(this).parents("tr").data("id");
        window.open("./flowData.html?urlId=b-"+id)
    })

    // 修改短网址（短网址管理页面）
    $(".shortManage tbody").on("click",".edit.btn",function(){
        var id = $(this).data("id");
        var type = $(this).data("type");
        var key = $(this).data("key");
        var longUrl = $(this).parent().siblings(".longUrl").text();
        var that = $(this);
        $(".editShortUrlPop .editLongUrl textarea").text(longUrl);
        if(type == "URI"){
            var html = '<b>短网址：</b><i>http://'+domain+'/</i><input type="text" value="'+key+'">';
        }else {
            var html = '<b>短网址：</b><i>http://</i><input type="text" value="'+key+'"><i>.'+domain+'</i>';
        }
        $(".editShortUrlPop .editShortUrl").html(html);
        creatConfirmPop($(".editShortUrlPop"),{title:"修改短网址"},function(){
            $(".editShortUrlPop .w_footer .submit").attr("disabled","disabled").addClass("v-disable");
            longUrl = $(".editShortUrlPop .editLongUrl textarea").val();
            key = $(".editShortUrlPop .editShortUrl input").val();
            if(type == "URI"){
                var shortUrl = "http://"+domain+"/"+key;
            }else {
                var shortUrl = "http://"+key+"."+domain;
            }
            if(!key){
                alertTip("err","请填写短网址",400,4000); 
                $(".editShortUrlPop .w_footer .submit").attr("disabled",false).removeClass("v-disable");
                return;
            }else if(!longUrl){
                alertTip("err","请填写原网址",400,4000); 
                $(".editShortUrlPop .w_footer .submit").attr("disabled",false).removeClass("v-disable");
                return;
            }
            ajaxPost("/manage/shortUrl/update",{id:id,key:key,url:longUrl},function(data){
                if(!data.code){
                    $(".editShortUrlPop").hide();
                    alertTip("success","修改成功",400,4000);
                    that.data("key",key);
                    that.parent().siblings(".longUrl").text(longUrl);
                    that.parent().siblings(".shortUrl").text(shortUrl);
                }else{
                    alertTip("err",data.msg,400,4000);
                }
                $(".editShortUrlPop .w_footer .submit").attr("disabled",false).removeClass("v-disable");
            })
        })
    })
    // 新增网址（监控页面）
    $(".monitor .searchBar").on("click",".addBtn",function(){
        creatConfirmPop($(".addUrlPop"),{title:"新增网址"},function(){
            $(".addUrlPop .w_footer .submit").attr("disabled","disabled").addClass("v-disable");
            var type = $(".addUrlPop .radio input:checked").val();
            var url = $(".addUrlPop .longUrl textarea").val();
            if(!url){
                alertTip("err","请填写链接",400,4000);
                $(".addUrlPop .w_footer .submit").attr("disabled",false).removeClass("v-disable");
                return;
            }
            ajaxPost("/manage/page/add",{type:type,url:url},function(data){
                if(!data.code){
                    monitoParamas.currentPage = 0;
                    getmonitorList(monitoParamas,true);
                    $(".addUrlPop").hide();
                    alertTip("success","新增成功",400,4000);
                }else{
                    alertTip("err",data.msg,400,4000);
                }
                $(".addUrlPop .w_footer .submit").attr("disabled",false).removeClass("v-disable");
            })
        })
    })
    // 刷新token
    $(".token .updateBtn").click(function(){
        creatConfirmPop($(".deletPop"),{message:"刷新将导致当前token失效，确定要刷新吗？"},function(){
            getCurrentToken();
            $(".deletPop").hide();
        })
    })
	$(".shortManage tbody").on("click",".blacklist.btn",function(){
	    var id = $(this).data("id");
	    creatConfirmPop($(".deletPop"),{message:"确定要一键封禁吗?"},function(){
	        $(".deletPop .w_footer .submit").attr("disabled","disabled").addClass("v-disable");
	        ajaxPost("/manage/shortUrl/addBlacklist",{id:id},function(data){
	            if(!data.code){
	                $(".deletPop").hide();
	                alertTip("success","操作成功",400,4000,function(){
	                });
	            }else{
	                alertTip("err",data.msg,400,4000);
	            }
	            $(".deletPop .w_footer .submit").attr("disabled",false).removeClass("v-disable");
	        })
	    })
	})
    // 删除
    $(".shortManage tbody").on("click",".delet.btn",function(){
        var id = $(this).data("id");
        creatConfirmPop($(".deletPop"),{message:"确定要删除吗?"},function(){
            $(".deletPop .w_footer .submit").attr("disabled","disabled").addClass("v-disable");
            ajaxPost("/manage/shortUrl/delete",{id:id},function(data){
                if(!data.code){
                    getshortList(shortParamas);
                    $(".deletPop").hide();
                    alertTip("success","删除成功",400,4000,function(){
                    });
                }else{
                    alertTip("err",data.msg,400,4000);
                }
                $(".deletPop .w_footer .submit").attr("disabled",false).removeClass("v-disable");
            })
        })
    })
	$(".blacklistManage tbody").on("click",".delet.btn",function(){
	    var id = $(this).data("id");
	    creatConfirmPop($(".deletPop"),{message:"确定要删除吗?"},function(){
	        $(".deletPop .w_footer .submit").attr("disabled","disabled").addClass("v-disable");
	        ajaxPost("/manage/blacklist/delete",{id:id},function(data){
	            if(!data.code){
	                getblacklist(blacklistParamas);
	                $(".deletPop").hide();
	                alertTip("success","删除成功",400,4000,function(){
	                });
	            }else{
	                alertTip("err",data.msg,400,4000);
	            }
	            $(".deletPop .w_footer .submit").attr("disabled",false).removeClass("v-disable");
	        })
	    })
	})
    $(".monitor tbody").on("click",".delet.btn",function(){
        var id = $(this).data("id");
        creatConfirmPop($(".deletPop"),{message:"确定要删除吗?"},function(){
            $(".deletPop .w_footer .submit").attr("disabled","disabled").addClass("v-disable");
            ajaxPost("/manage/page/delete",{id:id},function(data){
                if(!data.code){
                    getmonitorList(monitoParamas);
                    $(".deletPop").hide();
                    alertTip("success","删除成功",400,4000,function(){
                    });
                }else{
                    alertTip("err",data.msg,400,4000);
                }
                $(".deletPop .w_footer .submit").attr("disabled",false).removeClass("v-disable");
            })
        })
    })
    $(".userManage tbody").on("click",".delet.btn",function(){
        var id = $(this).data("id");
        creatConfirmPop($(".deletPop"),{message:"确定要删除吗?"},function(){
            $(".deletPop .w_footer .submit").attr("disabled","disabled").addClass("v-disable");
            ajaxPost("/manage/user/delete",{id:id},function(data){
                if(!data.code){
                    getuserList(userParamas);
                    $(".deletPop").hide();
                    alertTip("success","删除成功",400,4000,function(){
                    });
                }else{
                    alertTip("err",data.msg,400,4000);
                }
                $(".deletPop .w_footer .submit").attr("disabled",false).removeClass("v-disable");
            })
        })
    })
    // 查看流量分析

    // 获取短网址列表ajax
    function getshortList(paramas,changeCurrentPage){
        ajaxPost("/manage/shortUrl",paramas,function(data){//获取当前用户
            if(!data.code){
                // 渲染数据
                var list = data.data.list;
                $(".tableBox.shortManage tbody").html("");
                for(var i = 0 ; i < list.length ; i ++){
                    var listi = list[i];
                    if(listi.status == "NORMAL"){
                        var status = 'data-status="NORMAL"><img src="./img/use.png" title="禁用">'
                    }else{
                        var status = 'data-status="DISABLED"><img src="./img/unUse.png" title="启用">'
                    }
                    var html = '<tr data-id ="'+ listi.id +'">'+
                                    '<td class="shortUrl"><a href="'+ listi.shortUrl +'" target="_blank">'+ listi.shortUrl +'</a></td>'+
                                    '<td class="longUrl"><a href="'+ listi.url +'" target="_blank">'+ listi.url +'</a></td>'+
                                    '<td>'+ listi.createTime +'</td>'+
                                    '<td class="status" '+ status +'</td>'+
                                    '<td class="toChart"><img src="./img/chart.png" title="查看流量分析"></td>'+
                                    '<td>'+
                                        '<span class="edit btn" data-id ="'+ listi.id +'" data-type ="'+ listi.type +'" data-key ="'+ listi.key +'" data-shorturl="'+listi.shortUrl+'">修改</span>'+
                                        '<span class="delet btn" data-id ="'+ listi.id +'">删除</span>'+
										(admin?'<span class="blacklist btn" data-id ="'+ listi.id +'">一键封禁</span>':'')+
                                    '</td>'+
                                '</tr>';
                    $(".tableBox.shortManage tbody").append(html)
                }

                // 初始化分页
                if(data.data.total>10){
                    $(".shortManage .pageBar .totalPage").show().find("span").text(data.data.total);
                }else {
                    $(".shortManage .pageBar .totalPage").hide();
                }
                if(!shortIsFirst){
                    shortIsFirst = true;
                    initPagination("#shortPage",data.data.pageTotal,function(current){
                        shortParamas.currentPage = current - 1;
                        getshortList(shortParamas);
                    })
                }else if(changeCurrentPage){
                    $("#shortPage").pagination("setPage", 1, data.data.pageTotal)
                }
            }else {
                alertTip("err",data.msg,400,4000)
            }
        });
    }
    // 获取监控页面列表ajax
    function getmonitorList(paramas,changeCurrentPage){
        ajaxPost("/manage/page/list",paramas,function(data){//获取当前用户
            if(!data.code){
                // 渲染数据
                var list = data.data.list;
                $(".tableBox.monitor tbody").html("");
                for(var i = 0 ; i < list.length ; i ++){
                    var listi = list[i];
                    var type = listi.type == "URL"?"网址":"域名";
                    var html = '<tr data-id ="'+ listi.id +'">'+
                                    '<td>'+ type +'</td>'+
                                    '<td>'+ listi.url +'</td>'+
                                    '<td>'+ listi.createTime +'</td>'+
                                    '<td class="toChart"><img src="./img/chart.png" title="查看流量分析"></td>'+
                                    '<td>'+
                                        '<span class="delet btn" data-id ="'+ listi.id +'">删除</span>'+
                                    '</td>'+
                                '</tr>';
                    $(".tableBox.monitor tbody").append(html)
                }

                // 初始化分页
                if(data.data.total>10){
                    $(".monitor .pageBar .totalPage").show().find("span").text(data.data.total);
                }else {
                    $(".monitor .pageBar .totalPage").hide();
                }
                if(!monitoIsFirst){
                    monitoIsFirst = true;
                    initPagination("#monitorPage",data.data.pageTotal,function(current){
                        monitoParamas.currentPage = current - 1;
                        getmonitorList(monitoParamas);
                    })
                }else if(changeCurrentPage){
                    $("#monitorPage").pagination("setPage", 1, data.data.pageTotal)
                }
            }else {
                alertTip("err",data.msg,400,4000)
            }
        });
    }
    // 用户管理列表ajax
    function getuserList(paramas,changeCurrentPage){
        ajaxPost("/manage/user/list",paramas,function(data){//获取当前用户
            if(!data.code){
                // 渲染数据
                var list = data.data.list;
                $(".tableBox.userManage tbody").html("");
                for(var i = 0 ; i < list.length ; i ++){
                    var listi = list[i];
                    if(listi.status == "NORMAL"){
                        var status = 'data-status="NORMAL"><img src="./img/use.png" title="禁用">'
                    }else if(listi.status == "DISABLED"){
                        var status = 'data-status="DISABLED"><img src="./img/unUse.png" title="启用">'
                    }else{
                        var status = 'data-status="INIT">未激活'
                    }
                    var html = '<tr data-id ="'+ listi.id +'">'+
                                    '<td>'+ listi.id +'</td>'+
                                    '<td>'+ listi.name +'</td>'+
                                    '<td>'+ listi.mail +'</td>'+
                                    '<td>'+ listi.createTime +'</td>'+
                                    '<td class="status" '+ status +'</td>'+
                                    '<td>'+
                                        '<span class="delet btn" data-id ="'+ listi.id +'">删除</span>'+
                                    '</td>'+
                                '</tr>';
                    $(".tableBox.userManage tbody").append(html)
                }

                // 初始化分页
                if(data.data.total>10){
                    $(".userManage .pageBar .totalPage").show().find("span").text(data.data.total);
                }else {
                    $(".userManage .pageBar .totalPage").hide();
                }
                if(!userIsFirst){
                    userIsFirst = true;
                    initPagination("#userPage",data.data.pageTotal,function(current){
                        userParamas.currentPage = current - 1;
                        getuserList(userParamas);
                    })
                }else if(changeCurrentPage){
                    $("#userPage").pagination("setPage", 1, data.data.pageTotal)
                }
            }else {
                alertTip("err",data.msg,400,4000)
            }
        });
    }
	 // 黑名单管理列表
	function getblacklist (paramas,changeCurrentPage){
	    ajaxPost("/manage/blacklist/list",paramas,function(data){
	        if(!data.code){
	            // 渲染数据
	            var list = data.data.list;
	            $(".tableBox.blacklistManage tbody").html("");
	            for(var i = 0 ; i < list.length ; i ++){
	                var listi = list[i];
	                if(listi.status == "NORMAL"){
	                    var status = 'data-status="NORMAL"><img src="./img/use.png" title="禁用">'
	                }
					if(listi.status == "DISABLED"){
	                    var status = 'data-status="DISABLED"><img src="./img/unUse.png" title="启用">'
	                }
	                var html = '<tr data-id ="'+ listi.id +'">'+
									'<td>'+ listi.id +'</td>'+
	                                '<td>'+ listi.domain +'</td>'+
	                                '<td>'+ listi.createTime +'</td>'+
	                                '<td class="status" '+ status +'</td>'+
	                                '<td>'+
	                                    '<span class="delet btn" data-id ="'+ listi.id +'">删除</span>'+
	                                '</td>'+
	                            '</tr>';
	                $(".tableBox.blacklistManage tbody").append(html)
	            }
	
	            // 初始化分页
	            if(data.data.total>10){
	                $(".blacklistManage .pageBar .totalPage").show().find("span").text(data.data.total);
	            }else {
	                $(".blacklistManage .pageBar .totalPage").hide();
	            }
	            if(!blacklistIsFirst){
	                blacklistIsFirst = true;
	                initPagination("#blacklistPage",data.data.pageTotal,function(current){
	                    blacklistParamas.currentPage = current - 1;
	                    getblacklist(blacklistParamas);
	                })
	            }else if(changeCurrentPage){
	                $("#blacklistPage").pagination("setPage", 1, data.data.pageTotal)
	            }
	        }else {
	            alertTip("err",data.msg,400,4000)
	        }
	    });
	}
	
    // 获取当前用户token ajax
    function getCurrentToken(){
        ajaxPost("/manage/user/refreshToken","",function(data){
            if(!data.code){
                $(".token .currentToken b").text(data.data.token);
                alertTip("success","刷新成功",400,4000);
            }else{
                alertTip("err",data.msg,400,4000);
            }
        })
    }
    // 禁用启用请求ajax
    function setDisable(that,url){
        var status = that.parent("").data("status");
        var paramas = {
            status: status=="NORMAL"?"DISABLED":"NORMAL",
            id: that.parents("tr").data("id")
        }
        ajaxPost(url,paramas,function(data){
            if(!data.code){
                if(status=="NORMAL"){
                    that.attr("src","./img/unUse.png");
                    that.parent().data("status","DISABLED");
                    that.attr("title","启用");
                }else if(status=="DISABLED"){
                    that.attr("src","./img/use.png")
                    that.parent().data("status","NORMAL");
                    that.attr("title","禁用");
                }
                $(".deletPop").hide();
            }else{
                alertTip("err",data.msg,400,4000);
            }
        })
    }
    // 获取时间框的初始时间和结束时间
    function getTime(time,paramas){
        if(time){
            var dateArr = time.split(" 至 ");
            paramas.startDate = dateArr[0];
            paramas.endDate = dateArr[1];
        }else {
            paramas.startDate = "";
            paramas.endDate = "";
        }
    }

    // 重置清空搜索栏状态
    function resetCondition(params){
        for(var key in params){
            if(key == "currentPage"){
                params[key] = 0;
            }else if(key == "pageSize"){
                params[key] = 10;
            }else if(key != "status"){
                params[key] = "";
            }
        }
    }
})
