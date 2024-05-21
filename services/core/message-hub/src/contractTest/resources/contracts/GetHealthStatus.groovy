package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Service health check"
    request {
        method 'GET'
        url '/api/v1/health'
    }
    response {
        status 200
    }
}