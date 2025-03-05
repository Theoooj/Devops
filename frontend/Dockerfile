FROM node:22 as build
# Désactiver strict-ssl pour npm
RUN npm config set strict-ssl false
RUN npm install -g @angular/cli

WORKDIR /app

COPY . .

RUN npm install


CMD ["npm", "start"]
