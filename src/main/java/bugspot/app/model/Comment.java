package bugspot.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	   @JoinColumn(name="user_id")
	   private AppUser creatorAppUser;
	
	 @ManyToOne(fetch=FetchType.LAZY)
	   @JoinColumn(name="issue_id")
	   private Issue issue;
	 
	 @NotBlank
	 private String note;
	 
	 private String code;
	 
	 
}
