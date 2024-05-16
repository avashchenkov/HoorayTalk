package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("""
        A contract that tests the health endpoint with a 200 response.
    """)

    request {
        method 'GET'
        url '/api/v1/health'
    }
    response {
        status 200
    }
}