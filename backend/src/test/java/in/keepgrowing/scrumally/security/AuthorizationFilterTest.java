package in.keepgrowing.scrumally.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationFilterTest {

    private static final String HEADER_NAME = "Authorisation";
    private static final String HEADER_PREFIX = "Bearer ";
    private static final String HEADER_SECRET = "JwtSecretKey";

    @Mock
    private TokenProperties tokenProperties;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Claims claims;

    @Mock
    private JwtParser jwtParser;

    private AuthorizationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new AuthorizationFilter(tokenProperties, jwtParser);
        when(tokenProperties.getHeader())
                .thenReturn(HEADER_NAME);
    }

    @Test
    void shouldSkipGettingClaimsWhenHeaderIsNull() throws ServletException, IOException {
        when(request.getHeader(HEADER_NAME))
                .thenReturn(null);
        filter.doFilterInternal(request, response, filterChain);
        verify(claims, times(0)).getSubject();
    }

    @Test
    void shouldSkipGettingClaimsWhenInvalidHeader() throws ServletException, IOException {
        when(request.getHeader(HEADER_NAME))
                .thenReturn("faulty");
        when(tokenProperties.getPrefix())
                .thenReturn(HEADER_PREFIX);
        filter.doFilterInternal(request, response, filterChain);
        verify(claims, times(0)).getSubject();
    }

    @Test
    void shouldLogExpiredJwtException() throws ServletException, IOException {

        when(tokenProperties.getPrefix())
                .thenReturn(HEADER_PREFIX);
        when(tokenProperties.getSecret())
                .thenReturn(HEADER_SECRET);
        var jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiYXV0aG9yaXRpZXMiOlsiSm9obiB" +
                "Eb2UiXSwiaWF0IjoxNTE2MjM5MDIyfQ.S5ubC-fTaCQ3Pi09CyIaiSgW2bZXXeZ9tKpTg6Y6WP8";
        when(request.getHeader(HEADER_NAME))
                .thenReturn(HEADER_PREFIX + jwtToken);
        when(jwtParser.setSigningKey(HEADER_SECRET.getBytes(Charset.defaultCharset())))
                .thenReturn(jwtParser);

        when(jwtParser.parseClaimsJws(jwtToken))
                .thenThrow(ExpiredJwtException.class);

        filter.doFilterInternal(request, response, filterChain);

        verify(jwtParser).setSigningKey(HEADER_SECRET.getBytes(Charset.defaultCharset()));
    }
}