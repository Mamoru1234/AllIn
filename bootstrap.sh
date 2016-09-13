#!/usr/bin/env bash

apt-get install --yes python-software-properties
add-apt-repository ppa:webupd8team/java
apt-get update -qq
echo debconf shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
echo debconf shared/accepted-oracle-license-v1-1 seen true | /usr/bin/debconf-set-selections
apt-get install --yes oracle-java8-installer
yes "" | apt-get -f install

ROOT_PASSWORD="admin"

apt-get update
debconf-set-selections <<< "mysql-server mysql-server/root_password password $ROOT_PASSWORD"
debconf-set-selections <<< "mysql-server mysql-server/root_password_again password $ROOT_PASSWORD"
apt-get update
apt-get -y install mysql-server

mysql -u root -p$ROOT_PASSWORD<<EOI
CREATE DATABASE all_in;
USE all_in;
CREATE TABLE services(
    service_id VARCHAR(36) NOT NULL,
    service_name VARCHAR(200) NOT NULL UNIQUE,
    PRIMARY KEY (service_id)
);
exit
EOI

# start server
#cd /vagrant
#./gradlew :server:run
