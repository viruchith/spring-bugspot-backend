package bugspot.app.auth;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import bugspot.app.model.AppUser;
import bugspot.app.repository.AppUserRepository;

@Component
public class JWTProvider {

	private final long seconds = 86400; // 1 day

	@Value("${jwt.secret}")
	private String JWT_SECRET;

	private final Instant jwtExpireAtInstant = Instant.now().plusSeconds(seconds);

	private Map<String, Claim> claims;
	
	@Autowired
	private AppUserRepository appUserRepository;

	public String generateToken(Authentication authentication) {
		String username = authentication.getPrincipal()+"";
		AppUser appUser = appUserRepository.findFirstByUsername(username).orElseThrow(()->new UsernameNotFoundException("User with username : "+username+", does not exist !"));
		String token = JWT.create().withClaim("username", appUser.getUsername())
				.withClaim("id", appUser.getId()).withExpiresAt(jwtExpireAtInstant).withIssuedAt(Instant.now())
				.withIssuer("auth0").sign(getAlgorithm());
		return token;
	}

	public void decodeToken(String token) {
		try {
			JWTVerifier verifier = JWT.require(getAlgorithm()).withIssuer("auth0").build();
			DecodedJWT decodedJWT = verifier.verify(token);
			claims = decodedJWT.getClaims();
		} catch (JWTVerificationException e) {
			throw new BadCredentialsException("Invalid Auth Token !");
		}
		
	}

	public String getUsernameFromToken() {
		Claim usernameClaim = claims.get("username");
		return usernameClaim.asString();
	}

	public Long getUserId() {
		return claims.get("id").asLong();
	}

	private Algorithm getAlgorithm() {
		return Algorithm.HMAC512(JWT_SECRET);
	}
}
