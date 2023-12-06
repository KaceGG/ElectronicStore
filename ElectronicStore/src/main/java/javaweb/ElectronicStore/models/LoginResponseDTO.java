package javaweb.ElectronicStore.models;

public class LoginResponseDTO {
    private UserDTO user; // Sử dụng UserDTO thay vì User trực tiếp

    public LoginResponseDTO() {
        super();
    }

    public LoginResponseDTO(User user) {
        this.user = convertToUserDTO(user); // Chuyển đổi User thành UserDTO
    }

    public UserDTO getUser() {
        return this.user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    private UserDTO convertToUserDTO(User user) {
        // Code để chuyển đổi từ User sang UserDTO
        // Ví dụ: 
        UserDTO userDTO = new UserDTO();
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        // ...
        return userDTO;
    }
}
