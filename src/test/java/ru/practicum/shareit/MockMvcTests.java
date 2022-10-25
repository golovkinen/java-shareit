package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MockMvcTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("POST /users Создаю пользователя имя null")
    void testCreateUserNullName() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email1@mail.com\",\"name\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(2)
    @DisplayName("POST /users Если email - null")
    void emailNullError() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":null,\"name\":\"Name1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    @DisplayName("POST /users Если email - поле не заполнено")
    void emailBlankError() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\",\"name\":\"Name1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    @DisplayName("POST /users Если email - введен неверно")
    void emailWrongError() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email.comyandex@\",\"name\":\"Name1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    @DisplayName("POST /users Создаю пользователя OK")
    void testCreateUserOk() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email1@mail.com\",\"name\":\"Name1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("Email1@mail.com")))
                .andExpect(jsonPath("$.name", is("Name1")));
    }

    @Test
    @Order(6)
    @DisplayName("POST /users Создаю пользователя повторяющийся email - ошибка")
    void testCreateUserSameEmailFail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email1@mail.com\",\"name\":\"Name2\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(7)
    @DisplayName("GET /users/5 - Not Found")
    void testGetUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/users/{id}", 5))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь с ИД 5 не найден"));
    }

    @Test
    @Order(8)
    @DisplayName("PATCH /users/5 - Not Found")
    void testUpdateUserNotFound() throws Exception {
        mockMvc.perform(patch("/users/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"UpdatedEmail1@mail.com\",\"name\":\"UpdatedName1\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь с ИД 5 не найден"));
    }

    @Test
    @Order(9)
    @DisplayName("PATCH /users/1 - Update email & name OK")
    void testUpdateUserSuccess() throws Exception {
        mockMvc.perform(patch("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"UpdatedEmail1@mail.com\",\"name\":\"UpdatedName1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("UpdatedEmail1@mail.com")))
                .andExpect(jsonPath("$.name", is("UpdatedName1")));
    }

    @Test
    @Order(10)
    @DisplayName("PATCH /users/1 - Update only name OK")
    void testUpdateUserNameSuccess() throws Exception {
        mockMvc.perform(patch("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedName2\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("UpdatedEmail1@mail.com")))
                .andExpect(jsonPath("$.name", is("UpdatedName2")));
    }

    @Test
    @Order(11)
    @DisplayName("PATCH /users/1 - Update only email OK")
    void testUpdateUserEmailSuccess() throws Exception {
        mockMvc.perform(patch("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"UpdatedEmail2@mail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("UpdatedEmail2@mail.com")))
                .andExpect(jsonPath("$.name", is("UpdatedName2")));
    }

    @Test
    @Order(12)
    @DisplayName("POST /users Создаю пользователя 2 - ID 3 - OK")
    void testCreateUser2Id3Ok() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email1@mail.com\",\"name\":\"Name1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.email", is("Email1@mail.com")))
                .andExpect(jsonPath("$.name", is("Name1")));
    }

    @Test
    @Order(13)
    @DisplayName("PATCH /users/1 - Update only email OK")
    void testUpdateUserEmailDuplicateFail() throws Exception {
        mockMvc.perform(patch("/users/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"UpdatedEmail2@mail.com\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(14)
    @DisplayName("DELETE /users/5 - Not Found")
    void testDeleteUserByIdNotFound() throws Exception {
        mockMvc.perform(delete("/users/{id}", 5))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(15)
    @DisplayName("GET /users/1 - OK")
    void testGetUserByIdOk() throws Exception {
        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("UpdatedEmail2@mail.com")))
                .andExpect(jsonPath("$.name", is("UpdatedName2")));
    }

    @Test
    @Order(16)
    @DisplayName("GET /users - OK")
    void testGetAllUsersOk() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].email", is("UpdatedEmail2@mail.com")))
                .andExpect(jsonPath("$[0].name", is("UpdatedName2")))
                .andExpect(jsonPath("$[1].id", is(3)))
                .andExpect(jsonPath("$[1].email", is("Email1@mail.com")))
                .andExpect(jsonPath("$[1].name", is("Name1")));
    }

    @Test
    @Order(17)
    @DisplayName("POST /users Создаю пользователя 3 - ID 4 - OK")
    void testCreateUser3Id4Ok() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email3@mail.com\",\"name\":\"Name3\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.email", is("Email3@mail.com")))
                .andExpect(jsonPath("$.name", is("Name3")));
    }

    @Test
    @Order(18)
    @DisplayName("POST /users Создаю пользователя 4 - ID 5 - OK")
    void testCreateUser4Id5Ok() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email4@mail.com\",\"name\":\"Name4\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.email", is("Email4@mail.com")))
                .andExpect(jsonPath("$.name", is("Name4")));
    }

    @Test
    @Order(19)
    @DisplayName("POST /users Создаю пользователя 5 - ID 6 - OK")
    void testCreateUser5Id6Ok() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email5@mail.com\",\"name\":\"Name5\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(6)))
                .andExpect(jsonPath("$.email", is("Email5@mail.com")))
                .andExpect(jsonPath("$.name", is("Name5")));
    }

    @Test
    @Order(20)
    @DisplayName("POST /users Создаю пользователя 6 - ID 7 - OK")
    void testCreateUser6Id7Ok() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email6@mail.com\",\"name\":\"Name6\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(7)))
                .andExpect(jsonPath("$.email", is("Email6@mail.com")))
                .andExpect(jsonPath("$.name", is("Name6")));
    }

    @Test
    @Order(21)
    @DisplayName("DELETE /users/7 - OK")
    void testDeleteUserById() throws Exception {
        mockMvc.perform(delete("/users/{id}", 7))
                .andExpect(status().isOk());
    }

    @Test
    @Order(22)
    @DisplayName("POST /items Создаю item user not found")
    void testCreateItemUserNotFound() throws Exception {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 50)
                        .content("{\"name\":\"Отвертка\", \"description\":\"Отвертка крестовая\", \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(22)
    @DisplayName("GET /items/all Получаю все вещи No Items")
    void testGetAllItemsNoItems() throws Exception {

        mockMvc.perform(get("/items/all")
                        .contentType(MediaType.APPLICATION_JSON))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(23)
    @DisplayName("POST /items Создаю item без названия - null")
    void testCreateItemNullName() throws Exception {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"name\":null, \"description\":\"Отвертка крестовая\", \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(24)
    @DisplayName("POST /items Создаю item без описания - null")
    void testCreateItemNullDescription() throws Exception {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"name\":\"Отвертка\", \"description\":null, \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Order(25)
    @DisplayName("POST /items Создаю item без названия")
    void testCreateItemEmptyName() throws Exception {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"name\":\"\", \"description\":\"Отвертка крестовая\", \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(26)
    @DisplayName("POST /items Создаю item без описания")
    void testCreateItemEmptyDescription() throws Exception {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"name\":\"Отвертка\", \"description\":\"\", \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(27)
    @DisplayName("POST /items Создаю item AvailableNull")
    void testCreateItemAvailableNull() throws Exception {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"name\":\"Отвертка\", \"description\":\"Отвертка крестовая\", \"available\":null}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(28)
    @DisplayName("POST /items Создаю без X-Sharer")
    void testCreateWithoutXSharer() throws Exception {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Отвертка\", \"description\":\"Отвертка крестовая\", \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(29)
    @DisplayName("POST /items Создаю item OK")
    void testCreateItemOk() throws Exception {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"name\":\"Отвертка\", \"description\":\"Отвертка крестовая\", \"available\":false}"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Отвертка")))
                .andExpect(jsonPath("$.description", is("Отвертка крестовая")))
                .andExpect(jsonPath("$.available", is(false)));
    }

    @Test
    @Order(30)
    @DisplayName("PATCH /items/{id} Update all values item OK")
    void testUpdateItemOk() throws Exception {

        mockMvc.perform(patch("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"id\":1, \"name\":\"Отвертка+\", \"description\":\"Отвертка с насадками\", \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Отвертка+")))
                .andExpect(jsonPath("$.description", is("Отвертка с насадками")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    @Order(31)
    @DisplayName("PATCH /items/{id} Update Fail - user not found")
    void testUpdateItemWrongUser() throws Exception {

        mockMvc.perform(patch("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 50)
                        .content("{\"id\":1, \"name\":\"Отвертка-\", \"description\":\"Отвертка без насадок\", \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь с ИД 50 не найден"));
    }

    @Test
    @Order(32)
    @DisplayName("PATCH /items/{id} Update Fail - user not owner")
    void testUpdateItemWrongOwner() throws Exception {

        mockMvc.perform(patch("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .content("{\"id\":1, \"name\":\"Отвертка-\", \"description\":\"Отвертка без насадок\", \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("Обновлять может только собственник вещи"));
    }

    @Test
    @Order(33)
    @DisplayName("PATCH /items/{id} Update Fail - item not found")
    void testUpdateItemNotFound() throws Exception {

        mockMvc.perform(patch("/items/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"id\":1, \"name\":\"Отвертка-\", \"description\":\"Отвертка без насадок\", \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("Вещь с ИД 3 не найдена"));
    }

    @Test
    @Order(34)
    @DisplayName("PATCH /items/{id} Update Fail -  без X-Sharer")
    void testUpdateWithoutXSharer() throws Exception {

        mockMvc.perform(patch("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Отвертка\", \"description\":\"Отвертка крестовая\", \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(35)
    @DisplayName("PATCH /items/{id} Update item name OK")
    void testUpdateItemNameOk() throws Exception {

        mockMvc.perform(patch("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"name\":\"Отвертка\"}"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Отвертка")))
                .andExpect(jsonPath("$.description", is("Отвертка с насадками")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    @Order(36)
    @DisplayName("PATCH /items/{id} Update item description OK")
    void testUpdateItemDescriptionOk() throws Exception {

        mockMvc.perform(patch("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"description\":\"Отвертка крестовая\"}"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Отвертка")))
                .andExpect(jsonPath("$.description", is("Отвертка крестовая")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    @Order(37)
    @DisplayName("PATCH /items/{id} Update item available OK")
    void testUpdateItemAvailableOk() throws Exception {

        mockMvc.perform(patch("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"available\":false}"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Отвертка")))
                .andExpect(jsonPath("$.description", is("Отвертка крестовая")))
                .andExpect(jsonPath("$.available", is(false)));
    }

    @Test
    @Order(38)
    @DisplayName("POST /items Создаю item2 для Юзера1 OK")
    void testCreateItem2Ok() throws Exception {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"name\":\"Дрель\", \"description\":\"Дрель с перфоратором\", \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Дрель")))
                .andExpect(jsonPath("$.description", is("Дрель с перфоратором")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    @Order(39)
    @DisplayName("POST /items Создаю item3 для Юзера1 OK")
    void testCreateItem3Ok() throws Exception {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"name\":\"Шуруповерт\", \"description\":\"Шуруповерт с аккумулятором\", \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Шуруповерт")))
                .andExpect(jsonPath("$.description", is("Шуруповерт с аккумулятором")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    @Order(40)
    @DisplayName("GET /items Получаю все вещи Юзера1 OK")
    void testGetAllItemsUser1Ok() throws Exception {

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Отвертка")))
                .andExpect(jsonPath("$[0].description", is("Отвертка крестовая")))
                .andExpect(jsonPath("$[0].available", is(false)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Дрель")))
                .andExpect(jsonPath("$[1].description", is("Дрель с перфоратором")))
                .andExpect(jsonPath("$[1].available", is(true)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].name", is("Шуруповерт")))
                .andExpect(jsonPath("$[2].description", is("Шуруповерт с аккумулятором")))
                .andExpect(jsonPath("$[2].available", is(true)));
    }

    @Test
    @Order(41)
    @DisplayName("GET /items Получаю item 3 Юзера1 OK")
    void testGetItem3Ok() throws Exception {

        mockMvc.perform(get("/items/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Шуруповерт")))
                .andExpect(jsonPath("$.description", is("Шуруповерт с аккумулятором")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    @Order(41)
    @DisplayName("GET /items Получаю item 2 Юзера1 OK")
    void testGetItem2Ok() throws Exception {

        mockMvc.perform(get("/items/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    @Order(42)
    @DisplayName("GET /items Получаю все вещи Юзера3 - вещей нет")
    void testGetAllItemsUser3NotFound() throws Exception {

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("У пользователя с ИД 3 нет вещей"));
    }

    @Test
    @Order(43)
    @DisplayName("GET /items Получаю вещь с ИД 2 - Юзер не собственник")
    void testGetItemId2WrongUser() throws Exception {

        mockMvc.perform(get("/items/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Шуруповерт")))
                .andExpect(jsonPath("$.description", is("Шуруповерт с аккумулятором")))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.lastBooking").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.nextBooking").value(IsNull.nullValue()));
    }

    @Test
    @Order(44)
    @DisplayName("POST /items Создаю item1 для Юзера3 OK")
    void testCreateItem1User2Ok() throws Exception {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .content("{\"name\":\"Дрель\", \"description\":\"Дрель без перфоратора\", \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.name", is("Дрель")))
                .andExpect(jsonPath("$.description", is("Дрель без перфоратора")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    @Order(45)
    @DisplayName("POST /items Создаю item2 для Юзера3 OK")
    void testCreateItem2User3Ok() throws Exception {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .content("{\"name\":\"Шуруповерт\", \"description\":\"Шуруповерт с насадками\", \"available\":true}"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.name", is("Шуруповерт")))
                .andExpect(jsonPath("$.description", is("Шуруповерт с насадками")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    @Order(46)
    @DisplayName("POST /bookings Создаю booking Ok")
    void testCreateBookingOk() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusSeconds(1).withNano(0);
        LocalDateTime end = start.plusSeconds(1).withNano(0);
        CreateBookingDto booking1 = new CreateBookingDto(2, start, end);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.start", is(String.valueOf(start))))
                .andExpect(jsonPath("$.end", is(String.valueOf(end))))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.WAITING))))
                .andExpect(jsonPath("$.booker.id", is(5)))
                .andExpect(jsonPath("$.item.id", is(2)))
                .andExpect(jsonPath("$.item.name", is("Дрель")));
    }

    @Test
    @Order(47)
    @DisplayName("PATCH /bookings/{id} Approve booking 1 OK")
    void testApproveBooking1Ok() throws Exception {

        mockMvc.perform(patch("/bookings/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.APPROVED))))
                .andExpect(jsonPath("$.booker.id", is(5)))
                .andExpect(jsonPath("$.item.id", is(2)))
                .andExpect(jsonPath("$.item.name", is("Дрель")));
    }

    @Test
    @Order(47)
    @DisplayName("GET /items/search Поиск ДрЕЛь OK")
    void testSearchItemOk() throws Exception {

        mockMvc.perform(get("/items/search").param("text", "ДрЕЛь")
                        .contentType(MediaType.APPLICATION_JSON))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("Дрель")))
                .andExpect(jsonPath("$[0].description", is("Дрель с перфоратором")))
                .andExpect(jsonPath("$[0].available", is(true)))
                .andExpect(jsonPath("$[1].id", is(4)))
                .andExpect(jsonPath("$[1].name", is("Дрель")))
                .andExpect(jsonPath("$[1].description", is("Дрель без перфоратора")))
                .andExpect(jsonPath("$[1].available", is(true)));
    }

    @Test
    @Order(48)
    @DisplayName("GET /items/search Поиск Not Found")
    void testSearchItemNotFound() throws Exception {

        mockMvc.perform(get("/items/search").param("text", "Яблоко")
                        .contentType(MediaType.APPLICATION_JSON))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @Order(49)
    @DisplayName("GET /items/search Поиск часть слова OK")
    void testSearchItemPartWordOk() throws Exception {

        mockMvc.perform(get("/items/search").param("text", "ПеРФораТор")
                        .contentType(MediaType.APPLICATION_JSON))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("Дрель")))
                .andExpect(jsonPath("$[0].description", is("Дрель с перфоратором")))
                .andExpect(jsonPath("$[0].available", is(true)))
                .andExpect(jsonPath("$[1].id", is(4)))
                .andExpect(jsonPath("$[1].name", is("Дрель")))
                .andExpect(jsonPath("$[1].description", is("Дрель без перфоратора")))
                .andExpect(jsonPath("$[1].available", is(true)));
    }

    @Test
    @Order(50)
    @DisplayName("GET /items/search Поиск выводит только available OK")
    void testSearchItemOnlyAvailableOk() throws Exception {

        mockMvc.perform(get("/items/search")
                        .param("text", "Шуруповерт")
                        .contentType(MediaType.APPLICATION_JSON))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(3)))
                .andExpect(jsonPath("$[1].id", is(5)))
                .andExpect(jsonPath("$[0].name", is("Шуруповерт")))
                .andExpect(jsonPath("$[0].description", is("Шуруповерт с аккумулятором")))
                .andExpect(jsonPath("$[0].available", is(true)));
    }


    @Test
    @Order(52)
    @DisplayName("DELETE /items/{id} Удаляю вещь с ИД 2 - Юзер не собственник")
    void testDeleteItemId2WrongUser() throws Exception {

        mockMvc.perform(delete("/items/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 4))

                // Validate the response code and content type
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Только собственник может удалить свою вещь"));
    }

    @Test
    @Order(53)
    @DisplayName("DELETE /items/{id} Удаляю вещь с ИД 2 - Юзер не найден")
    void testDeleteItemId2UserNotFound() throws Exception {

        mockMvc.perform(delete("/items/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 50))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь с ИД 50 не найден"));
    }

    @Test
    @Order(54)
    @DisplayName("DELETE /items/{id} Удаляю вещь с ИД 20 - Item not found")
    void testDeleteItemId20NotFound() throws Exception {

        mockMvc.perform(delete("/items/{id}", 20)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("Вещь с ИД 20 не найдена"));
    }

    @Test
    @Order(54)
    @DisplayName("GET /items/all Получаю все вещи OK")
    void testGetAllItemsOk() throws Exception {

        mockMvc.perform(get("/items/all")
                        .contentType(MediaType.APPLICATION_JSON))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Отвертка")))
                .andExpect(jsonPath("$[0].description", is("Отвертка крестовая")))
                .andExpect(jsonPath("$[0].available", is(false)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Дрель")))
                .andExpect(jsonPath("$[1].description", is("Дрель с перфоратором")))
                .andExpect(jsonPath("$[1].available", is(true)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].name", is("Шуруповерт")))
                .andExpect(jsonPath("$[2].description", is("Шуруповерт с аккумулятором")))
                .andExpect(jsonPath("$[2].available", is(true)))
                .andExpect(jsonPath("$[3].id", is(4)))
                .andExpect(jsonPath("$[4].id", is(5)));
    }

    @Test
    @Order(55)
    @DisplayName("GET /items Получаю item 2 Юзера1 OK")
    void testGetItem2Ok2() throws Exception {

        mockMvc.perform(get("/items/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    @Order(55)
    @DisplayName("POST /items Создаю item2 для Юзера3 avail false OK")
    void testCreateItem1User4Ok() throws Exception {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 4)
                        .content("{\"name\":\"Шуруповерт2\", \"description\":\"Шуруповерт2 с насадками\", \"available\":false}"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(6)))
                .andExpect(jsonPath("$.name", is("Шуруповерт2")))
                .andExpect(jsonPath("$.description", is("Шуруповерт2 с насадками")))
                .andExpect(jsonPath("$.available", is(false)));
    }

    @Test
    @Order(56)
    @DisplayName("POST /bookings Создаю booking Current item 3 from user 5 Ok")
    void testCreateBooking5CurrentOk() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusSeconds(1).withNano(0);
        LocalDateTime end = start.plusMinutes(2).withNano(0);
        CreateBookingDto booking1 = new CreateBookingDto(3, start, end);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.start", is(String.valueOf(start))))
                .andExpect(jsonPath("$.end", is(String.valueOf(end))))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.WAITING))))
                .andExpect(jsonPath("$.booker.id", is(5)))
                .andExpect(jsonPath("$.item.id", is(3)))
                .andExpect(jsonPath("$.item.name", is("Шуруповерт")));
    }

    @Test
    @Order(57)
    @DisplayName("DELETE /items/{id} Удаляю вещь с ИД 6 - Ok")
    void testDeleteItemId2Ok() throws Exception {

        mockMvc.perform(delete("/items/{id}", 6)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 4))

                // Validate the response code and content type
                .andExpect(status().isOk());
    }

    @Test
    @Order(58)
    @DisplayName("POST /items Создаю item2 для Юзера4 avail false OK")
    void testCreateItem2User4Ok() throws Exception {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 4)
                        .content("{\"name\":\"Шуруповерт2\", \"description\":\"Шуруповерт2 с насадками\", \"available\":false}"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(7)))
                .andExpect(jsonPath("$.name", is("Шуруповерт2")))
                .andExpect(jsonPath("$.description", is("Шуруповерт2 с насадками")))
                .andExpect(jsonPath("$.available", is(false)));
    }

    @Test
    @Order(59)
    @DisplayName("DELETE /items Удаляю все вещи юзера 40 - Not Found")
    void testDeleteAllItemsUser40FailOk() throws Exception {

        mockMvc.perform(delete("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 40))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(59)
    @DisplayName("DELETE /items Удаляю все вещи юзера 4 - Ok")
    void testDeleteAllItemsUser1Ok() throws Exception {

        mockMvc.perform(delete("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 4))

                // Validate the response code and content type
                .andExpect(status().isOk());
    }


    @Test
    @Order(60)
    @DisplayName("GET /items Получаю все вещи Юзера4 - вещей нет")
    void testGetAllItemsUser1NotFound() throws Exception {

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 4))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("У пользователя с ИД 4 нет вещей"));
    }


    @Test
    @Order(61)
    @DisplayName("GET /items/{id} Получаю вещь Юзера1 от Юзера2 - вещей нет")
    void testGetItemUser1NotFound() throws Exception {

        mockMvc.perform(get("/items/{id}", 7)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("Вещь с ИД 7 не найдена"));
    }

    @Test
    @Order(62)
    @DisplayName("GET /items Получаю все вещи Юзера50 - Not Found")
    void testGetAllItemsUser5NotFound() throws Exception {

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 50))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь с ИД 50 не найден"));
    }

    @Test
    @Order(63)
    @DisplayName("POST /bookings Создаю booking item not found")
    void testCreateBookingItemNotFound() throws Exception {

        CreateBookingDto booking1 = new CreateBookingDto(10, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("Item with ID 10 not found"));
    }

    @Test
    @Order(64)
    @DisplayName("POST /bookings Создаю booking user not found")
    void testCreateBookingUserNotFound() throws Exception {

        CreateBookingDto booking1 = new CreateBookingDto(1, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 50)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with ID 50 not found"));
    }

    @Test
    @Order(65)
    @DisplayName("POST /bookings Создаю booking StartAfterEnd")
    void testCreateBookingStartAfterEnd() throws Exception {

        CreateBookingDto booking1 = new CreateBookingDto(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusMinutes(10));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 50)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest())
                .andExpect(content().string("End Date is before Start Date"));
    }

    @Test
    @Order(66)
    @DisplayName("POST /bookings Создаю booking by owner")
    void testCreateBookingByOwner() throws Exception {

        CreateBookingDto booking1 = new CreateBookingDto(2, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("Хозяин не может бронировать свои вещи"));
    }

    @Test
    @Order(67)
    @DisplayName("POST /bookings Создаю booking not available")
    void testCreateBookingNotAvailable() throws Exception {

        CreateBookingDto booking1 = new CreateBookingDto(1, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Item with ID 1 not available"));
    }


    @Test
    @Order(68)
    @DisplayName("GET /bookings/{id} получаю booking Wrong Id")
    void testGetBookingWrongId() throws Exception {

        mockMvc.perform(get("/bookings/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(69)
    @DisplayName("GET /bookings/{id} получаю booking User Not found")
    void testGetBookingUserNotFound() throws Exception {

        mockMvc.perform(get("/bookings/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 9))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(70)
    @DisplayName("GET /bookings/{id} получаю booking Wrong User")
    void testGetBookingWrongUser() throws Exception {

        mockMvc.perform(get("/bookings/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(71)
    @DisplayName("GET /bookings/{id} получаю booking User Ok")
    void testGetBookingUserOk() throws Exception {

        mockMvc.perform(get("/bookings/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.APPROVED))))
                .andExpect(jsonPath("$.booker.id", is(5)))
                .andExpect(jsonPath("$.item.id", is(2)))
                .andExpect(jsonPath("$.item.name", is("Дрель")));
    }

    @Test
    @Order(72)
    @DisplayName("GET /bookings/{id} получаю booking Owner Ok")
    void testGetBookingOwnerOk() throws Exception {

        mockMvc.perform(get("/bookings/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.APPROVED))))
                .andExpect(jsonPath("$.booker.id", is(5)))
                .andExpect(jsonPath("$.item.id", is(2)))
                .andExpect(jsonPath("$.item.name", is("Дрель")));
    }

    @Test
    @Order(73)
    @DisplayName("POST /bookings Создаю booking item 2 from user 4 Ok")
    void testCreateBooking2Ok() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusSeconds(9).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusMinutes(2).withNano(0);
        CreateBookingDto booking1 = new CreateBookingDto(2, start, end);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 4)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.start", is(String.valueOf(start))))
                .andExpect(jsonPath("$.end", is(String.valueOf(end))))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.WAITING))))
                .andExpect(jsonPath("$.booker.id", is(4)))
                .andExpect(jsonPath("$.item.id", is(2)))
                .andExpect(jsonPath("$.item.name", is("Дрель")));
    }

    @Test
    @Order(74)
    @DisplayName("POST /bookings Создаю booking OwnerFail")
    void testCreateBookingOwnerFail() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusSeconds(10).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusMinutes(2).withNano(0);
        CreateBookingDto booking1 = new CreateBookingDto(5, start, end);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("Хозяин не может бронировать свои вещи"));
    }

    @Test
    @Order(75)
    @DisplayName("POST /bookings Создаю booking item 2 from user 3 Ok")
    void testCreateBooking3Ok() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusSeconds(11).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusMinutes(2).withNano(0);
        CreateBookingDto booking1 = new CreateBookingDto(2, start, end);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.start", is(String.valueOf(start))))
                .andExpect(jsonPath("$.end", is(String.valueOf(end))))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.WAITING))))
                .andExpect(jsonPath("$.booker.id", is(3)))
                .andExpect(jsonPath("$.item.id", is(2)))
                .andExpect(jsonPath("$.item.name", is("Дрель")));
    }

    @Test
    @Order(76)
    @DisplayName("POST /bookings Создаю booking FUTURE item 2 from user 5 Ok")
    void testCreateBooking5FutureOk() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusMinutes(12).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusMinutes(20).withNano(0);
        CreateBookingDto booking1 = new CreateBookingDto(4, start, end);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.start", is(String.valueOf(start))))
                .andExpect(jsonPath("$.end", is(String.valueOf(end))))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.WAITING))))
                .andExpect(jsonPath("$.booker.id", is(5)))
                .andExpect(jsonPath("$.item.id", is(4)))
                .andExpect(jsonPath("$.item.name", is("Дрель")));
    }

    @Test
    @Order(77)
    @DisplayName("GET /items Получаю item 2 Юзера1 OK")
    void testGetItem2Ok3() throws Exception {

        mockMvc.perform(get("/items/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    @Order(77)
    @DisplayName("GET /bookings получаю booking User 5 Ok")
    void testGetAllBookingUser5Ok() throws Exception {

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id", is(5)))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(Status.WAITING))))
                .andExpect(jsonPath("$[0].booker.id", is(5)))
                .andExpect(jsonPath("$[0].item.id", is(4)))
                .andExpect(jsonPath("$[0].item.name", is("Дрель")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].status", is(String.valueOf(Status.WAITING))))
                .andExpect(jsonPath("$[1].booker.id", is(5)))
                .andExpect(jsonPath("$[1].item.id", is(3)))
                .andExpect(jsonPath("$[1].item.name", is("Шуруповерт")))
                .andExpect(jsonPath("$[2].id", is(1)))
                .andExpect(jsonPath("$[2].status", is(String.valueOf(Status.APPROVED))))
                .andExpect(jsonPath("$[2].booker.id", is(5)))
                .andExpect(jsonPath("$[2].item.id", is(2)))
                .andExpect(jsonPath("$[2].item.name", is("Дрель")));
    }

    @Test
    @Order(77)
    @DisplayName("GET /bookings получаю booking User 1 Fail")
    void testGetAllBookingUser1Ok() throws Exception {

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("У юзера с ИД 1 нет запросов на аренду"));
    }

    @Test
    @Order(77)
    @DisplayName("GET /bookings получаю booking User 40 Fail")
    void testGetAllBookingUser40NotFound() throws Exception {

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 40))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("Юзера с ИД 40 не существует"));
    }

    @Test
    @Order(78)
    @DisplayName("GET /bookings?state={state} получаю booking FUTURE User 5 Ok")
    void testGetAllBookingFUTUREUser5Ok() throws Exception {

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5)
                        .param("state", "FUTURE"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id", is(5)))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(Status.WAITING))))
                .andExpect(jsonPath("$[0].booker.id", is(5)))
                .andExpect(jsonPath("$[0].item.id", is(4)))
                .andExpect(jsonPath("$[0].item.name", is("Дрель")));
    }

    @Test
    @Order(80)
    @DisplayName("GET /bookings?state={state} получаю booking Wrong State User 5 Ok")
    void testGetAllBookingWrongStateUser5Ok() throws Exception {

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5)
                        .param("state", "TOMATO"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }


    @Test
    @Order(82)
    @DisplayName("GET /bookings/owner получаю booking User 1 Ok")
    void testGetAllItemOwnerBookingOk() throws Exception {

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id", is(4)))
                .andExpect(jsonPath("$[1].id", is(3)))
                .andExpect(jsonPath("$[2].id", is(2)))
                .andExpect(jsonPath("$[3].id", is(1)));
    }

    @Test
    @Order(84)
    @DisplayName("GET /bookings/owner?state={state} получаю booking User Future 1 Ok")
    void testGetAllItemOwnerBookingFutureOk() throws Exception {

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "FUTURE"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id", is(4)));
    }

    @Test
    @Order(85)
    @DisplayName("GET /bookings/owner?state={state} получаю booking User 10 Not Found")
    void testGetAllItemOwnerBookingNotFound() throws Exception {

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 10)
                        .param("state", "FUTURE"))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(86)
    @DisplayName("GET /bookings/owner?state={state} получаю booking User 5 Items not found")
    void testGetAllItemOwnerBookingItemsNotFound() throws Exception {

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5)
                        .param("state", "FUTURE"))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(86)
    @DisplayName("GET /bookings/owner?state={state} получаю booking User 1 Wrong State")
    void testGetAllItemOwnerBookingItemsNotFound2() throws Exception {

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "TOMATO"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(89)
    @DisplayName("PATCH /bookings/{id} Approve booking 3 User Not Found")
    void testApproveBookingUserNotFound() throws Exception {

        mockMvc.perform(patch("/bookings/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 10)
                        .param("approved", "true"))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(90)
    @DisplayName("PATCH /bookings/{id} Approve booking 3 Booking Not Found")
    void testApproveBookingNotFound() throws Exception {

        mockMvc.perform(patch("/bookings/{id}", 30)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 10)
                        .param("approved", "true"))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(91)
    @DisplayName("PATCH /bookings/{id} Approve booking 3 OK")
    void testApproveBookingOk() throws Exception {

        mockMvc.perform(patch("/bookings/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.APPROVED))))
                .andExpect(jsonPath("$.booker.id", is(4)))
                .andExpect(jsonPath("$.item.id", is(2)))
                .andExpect(jsonPath("$.item.name", is("Дрель")));
    }

    @Test
    @Order(92)
    @DisplayName("PATCH /bookings/{id} Approve booking 3 second time fail")
    void testApproveBookingSecondTimeFail() throws Exception {

        mockMvc.perform(patch("/bookings/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(93)
    @DisplayName("PATCH /bookings/{id} Reject booking 5 Ok")
    void testApproveBookingRejectOk() throws Exception {

        mockMvc.perform(patch("/bookings/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .param("approved", "false"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.REJECTED))))
                .andExpect(jsonPath("$.booker.id", is(5)))
                .andExpect(jsonPath("$.item.id", is(4)))
                .andExpect(jsonPath("$.item.name", is("Дрель")));
    }

    @Test
    @Order(94)
    @DisplayName("PATCH /bookings/{id} Unknown approval state fail")
    void testUnknownApprovalStateFail() throws Exception {

        mockMvc.perform(patch("/bookings/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "tomato"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(95)
    @DisplayName("POST /bookings Создаю booking item 2 from user 3 Fail TimeCrossing")
    void testCreateBooking2TimeCrossing() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusSeconds(2).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusMinutes(2).withNano(0);
        CreateBookingDto booking1 = new CreateBookingDto(2, start, end);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(96)
    @DisplayName("DELETE /bookings/{id} User Not Found")
    void testDeleteBookingUserNotFound() throws Exception {

        mockMvc.perform(delete("/bookings/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 20))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(97)
    @DisplayName("DELETE /bookings/{id} Booking Not Found")
    void testDeleteBookingNotFound() throws Exception {

        mockMvc.perform(delete("/bookings/{id}", 50)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(98)
    @DisplayName("DELETE /bookings/{id} Wrong Owner")
    void testDeleteBookingNotOwner() throws Exception {

        mockMvc.perform(delete("/bookings/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(99)
    @DisplayName("DELETE /bookings/{id} OK")
    void testDeleteBookingOk() throws Exception {

        mockMvc.perform(delete("/bookings/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5))

                // Validate the response code and content type
                .andExpect(status().isOk());
    }


    @Test
    @Order(102)
    @DisplayName("GET /bookings?state={state} получаю booking PAST User 5 Ok")
    void testGetAllBookingPastUser5Ok() throws Exception {

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5)
                        .param("state", "PAST"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(Status.APPROVED))))
                .andExpect(jsonPath("$[0].booker.id", is(5)))
                .andExpect(jsonPath("$[0].item.id", is(2)))
                .andExpect(jsonPath("$[0].item.name", is("Дрель")));
    }

    @Test
    @Order(103)
    @DisplayName("GET /bookings/owner?state={state} получаю booking Past User 1 Ok")
    void testGetAllItemOwnerBookingPastOk() throws Exception {

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "PAST"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    @Order(104)
    @DisplayName("POST /comments Создаю comment item not found")
    void testCreateCommentItemNotFound() throws Exception {

        CommentInfoDto comment = new CommentInfoDto(null, "Comment for Item", null, null);

        mockMvc.perform(post("/items/{itemId}/comment", 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5)
                        .content(objectMapper.writeValueAsString(comment)))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("Вещь с ИД 10 не найдена"));
    }

    @Test
    @Order(105)
    @DisplayName("POST /comments Создаю comment user not found")
    void testCreateCommentUserNotFound() throws Exception {

        CommentInfoDto comment = new CommentInfoDto(null, "Comment for Item", null, null);

        mockMvc.perform(post("/items/{itemId}/comment", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 50)
                        .content(objectMapper.writeValueAsString(comment)))

                // Validate the response code and content type
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь с ИД 50 не найден"));
    }

    @Test
    @Order(106)
    @DisplayName("POST /comments Создаю comment user without booking")
    void testCreateCommentUserNoBooking() throws Exception {

        CommentInfoDto comment = new CommentInfoDto(null, "Comment for Item", null, null);

        mockMvc.perform(post("/items/{itemId}/comment", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 6)
                        .content(objectMapper.writeValueAsString(comment)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пользователь с ИД 6 не бронировал ничего"));
    }

    @Test
    @Order(107)
    @DisplayName("POST /comments Создаю comment user with booking not passed")
    void testCreateCommentUserWithBookingFuture() throws Exception {

        CommentInfoDto comment = new CommentInfoDto(null, "Comment for Item", null, null);

        mockMvc.perform(post("/items/{itemId}/comment", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 4)
                        .content(objectMapper.writeValueAsString(comment)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Нельзя комментировать пока не прошла аренда"));
    }

    @Test
    @Order(108)
    @DisplayName("POST /comments Создаю comment Ok")
    void testCreateCommentOk() throws Exception {

        CommentInfoDto comment = new CommentInfoDto(null, "Comment for Item", null, null);

        mockMvc.perform(post("/items/{itemId}/comment", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5)
                        .content(objectMapper.writeValueAsString(comment)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("Comment for Item")))
                .andExpect(jsonPath("$.authorName", is("Name4")))
                .andExpect(jsonPath("$.created").value(IsNull.notNullValue()));
    }

    @Test
    @Order(109)
    @DisplayName("GET /items Получаю item 2 Юзера1 OK")
    void testGetItem2Ok4() throws Exception {

        mockMvc.perform(get("/items/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    @Order(109)
    @DisplayName("POST /requests Создаю request user not found")
    void testCreateRequestUserNotFound() throws Exception {

        RequestDto request = new RequestDto(null, "Request for Item", null, null, null);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 50)
                        .content(objectMapper.writeValueAsString(request)))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(110)
    @DisplayName("POST /requests Создаю request description blank")
    void testCreateRequestNoDescription() throws Exception {

        RequestDto request = new RequestDto(null, "", null, null, null);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .content(objectMapper.writeValueAsString(request)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(111)
    @DisplayName("POST /requests Создаю request description null")
    void testCreateRequestDescriptionNull() throws Exception {

        RequestDto request = new RequestDto(null, null, null, null, null);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .content(objectMapper.writeValueAsString(request)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(112)
    @DisplayName("POST /requests Создаю request Okl")
    void testCreateRequestOk() throws Exception {

        RequestDto request = new RequestDto(null, "Request for Item", null, null, null);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .content(objectMapper.writeValueAsString(request)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Request for Item")))
                .andExpect(jsonPath("$.created").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.requesterId", is(3)))
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    @Order(113)
    @DisplayName("GET /bookings/owner?state={state} получаю booking User Current 1 Ok")
    void testGetAllItemOwnerBookingCurrentOk() throws Exception {

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "CURRENT"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id", is(2)));
    }

    @Test
    @Order(114)
    @DisplayName("GET /bookings?state={state} получаю booking CURRENT User 4 Ok")
    void testGetAllBookingCurrentUser4Ok() throws Exception {

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5)
                        .param("state", "CURRENT"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].booker.id", is(5)))
                .andExpect(jsonPath("$[0].item.id", is(3)))
                .andExpect(jsonPath("$[0].item.name", is("Шуруповерт")));
    }

    @Test
    @Order(115)
    @DisplayName("PATCH /bookings/{id} Approve booking wrong State")
    void testApproveBookingWrongState() throws Exception {

        mockMvc.perform(patch("/bookings/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "tomato"))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(116)
    @DisplayName("PATCH /bookings/{id} Approve booking 4 Owner 1 REJECT")
    void testApproveBooking4RejectOk() throws Exception {

        mockMvc.perform(patch("/bookings/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "false"))

                // Validate the response code and content type
                .andExpect(status().isOk());
    }

    @Test
    @Order(117)
    @DisplayName("GET /bookings?state={state} получаю booking REJECTED User 3 Ok")
    void testGetAllBookingRejectedUser5Ok() throws Exception {

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .param("state", "REJECTED"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id", is(4)));
    }

    @Test
    @Order(118)
    @DisplayName("GET /bookings/owner?state={state} получаю booking Past User 1 Ok")
    void testGetAllItemOwnerBookingRejectedOk() throws Exception {

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "REJECTED"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id", is(4)));
    }

    @Test
    @Order(119)
    @DisplayName("POST /bookings Создаю booking 8 item 2 from user 3 Ok")
    void testCreateBooking8Ok() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusSeconds(10).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusMinutes(2).withNano(0);
        CreateBookingDto booking1 = new CreateBookingDto(3, start, end);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(6)))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.WAITING))));
    }

    @Test
    @Order(120)
    @DisplayName("GET /bookings?state={state} получаю booking WAITING User 5 Ok")
    void testGetAllBookingWaitingUser5Ok() throws Exception {

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .param("state", "WAITING"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id", is(6)));
    }

    @Test
    @Order(121)
    @DisplayName("GET /bookings/owner?state={state} получаю booking WAITING User 1 Ok")
    void testGetAllItemOwnerBookingWaitingOk() throws Exception {

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "WAITING"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id", is(6)));
    }

    @Test
    @Order(122)
    @DisplayName("GET /requests Get request Ok")
    void testGetRequestsOk() throws Exception {

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "10"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("Request for Item")))
                .andExpect(jsonPath("$[0].created").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$[0].requesterId", is(3)))
                .andExpect(jsonPath("$[0].items").isEmpty());
    }

    @Test
    @Order(123)
    @DisplayName("GET /requests Get request user not found")
    void testGetRequestsUserNotFound() throws Exception {

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 10)
                        .param("from", "0")
                        .param("size", "10"))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(125)
    @DisplayName("GET /requests/{id} Get request Ok")
    void testGetRequestsId1Ok() throws Exception {

        mockMvc.perform(get("/requests/{id}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "10"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Request for Item")))
                .andExpect(jsonPath("$.created").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.requesterId", is(3)))
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    @Order(126)
    @DisplayName("GET /requests/{id} Get request user not found")
    void testGetRequestsId1UserNotFound() throws Exception {

        mockMvc.perform(get("/requests/{id}", 1)
                        .header("X-Sharer-User-Id", 10)
                        .param("from", "0")
                        .param("size", "10"))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(128)
    @DisplayName("GET /requests/{id} Get request Not Found")
    void testGetRequestsId10NotFound() throws Exception {

        mockMvc.perform(get("/requests/{id}", 10)
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "10"))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(129)
    @DisplayName("PATCH /requests Get request user not found")
    void testPatchRequestsUserNotFound() throws Exception {

        RequestDto request = new RequestDto(null, "Request for Item", null, null, null);

        mockMvc.perform(patch("/requests/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 10)
                        .content(objectMapper.writeValueAsString(request)))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(130)
    @DisplayName("PATCH /requests Not Found")
    void testPatchRequestsRequestNotFound() throws Exception {

        RequestDto request = new RequestDto(null, "Request for Item", null, null, null);

        mockMvc.perform(patch("/requests/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .content(objectMapper.writeValueAsString(request)))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(131)
    @DisplayName("PATCH /requests Wrong Author")
    void testPatchRequestsWrongAuthor() throws Exception {

        RequestDto request = new RequestDto(null, "Updated Request for Item", null, null, null);

        mockMvc.perform(patch("/requests/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(request)))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(132)
    @DisplayName("PATCH /requests Ok")
    void testPatchRequestsOk() throws Exception {
        RequestDto request = new RequestDto(null, "Updated Request for Item", null, null, null);

        mockMvc.perform(patch("/requests/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .content(objectMapper.writeValueAsString(request)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Updated Request for Item")))
                .andExpect(jsonPath("$.created").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.requesterId", is(3)))
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    @Order(133)
    @DisplayName("GET /requests Get All User request Ok")
    void testGetAllUserRequestsId3Ok() throws Exception {

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 3))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("Updated Request for Item")))
                .andExpect(jsonPath("$[0].created").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$[0].requesterId", is(3)))
                .andExpect(jsonPath("$[0].items").isEmpty());
    }

    @Test
    @Order(134)
    @DisplayName("GET /requests Get All request user not found")
    void testGetAllRequestsUserNotFound() throws Exception {

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 10))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(135)
    @DisplayName("GET /requests Get All request No req")
    void testGetAllRequestsNoRequestsOk() throws Exception {

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @Order(136)
    @DisplayName("POST /items Создаю item1 для Request Not Found")
    void testCreateItemRequest2NotFound() throws Exception {

        ItemDto newItem = new ItemDto(null, "Item", "Some Item from Request", true, 2);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(newItem)))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(137)
    @DisplayName("POST /items Создаю item1 для Request1 OK")
    void testCreateItemRequest1Ok() throws Exception {

        ItemDto newItem = new ItemDto(null, "Item", "Some Item from Request", true, 1);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(newItem)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(8)))
                .andExpect(jsonPath("$.name", is("Item")))
                .andExpect(jsonPath("$.description", is("Some Item from Request")))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.requestId", is(1)));
    }

    @Test
    @Order(138)
    @DisplayName("GET /requests Get All User request Ok")
    void testGetAllUserRequestsWithItemId3Ok() throws Exception {

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 3))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("Updated Request for Item")))
                .andExpect(jsonPath("$[0].created").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$[0].requesterId", is(3)))
                .andExpect(jsonPath("$[0].items[0].id", is(8)));
    }

    @Test
    @Order(138)
    @DisplayName("POST /bookings Создаю booking 9 item 8 from user 3 Ok")
    void testCreateBooking9Ok() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusMinutes(10).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusMinutes(20).withNano(0);
        CreateBookingDto booking1 = new CreateBookingDto(3, start, end);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(7)))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.WAITING))));
    }

    @Test
    @Order(139)
    @DisplayName("GET /items Получаю item 8 Юзера1 OK")
    void testGetItem8Ok4() throws Exception {

        mockMvc.perform(get("/items/{id}", 8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(8)));
    }

    @Test
    @Order(140)
    @DisplayName("DELETE /bookings/{id} OK")
    void testDeleteBookingFutureOk() throws Exception {

        mockMvc.perform(delete("/bookings/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 4))

                // Validate the response code and content type
                .andExpect(status().isOk());
    }

    @Test
    @Order(140)
    @DisplayName("DELETE /bookings/{id} 4 OK")
    void testDeleteBooking4Ok() throws Exception {

        mockMvc.perform(delete("/bookings/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3))

                // Validate the response code and content type
                .andExpect(status().isOk());
    }

    @Test
    @Order(141)
    @DisplayName("GET /items Получаю item 2 Юзера1 OK")
    void testGetItem2Ok5() throws Exception {

        mockMvc.perform(get("/items/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    @Order(142)
    @DisplayName("DELETE /requests/{id} request user not found")
    void testDeleteRequestsId1UserNotFound() throws Exception {

        mockMvc.perform(delete("/requests/{id}", 1)
                        .header("X-Sharer-User-Id", 10))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(143)
    @DisplayName("DELETE /requests/{id} Get request Not Found")
    void testDeleteRequestsId10NotFound() throws Exception {

        mockMvc.perform(delete("/requests/{id}", 10)
                        .header("X-Sharer-User-Id", 3))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(144)
    @DisplayName("DELETE /requests/{id} Wrong requester")
    void testDeleteRequestsWrongRequester() throws Exception {

        mockMvc.perform(delete("/requests/{id}", 1)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(145)
    @DisplayName("DELETE /requests/{id} Get request Ok")
    void testDeleteRequestsId1Ok() throws Exception {

        mockMvc.perform(delete("/requests/{id}", 1)
                        .header("X-Sharer-User-Id", 3))

                // Validate the response code and content type
                .andExpect(status().isOk());
    }

    @Test
    @Order(146)
    @DisplayName("POST /items Создаю item9 OK")
    void testCreateItem9Ok() throws Exception {

        ItemDto newItem = new ItemDto(null, "Item2", "Some Item2 ", true, null);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(newItem)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(9)))
                .andExpect(jsonPath("$.name", is("Item2")))
                .andExpect(jsonPath("$.description", is("Some Item2 ")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    @Order(147)
    @DisplayName("POST /bookings Создаю booking 8 item 9 from user 3 Ok")
    void testCreateBooking8Item9Ok() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusMinutes(10).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusMinutes(20).withNano(0);
        CreateBookingDto booking1 = new CreateBookingDto(9, start, end);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3)
                        .content(objectMapper.writeValueAsString(booking1)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(8)))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.WAITING))));
    }

    @Test
    @Order(148)
    @DisplayName("GET /items Получаю item 9 Юзера1 OK")
    void testGetItem9Ok5() throws Exception {

        mockMvc.perform(get("/items/{id}", 9)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(9)));
    }
}
