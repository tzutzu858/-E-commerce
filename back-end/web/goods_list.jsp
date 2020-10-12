<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>商品列表</title>

    <meta property="og:title" content="DreamStore"/>
    <meta property="og:description" content="java">
    <meta property="og:type" content="website"/>
    <meta property="og:url" content="FB上的網址"/>
    <meta property="og:image" content="FB的圖片"/>
    <meta name="viewport" content="width=device-width,initial-scle=1.0">

    <link rel="shortcut icon" href="https://icons.iconarchive.com/icons/dakirby309/simply-styled/48/Java-icon.png">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css">
    <link rel="stylesheet" type="text/css" href="css/all.css"/>
</head>
<body>
<div class="wraper">
    <div class="header">
        <div class="nav-bar">
            <div class="nav-logo-block">
                <h1><a href="controller?action=list">Dream</a></h1>
                <c:if test="${not empty customerName}">
                    <div class="username-block">Hello，${customerName}</div>
                </c:if>
            </div>

            <ul class="menu">
                <li><a href="controller?action=list"><i class="list icon"></i>商品列表</a></li>
                <li><a href="controller?action=cart"><i class="cart icon"></i>購物車</a></li>
                <c:if test="${empty customerName}">
                    <li><a href="member.jsp"><i class="id card icon"></i>會員專區</a></li>
                </c:if>
                <c:if test="${not empty customerName}">
                    <li><a href="controller?action=logout"><i class="id card icon"></i>登出</a></li>
                </c:if>
            </ul>
        </div>
        <div class="banner">
            <h2>BUY A DREAM</h2>
            <p class="banner_msg">A dedached space for the soul to settle in the hustle and bustle of the confused
                city.</p>
        </div>
    </div>
    <div class="goodslist-intro">
        <div class="goodslist-intro-title">
            <h3>選擇下列商品，讓今夜在迷茫城市喧囂裡</h3>
            <h3>有個讓心靈沉澱的超然空間</h3>
            <h2>熱銷搶購中</h2>
        </div>
        <div class="swiper">
            <div class="swiper-container">
                <!-- Additional required wrapper -->
                <div class="swiper-wrapper">
                    <!-- Slides -->
                    <c:forEach var="goods" items="${goodsList}" varStatus="status">
                        <div class="swiper-slide"><img src="img/${goods.image}"></div>
                    </c:forEach>
                </div>
                <!-- If we need pagination -->
                <!-- <div class="swiper-pagination"></div> -->

                <!-- If we need navigation buttons -->
                <!-- <div class="swiper-button-prev"></div>
                <div class="swiper-button-next"></div> -->

                <!-- If we need scrollbar -->
                <!-- <div class="swiper-scrollbar"></div> -->
            </div>
        </div>
        <div class="goodslist-section">
            <h4 class="goodslist_msg">請從商品列表中選擇您喜愛的商品</h4>
            <br>
            <ul class="goods-ul">
                <c:forEach var="goods" items="${goodsList}" varStatus="status">
                    <li>
                        <a href="controller?action=detail&id=${goods.id}">
                            <img src="img/${goods.image}" alt="">
                        </a>
                        <div class="buy-info">
                            <h5 class="goods-name">${goods.name}</h5>
                            <i class="goods-price">${goods.price}</i>
                            <span><a
                                    href="controller?action=add&pagename=list&id=${goods.id}&name=${goods.name}&price=${goods.price}&img=${goods.image}"><i
                                    class="add icon"></i></a></span>
                        </div>
                    </li>
                </c:forEach>
            </ul>
            <ul class="pagination">
                <li><a href="controller?action=paging&page=prev">«</a></li>
                <c:forEach var="page" begin="1" end="${totalPageNumber}">
                    <li><a
                            <c:if test="${page == currentPage}">
                                class="active"
                            </c:if>
                            href="controller?action=paging&page=${page}">${page}</a></li>
                </c:forEach>
                <li><a href="controller?action=paging&page=next">»</a></li>
            </ul>
        </div>
    </div>
    <div class="footer">
        <div class="footer_info">
            <p>公司地址：台灣市台灣路台灣號123樓</p>
            <p>客服專線：01-1314-5566</p>
            <p>來電時間：週一〜週五 09:00~18:00 / 週六、週日、國定假日（含連假）休息</p>
            <p>email：java02@thatsdreaming.com.tw</p>
        </div>
        <div class="footer_icon">
            <a href="controller?action=list"><img class="footer_loge" src="svg/title_logo.svg"/></a>
            <ul class="social_network">
                <li><a href=""><img class='icon' src="svg/facebook.svg"/></a></li>
                <li><a href=""><img class='icon' src="svg/instagram-sketched.svg"/></a></li>
                <li><a href=""><img class='icon' src="svg/twitter.svg"/></a></li>
            </ul>
        </div>
    </div>
    <div class="footer_bottom">
        <p>happy coding © 2020 I Love Java.有限公司版權所有</p>
    </div>
</div>
<script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
<script src="js/goodlist.js" type="text/javascript" charset="utf-8"></script>
<script src="https://cdn.jsdelivr.net/npm/jquery@3.2/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
</body>
</html>
