$(function(){
    // 全局变量
    let isWidthScreen = false;
    var thirtyLeftData = [];//30天内的访问统计左侧
    var urlId = getQueryString("urlId");
    var chartColorList = ['#61a0a8', '#c23531', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a', '#c4ccd3'];
    ajaxPost("/manage/user/currentUser","",function(data){//获取当前用户
        if(!data.code){
            $(".token .currentToken b").text(data.data.token);
            $(".token .selfInfo .name span").text(data.data.name);
            $(".token .selfInfo .mail span").text(data.data.mail);
            $(".token .selfInfo .time span").text(data.data.createTime);
            $(".topBar .beforLogin").siblings(".afterLogin").show().find(".peopleName").text(data.data.name);
            if(data.data.id == 1){
                $(".leftLab .userManage").show();
            }
        }else {
            $(".topBar .afterLogin").siblings(".beforLogin").show();
        }
    });
    topBarAction();//topBar的一些动作
    // 宽屏模式切换
    $(".v-radio").click(function(){
        if(isWidthScreen){
            $(this).removeClass("v-active");
            $(".main").css("max-width","1366px")
        }else{
            $(this).addClass("v-active");
            $(".main").css("max-width","100%")
        }
        setTimeout(function() {
            chart30.resize();
            chart24.resize();
            chartEquipType1.resize();
            chartEquipType2.resize();
            chartBrow.resize();
            chartNet.resize();
            chinaMapChart.resize();
            worldMapChart.resize();
            chinaMapChart_addon.resize();
        }, 200);
        isWidthScreen = !isWidthScreen;
    })

    // 总数据统计栏
    getTotalData();
    function getTotalData(){
        ajaxPost("/manage/logs/totalVisits",{urlId:urlId},function(data){
            if(!data.code){
                var datas = data.data;
                $(".totalData .site .data").text(datas.url).attr("title",datas.url);
                $(".totalData .view .data").text(datas.pv);
                $(".totalData .ip .data").text(datas.ipCount);
                $(".totalData .uv .data").text(datas.userCount);
                
            }else {
                alertTip("err",data.msg,400,4000)
            }
        })
    }

    // 30天内访问数据 时间插件和图表插件初始化
    jeDate("#daterPicker30",{
        range:false,
        multiPane:true,
        donefun:function(val){
            getThirtyData(val.val) 
        }
    })
    var chart30 = creatBarEhart('chart30');
    getThirtyData()
    // 切换30天内访问数据按钮
    $(".thirtyData .dataView .btnBar span").click(function(){
        $(this).addClass("active").siblings().removeClass("active");
        rendLeftData(thirtyLeftData[$(this).index()])
    })
    function rendLeftData(data){
        $(".thirtyData .dataView .datas .view .unm").text(data.pv)
        $(".thirtyData .dataView .datas .ip .unm").text(data.ipCount)
        $(".thirtyData .dataView .datas .UV .unm").text(data.userCount)
    }
    // 请求30天内的访问统计ajax
    function getThirtyData(endDate){
        var paramas = {
            urlId:urlId,
            endDate:endDate
        }
        ajaxPost("/manage/logs/everydayPv",paramas,function(data){
            if(!data.code){
                // 左侧数据处理
                var datas = data.data;
                var everyday = datas.everyday;
                var length = everyday.length;
                var firstDate = everyday[length-1].logDate;
                var secondDate = everyday[length-2].logDate;
                thirtyLeftData[0] = datas[firstDate];
                thirtyLeftData[1] = datas[secondDate];
                thirtyLeftData[2] = datas["week"];
                thirtyLeftData[3] = datas["month"];
                rendLeftData(thirtyLeftData[0]);
                $(".thirtyData .dataView .btnBar span").eq(0).addClass("active").siblings().removeClass("active");
                if(firstDate==getCurrentDate()){
                    $(".thirtyData .btnBar span:eq(0)").text("今天")
                    $(".thirtyData .btnBar span:eq(1)").text("昨天")
                }else {
                    $(".thirtyData .btnBar span:eq(0)").text(firstDate)
                    $(".thirtyData .btnBar span:eq(1)").text(secondDate)
                }
                // 图表处理
                var pv=[],ipCount=[],userCount=[],logDate=[];
                for(var i in everyday){
                    var obj = everyday[i]
                    pv.push(obj.pv)
                    ipCount.push(obj.ipCount)
                    userCount.push(obj.userCount)
                    logDate.push(obj.logDate)
                }
                var option = {
                    xAxis: {
                        data: logDate
                    },
                    series: [
                        {
                            name: '访问量',
                            type: 'line',
                            data: pv
                        },{
                            name: 'IP数',
                            type: 'line',
                            data: ipCount
                        },{
                            name: 'UV数',
                            type: 'line',
                            data: userCount
                        }
                    ]
                }
                chart30.setOption(option);
            }else {
                alertTip("err",data.msg,400,4000)
            }
        })
    }

    // 24小时内访问统计
    jeDate("#daterPicker24",{
        range:false,
        multiPane:true,
        donefun:function(val){
            getHour24Data(val.val)
        }
    })
    var chart24 = creatBarEhart('chart24');
    getHour24Data();
    function getHour24Data(endDate){
        var paramas = {
            urlId:urlId,
            endDate:endDate
        }
        ajaxPost("/manage/logs/everyhourPv",paramas,function(data){
            if(!data.code){
                var datas = data.data;
                // 图表处理
                var pv=[],ipCount=[],userCount=[],logDate=[];
                for(var i in datas){
                    var obj = datas[i]
                    pv.push(obj.pv)
                    ipCount.push(obj.ipCount)
                    userCount.push(obj.userCount)
                    logDate.push(obj.logDate)
                }
                var option = {
                    xAxis: {
                        data: logDate
                    },
                    series: [
                        {
                            name: '访问量',
                            type: 'line',
                            data: pv
                        },{
                            name: 'IP数',
                            type: 'line',
                            data: ipCount
                        },{
                            name: 'UV数',
                            type: 'line',
                            data: userCount
                        }
                    ]
                }
                chart24.setOption(option);
            }else {
                alertTip("err",data.msg,400,4000)
            }
        })
    }

    // 访问者设备系统分析
    jeDate("#daterPickerEquipType",{
        range:" 至 ",
        multiPane:false,
        donefun:function(val){
            var time = getTime(val.val)
            getEquipTypeData(time.startDate,time.endDate)
        }
    })
    var chartEquipType1 = creatPerEhart('chartEquipType1');
    var chartEquipType2 = creatBarEhart('chartEquipType2');
    getEquipTypeData();
    function getEquipTypeData(startDate,endDate){
        var paramas = {
            urlId:urlId,
            startDate:startDate,
            endDate:endDate,
            attribute:"DEVICE"
        }
        ajaxPost("/manage/logs/queryPvByAttribute",paramas,function(data){
            if(!data.code){
                var datas = data.data;
                if(!datas.length){
                    $(".equipTypeData .noData").show().siblings(".dataView").hide();
                    return;
                }else{
                    $(".equipTypeData .noData").hide().siblings(".dataView").show();
                }
                // 图表处理
                var value=[],nameArr=[],per = [],shadow=[],total=0;
                for(var i in datas){
                    var obj = datas[i]
                    value[i] = {
                        value:obj.pv,
                        name:obj.name
                    }
                    nameArr.push(obj.name)
                    total += obj.pv
                }
                for(var i in datas.slice(0,5)){
                    if(total){
                        var val = datas[i].pv
                        per.push((val/total * 100).toFixed(2))
                        shadow.push(100)
                    }else{
                        per.push(0)
                        shadow.push(1)
                    }
                }
                var option1 = {
                    series:[
                        {
                            radius: ['40%', '60%'],
                            name: "访问者设备",
                            data:value
                        }
                    ]
                }
                var option2 = {
                    title: {
                        text: '设备系统TOP5',
                        textStyle:{
                            color:"#fff",
                        },
                        left: '2%',
                    },
                    legend:{
                        show:false,
                    },
                    tooltip: {
                        show:false,
                    },
                    grid: {
                        left: '2%',
                        // right: '20%',
                        containLabel: false,
                    },
                    xAxis: {
                        type: 'value',
                        max: 100,
                        show:false,
                    },
                    yAxis:{
                        type: 'category',
                        data: nameArr,
                        inverse:true,
                        show:false,
                    },
                    series: [
                        { // For shadow
                            type: 'bar',
                            barMaxWidth:"16px",
                            itemStyle: {
                                normal: {color: 'rgba(0,0,0,0.1)'}
                            },
                            barGap:'-100%',
                            barCategoryGap:'40%',
                            data: shadow,
                            animation: false
                        },
                        {
                            name: '访问量',
                            type: 'bar',
                            barMaxWidth:"16px",
                            itemStyle: {
                                barBorderRadius: 4,
                                normal: {
                                    color: function(params) {
                                        // build a color map as your need.
                                        var colorList = chartColorList;
                                        return colorList[params.dataIndex]
                                    }
                                }
                            },
                            label:{
                                show:true,
                                formatter:"{b} : {c}%",
                                position: [0, -20],
                            },
                            data: per
                        }
                    ]
                }
                chartEquipType1.setOption(option1);
                chartEquipType2.setOption(option2);
            }else {
                alertTip("err",data.msg,400,4000)
            }
        })
    }

    // 访问者浏览器统计
    jeDate("#daterPickerBrow",{
        range:" 至 ",
        multiPane:false,
        donefun:function(val){
            var time = getTime(val.val)
            getBrowData(time.startDate,time.endDate)
        }
    })
    var chartBrow = creatBarEhart('chartBrow');
    getBrowData();
    function getBrowData(startDate,endDate){
        var paramas = {
            urlId:urlId,
            startDate:startDate,
            endDate:endDate,
            attribute:"BROWSER"
        }
        ajaxPost("/manage/logs/queryPvByAttribute",paramas,function(data){
            if(!data.code){
                var datas = data.data;
                if(!datas.length){
                    $(".browData .noData").show().siblings(".dataView").hide();
                    return;
                }else{
                    $(".browData .noData").hide().siblings(".dataView").show();
                }
                // 图表处理
                var pv=[],name=[];
                for(var i in datas){
                    var obj = datas[i]
                    pv.push(obj.pv)
                    name.push(obj.name)
                }
                var option = {
                    xAxis: {
                        axisLabel:{'interval':0},
                        data: name
                    },
                    legend: {
                        show:false,
                    },
                    series: [
                        {
                            type: 'bar',
                            barMaxWidth:"100px",
                            data: pv,
                            itemStyle: {
                                normal: {
                                    color: function(params) {
                                        // build a color map as your need.
                                        var colorList = chartColorList;
                                        return colorList[params.dataIndex]
                                    }
                                }
                            }
                        }
                    ]
                }
                chartBrow.setOption(option);
            }else {
                alertTip("err",data.msg,400,4000)
            }
        })
    }

    // 网络供应商统计
    jeDate("#daterPickerNet",{
        range:" 至 ",
        multiPane:false,
        donefun:function(val){
            var time = getTime(val.val)
            getNetData(time.startDate,time.endDate)
        }
    })
    var chartNet = creatBarEhart('chartNet');
    getNetData();
    function getNetData(startDate,endDate){
        var paramas = {
            urlId:urlId,
            startDate:startDate,
            endDate:endDate,
            attribute:"NETWORK"
        }
        ajaxPost("/manage/logs/queryPvByAttribute",paramas,function(data){
            if(!data.code){
                var datas = data.data;
                if(!datas.length){
                    $(".netData .noData").show().siblings(".dataView").hide();
                    return;
                }else{
                    $(".netData .noData").hide().siblings(".dataView").show();
                }
                // 图表处理
                var pv=[],name=[];
                for(var i in datas){
                    var obj = datas[i]
                    pv.push(obj.pv)
                    name.push(obj.name?obj.name:"未知")
                }
                var option = {
                    xAxis: {
                        type: 'value',
                        axisLine:{
                            lineStyle:{
                                color:"#ccc",
                            },
                        },
                        splitLine: {
                            lineStyle:{
                                color: "rgba(255,255,255,0.1)"
                            }
                        },
                    },
                    yAxis: {
                        type: 'category',
                        axisLine:{
                            lineStyle:{
                                color:"#ccc",
                            },
                        },
                        splitLine: {
                            show:false,
                        },
                        data: name,
                        inverse:true,
                    },
                    legend: {
                        show:false,
                    },
                    series: [
                        {
                            type: 'bar',
                            barMaxWidth:"50px",
                            data: pv,
                            itemStyle: {
                                normal: {
                                    color: function(params) {
                                        // build a color map as your need.
                                        var colorList = chartColorList;
                                        return colorList[params.dataIndex]
                                    }
                                }
                            }
                        }
                    ]
                }
                chartNet.setOption(option);
            }else {
                alertTip("err",data.msg,400,4000)
            }
        })
    }

    // 访问记录明细
    var visit_startDate = "",visit_endDate = "",visit_currentPage = 0;
    jeDate("#daterPickerVisit",{
        range:" 至 ",
        multiPane:false,
        donefun:function(val){
            var time = getTime(val.val)
            visit_currentPage = 0;
            visit_startDate = time.startDate;
            visit_endDate = time.endDate;
            getVisitData()
        }
    })
    // 初始化分页
    initPagination("#visitPage",0,function(current){
        visit_currentPage = current - 1;
        getVisitData(true);
    })
    getVisitData();
    function getVisitData(changePage){
        var paramas = {
            urlId:urlId,
            startDate:visit_startDate,
            endDate:visit_endDate,
            currentPage: visit_currentPage,
            pageSize: 30,
        }
        ajaxPost("/manage/logs/recordInfo",paramas,function(data){
            if(!data.code){
                // 渲染数据
                var list = data.data.list;
                if(!list.length){
                    $(".visitData .noData").show().siblings(".dataView").hide();
                    // return;
                }else{
                    $(".visitData .noData").hide().siblings(".dataView").show();
                }
                $(".visitData .dataView tbody").html("");
                for(var i = 0 ; i < list.length ; i ++){
                    var listi = list[i];
                    if(listi.status == "SUCCESS"){
                        var status = "成功"
                    }else{
                        var status = '<i class="refuse">'+ '拦截' +'</i>'
                        // var status =  "拦截"
                    }
                    var html = '<tr data-id ="'+ listi.id +'" class="'+listi.status+'">'+
                                    '<td>'+ listi.createTime +'</td>'+
                                    '<td>'+ listi.url +'</td>'+
                                    '<td>'+ listi.device +'</td>'+
                                    '<td>'+ listi.browser +'</td>'+
                                    '<td>'+ listi.ip +'</td>'+
                                    '<td>'+ listi.province + '-' +listi.city + '-' +listi.networkOperator +'</td>'+
                                    '<td>'+ (listi.source?listi.source:'直接访问') +'</td>'+
                                    '<td>'+ status +'</td>'+
                                '</tr>';
                    $(".visitData .dataView tbody").append(html)

                    if(!changePage){
                        $(".visitData .pageBar .totalPage span").text(data.data.total)
                        $("#visitPage").pagination("setPage", 1, data.data.pageTotal);// 参数2：当前页数，参数3：总页数                    
                    }
                }
            }else {
                alertTip("err",data.msg,400,4000)
            }
        })
    }

    // 高频访问IP名单
    jeDate("#daterPickerIP",{
        range:" 至 ",
        multiPane:false,
        donefun:function(val){
            var time = getTime(val.val)
            getIPData(time.startDate,time.endDate)
        }
    })
    getIPData();
    function getIPData(startDate,endDate){
        var paramas = {
            urlId:urlId,
            startDate:startDate,
            endDate:endDate,
        }
        ajaxPost("/manage/logs/oftenIp",paramas,function(data){
            if(!data.code){
                // 渲染数据
                var list = data.data;
                if(!list.length){
                    $(".IPData .noData").show().siblings(".dataView").hide();
                    // return;
                }else{
                    $(".IPData .noData").hide().siblings(".dataView").show();
                }
                $(".IPData .dataView tbody").html("");
                for(var i = 0 ; i < list.length ; i ++){
                    var listi = list[i];
                    if(listi.status == "SUCCESS"){
                        var status = "成功"
                    }else{
                        var status = "失败"
                    }
                    var html = '<tr data-id ="'+ listi.ip +'">'+
                                    '<td>'+ listi.ip +'</td>'+
                                    '<td>'+ listi.province + '-' +listi.city + '-' +listi.network +'</td>'+
                                    '<td>'+ listi.pv +'</td>'+
                                '</tr>';
                    $(".IPData .dataView tbody").append(html)
                }
            }else {
                alertTip("err",data.msg,400,4000)
            }
        })
    }

    // 中国地图
    var chinaMap_startDate = "",chinaMap_endDate = "",chinaMap_attribute = "PROVINCE", chinaMap_condition = null;
    var CHINA_PROVINCE_MAP={'安徽':'anhui','澳门':'aomen','北京':'beijing','重庆':'chongqing','福建':'fujian','甘肃':'gansu','广东':'guangdong','广西':'guangxi','贵州':'guizhou','海南':'hainan','河北':'hebei','黑龙江':'heilongjiang','河南':'henan','湖北':'hubei','湖南':'hunan','江苏':'jiangsu','江西':'jiangxi','吉林':'jilin','辽宁':'liaoning','内蒙古':'neimenggu','宁夏':'ningxia','青海':'qinghai','山东':'shandong','上海':'shanghai','山西':'shanxi','陕西':'shanxi1','四川':'sichuan','台湾':'taiwan','天津':'tianjin','香港':'xianggang','新疆':'xinjiang','西藏':'xizang','云南':'yunnan','浙江':'zhejiang'};    
    var isChinaMap = true;
    var chinaMapJson ="/plug/mapJson/china.json",chinaMapType = "china";
    var chinaMapChart = echarts.init(document.getElementById("chartChinaMap"));
    var chinaMapChart_addon = creatBarEhart('chartChinaMap_addon'); 
    getChinaMapData()
    function getChinaMapData(){
        var paramas = {
            urlId:urlId,
            startDate:chinaMap_startDate,
            endDate:chinaMap_endDate,
            attribute: chinaMap_attribute,
            condition:chinaMap_condition
        }
        ajaxPost("/manage/logs/queryPvByAttribute",paramas,function(data){
            if(!data.code){
                // 渲染数据
                var list = data.data;
                drawChinaMap(chinaMapJson,chinaMapType,list)
                if(!list.length){
                    $(".ChinaMapData #chartChinaMap_addon").css({"opacity":0,"height":"1px"});
                    return;
                }else{
                    $(".ChinaMapData #chartChinaMap_addon").css("opacity",1);
                }
                drawChinaMap_addon(list)
            }else {
                alertTip("err",data.msg,400,4000)
            }
        })
    }
    function drawChinaMap(mapJson,mapType,dataList){
        var maxValue = 0;
        var newData = []; 
        if(dataList){
            for(var i in dataList){
                newData[i] = {
                    name: chinaMap_condition?dataList[i].name+"市":dataList[i].name,
                    value: dataList[i].pv
                }
                if(dataList[i].pv > maxValue){
                    maxValue = dataList[i].pv
                }
            }
        }
        $.get(mapJson, function (json) {
            echarts.registerMap(mapType, json);
            // echarts.registerMap('china', chinaJson);
            // var chart = echarts.init(document.getElementById('main'));
            // chart.setOption({
            //     series: [{
            //         type: 'map',
            //         map: 'china'
            //     }]
            // });
            var option = {
                tooltip: {
                    show: true,
                    formatter: function(params) {
                        if (params.data) {
                            return params.name + '：' + params.data['value']
                        }else {
                            return params.name + '：' + '该地区无访问'
                        }
                    },
                },
                visualMap: {
                    min: 0,
                    max: maxValue || 1,
                    left: 'right',
                    top: 'bottom',
                    text:['高','低'],  
                    textStyle:{
                        color:"#fff",   
                    },
                    calculable : true
                },
                series : [
                    {
                        type: 'map',
                        mapType: mapType,
                        roam: false,
                        label: {
                            normal: {
                                show: true,
                                color:"#333",
                                // formatter:function(val){
                                //     console.log(val, 9999999999)
                                //     var area_content =  '{a|' + val.name + '}' + '-' + '{b|' + val.value + '}';
                                //     return area_content.split("-").join("\n");
                                // },//让series 中的文字进行换行
                            },
                            emphasis: {
                                show: false
                            }
                        },
                        data:newData
                    },
                ]
            };
            chinaMapChart.setOption(option);
            $(window).resize(function (){
                chinaMapChart.resize();
            })
        });
    }
    function drawChinaMap_addon(dataList){
        // 图表处理
        var pv=[],name=[];
        for(var i in dataList.slice(0,10)){
            var obj = dataList[i]
            pv.push(obj.pv)
            name.push(obj.name?obj.name:"未知")
        }
        var option = {
            title: {
                text: '访问地区TOP10',
                textStyle:{
                    color:"#fff",
                },
                left: "center",
            },
            xAxis: {
                type: 'value',
                axisLine:{
                    lineStyle:{
                        color:"#ccc",
                    },
                },
                splitLine: {
                    lineStyle:{
                        color: "rgba(255,255,255,0.05)"
                    }
                },
            },
            yAxis: {
                type: 'category',
                axisLine:{
                    lineStyle:{
                        color:"#ccc",
                    },
                },
                splitLine: {
                    show:false,
                },
                inverse:true,
                data: name,
            },
            legend: {
                show:false,
            },
            series: [
                {
                    type: 'bar',
                    barMaxWidth:"50px",
                    data: pv,
                    itemStyle: {
                        normal: {
                            color: function(params) {
                                // build a color map as your need.
                                var colorList = chartColorList;
                                return colorList[params.dataIndex]
                            }
                        }
                    }
                }
            ]
        }
        chinaMapChart_addon.setOption(option);
    }
    chinaMapChart.on('click', function(params) {
        if(params.value && !chinaMap_condition){//有值
            chinaMapType = CHINA_PROVINCE_MAP[params.name];
            chinaMapJson = "/plug/mapJson/province/"+chinaMapType+".json";
            chinaMap_attribute = "CITY";
            chinaMap_condition = params.name;
            getChinaMapData()
        }else if(chinaMap_condition){
            chinaMapType = "china";
            chinaMapJson = "/plug/mapJson/china.json";
            chinaMap_attribute = "PROVINCE";
            chinaMap_condition = null;
            getChinaMapData()
        }
    });
    jeDate("#daterPickerChinaMap",{
        range:" 至 ",
        multiPane:false,
        donefun:function(val){
            var time = getTime(val.val)
            chinaMap_startDate = time.startDate;
            chinaMap_endDate = time.endDate;
            // chinaMap_attribute = "PROVINCE";
            // chinaMap_condition = null;
            // chinaMapJson ="/plug/mapJson/china.json";
            // chinaMapType = "china";
            getChinaMapData()
        }
    })

    // 全球访问地区统计
    var WORLD_NAME_MAP = {'Afghanistan':'阿富汗','Angola':'安哥拉','Albania':'阿尔巴尼亚','United Arab Emirates':'阿联酋','Argentina':'阿根廷','Armenia':'亚美尼亚','French Southern and Antarctic Lands':'法属南半球和南极领地','Australia':'澳大利亚','Austria':'奥地利','Azerbaijan':'阿塞拜疆','Burundi':'布隆迪','Belgium':'比利时','Benin':'贝宁','Burkina Faso':'布基纳法索','Bangladesh':'孟加拉国','Bulgaria':'保加利亚','The Bahamas':'巴哈马','Bosnia and Herzegovina':'波斯尼亚和黑塞哥维那','Belarus':'白俄罗斯','Belize':'伯利兹','Bermuda':'百慕大','Bolivia':'玻利维亚','Brazil':'巴西','Brunei':'文莱','Bhutan':'不丹','Botswana':'博茨瓦纳','Central African Republic':'中非共和国','Canada':'加拿大','Switzerland':'瑞士','Chile':'智利','China':'中国','Ivory Coast':'象牙海岸','Cameroon':'喀麦隆','Democratic Republic of the Congo':'刚果民主共和国','Republic of the Congo':'刚果共和国','Colombia':'哥伦比亚','Costa Rica':'哥斯达黎加','Cuba':'古巴','Northern Cyprus':'北塞浦路斯','Cyprus':'塞浦路斯','Czech Republic':'捷克共和国','Germany':'德国','Djibouti':'吉布提','Denmark':'丹麦','Dominican Republic':'多明尼加共和国','Algeria':'阿尔及利亚','Ecuador':'厄瓜多尔','Egypt':'埃及','Eritrea':'厄立特里亚','Spain':'西班牙','Estonia':'爱沙尼亚','Ethiopia':'埃塞俄比亚','Finland':'芬兰','Fiji':'斐','Falkland Islands':'福克兰群岛','France':'法国','Gabon':'加蓬','United Kingdom':'英国','Georgia':'格鲁吉亚','Ghana':'加纳','Guinea':'几内亚','Gambia':'冈比亚','Guinea Bissau':'几内亚比绍','Equatorial Guinea':'赤道几内亚','Greece':'希腊','Greenland':'格陵兰','Guatemala':'危地马拉','French Guiana':'法属圭亚那','Guyana':'圭亚那','Honduras':'洪都拉斯','Croatia':'克罗地亚','Haiti':'海地','Hungary':'匈牙利','Indonesia':'印尼','India':'印度','Ireland':'爱尔兰','Iran':'伊朗','Iraq':'伊拉克','Iceland':'冰岛','Israel':'以色列','Italy':'意大利','Jamaica':'牙买加','Jordan':'约旦','Japan':'日本','Kazakhstan':'哈萨克斯坦','Kenya':'肯尼亚','Kyrgyzstan':'吉尔吉斯斯坦','Cambodia':'柬埔寨','South Korea':'韩国','Kosovo':'科索沃','Kuwait':'科威特','Laos':'老挝','Lebanon':'黎巴嫩','Liberia':'利比里亚','Libya':'利比亚','Sri Lanka':'斯里兰卡','Lesotho':'莱索托','Lithuania':'立陶宛','Luxembourg':'卢森堡','Latvia':'拉脱维亚','Morocco':'摩洛哥','Moldova':'摩尔多瓦','Madagascar':'马达加斯加','Mexico':'墨西哥','Macedonia':'马其顿','Mali':'马里','Myanmar':'缅甸','Montenegro':'黑山','Mongolia':'蒙古','Mozambique':'莫桑比克','Mauritania':'毛里塔尼亚','Malawi':'马拉维','Malaysia':'马来西亚','Namibia':'纳米比亚','New Caledonia':'新喀里多尼亚','Niger':'尼日尔','Nigeria':'尼日利亚','Nicaragua':'尼加拉瓜','Netherlands':'荷兰','Norway':'挪威','Nepal':'尼泊尔','New Zealand':'新西兰','Oman':'阿曼','Pakistan':'巴基斯坦','Panama':'巴拿马','Peru':'秘鲁','Philippines':'菲律宾','Papua New Guinea':'巴布亚新几内亚','Poland':'波兰','Puerto Rico':'波多黎各','North Korea':'朝鲜','Portugal':'葡萄牙','Paraguay':'巴拉圭','Qatar':'卡塔尔','Romania':'罗马尼亚','Russia':'俄罗斯','Rwanda':'卢旺达','Western Sahara':'西撒哈拉','Saudi Arabia':'沙特阿拉伯','Sudan':'苏丹','South Sudan':'南苏丹','Senegal':'塞内加尔','Solomon Islands':'所罗门群岛','Sierra Leone':'塞拉利昂','El Salvador':'萨尔瓦多','Somaliland':'索马里兰','Somalia':'索马里','Republic of Serbia':'塞尔维亚共和国','Suriname':'苏里南','Slovakia':'斯洛伐克','Slovenia':'斯洛文尼亚','Sweden':'瑞典','Swaziland':'斯威士兰','Syria':'叙利亚','Chad':'乍得','Togo':'多哥','Thailand':'泰国','Tajikistan':'塔吉克斯坦','Turkmenistan':'土库曼斯坦','East Timor':'东帝汶','Trinidad and Tobago':'特里尼达和多巴哥','Tunisia':'突尼斯','Turkey':'土耳其','United Republic of Tanzania':'坦桑尼亚联合共和国','Uganda':'乌干达','Ukraine':'乌克兰','Uruguay':'乌拉圭','United States of America':'美国','Uzbekistan':'乌兹别克斯坦','Venezuela':'委内瑞拉','Vietnam':'越南','Vanuatu':'瓦努阿图','West Bank':'西岸','Yemen':'也门','South Africa':'南非','Zambia':'赞比亚','Zimbabwe':'津巴布韦'};    
    var worldMapChart = echarts.init(document.getElementById("chartWorldMap"));
    getWorldMapData()
    function getWorldMapData(){
        var paramas = {
            urlId:urlId,
            startDate:chinaMap_startDate,
            endDate:chinaMap_endDate,
            attribute: "COUNTRY",
        }
        ajaxPost("/manage/logs/queryPvByAttribute",paramas,function(data){
            if(!data.code){
                // 渲染数据
                var list = data.data;
                drawWorldMap("/plug/mapJson/world.json","world",list)
            }else {
                alertTip("err",data.msg,400,4000)
            }
        })
    }
    function drawWorldMap(mapJson,mapType,dataList){
        var maxValue = 0;
        var newData = []; 
        if(dataList){
            for(var i in dataList){
                newData[i] = {
                    name: dataList[i].name,
                    value: dataList[i].pv
                }
                if(dataList[i].pv > maxValue){
                    maxValue = dataList[i].pv
                }
            }
        }
        $.get(mapJson, function (json) {
            echarts.registerMap(mapType, json);
            var option = {
                tooltip: {
                    show: true,
                    formatter: function(params) {
                        if (params.data) {
                            return params.name + '：' + params.data['value']
                        }else {
                            return params.name + '：' + '该地区无访问'
                        }
                    },
                },
                visualMap: {
                    min: 0,
                    max: maxValue || 1,
                    left: 'right',
                    top: 'bottom',
                    text:['高','低'],  
                    textStyle:{
                        color:"#fff",   
                    },
                    calculable : true,
                },
                series : [
                    {
                        type: 'map',
                        mapType: mapType,
                        roam: false,
                        data:newData,
                        nameMap: WORLD_NAME_MAP
                    },
                ]
            };
            worldMapChart.setOption(option);
            $(window).resize(function (){
                worldMapChart.resize();
            })
        });
    }
    jeDate("#daterPickerWorldMap",{
        range:" 至 ",
        multiPane:false,
        donefun:function(val){
            var time = getTime(val.val)
            chinaMap_startDate = time.startDate;
            chinaMap_endDate = time.endDate;
            // chinaMap_attribute = "PROVINCE";
            // chinaMap_condition = null;
            // chinaMapJson ="/plug/mapJson/china.json";
            // chinaMapType = "china";
            getWorldMapData()
        }
    })

    // 图表插件bar/line
    function creatBarEhart(id){
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById(id));
        
        // 指定图表的配置项和数据
        var option = {
            color: chartColorList,
            tooltip: {
                trigger: 'axis',
            },
            legend: {
                // data:['销量']
                icon: "circle",
                selectedMode:"multiple",
                textStyle:{
                    color:"#fff"
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true,
            },
            textStyle:{
                color:"#fff",
            },
            xAxis: {
                axisLine:{
                    lineStyle:{
                        color:"#ccc",
                    },
                },
                splitLine: {
                    show:false,
                },
                data: []
            },
            yAxis: {
                axisLine:{
                    lineStyle:{
                        color:"#ccc",
                    },
                },
                splitLine: {
                    lineStyle:{
                        color: "rgba(255,255,255,0.1)"
                    }
                },
            },
            series: [
                // {
                //     name: '销量',
                //     type: 'line',
                //     data: [5, 20, 36, 10, 10, 20]
                // },{
                //     name: '销量2',
                //     type: 'line',
                //     data: [15, 10, 26, 20, 40, 30]
                // }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        $(window).resize(function (){
            myChart.resize();
        })
        return myChart;
    }
    // 图表插件per
    function creatPerEhart(id){
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById(id));
        
        // 指定图表的配置项和数据
        var option = {
            color:chartColorList,
            legend: {
                icon: "circle",
                selectedMode:"multiple",
                bottom: 0,
                textStyle:{
                    color:"#fff"
                }
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            series : [
                {
                    type: 'pie',
                    // radius : '60%',
                    center: ['50%', '50%'],
                    label: {
                        position:"outside",
                    },
                    data:[],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        // window.onresize = function () {
	    // 	myChart.resize();
        // }
        $(window).resize(function (){
            myChart.resize();
        })
        return myChart;
    }
    // 获取时间框的初始时间和结束时间
    function getTime(time){
        if(time){
            var dateArr = time.split(" 至 ");
            var paramas = {
                startDate:dateArr[0],
                endDate:dateArr[1]
            }
        }else {
            var paramas = {
                startDate:"",
                endDate:""
            }
        }
        return paramas
    }

})