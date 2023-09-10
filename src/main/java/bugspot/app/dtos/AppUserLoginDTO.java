package bugspot.app.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AppUserLoginDTO {
	@NotBlank
	@Size(min = 5,max = 10)
	@Pattern(regexp = "^^[a-z][a-z0-9]{4,9}$", message = "Username can contain only alphabets and numbers only !")
	private String username;
	
	@NotBlank
	@Size(min = 8,max = 25)
	private String password;
}
