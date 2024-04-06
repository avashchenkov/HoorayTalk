package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/health'
    }
    response {
        status 200
    }
}