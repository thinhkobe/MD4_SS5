package ra.scurity.securrity.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtTokenfillter extends OncePerRequestFilter {
    @Autowired
    private  JwtProvider jwtProvider;
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token=getTokenFromRequest(request);
            if (token!=null && jwtProvider.validateToken(token)){
                String username=jwtProvider.parseToken(token);
                if (username != null){
                    UserDetails userDetails=userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

        }catch (Exception e){
            log.error("UnAuthentication",e.getMessage());
        }
        filterChain.doFilter(request,response);
    }
    public String getTokenFromRequest(HttpServletRequest request){
        String header=request.getHeader("Authorization");
        if (header !=null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return header;
    }
}
