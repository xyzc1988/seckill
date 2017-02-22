/*存放主要交互逻辑*/
//javascript 模块化
var seckill = {
    //封装秒杀相关ajax的url
    URL: {
        now : function(){
            return "/seckill/time/now";
        },
        exposer:function(seckillId){
            return "/seckill/" + seckillId + "/exposer";
        },
        execution : function(seckillId,md5){
            return "/seckill/"+ seckillId +"/"+md5+"/execution";
        }
    },
    handleSeckill : function(seckillId,node){
        //获取秒杀地址,执行秒杀
        node.hide().html('<button class="btn btn-primary" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId),function(result){
            if(result && result.success){
                var exposer = result.data;
                if(exposer['exposed']){
                    //开启秒杀
                    node.show();
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId,md5);
                    //绑定一次事件
                    $("#killBtn").one('click', function () {
                        //执行秒杀请求
                        //1:禁用按钮
                        $(this).addClass('disabled');
                        //2:发送秒杀请求
                        $.post(killUrl, function (result) {
                            if(result){
                                var killResult = result.data;
                                var state = killResult.state;
                                var stateInfo = killResult.stateInfo;
                                if(result.success){
                                    node.html('<span class="label label-success">' +stateInfo+'</span>')
                                }else{
                                    node.html('<span class="label label-danger">' +stateInfo+'</span>')
                                }

                            }else {
                                console.info("执行秒杀结果:" + result);
                            }
                        })
                    })

                }else{
                    //未开启秒杀,重新激活计时逻辑
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.countDown(seckillId,now,start,end);
                }
            }else{
                console.log(result);
            }
        })
    },
    //验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    countDown : function (seckillId,nowTime,startTime,endTime) {
        var seckill_box = $("#seckill_box");
        if(nowTime > endTime){
            seckill_box.html("秒杀结束!");
        }else if(nowTime < startTime){
            var killTIme = new Date(startTime + 1000);
            seckill_box.countdown(killTIme, function (event) {
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
                seckill_box.html(format);
            }).on('finish.countdown', function () {
                //时间完成后回调事件
                seckill.handleSeckill(seckillId,seckill_box);
            })
        }else{
            seckill.handleSeckill(seckillId,seckill_box);
        }
        
    },
    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
            //用户的手机验证和登陆,技术交互
            //规划我们的交互流程
            //在Cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            var startTime = params.startTime;
            var endTime = params.endTIme;
            var seckillId = params.seckillId;
            if(!seckill.validatePhone(killPhone)) {
                //绑定Phone
                var killPhoneModal = $("#killPhoneModal")
                killPhoneModal.modal({
                    show:true,//显示弹出层
                    backdrop:'static',//禁止位置关闭
                    keyboard:false//关闭键盘事件
                })
                $("#killPhoneBtn").click(function(){
                    var inputPhone = $("#killPhoneKey").val();
                    if(seckill.validatePhone(inputPhone)){
                        //电话写入cookie
                        $.cookie("killPhone",inputPhone,{expires:7,path:'/seckill'})
                        //刷新页面
                        window.location.reload();
                    }else{
                        $("#killPhoneMessage").hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }

                })
            }
            //已经登录
            //及时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(),{}, function (result) {
                if(result && result['success']){
                    var nowTime = result["data"];
                    seckill.countDown(seckillId,nowTime,startTime,endTime);
                }else{
                    console.log("result:" + result);//TODO
                }
            })

        }
    }
}