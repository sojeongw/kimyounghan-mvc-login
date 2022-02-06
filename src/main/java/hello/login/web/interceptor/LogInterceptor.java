package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             // 어떤 컨트롤러가 호출되는지도 확인할 수 있다.
                             Object handler) throws Exception {

        // 처음부터 HttpServletRequest로 들어오므로 캐스팅이 필요없어 편하다.
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        /*
        afterCompletion()로 uuid 값을 전달하고 싶은데 방법이 없다.
        지역 변수로 뽑으면 싱글톤이기 때문에 위험하다.
        setAttribute()를 쓰면 해결된다.
        */
        request.setAttribute(LOG_ID, uuid);

        /*
        @RequestMapping을 사용하면 HandlerMethod에 핸들러 정보가 담겨온다.
        정적 리소스를 사용하면 ResourceHttpRequestHandler가 사용된다.
        */
        if (handler instanceof HandlerMethod) {
            // 호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
            HandlerMethod hm = (HandlerMethod) handler;
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);

        // false면 여기서 끝나고 true면 다음 컨트롤러가 호출된다.
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {

        // HTTP Request는 갔다가 돌아올 때까지 같은 요청인 게 보장되므로 uuid도 그대로 받을 수 있다.
        String logId = (String) request.getAttribute(LOG_ID);
        String requestURI = request.getRequestURI();

        log.info("RESPONSE [{}][{}]", logId, requestURI);

        if (ex != null) {
            log.error("afterCompletion error!!", ex);
        }
    }
}
