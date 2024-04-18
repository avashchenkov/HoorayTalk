package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("""
        A contract that tests the message endpoint with a 200 response.
    """)

    request {
        method 'POST'
        url '/api/v1/message'
        headers {
            contentType(applicationJson())
        }
        body("""
            {
                "bot_id": "d375ca03-291d-40ae-a908-78137273f722",
                "customer_chat_id": "1525492729",
                "message": "Hello, this is a test message"
            }
        """)
    }
    response {
        status 200
    }
}