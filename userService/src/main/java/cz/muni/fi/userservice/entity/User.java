package cz.muni.fi.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.Date;

@Entity
//In Derby, 'USER' is reserved keyword, we need to rename table
@Table(name="Users")
@Data
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(nullable=false,unique=true)
    @Email(regexp=".+@.+\\....?")
	@NotNull
	private String email;

	@NotNull
	private String givenName;

	@NotNull
	private String surname;

	@Pattern(regexp="\\+?\\d+")
	private String phone;

	@NotNull
	private String address;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date joinedDate;
}
