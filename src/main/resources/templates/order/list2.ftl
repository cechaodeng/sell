<html>
<#include "../common/header.ftl">
<body>

<div id="wrapper" class="toggled">
    <#-- 边栏 sidebar -->
    <#include "../common/nav.ftl">

    <#-- 主要内容content -->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <table class="table table-bordered table-condensed">
                        <thead>
                        <tr>
                            <th>订单id</th>
                            <th>姓名</th>
                            <th>手机号</th>
                            <th>地址</th>
                            <th>金额</th>
                            <th>订单状态</th>
                            <th>支付状态</th>
                            <th>创建时间</th>
                            <th colspan="2">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list orderDTOPage.content as orderDTO>
                        <tr>
                            <td>${orderDTO.orderId}</td>
                            <td>${orderDTO.buyerName}</td>
                            <td>${orderDTO.buyerPhone}</td>
                            <td>${orderDTO.buyerAddress}</td>
                            <td>${orderDTO.orderAmount}</td>
                            <td>${orderDTO.getOrderStatusEnum().message}</td>
                            <td>${orderDTO.getPayStatusEnum().message}</td>
                            <td>${orderDTO.createTime}</td>
                            <td><td><a href="/sell/seller/order/detail?orderId=${orderDTO.orderId}">详情</a></td></td>
                            <#if orderDTO.getOrderStatusEnum().message == "新订单">
                                <td><a href="/sell/seller/order/cancel?orderId=${orderDTO.orderId}">取消</a></td>
                            </#if>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <div class="col-md-12 column">
                    <ul class="pagination pull-right">
                    <#if currentPage lte 1>
                        <li class="disabled"><a href="#">Prev</a></li>
                    <#else>
                        <li>
                            <a href="/sell/seller/order/list?page=${currentPage - 1}&size=${orderDTOPage.getSize()}">Prev</a>
                        </li>
                    </#if>

                    <#list 1..orderDTOPage.getTotalPages() as index>
                        <#if currentPage == index>
                            <li class="disabled"><a href="#">${index}</a></li>
                        <#else>
                            <li><a href="/sell/seller/order/list?page=${index}&size=${orderDTOPage.getSize()}">${index}</a></li>
                        </#if>
                    </#list>

                    <#if currentPage gte orderDTOPage.getTotalPages()>
                        <li class="disabled"><a href="#">Next</a></li>
                    <#else>
                        <li>
                            <a href="/sell/seller/order/list?page=${currentPage + 1}&size=${orderDTOPage.getSize()}">Next</a>
                        </li>
                    </#if>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
    <#--提醒弹窗-->
    <div class="modal fade" id="myModal" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" id="myModalLabel">
                        提醒
                    </h4>
                </div>
                <div class="modal-body">
                    你有新的订单
                </div>
                <div class="modal-footer">
                    <button type="button" onclick="javascript:document.getElementById('notice').pause()" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="location.reload()" class="btn btn-primary">查看新订单</button>
                </div>
            </div>
        </div>
    </div>

<#--播放音乐-->
<audio id="notice" loop="loop">
    <source src="/sell/mp3/song.mp3" type="audio/mpeg">
</audio>
<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script>
    var websocket = null;
    if('WebSocket' in window) {
        websocket = new WebSocket('ws://localhost/sell/webSocket');
    }else {
        alert("该浏览器不支持WebSocket");
    }

    websocket.onopen = function (event) {
        console.log("建立连接");
    };

    websocket.onclose = function (event) {
        console.log("连接关闭");
    };

    websocket.onmessage = function (event) {
        console.log("收到消息:" + event.data);
        $("#myModal").modal("show");
        //播放音乐
        document.getElementById('notice').play();
    };

    websocket.onerror = function () {
        alert("webSocket通信发生错误");
    }

    window.onbeforeunload = function () {
        websocket.close();
    };
</script>
</body>
</html>
<#--
<h1>list</h1>-->
