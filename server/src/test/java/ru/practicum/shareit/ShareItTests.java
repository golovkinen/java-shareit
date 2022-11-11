package ru.practicum.shareit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ShareItTests {

	@Test
	@DisplayName("LastBooking Empty, NextBooking Empty, Comments Empty, Request null")
	void test1() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), new HashSet<>(), null);
		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.empty(), Optional.empty());

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertNull(itemInfoDto.getLastBooking());
		assertNull(itemInfoDto.getNextBooking());
		assertEquals(0, itemInfoDto.getComments().size());
		assertNull(itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking Empty, NextBooking Empty, Comments Empty, Request 1")
	void test2() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());

		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), new HashSet<>(), request);
		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);

		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.empty(), Optional.empty());

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertNull(itemInfoDto.getLastBooking());
		assertNull(itemInfoDto.getNextBooking());
		assertEquals(0, itemInfoDto.getComments().size());
		assertEquals(1, itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking Empty, NextBooking Empty, Comments 1, Request 1")
	void test3() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				null, user2);

		Set<Comment> comments = Collections.singleton(comment);
		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), comments, request);

		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.empty(), Optional.empty());

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertNull(itemInfoDto.getLastBooking());
		assertNull(itemInfoDto.getNextBooking());
		assertEquals(1, itemInfoDto.getComments().size());
		assertEquals(1, itemInfoDto.getComments().get(0).getId());
		assertEquals(1, itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking Empty, NextBooking Empty, Comments 1, Request null")
	void test4() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				null, user2);

		Set<Comment> comments = Collections.singleton(comment);
		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), comments, null);

		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.empty(), Optional.empty());

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertNull(itemInfoDto.getLastBooking());
		assertNull(itemInfoDto.getNextBooking());
		assertEquals(1, itemInfoDto.getComments().size());
		assertEquals(1, itemInfoDto.getComments().get(0).getId());
		assertNull(itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking 1, NextBooking Empty, Comments Empty, Request null")
	void test5() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				null, user2);

		Set<Comment> comments = Collections.singleton(comment);
		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), new HashSet<>(), null);

		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.of(lastBooking), Optional.empty());

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertEquals(1, itemInfoDto.getLastBooking().getId());
		assertNull(itemInfoDto.getNextBooking());
		assertEquals(0, itemInfoDto.getComments().size());
		assertNull(itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking 1, NextBooking Empty, Comments Empty, Request 1")
	void test6() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				null, user2);

		Set<Comment> comments = Collections.singleton(comment);
		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), new HashSet<>(), request);

		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.of(lastBooking), Optional.empty());

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertEquals(1, itemInfoDto.getLastBooking().getId());
		assertNull(itemInfoDto.getNextBooking());
		assertEquals(0, itemInfoDto.getComments().size());
		assertEquals(1, itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking 1, NextBooking Empty, Comments 1, Request 1")
	void test7() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				null, user2);

		Set<Comment> comments = Collections.singleton(comment);
		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), comments, request);

		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.of(lastBooking), Optional.empty());

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertEquals(1, itemInfoDto.getLastBooking().getId());
		assertNull(itemInfoDto.getNextBooking());
		assertEquals(1, itemInfoDto.getComments().size());
		assertEquals(1, itemInfoDto.getComments().get(0).getId());
		assertEquals(1, itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking 1, NextBooking Empty, Comments 1, Request null")
	void test8() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				null, user2);

		Set<Comment> comments = Collections.singleton(comment);
		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), comments, null);

		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.of(lastBooking), Optional.empty());

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertEquals(1, itemInfoDto.getLastBooking().getId());
		assertNull(itemInfoDto.getNextBooking());
		assertEquals(1, itemInfoDto.getComments().size());
		assertEquals(1, itemInfoDto.getComments().get(0).getId());
		assertNull(itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking Empty, NextBooking 2, Comments Empty, Request null")
	void test9() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				null, user2);

		Set<Comment> comments = Collections.singleton(comment);
		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), new HashSet<>(), null);

		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.empty(), Optional.of(nextBooking));

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertNull(itemInfoDto.getLastBooking());
		assertEquals(2, itemInfoDto.getNextBooking().getId());
		assertEquals(0, itemInfoDto.getComments().size());
		assertNull(itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking Empty, NextBooking 2, Comments Empty, Request 1")
	void test10() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				null, user2);

		Set<Comment> comments = Collections.singleton(comment);
		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), new HashSet<>(), request);

		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.empty(), Optional.of(nextBooking));

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertNull(itemInfoDto.getLastBooking());
		assertEquals(2, itemInfoDto.getNextBooking().getId());
		assertEquals(0, itemInfoDto.getComments().size());
		assertEquals(1, itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking Empty, NextBooking 2, Comments 1, Request 1")
	void test11() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				null, user2);

		Set<Comment> comments = Collections.singleton(comment);

		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), comments, request);

		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.empty(), Optional.of(nextBooking));

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertNull(itemInfoDto.getLastBooking());
		assertEquals(2, itemInfoDto.getNextBooking().getId());
		assertEquals(1, itemInfoDto.getComments().size());
		assertEquals(1, itemInfoDto.getComments().get(0).getId());
		assertEquals(1, itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking Empty, NextBooking 2, Comments 1, Request null")
	void test12() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				null, user2);

		Set<Comment> comments = Collections.singleton(comment);
		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), comments, null);

		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.empty(), Optional.of(nextBooking));

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertNull(itemInfoDto.getLastBooking());
		assertEquals(2, itemInfoDto.getNextBooking().getId());
		assertEquals(1, itemInfoDto.getComments().size());
		assertEquals(1, itemInfoDto.getComments().get(0).getId());
		assertNull(itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking 1, NextBooking 2, Comments Empty, Request null")
	void test13() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				null, user2);

		Set<Comment> comments = Collections.singleton(comment);
		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), new HashSet<>(), null);

		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.of(lastBooking), Optional.of(nextBooking));

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertEquals(1, itemInfoDto.getLastBooking().getId());
		assertEquals(2, itemInfoDto.getNextBooking().getId());
		assertEquals(0, itemInfoDto.getComments().size());
		assertNull(itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking 1, NextBooking 2, Comments Empty, Request 1")
	void test14() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				null, user2);

		Set<Comment> comments = Collections.singleton(comment);
		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), new HashSet<>(), request);

		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.of(lastBooking), Optional.of(nextBooking));

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertEquals(1, itemInfoDto.getLastBooking().getId());
		assertEquals(2, itemInfoDto.getNextBooking().getId());
		assertEquals(0, itemInfoDto.getComments().size());
		assertEquals(1, itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking 1, NextBooking 2, Comments 1, Request 1")
	void test15() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				null, user2);

		Set<Comment> comments = Collections.singleton(comment);

		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), comments, request);

		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.of(lastBooking), Optional.of(nextBooking));

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertEquals(1, itemInfoDto.getLastBooking().getId());
		assertEquals(2, itemInfoDto.getNextBooking().getId());
		assertEquals(1, itemInfoDto.getComments().size());
		assertEquals(1, itemInfoDto.getComments().get(0).getId());
		assertEquals(1, itemInfoDto.getRequestId());
	}

	@Test
	@DisplayName("LastBooking Empty, NextBooking 2, Comments 1, Request null")
	void test16() {

		User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Request request = new Request(1, "Request descr",
				LocalDateTime.of(2022, 10, 20, 10, 00, 00),
				user2, new HashSet<>());
		Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
				null, user2);

		Set<Comment> comments = Collections.singleton(comment);
		Item item = new Item(1, "Item1", "Item1 Descr", true, user1, new HashSet<>(), comments, null);

		Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
				LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item, user2);
		Booking nextBooking = new Booking(2, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
				LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item, user2);


		ItemInfoDto itemInfoDto = ItemMapper.toItemDto(item, Optional.of(lastBooking), Optional.of(nextBooking));

		assertNotNull(itemInfoDto);
		assertEquals(1, itemInfoDto.getId());
		assertEquals(1, itemInfoDto.getLastBooking().getId());
		assertEquals(2, itemInfoDto.getNextBooking().getId());
		assertEquals(1, itemInfoDto.getComments().size());
		assertEquals(1, itemInfoDto.getComments().get(0).getId());
		assertNull(itemInfoDto.getRequestId());
	}
}
