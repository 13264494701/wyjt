<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>加入我们-无忧借条</title>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" href="${homeStatic}/assets/css/p1.css?v=25" type="text/css">
    <link rel="stylesheet" href="${homeStatic}/assets/css/home/aboutUs.css?v=24" type="text/css">
</head>
<body>

<div class="v1-topbg">
 <%@include file="/WEB-INF/views/home/header.jsp" %>
    <div class="aboutus">
        <%@include file="menus.jsp" %>
        <div class="joinus clearfix">
            <a class="left"></a>

            <div class="join clearfix" style="margin-left: 94px;width: 688px;">
                <p class="txt">我们的事业正在迅猛发展，诚邀和我们一样有梦想、有激情的年轻人加盟，共创未来。请将发送简历至：<a>hr@cpbao.com</a></p>
            </div>
            <div class="scroller">
                <div class="scroll-inner">
                    <div class="join">
                        <div class="con clearfix">
                            <div class="con-l"><p>高级IOS<br/>开发工程师</p></div>
                            <div class="con-r">
                                <p><span>工作地点:北京</span> <span>招聘人数：2人</span> <span>岗位职责：</span> 1、负责手机ios客户端业务的软件设计与开发。 2、进行软件需求分析及可行性分析；<br/> 3、与项目相关人员配合共同完成手机应用软件的开发设计工作；<br/> 4、遵循软件开发流程，独立的进行应用及人机界面软件模块的设计和实现；<br/> 5、手机应用软件开发环境的设计、实现和维护；<br/> <span> 任职要求：</span> 1、1年以上手机客户端相关开发经验；<br/> 2、具备 ios 开发经验， 具备不同手机的丰富移植经验对手机终端的各类客户端应用有广泛的了解，针对实用性软件等有比较深入的研究；<br/> 3、熟悉手机UI，socket编程。</p>
                            </div>
                        </div>
                    </div>
                    <div class="join">

                        <div class="con clearfix">
                            <div class="con-l"><p>Android<br/>开发工程师</p></div>
                            <div class="con-r">
                                <p><span>工作地点:北京</span> <span>招聘人数：1人</span> <span>岗位职责：</span> 1、在上级的领导和监督下定期完成量化的工作要求，并能独立处理和解决所负责的任务；<br/> 2、与项目相关人员一起，对嵌入式产品应用软件进行需求分析和可行性评估；<br/> 3、负责android嵌入式产品软件的架构设计、开发，并配合测试人员进行测试工作；<br/> 4、负责软件项目的功能模块设计及相关过程文档的编写；<br/> 5、负责应用软件的维护和升级；<br/>
                                    <span> 任职要求：</span> 1、计算机、通信及相关专业，本科及以上学历；<br/> 2、至少1年android开发经验，1~2年以上嵌入式软件开发工作经验；<br/> 3、熟悉android SDK，精通JAVA语言开发，熟悉C语言开发；<br/> 4、熟悉android上的ui界面设计，熟悉JavaScript、HTML、XQuery等；<br/> 5、良好的团队合作意识。 </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <a class="right"></a>
        </div>
    </div>
 <%@include file="/WEB-INF/views/home/footer.jsp" %>
</div>
</body>
<script>
    seajs.use('jquery', function ($) {
        var  $box = $('.scroll-inner'), $boxFirstCh = $box.find('.join').eq(0),
                _width = $boxFirstCh.width(), _index = 0, length;
        var cloneJoin = $boxFirstCh.clone(true);
        $box.append(cloneJoin);
        length = $box.find('.join').length;
        $box.css('width', _width * length + 'px');

        var $left = $('.left'), $right = $('.right');
        $left.click(function () {
            if (_index == length - 1) {
                _index = 0;
                $box.css('margin-left', '0');
                $box.animate({'margin-left': -_width * (++_index) + 'px'}, 'slow');
            }
            else {
                $box.animate({'margin-left': -_width * (++_index) + 'px'}, 'slow');
            }
        });
        $right.click(function () {
            if (_index == length - 1) {
                _index = 0;
                $box.css('margin-left', -_width * (++_index) + 'px');
            }
            if (_index == 0) {
                _index = 1;
                $box.css('margin-left', -_width * (length - 1) + 'px');
                $box.animate({'margin-left': -_width * (_index) + 'px'}, '20000');
            }
            else  $box.animate({'margin-left': -_width * (--_index) + 'px'}, 'slow');
        });

    });

</script>
</html>
<!-- created in 2016-04-12 16:49-->