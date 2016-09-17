#!/usr/bin/env bash

apt-get install --yes python-software-properties
add-apt-repository ppa:webupd8team/java
apt-get update -qq
echo debconf shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
echo debconf shared/accepted-oracle-license-v1-1 seen true | /usr/bin/debconf-set-selections
apt-get install --yes oracle-java8-installer
yes "" | apt-get -f install

apt-get update
apt-get install -y redis-server
/etc/init.d/redis-server start

ROOT_PASSWORD="admin"

apt-get update
debconf-set-selections <<< "mysql-server mysql-server/root_password password $ROOT_PASSWORD"
debconf-set-selections <<< "mysql-server mysql-server/root_password_again password $ROOT_PASSWORD"
apt-get update
apt-get -y install mysql-server

mysql -u root -p$ROOT_PASSWORD<<EOI
CREATE DATABASE all_in;
USE all_in;
CREATE TABLE clients(
    client_id VARCHAR(36) NOT NULL,
    client_name VARCHAR(200) NOT NULL UNIQUE,
    client_secret VARCHAR(200) NOT NULL UNIQUE,
    PRIMARY KEY (client_id)
);
CREATE TABLE users(
    user_id VARCHAR(36) NOT NULL,
    user_mail VARCHAR(200) NOT NULL UNIQUE,
    user_password VARCHAR(200) NOT NULL UNIQUE,
    PRIMARY KEY (user_id)
);
CREATE TABLE users_personal_info(
    user_id VARCHAR(36) NOT NULL,
    country VARCHAR(200) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
    ON DELETE CASCADE
);
CREATE TABLE permissions (
    access_token VARCHAR(36) NOT NULL UNIQUE,
    user_id VARCHAR(36) NOT NULL,
    client_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
    ON DELETE CASCADE,
    FOREIGN KEY (client_id) REFERENCES clients (client_id)
    ON DELETE CASCADE
);
exit
EOI

# start server
cd /vagrant
./gradlew :server:bootRun
