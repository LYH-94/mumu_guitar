package filters;

import filters.exception.OpenSessionInViewFilterException;
import util.TransationManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


@WebFilter("*.do")
public class OpenSessionInViewFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            //開啟事務
            TransationManager.beginTrans();
            System.out.println("事務已開啟!");

            //放行
            System.out.println("請求已放行!");
            filterChain.doFilter(servletRequest, servletResponse);

            //提交
            TransationManager.commit();
            System.out.println("事務已提交!");

            //關閉事務(連線)
            TransationManager.close();
            System.out.println("事務已關閉!");
        } catch (Exception e) {
            TransationManager.rollback();
            e.printStackTrace();
            throw new OpenSessionInViewFilterException("出現異常，事務已回滾!");
        }
    }

    @Override
    public void destroy() {

    }
}
