var domain = "url.com";
//后台接口url
var reqUrl = "http://"+domain;

// topBar栏的相关逻辑
function topBarAction(){
    // 点击人头 显示隐藏 下拉框
    $(".topBar .info").click(function(){
        
        // alert("info")
        var obj = $(this).find(".downInfo");
        if(obj.is(":visible")){
            obj.slideUp(300)
        }else{
            obj.slideDown(300)
        }
        stopBubble(event)
    });

    $("body").click(function(){
        var obj = $(this).find(".downInfo");
        if(obj.is(":visible")){
            obj.slideUp(300)
        }
    })

    // 点击退出登录
    $(".topBar .info .loginOut ").click(function(){
        ajaxPost("/manage/user/exit","",function(data){
            if(!data.code){
                sessionStorage.clear();
                window.location.href="./home.html";
            }else{
                alertTip("err",data.msg,400,2000)
            }
        })
    });
}

// 通知提示框
/*
* type:类型 err ,success
* message:显示的内容 
* fadeInTime:渐显时间
* fadeOutTime: 渐隐时间
*/
// alertTip("err","data.msg",400,2000,function(){})
// alertTip("err","data.msg",400,2000)
function alertTip(type,message,fadeInTime,fadeOutTime,callBack){
    if(type == "err"){
        $(".notePop").addClass("err").stop().fadeIn(fadeInTime,function(){
            if(fadeOutTime != undefined && fadeOutTime != ""){
                $(this).fadeOut(fadeOutTime) ;
            }
            if(callBack != undefined){
                callBack();
            }
        }).find("b").text(message);
    }else if(type == "success"){
        $(".notePop").removeClass("err").stop().fadeIn(fadeInTime,function(){
            if(fadeOutTime != undefined && fadeOutTime != ""){
                $(this).fadeOut(fadeOutTime) ;
            }
            if(callBack != undefined){
                callBack();
            }
        }).find("b").text(message);
    }
    $(".notePop i").click(function(){
        $(this).parent().stop().fadeOut(100);
    })
}

// 封装ajax
/*
*uri 
*data 对象传参
*/
function ajaxPost(uri,data,callBack){
    $.ajax({
        type: "POST",
        url: reqUrl+uri,
        data: data,
        xhrFields: {
            withCredentials: true
        },
        Connection:true,
        dataType: "json",
        success: function(data){
            if(data.code == 2002){
                var url = window.location.href ;
                sessionStorage.setItem("c1n_url",url);
                window.location.href = "./login.html";
            }else{
                callBack(data);
            }
        },
        error: function(err){
            console.log(err);
            $("button").attr("disable",false).removeClass("v-disable");
        }
    })
}

// 阻止冒泡事件
function stopBubble(e) { 
    //如果提供了事件对象，则这是一个非IE浏览器 
    if ( e && e.stopPropagation ) 
        //因此它支持W3C的stopPropagation()方法 
        e.stopPropagation(); 
    else 
        //否则，我们需要使用IE的方式来取消事件冒泡 
        window.event.cancelBubble = true; 
}

// 确认框 
/*
*el  为jquery对象 如：$(".ab")/$("#ab")
*setParams = {width:"";message:"";title:""} ;弹窗宽度、主体信息
*callBack 回调函数
*/
function creatConfirmPop(el,setParams,callBack){
    if(setParams.message){
        var content = setParams.message;
        el.html('<div class="w_hide" hidden>'+content+'</div>');
    }else if(!el.find(".w_hide").length){
        var content = el.html();
        el.empty();
        var hideContent = '<div class="w_hide" hidden>'+content+'</div>';
        el.html(hideContent);
    }else {
       el.find(".w_main").remove();
        var content = el.find(".w_hide").html();
    }
    
    // var content = setParams.message || el.find(".w_hide").html();
    var title = setParams.title || "提示";
    var width = setParams.width;
    var html = '<div class="w_main" style="min-width:'+width+'">'+
                    '<div class="w_header">'+
                        '<div class="w_title">'+title+'</div>'+
                        '<div class="w_close">×</div>'+
                    '</div>'+
                    '<div class="w_body">'+
                    content+
                    '</div>'+
                    '<div class="w_footer">'+
                        '<button class="submit">确定</button>'+
                        '<button class="cancel">取消</button>'+
                    '</div>'+
                '</div>';
    el.prepend(html);
    el.show();
    el.find(".w_footer .submit").off("click").on("click",function(){
       callBack() ;
    })
    el.on("click",".w_footer .cancel",function(){
        el.find(".w_footer .submit").off("click")
        el.hide();
        // el.hide().html(content);
    })
    el.on("click",".w_close",function(){
        el.find(".w_footer .submit").off("click")
        el.hide();
        // el.hide().html(content);
    })
}

// 获取url ？号后面携带的参数
function getQueryString(name) { 
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
    var r = window.location.search.substr(1).match(reg); 
    if (r != null) return unescape(r[2]); 
    return null; 
} 

// 分页
function initPagination(el,totalPage,callBack){
    $(el).pagination({
        currentPage: 1,
        totalPage: totalPage,
        isShow: true,
        count: 7,
        homePageText: "首页",
        endPageText: "尾页",
        prevPageText: "上一页",
        nextPageText: "下一页",
        callback: function(current) {
            callBack(current)
        }
    });
    // $("#setPage").on("click", function() {
    //     $("#pagination").pagination("setPage", 1, 10);
    // });
}


// 获取当前日期 例如：2019-07-05
function getCurrentDate(){
    var mydate = new Date();
    var year = mydate.getFullYear();
    var month = mydate.getMonth()+1;
    var date = mydate.getDate();
    var str = "" + year + "-";
    str += (month<10?"0"+month:month) + "-";
    str += date<10?"0"+date:date;
    return str;
}
if(!window.navigator.cookieEnabled){
	alertTip("success","您的浏览器不支持cookies功能，请在浏览器设置中启用",400);
}

// 