package javaweb.ElectronicStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import javaweb.ElectronicStore.models.Role;

public interface RoleRepository extends JpaRepository<Role,Long>{
	Role findByName(String name);
}
