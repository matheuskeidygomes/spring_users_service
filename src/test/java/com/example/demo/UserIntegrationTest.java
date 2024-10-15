package com.example.demo;

import com.example.demo.rest.common.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.demo.rest.dtos.UserRegisterDto;
import com.example.demo.rest.dtos.UserResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)                 // Indica que a aplicação será iniciada com um servidor web em uma porta aleatória 
@Sql(scripts = "/seed/create-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Executa o script create-users.sql antes de cada teste
@Sql(scripts = "/seed/clean-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)   // Executa o script clean-users.sql depois de cada teste
public class UserIntegrationTest {

  @Autowired // Injeta um WebTestClient configurado para a aplicação
  private WebTestClient webTestClient;

  @Test
  public void testGetUsers() {
    UserResponseDto[] responseBody = webTestClient.get().uri("/users")
      .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "keidy@gmail.com", "123456"))
      .exchange()
      .expectStatus().isOk()
      .expectBody(UserResponseDto[].class)
      .returnResult().getResponseBody();

      assert responseBody != null;
      assert responseBody.length == 3;
  }
  
   @Test
   public void testGetUserById() {
     UserResponseDto responseBody = webTestClient.get().uri("/users/1")
       .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "keidy@gmail.com", "123456"))
       .exchange()
       .expectStatus().isOk()
       .expectBody(UserResponseDto.class)
       .returnResult().getResponseBody();
     assert responseBody != null;
   }

   @Test
   public void testRegisterUser() {
     UserResponseDto responseBody = webTestClient.post().uri("/auth/register")                 // Faz uma requisição POST para a URI /users
       .contentType(MediaType.APPLICATION_JSON)                                                     // Indica que o conteúdo da requisição é um JSON
       .bodyValue(new UserRegisterDto("kubota@gmail.com", "123456", "USER"))    // Adiciona um objeto UserCreate no corpo da requisição
       .exchange()                                                                                  // Envia a requisição para o servidor
       .expectStatus().isOk()                                                                       // Espera que o status retornado seja 201 (CREATED)
       .expectBody(UserResponseDto.class)                                                           // Espera que o corpo da resposta seja vazio
       .returnResult().getResponseBody();                                                           // Retorna o corpo da resposta
    
     assert responseBody != null;                                                                   // Verifica se o corpo da resposta não é nulo
     assert responseBody.getId() != null;                                                           // Verifica se o id do usuário retornado não é nulo
     assert responseBody.getEmail().equals("kubota@gmail.com");                                     // Verifica se o email do usuário retornado é igual ao email enviado
     assert responseBody.getRole().equals("USER");                                                  // Verifica se o role do usuário retornado é igual ao role enviado
   }

   @Test
   public void testRegisterUserWithInvalidEmail() {
      UserRegisterDto userRegisterDto = new UserRegisterDto("kub", "123456", "USER");

      System.out.println("UserRegisterDto: " + userRegisterDto);

     ErrorMessage responseBody = webTestClient.post().uri("/auth/register")
       .contentType(MediaType.APPLICATION_JSON)
       .bodyValue(new UserRegisterDto("kub", "123456", "USER"))
       .exchange()
       .expectStatus().is4xxClientError()
       .expectBody(ErrorMessage.class)
       .returnResult().getResponseBody();

     assert responseBody != null;
     assert responseBody.getMessage().equals("Invalid arguments");
    }

   @Test
   public void testUpdateUser() {
       UserResponseDto response = webTestClient.put().uri("/users/1")
               .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "keidy@gmail.com", "123456"))
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(new UserRegisterDto("updated@gmail.com", null, null))
               .exchange()
               .expectStatus().isOk()
               .expectBody(UserResponseDto.class)
               .returnResult().getResponseBody();

       assert response != null;
       assert response.getEmail().equals("updated@gmail.com");
       assert response.getRole().equals("ADMIN");
   }

   @Test
   public void testDeactivateUser() {
     UserResponseDto responseBody = webTestClient.put().uri("/users/1/deactivate")
       .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "keidy@gmail.com", "123456"))
       .exchange()
       .expectStatus().isOk()
       .expectBody(UserResponseDto.class)
       .returnResult().getResponseBody();

     assert responseBody != null;
     assert responseBody.getDeletedAt() != null;
   }

   @Test
   public void testActivateUser() {
        UserResponseDto responseBody = webTestClient.put().uri("/users/3/activate")
        .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "keidy@gmail.com", "123456"))
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserResponseDto.class)
        .returnResult().getResponseBody();

        assert responseBody != null;
        assert responseBody.getDeletedAt() == null;
   }
}
