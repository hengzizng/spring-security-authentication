package nextstep.app.interceptor;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.*;


@Component
public class FormLoginInterceptor implements HandlerInterceptor {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private final MemberRepository memberRepository;

    public FormLoginInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Member member = memberRepository.findByEmail(request.getParameter("username")).orElseThrow(AuthenticationException::new);
        if (!member.getPassword().equals(request.getParameter("password"))) {
            throw new AuthenticationException();
        }

        HttpSession session = request.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, member);

        return true;
    }
}