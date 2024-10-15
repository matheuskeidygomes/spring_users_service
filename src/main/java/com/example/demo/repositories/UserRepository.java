package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.User;
import com.example.demo.enums.UserRole;

public interface UserRepository extends JpaRepository<User, Long> {   // JpaRepository é uma interface do Spring Data JPA que possui métodos prontos para manipulação de dados no banco de dados
    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")   // Usando a anotação @Query para criar uma consulta personalizada
    Optional<User> findByEmail(@Param("email") String email);   // Inserindo um método personalizado para buscar um usuário pelo email (Optional é usado pois o usuário pode não existir e retornar null)

    @Query("SELECT u.role FROM User u WHERE u.email = :email")   // Usando a anotação @Query para criar uma consulta personalizada
    Optional<UserRole> findRoleByEmail(@Param("email") String email);
}