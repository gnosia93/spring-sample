package com.sbk.ssample.ui.user.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.sbk.ssample.ui.user.annotation.PasswordMatches;
import com.sbk.ssample.ui.user.annotation.ValidEmail;

import lombok.Data;

@Data
@PasswordMatches
public class AddUserRequest {
    @NotNull
    @NotEmpty
    private String firstName;
     
    @NotNull
    @NotEmpty
    private String lastName;
     
    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;
     
    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;
}