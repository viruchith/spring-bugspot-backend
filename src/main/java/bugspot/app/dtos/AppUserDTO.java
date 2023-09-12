package bugspot.app.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public interface AppUserDTO {
	Long getId();
	
	String getUsername();
	
	
	String getEmail();
	
	String getFirstName();
	
	String getLastName();
	
}
