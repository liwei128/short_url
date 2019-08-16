$(function(){
    topBarAction();//topBar的一些动作
    ajaxPost("/manage/user/currentUser","",function(data){//获取当前用户
        if(!data.code){
            $(".topBar .beforLogin").siblings(".afterLogin").show().find(".peopleName").text(data.data.name);
        }else {
            $(".topBar .afterLogin").siblings(".beforLogin").show();
        }
	});
    // 切换  短连接  自定义短连接  还原网址
    $(".switchBtn").click(function(){
        $(this).addClass("active").siblings("").removeClass("active");
        var index = $(this).index("button");
        $(".opate>div").eq(index).addClass("active").siblings().removeClass("active");
    })

    // 改变自定义类型
    $("#select").change(function(){
		switchType($(this).val());
    })
	switchType('URI');
	function switchType(type){
		var html = "";
		if(type == "URI"){
            html = '<i>http://'+domain+'/</i>'+
                    '<input type="text" class="keyWords"  placeholder="请输入自定义内容">'
            
        }else {
            html = '<i>http://</i>'+
                    '<input type="text" class="keyWords"  placeholder="请输入自定义内容">'+
                    '<i>.'+domain+'</i>'
        }
        $(".short .define span").html(html)
	}

    // 点击确定（生成端网址）
    $(".opate .short button").click(function(){
        var url = $(this).siblings(".site").val();
        if(!url){
            $(".opate .active .result").addClass("err").text("请填写长网址");
            return;
        }
        var key = $(this).siblings(".define").find(".keyWords").val();
        var type = $(this).siblings(".define").find("#select").val()
        if(type && !key){
            $(".opate .active .result").addClass("err").text("请填写自定义内容");
            return;
        }
        PostShortSite(url,type,key);
    })

    // 点击复制按钮
    $(".result").on("click",".copyIcon",function(){
        copy(".copyIcon");
    })

    // ajax请求 生成短网址 参数type，必填，只能填写"URI"或者"DOMAIN"默认URI
    function PostShortSite(url,type,key){
        var data = {
            url:url,
            type:type || "URI",
            key:key
        };
        ajaxPost("/link/short",data,function(data){
            var code = data.code;
            if(!code){
                var html = '短网址：<a href="'+data.data+'" target="_blank">'+data.data+
                '</a><i class="copyIcon" data-clipboard-text="'+data.data+'">复制</i>';
                $(".opate .active .result").removeClass("err").html(html);
            }else{
                $(".opate .active .result").addClass("err").text(data.msg);
            };
        });
    }

    // 复制
    function copy(tag){
        var clipboard1 = new ClipboardJS(tag); 
        clipboard1.on('success', function(e) {
            alertTip("success","复制成功！",400,2000)
        });
        clipboard1.on('error', function(e) {
            alertTip("err","复制失败！请手动复制！",400,2000)
        });
    }

})