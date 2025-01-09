package med.vall.api.domain.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var tokenJWT = recuperarToken(response);
        filterChain.doFilter(request,response);
    }

    private String recuperarToken(HttpServletResponse response) {

        var authorizationHeader = response.getHeader("Authorization");
        if (authorizationHeader == null){
            throw new RuntimeException("Token não enviado.");
        }else {
            return authorizationHeader.replace("Bearer","");
        }
    }
}
