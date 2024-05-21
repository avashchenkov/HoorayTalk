package contracts

import org.springframework.cloud.contract.spec.Contract

import static org.springframework.cloud.contract.spec.internal.HttpHeaders.*

Contract.make {
    description "Handle a newly created message from the customer side"
    request {
        method 'POST'
        url '/api/v1/customer-message'
        headers {
            header(CONTENT_TYPE, 'application/json')
            header('API-KEY', env('MESSAGE_HUB_API_KEY'))
        }
        body([
                bot_id: env('TEST_TELEGRAM_BOT_ID'),
                customer_chat_id: env('TEST_TELEGRAM_CHAT_ID'),
                message: $(consumer('Hello from customer'), producer(regex('.+')))
        ])
    }
    response {
        status 200
    }
}