FROM nginx:alpine

RUN rm /etc/nginx/conf.d/default.conf

COPY nginx.conf /etc/nginx/nginx.conf

COPY ./nginx/ssl/hooraytalk.crt /etc/nginx/ssl/hooraytalk.crt
COPY ./nginx/ssl/hooraytalk.key /etc/nginx/ssl/hooraytalk.key