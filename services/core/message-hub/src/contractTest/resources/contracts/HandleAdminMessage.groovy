package contracts

import org.springframework.cloud.contract.spec.Contract

import static org.springframework.cloud.contract.spec.internal.HttpHeaders.*

Contract.make {
    description "Handle a newly created message from the admin side"
    request {
        method 'POST'
        url '/api/v1/admin-message'
        headers {
            header(CONTENT_TYPE, 'application/json')
            header('API-KEY', env('MESSAGE_HUB_API_KEY'))
        }
        body([
                bot_id: $(consumer('d375ca03-291d-40ae-a908-78137273f722'), producer(regex('.+'))),
                admin_chat_id: $(consumer('admin123'), producer(regex('.+'))),
                message: $(consumer('Hello from admin'), producer(regex('.+')))
        ])
    }
    response {
        status 200
    }
}