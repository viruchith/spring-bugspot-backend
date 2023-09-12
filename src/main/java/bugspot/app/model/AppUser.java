package bugspot.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Column(unique = true)
	@Size(min = 5, max = 10)
	@Pattern(regexp = "^^[a-z][a-z0-9]{4,9}$", message = "Username can contain only alphabets and numbers only !")
	private String username;

	@NotBlank
	@Email
	@Column(unique = true)
	private String email;

	@NotBlank
	@Size(min = 8)
	private String password;

	@NotBlank(message = "First Name must not be empty !")
	private String firstName;

	@NotBlank(message = "Last Name must not be empty !")
	private String lastName;

	@NotBlank
	@Pattern(regexp = "^\\d{10}$", message = "Mobile Number can contain only 10 digits !")
	private String mobile;

	@ManyToMany(mappedBy = "members", cascade = CascadeType.DETACH)
	private Set<Project> projects;
	
    @OneToMany(mappedBy = "creatorAppUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Issue> issues = new ArrayList<>();

    @OneToMany(mappedBy = "creatorAppUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	private void onCreate() {
		createdAt = LocalDateTime.now();
	}

	@PreUpdate
	private void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		AppUser other = (AppUser) obj;

		return id != null && id.equals(other.getId());
	}

}
