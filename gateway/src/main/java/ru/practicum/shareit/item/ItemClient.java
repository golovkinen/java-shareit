package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> readAllUserItems(int userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> readAll(int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", parameters);
    }

    public ResponseEntity<Object> createItem(int userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> getItem(int itemId, int userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> searchItemByWord(String text, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size);

        return get("/search?text={text}&from={from}&size={size}", parameters);
    }

    public ResponseEntity<Object> updateItem(ItemDto itemDto, int userId, int itemId) {

        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> deleteAllUserItems(int userId) {
        return delete("/", userId);

    }

    public ResponseEntity<Object> createComment(CommentInfoDto commentInfoDto, int userId, int itemId) {
        return post("/" + itemId + "/comment", userId, commentInfoDto);
    }

    public ResponseEntity<Object> delete(int itemId, int userId) {
        return delete("/" + itemId, userId);
    }
}
