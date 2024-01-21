package com.qa.gorest.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//POJO Class
@JsonInclude(JsonInclude.Include.NON_NULL) //With this annotation we can ignore the null values. While using Builder pattern, it doesn't know which constructor to call, so it can send null values for some variables. hence, we can use this annotation.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("name")//Generally, we need to use the same variable names as present in the JSON body, but incase if you want to use some other variable name then we can use annotation from Jackson library like @JsonProperty("exact attribute name in Json") and then we can use any other variable name.
	private String name; 

	@JsonProperty("email") //But better to use the same variable names as otherwise later it can create unnecessary confusion.
	private String email;

	@JsonProperty("gender")
	private String gender;

	@JsonProperty("status")
	private String status;

	public User(String name, String email, String gender, String status) { //This is a constructor without "id", we created this constructor because in Request body, we don't need "id". It is seen in the response.
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.status = status;
	}

}
