package med.vall.api.domain.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import med.vall.api.domain.usuario.Usuario;
import med.vall.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        var tokenJWT = recuperarToken( request);
        System.out.println("tokenJWT = " + tokenJWT);
        if (tokenJWT != null){

            System.out.println("entrou no if");
            var subject = tokenService.getSubject(tokenJWT);
            System.out.println("subject = "+ subject);
            var usuario = repository.findByLogin(subject);
            System.out.println("usuario = "+ usuario);

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request,response);
    }

    private String recuperarToken(HttpServletRequest request) {
        System.out.println("request.getHeader(Authorization) = "+request.getHeader("Authorization"));
        var authorizationHeader = request.getHeader("Authorization");
        System.out.println("authorizationHeader = " + authorizationHeader);
        if (authorizationHeader != null) {
            System.out.println("recuperar touken if");
            return authorizationHeader.replace("Bearer", "").trim();
        }

        return null;
    }
}
