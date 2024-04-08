package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("""
        A contract that tests the message endpoint with a 200 response.
    """)

    request {
        method 'POST'
        url '/message'
        headers {
            contentType(applicationJson())
        }
        body("""
            {
                "bot_id": "123456",
                "customer_chat_id": "654321",
                "message": "Hello, this is a test message"
            }
        """)
    }
    response {
        status 200
    }
}