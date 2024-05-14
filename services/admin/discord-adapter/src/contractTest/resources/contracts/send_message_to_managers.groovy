import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("""
        A request to send a message to the admins via Discord API with a valid request should return 200 OK.
    """)

    request {
        method 'POST'
        url '/api/v1/message'
        headers {
            contentType(applicationJson())
            header('API-KEY', 'some-valid-api-key')
        }
        body("""
            {
              "bot_id": "bot-1234",
              "customer_chat_id": "chat-5678",
              "admin_chat_id": "chat-9012",
              "space_id": "space-3456",
              "message": "Hello, admin!"
            }
        """)
    }

    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body("""
            {
              "message": "Message sent successfully."
            }
        """)
    }
}
