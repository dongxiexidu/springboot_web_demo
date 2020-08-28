# springboot_web_demo
 springboot_web_learn

## 一个员工的增删改查小示例
### 1.只有登录以后才可以查看,如何拦截请求

1.自定义拦截器
```java
// 所有请求都会先拦截,进入这里
//自定义拦截器
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截请求--------");
        //登录成功后，应该有用户的session
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser==null){//没有登录
            request.setAttribute("msg","没有权限，请先登录");
            request.getRequestDispatcher("/index.html").forward(request,response);
            return false;
        }else {
            return true;
        }
    }
}
```

2.添加拦截器(添加到MyMVCConfig中)
```java
// 添加拦截器,添加放行的路径
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LoginHandlerInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/index.html","/","/user/login","/css/*","/js/*","/img/*");
}
```
**注意:放行的路径一定要添加**


### 配置上下文访问路径
```java
#配置上下文访问路径  http://localhost:8080/ldx/
server.servlet.context-path=/ldx
```

### 配置登录页,首页
```java
// 配置首页
@Override
public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("index");//可以访问 http://localhost:8080/ldx/
    registry.addViewController("/index.html").setViewName("index"); //可以访问 http://localhost:8080/ldx/index.html
    registry.addViewController("/main.html").setViewName("dashboard");
    //登录后可以访问 http://localhost:8080/ldx/main.html, 如果没有登录会被拦截
}
```

### 登录控制器
```java
// index.html就是登录页面
// 登录请求,一般单独一个控制器
@Controller
public class LoginController {


    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session) {

        // 登录成功后,使用session保存"loginUser",只要浏览器不退出,一般浏览器保存30分钟?
        if (!StringUtils.isEmpty(username) && password.equals("123456")) {
            session.setAttribute("loginUser", username);
            return "redirect:/main.html";
        } else {
            model.addAttribute("msg", "用户名或密码错误");
            return "index";
        }
    }
}
```

### 错误页面处理
在resources下面新建一个error页面,404.html和500.html放到下面就可以
测试比如以下不存在的页面 http://localhost:8080/ldx/main.html123456
