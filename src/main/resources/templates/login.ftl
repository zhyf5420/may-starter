<!DOCTYPE html>
<html lang="zh">
<head>
    <title>登陆页</title>
    <meta charset="UTF-8">
    <#setting number_format="#">
    <#assign path="${springMacroRequestContext.getContextPath()}">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script type="text/javascript" src="${path}/js/jquery-2.1.4.min.js"></script>
    <link href="${path}/bootstrap/bootstrap.min.css" rel="stylesheet">
    <link href="${path}/css/login.css" rel="stylesheet">

    <script type="application/javascript">
        // 回车提交表单
        document.onkeydown = function (e) {
            // 兼容FF和IE和Opera
            let theEvent = window.event || e;
            let code = theEvent.keyCode || theEvent.which || theEvent.charCode;
            if (code === 13) {
                Login();
            }
        };

        /**
         * 登陆
         */
        function Login() {
            // let data = {
            //     accountName: $("#accountName").val(),
            //     password: $("#password").val()
            // };
            // $.ajax({
            //     url: "login",
            //     type: "POST",
            //     data: JSON.stringify(data),
            //     dataType: "json",
            //     headers: {"Content-Type": "application/json"},
            //     success: function (res) {
            //         console.log(res);
            //         console.log(res.token);
            //         if (res.code !== 0) {
            //             $("#loginResult").html(res.message);
            //             $("#password").val("");
            //         } else {
            //             sessionStorage.setItem("token", res.token);
            //             sessionStorage.setItem("userId", res.id);
            //             window.location.href = "/label-starter/index";
            //         }
            //     },
            //     error: function (res) {
            //         console.log(error);
            //         $("#loginResult").html("用户名或者密码不正确");
            //         $("#password").val("");
            //     }
            // });

            window.location.href = "index.html";
        }
    </script>
</head>
<div class="container">
    <div class="row">
        <div class="col-md-offset-3 col-md-6">
            <form class="form-horizontal" id="loginForm">
                <span class="heading">用户登录</span>
                <div class="form-group">
                    <input type="text" class="form-control" name="accountName" id="accountName" placeholder="用户名">
                    <i class="fa fa-user"></i>
                </div>
                <div class="form-group help">
                    <input type="password" class="form-control" name="password" id="password" placeholder="密　码">
                    <i class="fa fa-lock"></i>
                    <a href="#" class="fa fa-question-circle"></a>
                    <span id="loginResult" style="color: #bf160b"></span>
                </div>
                <div class="form-group">
                    <button type="button" class="btn btn-default" onclick="Login()">登录</button>
                </div>
            </form>
        </div>
    </div>
</div>