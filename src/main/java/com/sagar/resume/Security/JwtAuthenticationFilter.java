package com.sagar.resume.Security;

import com.sagar.resume.Document.User;
import com.sagar.resume.Repository.UserRepository;
import com.sagar.resume.Service.AuthService;
import com.sagar.resume.Util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public JwtAuthenticationFilter(JwtUtil jwtUtil,UserRepository userRepository)
    {
        this.jwtUtil=jwtUtil;
        this.userRepository=userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String authHeader =request.getHeader("Authorization");
        String token = null;
        String userId;

        if(authHeader !=null && authHeader.startsWith("Bearer"))
        {
            token =authHeader.substring(7);

            try
            {
                userId =jwtUtil.getUserIdFromToken(token);

                if(userId !=null && SecurityContextHolder.getContext().getAuthentication()==null)
                {
                    try{
                        if(jwtUtil.validateToken(token) && !jwtUtil.isTokenExpired(token))
                        {
                            User user=userRepository.findById(userId).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
                            UsernamePasswordAuthenticationToken authtoken =new UsernamePasswordAuthenticationToken(user,null, new ArrayList<>());
                            authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authtoken);
                        }

                    }
                    catch (Exception e)
                    {
                        log.error("Exception occured while validating the token");

                    }

                }
            }
            catch (Exception e)
            {
                log.error("Token is not valid/available");

            }

        }
        filterChain.doFilter(request,response);

    }
}
