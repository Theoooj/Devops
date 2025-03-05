FROM node:22

RUN npm config set strict-ssl false
RUN npm install -g @angular/cli

WORKDIR /app

COPY . .

RUN npm install


CMD ["npm", "start"]
