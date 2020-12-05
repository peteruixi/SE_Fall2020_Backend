package com.healthMonitor.fall2020.filter;

import com.healthMonitor.fall2020.utils.JwtTokenUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 *
 */
public class AuthenticationInterceptor implements HandlerInterceptor {




    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
      //  String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        String token=(String) request.getHeader("token");
        if(token!=null&&!token.equals(""))
        {
            //System.out.println("获取到header了");
        }
        String token1=(String) request.getParameter("token");

        if(token==null) token=token1;
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();

        String userId=null ;
        try {
            JwtTokenUtil util = new JwtTokenUtil();
            userId = util.getUserIdFromToken(token);
            if((userId!=null && !userId.equals(""))||userId.equals("tempToken"))
            {
                request.getSession().setAttribute("userId",userId );
            }
        }catch (Exception eee)
        {

        }

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(NeedToken.class)) {
            NeedToken needToken = method.getAnnotation(NeedToken.class);
            if (needToken.required()) {
                //System.out.println("进来了");
                if(token==null||token.equals(""))
                {
                    response.reset();
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json;charset=UTF-8");
                    PrintWriter pw = response.getWriter();
                    pw.write("{\"code\":-1,\"msg\":\"Unauthorized Activity\"}");
                    pw.flush();
                    pw.close();
                    return false;

                }
                boolean b1=true;
                //String userId=null ;
                try {
                    JwtTokenUtil util = new JwtTokenUtil();
                     userId = util.getUserIdFromToken(token);
                    boolean isExpired = util.isTokenExpired(token);
                    if(userId==null||userId.equals("")||userId.equals("tempToken"))
                    {
                        b1=false;
                    }
                    if(isExpired)
                    {
                        b1=false;
                    }
                }catch (Exception eee)
                {
                    b1=false;
                }
                if(!b1)
                {
                    response.reset();
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json;charset=UTF-8");
                    PrintWriter pw = response.getWriter();
                    pw.write("{\"code\":-1,\"msg\":\"Unauthorized Activity\"}");
                    pw.flush();
                    pw.close();
                    return false;
                }
                else
                {
                    if(userId!=null) {
                        request.getSession().setAttribute("userId",userId );
                    }
                }
                //request.getSession().setAttribute("userId",);
                return true;
            }
        }
        return true;
    }


    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }


    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }

}