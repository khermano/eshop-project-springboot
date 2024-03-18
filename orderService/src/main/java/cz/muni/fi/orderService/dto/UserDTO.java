package cz.muni.fi.orderService.dto;

import lombok.Data;
import java.util.Date;

@Data
public class UserDTO {
	private Long id;

	private String email;

	private String givenName;

	private String surname;

	private String phone;

	private String address;

	private Date joinedDate;
}
