FROM selenium/standalone-chrome

USER root

RUN apt-get update && apt-get install -y python3 python3-pip && ln -s /usr/bin/python3 /usr/bin/python

WORKDIR /app

COPY . /app

USER seluser

CMD ["python", "StartTest.py"]
