package filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("*.do")
public class CharacterEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // 由於不管是 get 還是 post 請求，只要符合以 .do 結尾的請求，都會被攔截下來，於過濾器中預處理，設置編碼。
        ((HttpServletRequest) servletRequest).setCharacterEncoding("UTF-8");
        ((HttpServletResponse) servletResponse).setCharacterEncoding("UTF-8");
        ((HttpServletResponse) servletResponse).setContentType("text/html;charset=utf-8");

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
