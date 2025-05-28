import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

const errorRate = new Rate('error_rate');
const requestDuration = new Trend('request_duration');
const successfulRequests = new Counter('successful_requests');

export const options = {
    scenarios: {
        ramping_load: {
            executor: 'ramping-vus',
            startVUs: 1,
            stages: [
                { duration: '30s', target: 10 },
                { duration: '1m', target: 100 },
                { duration: '30s', target: 10 },
            ],
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<500'],
        http_req_failed: ['rate<0.1'],
        error_rate: ['rate<0.1'],
    },
};

const BASE_URL = 'http://localhost:8080';
const SEATS_ENDPOINT = '/api/v1/seats';

export default function() {
    testGetSeats();
    sleep(Math.random() * 2 + 1);
}

function testGetSeats() {
    const url = `${BASE_URL}${SEATS_ENDPOINT}`;

    const response = http.get(url, {
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        timeout: '30s',
    });

    const isSuccess = check(response, {
        'GET /api/v1/seats - status is 200': (r) => r.status === 200,
        'GET /api/v1/seats - response time < 2000ms': (r) => r.timings.duration < 2000,
        'GET /api/v1/seats - response has body': (r) => r.body.length > 0,
        'GET /api/v1/seats - content type is JSON': (r) =>
            r.headers['Content-Type'] && r.headers['Content-Type'].includes('application/json'),
    });

    errorRate.add(!isSuccess);
    requestDuration.add(response.timings.duration);
    if (isSuccess) {
        successfulRequests.add(1);
    }

    if (response.status !== 200) {
        console.log(`GET request failed: ${response.status} - ${response.body}`);
    }
}

export function teardown(data) {
    console.log('Performance test completed');
}
