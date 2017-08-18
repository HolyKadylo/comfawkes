# Runs to setup enviroment
# Ubuntu linux64
echo "-->Setting enviroment for Ubuntu Linux64"
sudo apt-get update
sudo apt-get install docker-io
sudo apt-get install git
sudo apt-get install maven
sudo apt-get install java
sudo docker pull selenium/standalone-firefox
sudo docker pull rabbitmq
sudo docker pull java
wget https://github.com/mozilla/geckodriver/releases/download/v0.17.0/geckodriver-v0.17.0-linux64.tar.gz
tar -xvf geckodriver-v0.17.0-linux64.tar.gz
rm geckodriver-v0.17.0-linux64.tar.gz
git init
git remote add fawkes https://github.com/holykadylo/comfawkes
git pull fawkes master
sudo chmod +x *.sh
mvn package
echo "-->Consider enviroment set"