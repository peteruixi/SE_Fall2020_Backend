package com.healthMonitor.fall2020.filter;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/*
Written by: Ruixi Li
Tested by: Ruixi Li
Debugged by: Ruixi Li
 */

@Order(1)
@WebFilter(urlPatterns = "/*",filterName = "MyFilter")
public class MyFilter implements Filter {


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        Date d1=new Date();


/*
        if(strUrl.equals("/reg")||strUrl.equals("/reg/")
                || strUrl.equals("/reg/add")||strUrl.equals("/reg/add/")
                || strUrl.equals("/BussinessReg")||strUrl.equals("/BussinessReg/")
                || strUrl.equals("/BussinessReg/add")||strUrl.equals("/BussinessReg/add/")
        )

 */
/*
        {
            String param = "";
            String paramValue = "";
            java.util.Enumeration params = request.getParameterNames();
            while (params.hasMoreElements()) {
                param = (String) params.nextElement();
                String[] values = request.getParameterValues(param);// 获得每个参数的value
                for (int i = 0; i < values.length; i++) {
                    paramValue = values[i];
                    if(paramValue!=null)
                    {
                        paramValue = paramValue.replaceAll("<", "");
                        paramValue = paramValue.replaceAll(">", "");
                        paramValue = paramValue.replaceAll("\"", "");
                        paramValue = paramValue.replaceAll("'", "");
                        paramValue = paramValue.replaceAll("script", "");

                        Pattern pattern=Pattern.compile("(eval\\((.*)\\)|script)",Pattern.CASE_INSENSITIVE);
                        Matcher matcher=pattern.matcher(paramValue);
                        paramValue = matcher.replaceAll("");


                        // 这里还可以增加，如领导人 自动转义成****,可以从数据库中读取非法关键字。
                        values[i] = paramValue;

                       // request.setAttribute(param, paramValue+"aaa");
                    }
                }
                //request.setAttribute(param, paramValue);

            }
        }
*/


        filterChain.doFilter(new MyServletRequest((HttpServletRequest) request),response);
        Date d2=new Date();
        long time=d2.getTime()- d1.getTime();
        String strUrl=((HttpServletRequest)request).getRequestURI();
        if(time>1000)
        {

           // System.out.println("写入慢接口日志服务:"+strUrl);
        }
        System.out.println("time："+time);
    }
}
