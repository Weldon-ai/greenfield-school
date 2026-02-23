
package com.greenfield.sms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "school_settings")
public class SchoolSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
@Lob
@Column(columnDefinition = "LONGBLOB")
private byte[] logoFile;

    // ===== School Identity =====
    @NotBlank(message = "School name must not be blank")
    private String name;
    private String motto;
    private String address;
    private String city;
    private String county;
    private String country;

    // Contact
    private String contactEmail;
    private String contactPhone;

    // ===== Logo =====
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    
    private String logoFileName;
    private String logoFileType;

    // ===== Academic Settings =====
    private String academicYear;
    private String currentTerm;
    private boolean registrationOpen = false;
    private boolean autoPromotionEnabled = false;

    // ===== Portal Toggles =====
    private boolean enableStudentPortal = false;
    private boolean enableParentPortal = false;
    private boolean enableTeacherPortal = false;

    // ===== System Settings =====
    private boolean maintenanceMode = false;
    private String maintenanceMessage;
    private String theme;
    private boolean darkMode = false;

    // ===== Notifications =====
    private boolean emailEnabled = false;
    private boolean smsEnabled = false;

    // ===== Audit =====
    private String lastModifiedBy;
    private LocalDateTime lastModifiedAt;

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMotto() { return motto; }
    public void setMotto(String motto) { this.motto = motto; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCounty() { return county; }
    public void setCounty(String county) { this.county = county; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public byte[] getLogoFile() { return logoFile; }
    public void setLogoFile(byte[] logoFile) { this.logoFile = logoFile; }

    public String getLogoFileName() { return logoFileName; }
    public void setLogoFileName(String logoFileName) { this.logoFileName = logoFileName; }

    public String getLogoFileType() { return logoFileType; }
    public void setLogoFileType(String logoFileType) { this.logoFileType = logoFileType; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public String getCurrentTerm() { return currentTerm; }
    public void setCurrentTerm(String currentTerm) { this.currentTerm = currentTerm; }

    public boolean isRegistrationOpen() { return registrationOpen; }
    public void setRegistrationOpen(boolean registrationOpen) { this.registrationOpen = registrationOpen; }

    public boolean isAutoPromotionEnabled() { return autoPromotionEnabled; }
    public void setAutoPromotionEnabled(boolean autoPromotionEnabled) { this.autoPromotionEnabled = autoPromotionEnabled; }

    public boolean isEnableStudentPortal() { return enableStudentPortal; }
    public void setEnableStudentPortal(boolean enableStudentPortal) { this.enableStudentPortal = enableStudentPortal; }

    public boolean isEnableParentPortal() { return enableParentPortal; }
    public void setEnableParentPortal(boolean enableParentPortal) { this.enableParentPortal = enableParentPortal; }

    public boolean isEnableTeacherPortal() { return enableTeacherPortal; }
    public void setEnableTeacherPortal(boolean enableTeacherPortal) { this.enableTeacherPortal = enableTeacherPortal; }

    public boolean isMaintenanceMode() { return maintenanceMode; }
    public void setMaintenanceMode(boolean maintenanceMode) { this.maintenanceMode = maintenanceMode; }

    public String getMaintenanceMessage() { return maintenanceMessage; }
    public void setMaintenanceMessage(String maintenanceMessage) { this.maintenanceMessage = maintenanceMessage; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public boolean isDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }

    public boolean isEmailEnabled() { return emailEnabled; }
    public void setEmailEnabled(boolean emailEnabled) { this.emailEnabled = emailEnabled; }

    public boolean isSmsEnabled() { return smsEnabled; }
    public void setSmsEnabled(boolean smsEnabled) { this.smsEnabled = smsEnabled; }

    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }

    public LocalDateTime getLastModifiedAt() { return lastModifiedAt; }
    public void setLastModifiedAt(LocalDateTime lastModifiedAt) { this.lastModifiedAt = lastModifiedAt; }
}
