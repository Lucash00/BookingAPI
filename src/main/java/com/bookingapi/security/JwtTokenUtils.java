package com.bookingapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenUtils {

	// Inyectar la clave secreta desde application.properties
	@Value("${jwt.secret-key}")
	private String secretKey;

	// Definimos la clave para el JWT en base a la propiedad inyectada
	private Key key;

	// Inicializar la clave en un método postconstruct, luego de la inyección de las
	// propiedades
	@PostConstruct
	public void init() {
		if (secretKey == null || secretKey.isEmpty()) {
			throw new IllegalStateException("La clave secreta JWT no está configurada correctamente.");
		}
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	// Genera un token JWT para el usuario con roles
	public String generateToken(String username, List<String> roles) {
		return Jwts.builder().setSubject(username).claim("roles", roles) // Agrega los roles como claim
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Expira en 1
																										// hora
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	// Extrae el nombre de usuario del token
	public String extractUsername(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	// Extrae la fecha de expiración del token
	private Date extractExpiration(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
	}

	// Extrae los roles del token
	public List<String> extractRoles(String token) {
		Claims claims = extractAllClaims(token);
		Object rolesObj = claims.get("roles");

		// Usamos Gson para hacer el cast seguro
		if (rolesObj != null) {
			Gson gson = new Gson();
			Type listType = new TypeToken<List<String>>() {
			}.getType();
			return gson.fromJson(gson.toJson(rolesObj), listType);
		}
		return List.of(); // Devuelve una lista vacía si no se encuentra "roles"
	}

	// Método auxiliar para obtener todos los claims
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	// Verifica si el token ha expirado
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// Valida el token comparando el nombre de usuario y verificando si ha expirado
	public boolean validateToken(String token, String username) {
		return (username.equals(extractUsername(token)) && !isTokenExpired(token));
	}
}
