package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Base64;
import java.util.List;

import static nextstep.app.ui.LoginController.SPRING_SECURITY_CONTEXT_KEY;

@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list(HttpServletRequest request, HttpSession session) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            throw new AuthenticationException();
        }

        String encodedCredentials = authHeader.substring("Basic ".length());
        String decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials));

        String[] credentials = decodedCredentials.split(":", 2);
        String username = credentials[0];
        Member member = memberRepository.findByEmail(username).orElse(null);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, member);
        return ResponseEntity.ok(memberRepository.findAll());
    }
}
