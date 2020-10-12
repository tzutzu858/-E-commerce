package com.company.store.web;

import com.company.store.domain.Customer;
import com.company.store.domain.Goods;
import com.company.store.server.CustomerService;
import com.company.store.server.GoodsService;
import com.company.store.server.OrdersService;
import com.company.store.server.ServiceException;
import com.company.store.server.imp.CustomerServiceImp;
import com.company.store.server.imp.GoodsServiceImp;
import com.company.store.server.imp.OrdersServiceImp;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@javax.servlet.annotation.WebServlet(name = "Controller",urlPatterns = {"/controller}"})
public class Controller extends javax.servlet.http.HttpServlet {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private CustomerService customerService = new CustomerServiceImp();
    private GoodsService goodsService = new GoodsServiceImp();
    private OrdersService ordersService = new OrdersServiceImp();

    private int totalPageNumber = 0; // 總頁數
    private int pageSize = 10; // 每頁行數
    private int currentPage = 1; // 目前頁數

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        pageSize = new Integer(config.getInitParameter("pageSize"));


    }
    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //從client 提交的 操作參數
        String action = request.getParameter("action");

        if ("reg".equals(action)) {
            // -----------客戶註冊------------
            String userid = request.getParameter("userid");
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String birthday = request.getParameter("birthday");
            String phone = request.getParameter("phone");

            // 服務端驗證
            List<String> errors = new ArrayList<>();
            List<String> ok = new ArrayList<>();
            if (userid == null || userid.equals("")) {
                errors.add("帳號不能為空值！");
            }
            if (name == null || name.equals("")) {
                errors.add("姓名尚未填寫！");
            }
            if (password == null || password.equals("")) {
                errors.add("密碼尚未輸入！");
            }

            String pattern = "^((((19|20)(([02468][048])|([13579][26]))-02-29))|((20[0-9][0-9])|(19[0-9][0-9]))-((((0[1-9])|(1[0-2]))-((0[1-9])|(1\\d)|(2[0-8])))|((((0[13578])|(1[02]))-31)|(((0[1,3-9])|(1[0-2]))-(29|30)))))$";
            if (!Pattern.matches(pattern, birthday)) {
                errors.add("日期格式有誤！");
            }

            if (errors.size() > 0) { // 驗證失敗
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("member.jsp").forward(request, response);
            } else { // 驗證成功
                Customer customer = new Customer();
                customer.setId(userid);
                customer.setName(name);
                customer.setPassword(password);
                customer.setPhone(phone);
                try {
                    Date d = dateFormat.parse(birthday);
                    customer.setBirthday(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // 註冊
                try {
                    // 註冊成功
                    customerService.register(customer);
                    ok.add("註冊成功！");
                    request.setAttribute("ok", ok);
                    request.getRequestDispatcher("member.jsp").forward(request, response);
                } catch (ServiceException e) {
                    // ID已經被註冊
                    errors.add("ID已經有人使用，請重新輸入！");
                    request.setAttribute("errors", errors);
                    request.getRequestDispatcher("member.jsp").forward(request, response);
                }

            }
        } else if ("login".equals(action)) {
            //------------客戶登入--------------
            String userid = request.getParameter("userid");
            String password = request.getParameter("password");

            Customer customer = new Customer();
            customer.setId(userid);
            customer.setPassword(password);


            if (customerService.login(customer)) { // 登入成功
                HttpSession session = request.getSession();
                session.setAttribute("customer", customer);
                session.setAttribute("customerName", customer.getName()); // 把使用者名字存到 session
                request.getRequestDispatcher("controller?action=list").forward(request, response);
            } else { // 登入失敗
                List<String> errors = new ArrayList<>();
                errors.add("您輸入的帳號或密碼有誤！");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("member.jsp").forward(request, response);
            }

        }else if ("logout".equals(action)) {
            HttpSession session = request.getSession();
            session.removeAttribute("customerName");
            request.getRequestDispatcher("controller?action=list").forward(request, response);
        }
        else if ("list".equals(action)) {
            //------------商品列表--------------
            List<Goods> goodsList = goodsService.queryAll();

            if (goodsList.size() % pageSize == 0) {
                totalPageNumber = goodsList.size() / pageSize;
            } else {
                totalPageNumber = goodsList.size() / pageSize + 1;
            }

            request.setAttribute("totalPageNumber", totalPageNumber);
            request.setAttribute("currentPage", currentPage);

            int start = (currentPage - 1) * pageSize;
            int end = currentPage * pageSize;
            if (currentPage == totalPageNumber) { // 最後一頁
                end = goodsList.size();
            }

            request.setAttribute("goodsList", goodsList.subList(start, end));
            request.getRequestDispatcher("goods_list.jsp").forward(request, response);

            //------------有登入秀名字----------------

            HttpSession session = request.getSession();
            session.getAttribute("customerName");



        } else if ("paging".equals(action)) {
            //------------商品列表分頁-------------
            String page = request.getParameter("page");

            if (page.equals("prev")) { //上一頁
                currentPage--;
                if (currentPage < 1) {
                    currentPage = 1;
                }
            } else if (page.equals("next")) {// 下一頁
                currentPage++;
                if (currentPage > totalPageNumber) {
                    currentPage = totalPageNumber;
                }
            } else {
                currentPage = Integer.valueOf(page);
            }
            int start = (currentPage - 1) * pageSize;
            int end = currentPage * pageSize;

            List<Goods> goodsList = goodsService.queryByStartEnd(start, end);

            request.setAttribute("totalPageNumber", totalPageNumber);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("goodsList", goodsList);
            request.getRequestDispatcher("goods_list.jsp").forward(request, response);

        } else if ("detail".equals(action)) {
            //----------查看商品詳細資料-----------
            String goodsid = request.getParameter("id");
            Goods goods = goodsService.queryDetail(new Long(goodsid));

            request.setAttribute("goods", goods);
            request.getRequestDispatcher("goods_detail.jsp").forward(request, response);

        } else if ("add".equals(action)) {
            //----------添加購物車------------
            Long goodsid = new Long(request.getParameter("id"));
            String goodsname = request.getParameter("name");
            Float price = new Float(request.getParameter("price"));
            String goodsimg = request.getParameter("img");
            // 購物車結構是List中包含Map，每一個Map是一個商品
            // 從Session中取出的購物車
            List<Map<String, Object>> cart = (List<Map<String, Object>>) request.getSession().getAttribute("cart");

            if (cart == null) { // 第一次取出是null
                cart = new ArrayList<Map<String, Object>>();
                request.getSession().setAttribute("cart", cart);
            }

            // 購物車中有打勾(選擇)的商品
            int flag = 0;
            for (Map<String, Object> item : cart) {
                Long goodsid2 = (Long) item.get("goodsid");
                if (goodsid.equals(goodsid2)) {

                    Integer quantity = (Integer) item.get("quantity");
                    quantity++;
                    item.put("quantity", quantity);

                    flag++;
                }
            }
            // 購物車中没有打勾(選擇)的商品
            if (flag == 0) {
                Map<String, Object> item = new HashMap<>();
                // item 結構是Map [商品id，商品名稱，價格，數量]
                item.put("goodsid", goodsid);
                item.put("goodsname", goodsname);
                item.put("quantity", 1);
                item.put("price", price);
                item.put("goodsimg", goodsimg);
                // 添加到购物车
                cart.add(item);
            }

            System.out.println(cart);

            String pagename = request.getParameter("pagename");

            if (pagename.equals("list")) {
                int start = (currentPage - 1) * pageSize;
                int end = currentPage * pageSize;

                List<Goods> goodsList = goodsService.queryByStartEnd(start, end);

                request.setAttribute("totalPageNumber", totalPageNumber);
                request.setAttribute("currentPage", currentPage);
                request.setAttribute("goodsList", goodsList);
                request.getRequestDispatcher("goods_list.jsp").forward(request, response);

            } else if (pagename.equals("detail")) {

                Goods goods = goodsService.queryDetail(new Long(goodsid));
                request.setAttribute("goods", goods);
                request.getRequestDispatcher("goods_detail.jsp").forward(request, response);
            }
        } else if ("cart".equals(action)) {
            //---------查看購物車--------
            // 從Session中取出的購物車
            List<Map<String, Object>> cart = (List<Map<String, Object>>) request.getSession().getAttribute("cart");

            double total = 0.0;

            if (cart != null) {
                for (Map<String, Object> item : cart) {

                    Integer quantity = (Integer) item.get("quantity");
                    Float price = (Float) item.get("price");
                    double subtotal = price * quantity;
                    total += subtotal;
                }
            }

            request.setAttribute("total", total);
            HttpSession session = request.getSession();
            session.getAttribute("customerName");
            request.getRequestDispatcher("cart.jsp").forward(request, response);
        } else if ("sub_ord".equals(action)) {
            //------------提交訂單-----------
            // Session中取出的從購物車
            List<Map<String, Object>> cart = (List<Map<String, Object>>) request.getSession().getAttribute("cart");
            for (Map<String, Object> item : cart) {
                Long goodsid = (Long) item.get("goodsid");
                String strquantity = request.getParameter("quantity_" + goodsid);
                 int quantity = 0;
                try {
                    quantity = new Integer(strquantity);
                } catch (Exception e) {
                }

                item.put("quantity", quantity);
            }

            // 提交訂單
            String ordersid = ordersService.submitOrders(cart);
            request.setAttribute("ordersid", ordersid);
            request.getRequestDispatcher("order_finish.jsp").forward(request, response);
            // 清空購物車
            request.getSession().removeAttribute("cart");
        } else if ("main".equals(action)) {
            //--------------進入主頁面--------------------
            request.getRequestDispatcher("main.jsp").forward(request, response);
        }else if ("logout".equals(action)) {
            //-----------註銷-----------------------
            // 清空購物車
            request.getSession().removeAttribute("cart");
            // 清除登入(登出)
            request.getSession().removeAttribute("customer");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }else if ("reg_init".equals(action)) {
            // -----------客户註冊頁面進入------------
            request.getRequestDispatcher("customer_reg.jsp").forward(request, response);
        }
    }
}
