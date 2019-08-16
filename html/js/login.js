$(function(){
    var pageName = $("#pageName").text();
    var Chinese = "";
    var params = {};
    var url = "";

    // 点击登录
    $(".form>button").click(function(){
        $(this).attr("disabled","disabled").addClass("v-disable");
        getParams(pageName);
        if(!checkParams(params)){
            $(this).attr("disabled",false).removeClass("v-disable");
            return;
        }
        var that = $(this)
        if(params.pwd){
            params.pwd = hex_md5(params.pwd).toUpperCase();
        }
        if(params.oldPwd){
            params.oldPwd = hex_md5(params.oldPwd).toUpperCase();
        }
        ajaxPost(url,params,function(data){
            var code = data.code;
            var msg = data.msg;
            if(!code){
                if(pageName == "register"){
                    alertTip("success","注册邮件已发送，请前往邮箱进行验证!",400);
                    that.attr("disabled",false).removeClass("v-disable");
                }else if(pageName == "changePassword" || pageName == "resetPassword"){
                    sessionStorage.clear();
                    alertTip("success",Chinese + "成功",400,4000,function(){
                        window.location.href="./login.html";
                    })
                }else if(pageName == "login"){
                    var url = sessionStorage.getItem("c1n_url");
                    window.location.href = url || "./home.html";
                }
            }else{
                that.attr("disabled",false).removeClass("v-disable");
                alertTip("err",msg,400,4000)
            };
        })
        
    })

    // 获取Params
    function getParams(pageName){
        if(pageName == "login"){
            var name = $.trim($("#name").val());
            var pwd = $("#pwd").val();
            params = {
                // name: "admin",
                // pwd: "123456",
                name: name,
                pwd: pwd
            };
            url = "/manage/user/login";
            Chinese = "登录";
        }else if(pageName == "register"){
            var mail = $.trim($("#mail").val());
            var name = $.trim($("#name").val());
            var pwd = $("#pwd").val();
            params = {
                mail: mail,
                name: name,
                pwd: pwd
            };
            url = "/manage/user/register";
            Chinese = "注册";            
        }else if(pageName == "changePassword"){
            var oldPwd = $("#oldPwd").val();
            var pwd = $("#pwd").val();
            params = {
                oldPwd: oldPwd,
                pwd: pwd
            };
            url = "/manage/user/changePassword";
            Chinese = "修改";
        }else if(pageName == "resetPassword"){
            var user = $.trim($("#user").val());
            var code = $.trim($("#code").val());
            var pwd = $("#pwd").val();
            params = {
                user: user,
                code: code,
                pwd: pwd
            };
            url = "/manage/user/resetPassword";
            Chinese = "重置";
        }
    }

    // 校验判断
    function checkParams(params){
        for(var key in params){
            if(!params[key]){
                alertTip("err","请填写完整",400,4000)
                return false;
            }
        }
		if(pageName == "login"){
			if(!window.navigator.cookieEnabled){
				alertTip("err","开启Cookie之后才能登录",400,4000)
				return false;
			}
		}
        if(pageName == "changePassword" || pageName == "resetPassword"){
            var rePwd = $("#rePwd").val();
            if(rePwd != params.pwd){
                alertTip("err","两次输入密码不一致",400,4000)
                return false;
            }
        }
        for(var key in params){
            if(key == "name" || key == "user"){
                var re = /^[a-zA-Z0-9]{3,18}$/;
                if(! re.test(params[key])) {
                    alertTip("err","用户名须为3到18位数字和字母组成",400,4000)
                    return false;
                }
            }else if(key == "pwd" || key == "oldPwd"){
                var length = params[key].length;
                if(length<6 || length>18) {
                    alertTip("err","密码长度须为6到18位",400,4000)
                    return false;
                }
            }else if(key == "mail"){
                var re = /^[\.a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
                if(! re.test(params[key])) {
                    alertTip("err","邮箱格式不正确",400,4000)
                    return false;
                }
            }
        }
        return true;
    }
})