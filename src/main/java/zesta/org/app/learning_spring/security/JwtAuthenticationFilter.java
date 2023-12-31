package zesta.org.app.learning_spring.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import zesta.org.app.learning_spring.dao.UserDao;

import java.io.IOException;

@Component
@RequiredArgsConstructor// it creates a constructor using final fields we declare
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtService;
    private final UserDao userDao;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request,response); //why???
            return;
        }
        final String jwtToken = authHeader.split(" ")[1];
        userEmail = jwtService.extractUsername(jwtToken);
        System.out.println("UserEMAIL"  +  userEmail);
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDao.findUserByEmail(userEmail);
            System.out.println("UserDetails:" + userDetails);
            if(jwtService.validateToken(jwtToken,userDetails)){
                System.out.println("means it's valid!");
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                System.out.println("context set!" + SecurityContextHolder.getContext().toString());
            }
        }
    }
}

