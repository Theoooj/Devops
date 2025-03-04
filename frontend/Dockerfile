FROM node:22

RUN npm install -g @angular/cli

WORKDIR /
RUN mkdir frontend
WORKDIR /frontend

ENV APP_NAME 'frontend'
ENV ROUTING 'true'
ENV STANDALONE 'false'
ENV STRICT 'true'
ENV STYLE 'scss'

CMD ng new $APP_NAME --routing=$ROUTING --standalone=$STANDALONE --strict=$STRICT --style=$STYLE \
    && mv $APP_NAME/* . \
    && rm -rf $APP_NAME \
    && ng serve --host 0.0.0.0 --port 4200

EXPOSE 4200
