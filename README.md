# Hotel API

API feita em Spring Boot para o desafio técnico — gerenciamento de clientes, quartos e reservas de um hotel.

## Stack

Java 21, Spring Boot 3.5.5, Spring Data JPA, MySQL, Lombok e Swagger (springdoc) pra documentação.

## Rodando o projeto

Precisa ter o MySQL rodando localmente. Antes de subir a aplicação, cria o banco:

```sql
CREATE DATABASE hotel_db;
```

Ou deixa o próprio Hibernate criar, já que a URL no `application.properties` está com `createDatabaseIfNotExist=true`. Só ajustar usuário e senha lá:

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/hotel_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=sua_senha
```

As tabelas são criadas automaticamente (ddl-auto=update), não precisa rodar script nenhum manualmente pra estrutura.

Depois é só:
mvn spring-boot:run


Sobe em `localhost:8080`.

## Documentação / testando os endpoints

Com a aplicação rodando, dá pra ver e testar tudo pelo Swagger:
http://localhost:8080/swagger-ui/index.html


Também deixei uma collection do Postman (`postman_collection.json`) na raiz, com exemplos reais de cada requisição — já com payload preenchido.

## O que tem implementado

- CRUD completo de clientes (customers) — e o endereço vai junto no mesmo payload, já que tem relação 1:1 com address
- CRUD completo de quartos (rooms)
- Abrir e encerrar reserva
- Buscar reservas por intervalo de datas
- Ver quais quartos estão ocupados no momento

Os endpoints estão organizados em `/api/customers`, `/api/rooms` e `/api/reservations` — dá pra ver todos com detalhe no Swagger mesmo, preferi não duplicar isso aqui no README.

## Estrutura

Separei em controller, service, repository, model e dto, dentro de `com.yasmim.hotel`. Nada muito fora do padrão Spring.

## Queries SQL

As 12 queries pedidas (total de clientes, quartos ocupados, receita média etc.) estão no `queries.sql`, na raiz.

## Sobre a branch feature/improvement

Enquanto testava, percebi um problema real no fluxo de reservas: o sistema só bloqueava uma reserva nova se o quarto estivesse `IN_USE` — mas duas reservas com status `OPEN` conseguiam ter datas sobrepostas sem nenhum aviso, o que geraria overbooking na prática (dois clientes reservando o mesmo quarto pro mesmo período, e o conflito só apareceria no dia do check-in).

Na branch `feature/improvement` adicionei uma validação de sobreposição de data ao abrir a reserva, considerando qualquer reserva ativa (OPEN ou IN_USE) do quarto, não só o status atual. Achei que fazia mais sentido isolar isso numa branch separada, como pedido, já que é uma mudança de comportamento e não só uma correção pontual.