package org.example.hotappresi.models;


import jakarta.validation.constraints.NotBlank;

public class PasswordUpdateForm {

    @NotBlank(message = "Stare hasło jest wymagane")
    private String oldPassword;

    @NotBlank(message = "Nowe hasło jest wymagane")
    private String newPassword;

    @NotBlank(message = "Potwierdzenie hasła jest wymagane")
    private String confirmPassword;

    // Gettery i settery
    public String getOldPassword() {
        return oldPassword;
    }
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}