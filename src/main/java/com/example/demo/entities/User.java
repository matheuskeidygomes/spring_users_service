package com.example.demo.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.demo.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter                         // Cria os métodos setters em tempo de compilação
@Getter                         // Cria os métodos getters em tempo de compilação
@Entity                         // Indica que a classe é uma entidade
@Table(name = "users")          // Indica a tabela que a entidade representa
@NoArgsConstructor              // Cria um construtor sem argumentos
@AllArgsConstructor             // Cria um construtor com todos os argumentos
@EqualsAndHashCode(of = "id")   // Cria os métodos equals e hashCode em tempo de compilação (compara o id)
@ToString(exclude = "password") // Cria o método toString em tempo de compilação (exclui o atributo password)
public class User implements UserDetails, Serializable {
    @Id                                                             // Indica que o atributo é uma chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)             // Indica que o valor do atributo é gerado automaticamente pelo banco de dados (GenerationType.IDENTITY = auto increment)
    @Column(name = "id")                                            // Indica o nome da coluna no banco de dados
    private Long id;

    @Column(name = "email", unique = true, nullable = false)        // Indica o nome da coluna no banco de dados e que o valor é único e não pode ser nulo
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)                                    // Indica que o atributo é um enum e que o valor será salvo como string no banco de dados
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist                                         // Indica que o método será executado antes de persistir a entidade no banco de dados
    public void prePersist() {
        this.email = email.toLowerCase();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate                                          // Indica que o método será executado antes de atualizar a entidade no banco de dados
    public void preUpdate() {
        this.email = email.toLowerCase();
        this.updatedAt = LocalDateTime.now();
    }

//    @PreRemove                                          // Indica que o método será executado antes de remover a entidade no banco de dados
//    public void preRemove() {
//        this.deletedAt = LocalDateTime.now();
//    }

    @Override
    public String getUsername() {     // Método para retornar o nome de usuário (UserDetails)
        return email;
    }

    @Override   
    public Collection<? extends GrantedAuthority> getAuthorities() {      // Método para retornar as autoridades do usuário (UserDetails)
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}


