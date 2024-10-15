package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.User;
import com.example.demo.enums.UserRole;
import com.example.demo.repositories.UserRepository;
import com.example.demo.rest.exceptions.BadRequestException;
import com.example.demo.rest.exceptions.NotFoundUserException;
import com.example.demo.rest.exceptions.UniqueUserEmailException;

import lombok.RequiredArgsConstructor;

@Service // Indica que essa classe é um serviço
@RequiredArgsConstructor // Gera um construtor com todos os atributos final (para injeção de dependência)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional // Indica que o método será executado dentro de uma transação com o banco de dados (rollback em caso de erro)
    public User save(User user) {
        User existing = findByEmail(user.getEmail());
        if (existing != null)
            throw new UniqueUserEmailException("Email já cadastrado");

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Codifica a senha do usuário
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar usuário");
        }
    }

    @Transactional(readOnly = true) // Indica que o método será apenas de leitura no banco de dados (não fazendo alterações)
    public List<User> findAll() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuários");
        }
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        try {
            return userRepository.findById(id).orElseThrow(() -> new NotFoundUserException("Usuário não encontrado"));
        } catch (NotFoundUserException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuário");
        }
    }

    @Transactional(readOnly = true)
    public UserRole findRoleByEmail(String email) {
        try {
            return userRepository.findRoleByEmail(email).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar papel do usuário por email");
        }
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        try {
            return userRepository.findByEmail(email).orElse(null);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Erro ao buscar usuário por email");
        }
    }

    @Transactional
    public User update(Long id, User user) {
        User userToUpdate = findById(id);

        if (user.getEmail() != null) {
            User existing = findByEmail(user.getEmail());
            if (existing != null && !existing.getId().equals(id))
                throw new UniqueUserEmailException("Email já cadastrado");
        }
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Codifica a senha do usuário
        }

        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull()); // Configuração para que o ModelMapper só copie os valores que não são nulos
            modelMapper.map(user, userToUpdate); // Copia os valores de user para userToUpdate, alterando o objeto original no banco de dados

            return userToUpdate;
        } catch (UniqueUserEmailException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar usuário");
        }
    }

    @Transactional
    public User activate(Long id) {
        User user = findById(id);
        if (user.getDeletedAt() == null)
            throw new BadRequestException("Usuário já ativado");
        try {
            user.setDeletedAt(null); // Seta o campo deletedAt como nulo, ativando o usuário, no objeto original no banco de dados
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ativar usuário");
        }
    }

    @Transactional
    public User deactivate(Long id) {
        User user = findById(id);
        if (user.getDeletedAt() != null)
            throw new BadRequestException("Usuário já desativado");
        try {
            user.setDeletedAt(LocalDateTime.now()); // Seta o campo deletedAt com a data e hora atual, desativando o usuário, no objeto original no banco de dados
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao desativar usuário");
        }
    }
}
