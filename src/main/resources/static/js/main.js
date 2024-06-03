"use strict";

$(document).ready(function() {
  let selectUser = $(".item-user-receive");
  let chatPage = $("#chat-page");
  let messageForm = $("#messageForm");
  let messageInput = $("#message-to-send");
  let messageArea = $("#messageArea");
  let connectingElement = $(".connecting");
  let isConnected = false;
  let stompClient = null;
  let username = null;
  let channel = null;

  // Handlebars templates
  let messageTemplate = Handlebars.compile($("#message-template").html());
  let messageResponseTemplate = Handlebars.compile($("#message-response-template").html());

  function connect() {
    if (!isConnected) { // Kiểm tra xem có đang kết nối hay không
      username = $("#username").text();
      if (username && channel) {
        chatPage.removeClass("hidden");
        let socket = new SockJS("/websocket");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function() {
          isConnected = true; // Đánh dấu kết nối thành công
          onConnected();
        }, function(error) {
          onError(error);
          isConnected = false; // Đặt lại trạng thái kết nối
        });
      }
    }
  }

  function onConnected() {
    // Subscribe to the specified channel
    stompClient.subscribe(`/topic/${channel}`, onMessageReceived);

    stompClient.send(
        "/app/chat.register",
        {},
        JSON.stringify({ sender: username, type: "JOIN", channel: channel })
    );

    connectingElement.addClass("hidden");
  }

  function onError(error) {
    connectingElement.text("Could not connect to WebSocket! Please refresh the page and try again or contact your administrator.");
    connectingElement.css("color", "red");
  }

  function send(event) {
    let messageContent = messageInput.val().trim();

    if (messageContent && stompClient) {
      let chatMessage = {
        sender: username,
        content: messageInput.val(),
        type: "CHAT",
        channel: channel,
      };

      stompClient.send(`/app/chat.send/${channel}`, {}, JSON.stringify(chatMessage));
      messageInput.val("");
    }
    event.preventDefault();
  }

  /**
   * Handles the received message and updates the chat interface accordingly.
   * @param {Object} payload - The payload containing the message data.
   */
  function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    let messageElement;

    if (message.type === "JOIN") {
      messageElement = $("<li></li>");
      messageElement.addClass("event-message");
      message.content = message.sender + " joined!";
      let textElement = $("<p></p>");
      let messageText = document.createTextNode(message.content);
      textElement.append(messageText);
      messageElement.append(textElement);
    } else if (message.type === "LEAVE") {
      messageElement = $("<li></li>");
      messageElement.addClass("event-message");
      message.content = message.sender + " left!";
      let textElement = $("<p></p>");
      let messageText = document.createTextNode(message.content);
      textElement.append(messageText);
      messageElement.append(textElement);
    } else {
      if (message.sender === username) {
        messageElement = $("<li></li>");
        messageElement.html(messageTemplate({
          time: new Date().toLocaleTimeString(),
          sender: username,
          messageOutput: message.content,
        }));
      } else {
        messageElement = $("<li></li>");
        messageElement.html(messageResponseTemplate({
          time: new Date().toLocaleTimeString(),
          sender: message.sender,
          response: message.content,
        }));
      }
    }
    messageArea.append(messageElement);
    messageArea.scrollTop(messageArea.prop("scrollHeight"));
  }

  messageForm.on("submit", send);
  messageInput.on("keydown", function(event) {
    if (event.keyCode === 13 && !event.shiftKey) {
      send(event);
    }
  });

  selectUser.on("click", function(event) {
    let user_receive_name = $("#user_receive_name");
    user_receive_name.text($(this).find('.name').text());
    let receiverId = $(this).data('id'); // Lấy id của người nhận
    if (isConnected) { // Nếu đang kết nối, ngắt kết nối trước khi thay đổi kênh
      stompClient.disconnect();
      isConnected = false; // Đặt lại trạng thái kết nối
    }
    $.ajax({
      url: "/getRoom",
      method: "POST",
      data: {
        sender: $('#me_id').val(),
        receiver: receiverId,
      },
      success: function(result) {
        if (result) {
          channel = result;
          connect();
        } else {
          console.error("Failed to get channel.");
        }
      },
      error: function(xhr, status, error) {
        console.error("Failed to get channel: " + error);
      }
    });
  });
});
