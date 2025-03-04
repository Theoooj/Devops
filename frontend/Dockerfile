FROM node:22 as build
RUN npm install -g @angular/cli

WORKDIR /app

COPY . .

RUN npm install


CMD ["npm", "start"]
